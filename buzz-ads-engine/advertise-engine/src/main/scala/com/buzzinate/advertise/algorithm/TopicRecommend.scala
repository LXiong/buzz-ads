package com.buzzinate.advertise.algorithm

import scala.Array.canBuildFrom
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.mutable.ListBuffer
import org.apache.commons.lang.StringUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes
import com.buzzinate.advertise.elasticsearch.api.AdQuery
import com.buzzinate.advertise.elasticsearch.api.HitAd
import com.buzzinate.advertise.model.KeywordInfo
import com.buzzinate.advertise.redis.json.AdCtr
import com.buzzinate.advertise.redis.json.SiteConfig
import com.buzzinate.advertise.redis.LimitAdRedisClient
import com.buzzinate.advertise.redis.RedisClient
import com.buzzinate.advertise.server.Message.AdLevel
import com.buzzinate.advertise.server.Message.CampaignLevel
import com.buzzinate.advertise.server.Message.EntryLevel
import com.buzzinate.advertise.server.Message.OrderLevel
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.tools.LdaUtil
import com.buzzinate.advertise.util.Constants.SEARCH_EXPAND_COEFFICIENT
import com.buzzinate.advertise.util.Constants.SECS_ONE_DAY
import com.buzzinate.advertise.util.Constants.SITE_CONFIG_EVENT_QUEUE
import com.buzzinate.advertise.util.DoublePriorityQueue
import com.buzzinate.advertise.util.KeywordUtil
import com.buzzinate.advertise.util.UserinfoParser
import com.buzzinate.buzzads.enums.BidTypeEnum
import com.buzzinate.buzzads.event.PublisherSiteConfigEvent
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum
import com.buzzinate.common.util.ip.ProvinceName
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer
import com.buzzinate.nlp.util.TextUtil
import TopicRecommend.getTopTopicText
import TopicRecommend.getWeighedWords
import TopicRecommend.ldaModel
import TopicRecommend.te
import edu.stanford.nlp.tmt.stage.LoadGibbsLDA
import scalanlp.pipes.Pipes.global.file
import com.buzzinate.advertise.server.Message.UserCategoryLog

class TopicRecommend() extends Recommend {
  import TopicRecommend._

  //粗糙搜索系数，比如前端需要3条展示广告，则我们先从索引中搜索到最相关的 （3 * searchExpandCoefficient） 条广告，然后对这些广告再进行混合排序
  val searchExpandCoefficient = Servers.prop.getInt(SEARCH_EXPAND_COEFFICIENT, 4)
  val redisClient = new RedisClient(Servers.jedisPool)
  val limitAdRedisClient = new LimitAdRedisClient(Servers.jedisPool)

