package com.buzzinate.advertise.redis

import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.mutable.ListBuffer

import com.buzzinate.advertise.redis.json.AdCtr
import com.buzzinate.advertise.redis.json.CampaignBalanceRatio
import com.buzzinate.advertise.redis.json.CampaignPriority
import com.buzzinate.advertise.redis.json.SiteConfig
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Constants
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdStats
import com.buzzinate.buzzads.data.thrift.TCampaignBudget

import RedisClient.adClickThroughRatePrefix
import RedisClient.balanceRatioPrefix
import RedisClient.blackChannelPrefix
import RedisClient.campaignPriorityPrefix
import RedisClient.pageTopicPrefix
import RedisClient.remainSecsInDay
import RedisClient.siteConfigPrefix
import redis.clients.jedis.ShardedJedisPool

class RedisClient(pool: ShardedJedisPool) extends Loggable {
  import RedisClient._
  import com.buzzinate.advertise.server.Message._
  private val redisCache = new RedisCache(pool)

  val historyCtrWeight = Math.pow(Math.E, -Servers.ctrAttGravity)

  /**
   * 根据campaign 的id获得其对应的balance ratio
   */
  def getCampaignBalanceRatio(campaignIds: List[String]): Map[String, Double] = {
    val keys = campaignIds map (id => balanceRatioPrefix + id)
    val kvs = redisCache.batchGetCache[CampaignBalanceRatio](keys)
    var id2ratio = Map[String, Double]()
    kvs foreach { case (_, cbr) =>
        id2ratio += cbr.campaignId -> cbr.balanceRatio
    }
    id2ratio
  }

  /**
   * 根据对应的ad id，获得其历史点击率
   */
  def getAdCTR(adIds: List[String]): Map[String, AdCtr] = {
    val keys = adIds map (id => adClickThroughRatePrefix + id)
    val kvs = redisCache.batchGetCache[AdCtr](keys)
    var id2ctr = Map[String, AdCtr]()
    kvs foreach { case (_, adCtr) =>
        id2ctr += adCtr.adId -> adCtr
    }
    id2ctr
  }

  /**
   * 获得uuid对应投放站点的一些设置
   */
  def getSiteConfig(uuid: String): Option[SiteConfig] = {
    val conf = redisCache.getCache[SiteConfig](siteConfigPrefix + uuid)
    if (conf.isEmpty) {
      None
    } else {
      Some(conf.get._1)
    }
  }

  /**
   * 根据campaignId，获得对应的 priority
   */
  def getAdPriority(campaignIds: List[String]): Map[String, Double] = {
    val keys = campaignIds map (id => campaignPriorityPrefix + id)
    val kvs = redisCache.batchGetCache[CampaignPriority](keys)
    var id2priority = Map[String, Double]()
    kvs foreach { case (_, campaignPriority) =>
        id2priority += campaignPriority.campaignId -> campaignPriority.priority
    }
    id2priority
  }

  /**
   * 获得url对应的topTopic
   */
  def getPageTopic(url: String): String = {
    redisCache.getCache(pageTopicPrefix + url)
  }
  
  def getBlackChannel(): List[String] = {
    RedisCache.use(pool) { jedis =>
      jedis.smembers(blackChannelPrefix).asScala.toList
    }
  }

  /**
   * 根据uuid，更新对应站点的设置
   */
  def updateSiteConfig(uuid: String, config: SiteConfig): Unit = {
    //1小时后缓存失效
    redisCache.putCache(siteConfigPrefix + uuid, config, 3600)
  }

  /**
   * 更新url对应页面的top topicDistribution
   */
  def updatePageTopic(url: String, pageTopic: String, expireInSecs: Int): Unit = {
    redisCache.putCache(pageTopicPrefix + url, pageTopic, expireInSecs)
  }