  def recommend(url: String, rawTitle: String, metaKeywords: List[String], entryType: AdEntryTypeEnum, resourceSize: String, count: Int, userId: String, province: ProvinceName, netWork: Int, uuid: String): List[HitAd] = {

    try {

      //获取站长设置的一些黑名单信息
      var blackKeywords = ""
      var blackDomains = ""
      var blackAdRealUrl = ""
      val siteConfig = getSiteConfig(uuid)
      if (!siteConfig.isEmpty) {
        blackKeywords = siteConfig.get.getBlackKeywords
        blackDomains = siteConfig.get.getBlackDomains
        blackAdRealUrl = siteConfig.get.getBlackRealUrls
      }

      //获取超过限制浏览次数的广告
      val blackAdIds = limitAdRedisClient.getCookie2BlackAdSet(userId, new EntryLevel)
      val blackAdOrders = limitAdRedisClient.getCookie2BlackAdSet(userId, new OrderLevel)
      val blackAdCampaigns = limitAdRedisClient.getCookie2BlackAdSet(userId, new CampaignLevel)

      //获取针对此user进行优先投放的广告
      val preferAds = limitAdRedisClient.getPreferAd(userId)

      //定向投放的一些广告，这些广告只针对定向cookie进行投放
      val audienceAds = limitAdRedisClient.getAudienceAds

      //获取用户兴趣信息
      var categories = List[Integer]()
      var ucLog = new UserCategoryLog(false, true)
      if (Servers.isMatchAudienceCategory) {
        val table = Servers.hTablePool.getTable("user_category")
        try {
          val g = new Get(Bytes.toBytes(userId))
          val start = System.currentTimeMillis
          val r = table.get(g)
          val cost = System.currentTimeMillis - start
          val data = Bytes.toString(r.getValue(Bytes.toBytes("data"), Bytes.toBytes("1")))
          val userInfo = UserinfoParser.parse(data)
          categories = userInfo.categories
          if(categories.size > 0) {
            ucLog = new UserCategoryLog(true, false, cost)
          } else {
            ucLog = new UserCategoryLog(false, false, cost)
          }
        } catch {
          case e: Exception => {
            error(e.getMessage)
            e.getStackTrace().foreach(trace => error(trace.toString()))
          }
        } finally {
          Servers.hTablePool.putTable(table)
        }
      }
      Servers.asnyc.asnyc(userId) {
        Servers.logCollector ! ucLog
      }

      var topicText =
        if (StringUtils.isEmpty(url)) { null } else { redisClient.getPageTopic(url) }
      if (null == topicText) {
        //第一次遇见这个页面，暂时用默认分布代替，异步计算实际的topic分布
        topicText = getTopTopicText(LdaUtil.defaultDist, Servers.topnTopic)
        Servers.asnyc.asnyc(url) {
          val filledTitle = TextUtil.fillText(te.extract(rawTitle))
          val keywords = metaKeywords.map { w =>
            val field = if (filledTitle.contains(TextUtil.fillWord(w))) KeywordInfo.META_TITLE else KeywordInfo.META
            KeywordInfo(w, 1, field)
          }
          val refinedKeywords = KeywordUtil.extractKeywords(filledTitle, keywords.filter(keyword => keyword.field != KeywordInfo.META))
          val weighedWords = getWeighedWords(refinedKeywords)
          var distribution = ldaModel.infer(weighedWords)
          //不能infer的webpage，用默认分布代替，今后要refine lda model，尽量减少这种case
          if (distribution.apply(0).isNaN) {
            distribution = LdaUtil.defaultDist
          }
          val topicText = getTopTopicText(distribution, Servers.topnTopic)
          redisClient.updatePageTopic(url, topicText, SECS_ONE_DAY)
        }
      }

      val adQuery = new AdQuery

      adQuery.setUrl(url).setCats(categories.asJava).setTopicDistribution(topicText).setBlackIds((blackAdIds ++ audienceAds).asJava)
        .setBlackOrders(blackAdOrders.asJava).setBlackCampaigns(blackAdCampaigns.asJava).setBlackWords(blackKeywords).setBlackDomains(blackDomains)
        .setBlackRealUrls(blackAdRealUrl).setEntryType(entryType).setResourceSize(resourceSize).setUserProvince(province).setNetWork(netWork)
        .setMax(Math.max(count, Math.min(count * searchExpandCoefficient, 20)))

      val hitAds = Servers.esClient.query(adQuery).asScala.toList

      //按点击付费的广告id
      val clickPayAdIds = hitAds.flatMap(hitAd => if (hitAd.bidType == BidTypeEnum.CPC.getCode) List(hitAd.entryId) else Nil)
      val campaignIds = hitAds.map(hitAd => hitAd.campaignId).distinct

      val adPriorities = redisClient.getAdPriority(campaignIds)
      val campaignCbrs = redisClient.getCampaignBalanceRatio(campaignIds)
      val adCtrs = redisClient.getAdCTR(clickPayAdIds)

      var (res, timeAdSize) = getTopAds(hitAds, adPriorities, campaignCbrs, adCtrs, count)

      //如果包时段广告不足请求数，则尝试用优先给此user投放的一些广告填满请求数
      if (timeAdSize < count) {
        val filtedPreferAds = preferAds.filter { preferAd =>
          !blackAdCampaigns.contains(preferAd.campaignId) && !blackAdIds.contains(preferAd.entryId)
        }

        if (filtedPreferAds.size > 0) {
          val entryIds = filtedPreferAds.map(ad => ad.entryId).mkString(",")
          adQuery.setEntryIds(entryIds)
          adQuery.setBlackIds(blackAdIds.asJava)
          val originalPreferHitAd = Servers.esClient.query(adQuery).asScala.toList
          val preferHitAds = dedup(originalPreferHitAd, true)
          if (preferHitAds.size > 0) {
            res = combine(res, preferHitAds, timeAdSize, count)
          }
        }
      }

      Servers.asnyc.asnyc(userId) {
        val adIds = res map { hitAd =>
          hitAd.entryId
        }
        val orderIds = res map { hitAd =>
          hitAd.orderId
        }
        val campaignIds = res map { hitAd =>
          hitAd.campaignId
        }
        processLimitShowAd(userId, adIds, new EntryLevel)
        processLimitShowAd(userId, orderIds, new OrderLevel)
        processLimitShowAd(userId, campaignIds, new CampaignLevel)
      }
      res
    } catch {
      case e: Exception => {
        error(e.getMessage)
        e.getStackTrace().foreach(trace => error(trace.toString()))
        List[HitAd]()
      }
    }
  }

  private def processLimitShowAd(userId: String, adIds: List[String], adLevel: AdLevel): Unit = {
    //找到被推送出的广告中被限定浏览次数的广告
    val limitAds = limitAdRedisClient.getLimitShowAd(adIds, adLevel)
    if (limitAds.isEmpty) {
      return
    }
    val ad2AlreadyShowTime = limitAdRedisClient.increByCookieAdId(userId, limitAds, adLevel)
    val blackAds = limitAdRedisClient.getBlackAdId(userId, ad2AlreadyShowTime, adLevel)
    if (!blackAds.isEmpty) {
      limitAdRedisClient.updateCookie2BlackAdSet(userId, blackAds.toList, adLevel)
    }
  }

  private def getSiteConfig(uuid: String): Option[SiteConfig] = {
    if (StringUtils.isEmpty(uuid)) {
      None
    } else {
      val siteConfig = redisClient.getSiteConfig(uuid)
      //如果缓存中拿不到，将这个uuid发到队列中，让EventListener去处理
      if (siteConfig.isEmpty) {
        val event = new PublisherSiteConfigEvent
        event.setUuid(uuid)
        Servers.kestrelClient.put(SITE_CONFIG_EVENT_QUEUE, event)
      }
      siteConfig
    }
  }

  /**
   * 对搜索到的广告进行重新排序，排序公式为 searchScore（即投放页面内容的topic和广告topic的匹配度） * adPriority * eCPM * campaignBalanceRatios
   */
  private def getTopAds(ads: List[HitAd], adPriorities: Map[String, Double], cbrs: Map[String, Double], ctrs: Map[String, AdCtr], count: Int): (List[HitAd], Int) = {

    if (ads.length <= 1) {
      val timeAdSize = if (ads.length == 1) {
        if (isTimeAd(ads.apply(0))) {
          1
        } else {
          0
        }
      } else {
        0
      }
      return (ads, timeAdSize)
    }

    //按点击付费的广告
    val clickPayAds = ads.filter(ad => ad.bidType == BidTypeEnum.CPC.getCode || ad.bidType == BidTypeEnum.CPS.getCode || ad.bidType == BidTypeEnum.CPA.getCode)

    //非按点击付费的广告
    val unClickPayAds = ads.filter(ad => ad.bidType == BidTypeEnum.CPM.getCode)

    //cpt广告优先级最高，其次是cpd
    val cptPayAds = dedup(ads.filter(ad => ad.bidType == BidTypeEnum.CPT.getCode))

    val cpdPayAds = dedup(ads.filter(ad => ad.bidType == BidTypeEnum.CPD.getCode))

    val minCbr = if (cbrs.size > 0) {
      cbrs.values.min
    } else {
      1.0
    }

    cptPayAds.foreach(hitAd => hitAd.score = Double.MaxValue)
    cpdPayAds.foreach(hitAd => hitAd.score = Double.MaxValue / 2.0)

    unClickPayAds foreach { ad =>
      val cbr = cbrs getOrElse (ad.campaignId, minCbr)
      val priority = adPriorities getOrElse (ad.campaignId, 1.0)
      //eCPM = bidprice / 1000 表示一次浏览记录带来的收益
      ad.score = ad.score * cbr * priority * ad.bidPrice / 1000.0
      /****for test start***/
      //val reason = "hitAd.score: " +  ad.score + " cbr: " + cbr + " priority: " + priority
      //ad.description = reason
      /****for test end***/
    }

    val maxCtr = if (ctrs.size > 0) {
      val maxCtr = ctrs.values.map(c => c.ctr).max
      if (maxCtr == 0.0) {
        1.0
      } else {
        maxCtr
      }
    } else {
      1.0
    }

    val realBidPriceList = clickPayAds filter (hitAd => hitAd.bidPrice > 0.0) map (hitAd => hitAd.bidPrice.toDouble)

    val minBidPrice = if (realBidPriceList.size > 0) {
      Math.min(realBidPriceList.min, 0.001)
    } else {
      1.0
    }

    clickPayAds foreach { ad =>
      val bidPrice = if (ad.bidPrice > 0.0) {
        ad.bidPrice.toDouble
      } else {
        minBidPrice
      }
      val cbr = cbrs getOrElse (ad.campaignId, minCbr)
      val priority = adPriorities getOrElse (ad.campaignId, 1.0)
      //如果一个广告的ctr未知，说明它从未被展示，今后对于这种广告会进行CTR预估，目前暂时先设定其CTR为max进行优先投放，防止投放广告产生马太效应
      val ctr = if (ctrs.contains(ad.entryId)) {
        val curCtr = ctrs.get(ad.entryId).get.ctr
        if (curCtr == 0.0) {
          maxCtr
        } else {
          curCtr
        }
      } else {
        maxCtr
      }
      //eCPM = bidprice * ctr 表示一次浏览记录带来的收益
      ad.score = ad.score * bidPrice * ctr * priority * cbr
      /****for test start***/
      //val reason = "hitAd.score: " +  ad.score + " bidPrice: " + bidPrice + " cbr: " + cbr + " priority: " + priority + " ctr: " + ctr
      //ad.description = reason
      /****for test end***/
    }
    var usedTitles = Set[String]()
    var usedCampaignIds = Set[String]()
    var timeAdSize = 0
    val results = ListBuffer[HitAd]()
    (clickPayAds ++ unClickPayAds ++ cptPayAds ++ cpdPayAds) sort ((hitAd1, hitAd2) => hitAd1.score > hitAd2.score) foreach { hitAd =>
      val title = hitAd.title
      val campaignId = hitAd.campaignId
      if (!usedTitles.contains(title) && !usedCampaignIds.contains(campaignId)) {
        results.append(hitAd)
        usedTitles += title
        usedCampaignIds += campaignId
        if (isTimeAd(hitAd)) {
          timeAdSize += 1
        }
      }
    }
    return (results.result.slice(0, count), timeAdSize)
  }