  /**
   * 更新广告的clickThroughRate
   */
  def updateAdCtr(adStats: List[TAdStats]): Unit = {
    val ids = adStats map { stat =>
      info(stat.toString)
      stat.getEntryId.toString
    }
    val historyCtrs = getAdCTR(ids)
    val id2Stats = new ListBuffer[(String, AdCtr)]
    adStats foreach { stat =>
      val id = stat.getEntryId.toString
      val currentCtr = if (historyCtrs.contains(id)) {
        val historyCtr = historyCtrs.get(id).get
        if (historyCtr.lastUpdateDay != stat.getDateDay && historyCtr.ctr < 1.0) {
          historyCtrWeight * historyCtr.ctr + (1 - historyCtrWeight) * (stat.getCpcClicks.toDouble / stat.getViews.toDouble)
        } else {
          stat.getCpcClicks.toDouble / stat.getViews.toDouble
        }
      } else {
        stat.getCpcClicks.toDouble / stat.getViews.toDouble
      }
      if (currentCtr >= 0.0 && currentCtr < 1.0) {
        val currentAdCtr = new AdCtr
        currentAdCtr.adId = stat.getEntryId.toString
        currentAdCtr.lastUpdateDay = stat.getDateDay
        currentAdCtr.ctr = currentCtr
        id2Stats.append((adClickThroughRatePrefix + stat.getEntryId, currentAdCtr))
      }
    }
    id2Stats.foreach(idstat => debug(idstat._1 + " : " + idstat._2.getAdId() + " : " + idstat._2.getCtr()))
    redisCache.batchPutCache(id2Stats.result)
  }

  /**
   * 更新campaign的BalaceRatio
   */
  def updateCampaignBalanceRatio(campaignBudgets: List[TCampaignBudget]): Unit = {
    val id2cbr = new ListBuffer[(String, CampaignBalanceRatio)]
    campaignBudgets foreach { cb =>
      info("campaignBalanceRatio, campaignid: " + cb.toString)
      val maxBudgetDay = if (cb.getMaxBudgetDay == 0l) {
        Long.MaxValue
      } else {
        cb.getMaxBudgetDay
      }

      val maxBudgetTotal = if (cb.getMaxBudgetTotal == 0l) {
        Long.MaxValue
      } else {
        cb.getMaxBudgetTotal
      }
      if ((maxBudgetDay - cb.getBudgetDay) > 0l && cb.getAdvertiserBalance > 0l && maxBudgetTotal - cb.getBudgetTotal > 0l && maxBudgetTotal - cb.getBudgetTotal + cb.getBudgetDay > 0l) {
        val campaignBalanceRatio = Math.min(Math.min(maxBudgetDay - cb.getBudgetDay, maxBudgetTotal - cb.getBudgetTotal), cb.getAdvertiserBalance).toDouble / Math.min(Math.min(maxBudgetDay, maxBudgetTotal - cb.getBudgetTotal + cb.getBudgetDay), cb.getAdvertiserBalance + cb.getBudgetDay).toDouble
        info("campaignBalanceRatio, campaignid: " + cb.getCampaignId + " score : " + campaignBalanceRatio)
        if (campaignBalanceRatio > 0) {
          val balanceScore = 1.0 - Math.pow(Math.E, -campaignBalanceRatio)
          val cbr = new CampaignBalanceRatio
          cbr.campaignId = cb.getCampaignId.toString
          cbr.balanceRatio = balanceScore
          id2cbr.append((balanceRatioPrefix + cb.getCampaignId, cbr))
        }
      }
    }
    redisCache.batchPutCache(id2cbr.result)
  }

  /**
   * 更新campaign的priority
   */
  def updateCampaignPriority(priorities: List[CampaignPriority]): Unit = {
    val id2priority = priorities.map { priority => campaignPriorityPrefix + priority.campaignId -> priority
    }
    redisCache.batchPutCache(id2priority, remainSecsInDay)
  }
  
  def flushBlackChannel(domains: List[String]): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.del(blackChannelPrefix)
      val p = jedis.pipelined
      domains map { domain =>
        p.sadd(blackChannelPrefix, domain)
      }
      p.sync
    }
  }

  def updateBlackChannel(domain: String): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.sadd(blackChannelPrefix, domain)
    }
  }
  
  def openChannel(doamin: String): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.srem(blackChannelPrefix, doamin)
    }
  }

}

object RedisClient {

  val balanceRatioPrefix = "campaign:balace:ratio"
  val campaignPriorityPrefix = "campaign:priority"
  val adClickThroughRatePrefix = "ad:ctr"
  val pageTopicPrefix = "page:topic"
  val siteConfigPrefix = "engine:siteconfig"
  val countCookieSeeAdTimesPrefix = "cookie:seenad:times"
  val blackChannelPrefix = "black:channels"

  def remainSecsInDay(): Int = {
    val secs = System.currentTimeMillis / 1000l
    Constants.SECS_ONE_DAY - ((secs + 8 * 3600) % Constants.SECS_ONE_DAY).toInt
  }

}