  /**
   * 按照campaignId进行去重，campaignId相同的，看byBidPrice，如果为true，则根据bidPrice取最高者，
   * 如果bidPrice有多个广告相同，则随机选取其中的一个；如果bidPrice为false，则直接进行随机选取
   */
  def dedup(ads: List[HitAd], byBidPrice: Boolean = false): List[HitAd] = {
    ads.groupBy(ad => ad.campaignId).map {
      case (campaignId, ads) =>
        if (byBidPrice) {
          var prevBidPrice = Double.MinValue
          var candidates = List[HitAd]()
          ads.foreach { ad =>
            if (ad.bidPrice > prevBidPrice) {
              candidates = List(ad)
              prevBidPrice = ad.bidPrice
            } else if (ad.bidPrice == prevBidPrice) {
              candidates = candidates ++ List(ad)
            } else {
              // do nothing
            }
          }
          if (candidates.size == 1) {
            candidates.apply(0)
          } else {
            val random = System.currentTimeMillis % candidates.size
            candidates.apply(random.toInt)
          }
        } else {
          val random = System.currentTimeMillis % ads.size
          ads.apply(random.toInt)
        }
    }.toList
  }

  /**
   * 判定这个广告是否为包时段广告
   */
  def isTimeAd(ad: HitAd): Boolean = {
    if (ad.bidType == BidTypeEnum.CPT.getCode || ad.bidType == BidTypeEnum.CPD.getCode) {
      true
    } else {
      false
    }
  }

  private def combine(res: List[HitAd], preferAds: List[HitAd], timeAdSize: Int, count: Int): List[HitAd] = {
    var usedTitles = Set[String]()
    var usedCampaignIds = Set[String]()
    val results = ListBuffer[HitAd]()
    (res.slice(0, timeAdSize) ++ preferAds ++ res.slice(timeAdSize, res.size)).foreach { hitAd =>
      val title = hitAd.title
      val campaignId = hitAd.campaignId
      if (!usedTitles.contains(title) && !usedCampaignIds.contains(campaignId)) {
        results.append(hitAd)
        usedTitles += title
        usedCampaignIds += campaignId
      }
    }
    results.toList.slice(0, count)
  }

}

object TopicRecommend {

  val topicSmoothCoefficient = 0.001
  val te = new com.buzzinate.nlp.util.TitleExtractor
  val modelFile = file(Servers.ldaModelPath)
  SimpleChineseTokenizer
  val ldaModel = LoadGibbsLDA(modelFile)

  val topicTermCount = getTopicTermCount
  val topicTermIndex = ldaModel.termIndex.get

  def getTopicTermCount(): Array[Double] = {
    val topicTermCount = Array.make(ldaModel.countTopicTerm.apply(0).size, 0.0)
    ldaModel.countTopicTerm foreach { perTopicTermCount =>
      for (i <- 0 until perTopicTermCount.size) {
        topicTermCount update (i, topicTermCount.apply(i) + perTopicTermCount.apply(i))
      }
    }
    topicTermCount
  }

  def getWeighedWords(keywordsInfos: List[KeywordInfo]): String = {
    var minIdf = Double.MaxValue
    val word2idf = keywordsInfos.foldLeft(Map[String, Double]()) { (word2idf, keywordInfo) =>
      val word = keywordInfo.word
      val index = topicTermIndex.apply(word)
      if (index >= 0) {
        val idf = Math.log(50000 / (1 + topicTermCount.apply(index)))
        val weighedIdf = Math.pow(idf, 2.0)
        if (minIdf > weighedIdf) minIdf = weighedIdf
        word2idf + (word -> weighedIdf)
      } else {
        word2idf
      }
    }
    val weighedWords = if (minIdf == Double.MaxValue) {
      keywordsInfos.map(rk => rk.word).mkString(",")
    } else {
      val weighedWordsSb = word2idf.foldLeft(new StringBuilder) { (sb, w2idf) =>
        val (word, quantum) = (w2idf._1, Math.round(w2idf._2 / minIdf))
        for (i <- 0l until quantum) {
          sb.append(word).append(",")
        }
        sb
      }
      weighedWordsSb.toString
    }
    weighedWords
  }

  def getTopTopicText(distribution: Array[Double], topN: Int): String = {
    val tdpq = new DoublePriorityQueue[Int](topN)
    var counter = 0
    distribution foreach { prob =>
      tdpq.add(prob, counter)
      counter += 1
    }
    val topTopics = tdpq.entries

    topTopics.asScala.map { kv =>
      val (prob, index) = (kv.key, kv.value)
      index + "|" + prob
    }.mkString(" ")
  }

  /**
   * 给定广告的topic分布，format，并将概率为0的分布做平滑后，以String的形式输出
   */
  def getTopicDistStr(distribution: Array[Double], weight: Double = 1.0): String = {
    var counter = 0
    distribution.map { dist =>
      val counterProb =
        if (dist == 0.0) {
          counter + "|" + topicSmoothCoefficient * weight
        } else {
          counter + "|" + dist * weight
        }
      counter += 1
      counterProb
    }.mkString(" ")
  }

}