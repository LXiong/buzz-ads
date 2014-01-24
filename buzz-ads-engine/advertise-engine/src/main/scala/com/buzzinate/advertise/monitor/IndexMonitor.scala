package com.buzzinate.advertise.monitor

import java.lang.Object
import java.util.concurrent.TimeUnit
import java.util.HashMap
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.mutable.ListBuffer
import org.apache.commons.lang.StringUtils
import org.elasticsearch.indices.IndexMissingException
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum
import com.buzzinate.advertise.elasticsearch.api.Ad
import com.buzzinate.advertise.kestrel.MetaAd
import com.buzzinate.advertise.redis.LimitAdRedisClient
import com.buzzinate.advertise.redis.RedisClient
import com.buzzinate.advertise.server.Message.CheckIndex
import com.buzzinate.advertise.server.Message.CheckUpdateField
import com.buzzinate.advertise.server.Message.OrderLevel
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.tools.LdaUtil
import com.buzzinate.advertise.util.Constants.BLANK_CHANNEL
import com.buzzinate.advertise.util.Constants.PRIORITY_CPC
import com.buzzinate.advertise.util.Constants.PRIORITY_CPD
import com.buzzinate.advertise.util.Constants.PRIORITY_CPM
import com.buzzinate.advertise.util.Constants.PRIORITY_CPT
import com.buzzinate.advertise.util.CalendarUtil
import com.buzzinate.advertise.util.Constants
import com.buzzinate.advertise.util.LinkParser
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdItem
import com.buzzinate.buzzads.data.thrift.TPagination
import com.buzzinate.buzzads.enums.BidTypeEnum
import com.buzzinate.common.util.ip.ProvinceName
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.util.Duration
import com.buzzinate.advertise.server.Message.EntryLevel

class IndexMonitor extends Actor with Loggable {
  import com.buzzinate.advertise.util.Constants._

  val redisClient = new RedisClient(Servers.jedisPool)
  val limitAdRedisClient = new LimitAdRedisClient(Servers.jedisPool)

  //对bidType对不同类型广告的投放优先级进行设置
  val cpmPriority = Servers.prop.getString(PRIORITY_CPM).toDouble
  val cpcPriority = Servers.prop.getString(PRIORITY_CPC).toDouble
  val cptPriority = Servers.prop.getString(PRIORITY_CPT).toDouble
  val cpdPriority = Servers.prop.getString(PRIORITY_CPD).toDouble

  def receive = {

    //按照要求，更新索引中某条广告的指定field
    case CheckUpdateField(id, fieldName, fieldValue) => {
      try {
        val existedAd = Servers.esClient.get(List(id).asJava)
        if (!existedAd.isEmpty) {
          val updateFields = new java.util.HashMap[String, Object]
          updateFields.put(fieldName, fieldValue)
          Servers.esClient.update(id, updateFields)
        } else {
          warn("[CheckUpdateField]: the EntryId : " + id + " dosen't exist . FieldName : " + fieldName + "; FieldValue : " + fieldValue.toString)
        }
      } catch {
        case e: Exception => {
          warn("[CheckUpdateField]: " + e.getMessage)
        }
      }
    }

    //保持索引和buzz-ads系统中的数据一致性，当monitMode是true时，表示这是一个不断定时执行的任务
    case CheckIndex(criteria, monitMode, initMode) => {
      try {
        var isLastPage = false
        var start = 0
        val count = 50
        val pBuilder = new TPagination.Builder
        pBuilder.count(count.shortValue)
        val newAds = ListBuffer[Ad]()
        while (!isLastPage) {
          info("monitMode => " + monitMode + " page info => " + start + " , " + count)
          pBuilder.start(start.shortValue)
          val adItems = Servers.adDataAccessClient.findAdItems(criteria, pBuilder.build).get().asScala.toList
          info("get " + adItems.length + " items")
          val legalItems = adItems filter { item =>
            //将需要抓取相关信息的广告放入kestrel队列供 advertise-crawler抓取
            if (item.getLinkOption.isDefined && (!item.getTitleOption.isDefined || !item.getDestinationOption.isDefined)) {
              val metaAd = new MetaAd()
              metaAd.setEntryId(item.getEntryId)
              metaAd.setOrderId(item.getOrderId)
              metaAd.setStatus(item.getStatus.getValue)
              metaAd.setLink(item.getLink)

              if (item.getTitleOption.isDefined) {
                metaAd.setTitle(item.getTitle)
              }
              if (item.getKeywordsOption.isDefined) {
                metaAd.setKeywords(item.getKeywords)
              }
              if (item.getResourceUrlOption.isDefined) {
                metaAd.setResourceUrl(item.getResourceUrl)
              }
              if (item.getTitleOption.isDefined && !item.getDestinationOption.isDefined) {
                metaAd.setIsTrackRealurlMode(true)
              } else {
                metaAd.setIsTrackRealurlMode(false)
              }
              Servers.kestrelClient.put(Constants.CRAWL_AD_QUEUE, metaAd)
            }
            isLegalAd(item)
          }
          if (legalItems.length > 0) {
            val entryIds = legalItems map (item => item.getEntryId.toString)
            info("search existedAds ...")
            val existedAds = try { Servers.esClient.get(entryIds.asJava) } catch {
              // 初次初始化索引之前，会遇到这种情况
              case e: IndexMissingException => {
                new HashMap[String, Ad]()
              }
            }
            info("existedAds size : " + existedAds.asScala.size)
            val existedAdIds = existedAds.keySet().asScala
            legalItems foreach { item =>
              val entryId = item.getEntryId.toString
              if (existedAdIds.contains(entryId)) {
                val updateFields = getUpdateFieldsInfo(item, existedAds.get(entryId))
                if (updateFields.size > 0) {
                  Servers.esClient.update(entryId, updateFields)
                }
              } else {
                newAds.append(convert(item))
              }
            }
            if (newAds.size > count) {
              info("esClient bulkAdd ads(in loop), size : " + newAds.length)
              Servers.esClient.bulkAdd(newAds.result.asJava)
              newAds.clear
            }
            // use the timestamp as asnyc key
            Servers.asnyc.asnyc(System.currentTimeMillis.toString) {
              processCampaignRatio(legalItems map (item => new Integer(item.getCampaignId)))
            }
          }
          if (adItems.size < count || !monitMode) {
            isLastPage = true
          }
          start += count
        }

        if (newAds.size > 0) {
          info("esClient bulkAdd ads, size : " + newAds.length)
          Servers.esClient.bulkAdd(newAds.result.asJava)
          newAds.clear
        }
      } catch {
        case e: Exception => {
          error("IndexMonitor Exception => " + e.getMessage())
          e.getStackTrace().foreach(trace => error(trace.toString()))
        }
      }
      if (monitMode) {
          val monitSegTime = if (initMode) {
            //从00：01：00开始，每隔6小时重新check一遍索引
            Duration.apply(RedisClient.remainSecsInDay % (Constants.SECS_ONE_DAY / 4) + 60, TimeUnit.SECONDS)
          } else {
            Duration.apply(6, TimeUnit.HOURS)
          }
          Servers.system.scheduler.scheduleOnce(monitSegTime) {
            self ! CheckIndex(criteria, monitMode, false)
          }
        }
    }
  }

  private def isLegalAd(item: TAdItem): Boolean = {
    return item.getTitleOption.isDefined && item.getLinkOption.isDefined
  }

  private def processCampaignRatio(campaignIds: List[Integer]): Unit = {
    val cbs = Servers.adDataAccessClient.findCampaignBudgets(campaignIds.distinct.asJava).get.asScala.toList
    if (cbs.size > 0) {
      redisClient.updateCampaignBalanceRatio(cbs)
    }
  }

  private def convert(item: TAdItem): Ad = {
    val ad = new Ad

    ad.setAdvertiserId(item.getAdvertiserId.toString)
    ad.setCampaignId(item.getCampaignId.toString)
    ad.setEntryId(item.getEntryId.toString)
    ad.setGroupId(item.getOrderId.toString)
    ad.setStatus(item.getStatus.getValue)

    var adWeight = 1.0

    if (item.getBidTypeOption.isDefined) {
      ad.setBidType(item.getBidType.getValue)
      if (item.getBidType.getValue == BidTypeEnum.CPM.getCode) {
        adWeight = cpmPriority
      } else if (item.getBidType.getValue == BidTypeEnum.CPC.getCode) {
        adWeight = cpcPriority
      } else if (item.getBidType.getValue == BidTypeEnum.CPT.getCode) {
        adWeight = cptPriority
      } else if (item.getBidType.getValue == BidTypeEnum.CPD.getCode) {
        adWeight = cpdPriority
      } else {

      }
    }

    if (item.getBidType.getValue == BidTypeEnum.CPT.getCode || item.getBidType.getValue == BidTypeEnum.CPD.getCode) {
      ad.setTopicDistribution(LdaUtil.uniformDistribution(adWeight))
    } else {
      val realKeywords =
        if (item.getKeywordsOption.isDefined) {
          item.getKeywords
        } else {
          ""
        }
      val (topicStr, topTopicStr) = LdaUtil.getTopicDistribution(item.getTitle, realKeywords, Servers.topnTopic, adWeight)
      ad.setTopicDistribution(topicStr)
      if (!StringUtils.isEmpty(topTopicStr)) {
        ad.setTopTopicDistribution(topTopicStr)
      }
    }

    if (item.getBidPriceOption.isDefined) {
      ad.setBidPrice(item.getBidPrice.toDouble)
    }

    if (item.getAudienceCategoriesOption.isDefined) {
      val mainAudienceCats = item.getAudienceCategories.asScala.filter(cat => cat < 100).mkString(",")
      val subAudienceCats = item.getAudienceCategories.asScala.filterNot(cat => cat < 100).mkString(",")
      if (!StringUtils.isEmpty(mainAudienceCats)) {
        ad.setAudienceMainCat(mainAudienceCats)
      }
      if (!StringUtils.isEmpty(subAudienceCats)) {
        ad.setAudienceSubCat(subAudienceCats)
      }
    }

    if (item.getStartDateOption.isDefined) {
      ad.setStartTime(item.getStartDate)
    }

    if (item.getEndDateOption.isDefined) {
      ad.setEndTime(item.getEndDate)
    }

    if (item.getDescriptionOption.isDefined) {
      ad.setDescription(item.getDescription)
    }

    if (item.getDisplayUrlOption.isDefined) {
      ad.setDisplayUrl(item.getDisplayUrl)
    }

    if (item.getDestinationOption.isDefined) {
      ad.setRealUrl(item.getDestination)
    }

    if (item.getKeywordsOption.isDefined) {
      ad.setKeyword(item.getKeywords)
    }

    if (item.getLinkOption.isDefined) {
      ad.setLink(item.getLink)
    }

    if (item.getLocationsOption.isDefined) {
      if (item.getLocations().size() > 0) {
        ad.setLocation(item.getLocations.asScala.mkString(","))
      }

    }

    if (item.getNetworkOption.isDefined) {
      if (item.getNetwork.size > 0) {
        ad.setNetwork(item.getNetwork.asScala.map(nw => nw.getValue.toString).mkString(","))
      }
    }

    if (item.getResourceTypeOption.isDefined) {
      ad.setResourceType(item.getResourceType.getValue)
    }

    if (item.getResourceUrlOption.isDefined) {
      ad.setResourceUrl(item.getResourceUrl)
    }
    
    ad.setResourceSize(item.getResourceSize.getValue.toString)

    if (item.getScheduleDayOption.isDefined) {
      if (item.getScheduleDay.size > 0) {
        ad.setScheduleDay(item.getScheduleDay.asScala.map(sd => sd.getValue.toString).mkString(","))
      }
    }

    if (item.getScheduleTimeOption.isDefined) {
      if (item.getScheduleTime.size > 0) {
        //当前只有一个时间区段，将来可能会是多个
        val scheduleTime = item.getScheduleTime().asScala.head
        ad.setStartScheduleTime(CalendarUtil.getMinute(scheduleTime.getStart))
        ad.setEndScheduleTime(CalendarUtil.getMinute(scheduleTime.getEnd))
      }
    }

    if (item.getTitleOption.isDefined) {
      ad.setTitle(item.getTitle)
    }

    if (item.getChannelsOption.isDefined) {
      if (!item.getChannels.isEmpty) {
        ad.setChannel(item.getChannels.asScala.map(channel => LinkParser.formatDomain(channel)).mkString(","))
      }
    }

    if (item.getOrderFrequency > 0) {
      Servers.asnyc.asnyc(item.getOrderId.toString) {
        limitAdRedisClient.updateLimitAdSet(List(item.getOrderId.toString), new OrderLevel)
        limitAdRedisClient.updateLimitAdShowTimes(item.getOrderId.toString, item.getOrderFrequency.toDouble, new OrderLevel)
      }
    }
    
    if (item.getEntryFrequency > 0) {
      Servers.asnyc.asnyc(item.getEntryId.toString) {
        limitAdRedisClient.updateLimitAdSet(List(item.getEntryId.toString), new EntryLevel)
        limitAdRedisClient.updateLimitAdShowTimes(item.getEntryId.toString, item.getEntryFrequency.toDouble, new EntryLevel)
      }
    }

    ad

  }

  private def getUpdateFieldsInfo(item: TAdItem, existedAd: Ad): java.util.Map[String, Object] = {
    val updateFields = new java.util.HashMap[String, Object]

    var adWeight = 1.0

    if (item.getBidTypeOption.isDefined) {
      if (item.getBidType.getValue != existedAd.bidType) {
        updateFields.put(FieldEnum.BID_TYPE.getFieldName, new java.lang.Integer(item.getBidType.getValue))
      }
      if (item.getBidType.getValue == BidTypeEnum.CPM.getCode) {
        adWeight = cpmPriority
      } else if (item.getBidType.getValue == BidTypeEnum.CPC.getCode) {
        adWeight = cpcPriority
      } else if (item.getBidType.getValue == BidTypeEnum.CPT.getCode) {
        adWeight = cptPriority
      } else if (item.getBidType.getValue == BidTypeEnum.CPD.getCode) {
        adWeight = cpdPriority
      } else {
        //do nothing
      }
    }

    var topicStr = ""
    var topTopicStr = ""

    if (item.getBidType.getValue == BidTypeEnum.CPT.getCode || item.getBidType.getValue == BidTypeEnum.CPD.getCode) {
      topicStr = LdaUtil.uniformDistribution(adWeight)
    } else {
      val realKeywords =
        if (item.getKeywordsOption.isDefined) {
          item.getKeywords
        } else {
          ""
        }
      val (ldaTopicStr, ldaTopTopicStr) = LdaUtil.getTopicDistribution(item.getTitle, realKeywords, Servers.topnTopic, adWeight)
      topicStr = ldaTopicStr
      topTopicStr = ldaTopTopicStr
    }

    if (!topicStr.equals(existedAd.topicDistribution)) {
      updateFields.put(FieldEnum.TOPIC_DISTRIBUTION.getFieldName, topicStr)
      if (!StringUtils.isEmpty(topTopicStr)) {
        updateFields.put(FieldEnum.TOP_TOPIC_DISTRIBUTION.getFieldName, topTopicStr)
      }
    }

    if (!item.getAdvertiserId.toString.equals(existedAd.advertiserId)) {
      updateFields.put(FieldEnum.ADVERTISER_ID.getFieldName, item.getAdvertiserId.toString)
    }

    if (!item.getCampaignId.toString.equals(existedAd.campaignId)) {
      updateFields.put(FieldEnum.CAMPAIGN_ID.getFieldName, item.getCampaignId.toString)
    }

    if (!item.getOrderId.toString.equals(existedAd.groupId)) {
      updateFields.put(FieldEnum.GROUP_ID.getFieldName, item.getOrderId.toString)
    }

    if (item.getStatus.getValue != existedAd.status) {
      updateFields.put(FieldEnum.STATUS.getFieldName, new java.lang.Integer(item.getStatus.getValue))
    }

    if (item.getBidPriceOption.isDefined) {
      if (item.getBidPrice.toDouble != existedAd.bidPrice) {
        updateFields.put(FieldEnum.BID_PRICE.getFieldName, new java.lang.Double(item.getBidPrice.toDouble))
      }
    }

    if (item.getAudienceCategoriesOption.isDefined) {
      val mainAudienceCats = item.getAudienceCategories.asScala.filter(cat => cat < 100).mkString(",")
      val subAudienceCats = item.getAudienceCategories.asScala.filterNot(cat => cat < 100).mkString(",")
      if (!mainAudienceCats.equals(existedAd.audienceMainCat)) {
        if (StringUtils.isEmpty(mainAudienceCats)) {
          updateFields.put(FieldEnum.AUDIENCE_MAIN_CAT.getFieldName, Constants.BLANK_CATEGORY)
        } else {
          updateFields.put(FieldEnum.AUDIENCE_MAIN_CAT.getFieldName, mainAudienceCats)
        }
      }
      if (!subAudienceCats.equals(existedAd.audienceSubCat)) {
        if (StringUtils.isEmpty(subAudienceCats)) {
          updateFields.put(FieldEnum.AUDIENCE_SUB_CAT.getFieldName, Constants.BLANK_CATEGORY)
        } else {
          updateFields.put(FieldEnum.AUDIENCE_SUB_CAT.getFieldName, subAudienceCats)
        }
      }
    } else {
      updateFields.put(FieldEnum.AUDIENCE_MAIN_CAT.getFieldName, Constants.BLANK_CATEGORY)
      updateFields.put(FieldEnum.AUDIENCE_SUB_CAT.getFieldName, Constants.BLANK_CATEGORY)
    }

    //如果开始日期没有设定，则检查索引中的开始日期是否是默认值
    if (item.getStartDateOption.isDefined) {
      if (item.getStartDate != existedAd.startTime) {
        updateFields.put(FieldEnum.START_TIME.getFieldName, new java.lang.Long(item.getStartDate))
      }
    } else {
      if (existedAd.startTime != CalendarUtil.MIN_START_TIME) {
        updateFields.put(FieldEnum.START_TIME.getFieldName, new java.lang.Long(CalendarUtil.MIN_START_TIME))
      }
    }

    //如果结束日期没有设定，则检查索引中的结束日期是否是默认值
    if (item.getEndDateOption.isDefined) {
      if (item.getEndDate != existedAd.endTime) {
        updateFields.put(FieldEnum.END_TIME.getFieldName, new java.lang.Long(item.getEndDate))
      }
    } else {
      if (existedAd.endTime != CalendarUtil.MAX_END_TIME) {
        updateFields.put(FieldEnum.END_TIME.getFieldName, new java.lang.Long(CalendarUtil.MAX_END_TIME))
      }
    }

    if (item.getDescriptionOption.isDefined) {
      if (!item.getDescription.equals(existedAd.description)) {
        updateFields.put(FieldEnum.DESCRIPTION.getFieldName, item.getDescription)
      }
    }

    if (item.getDisplayUrlOption.isDefined) {
      if (!item.getDisplayUrl.equals(existedAd.displayUrl)) {
        updateFields.put(FieldEnum.DISPLAY_URL.getFieldName, item.getDisplayUrl)
      }
    }

    if (item.getKeywordsOption.isDefined) {
      if (!item.getKeywords.equals(existedAd.keyword)) {
        updateFields.put(FieldEnum.KEYWORD.getFieldName, item.getKeywords)
      }
    }

    if (item.getLinkOption.isDefined) {
      if (!item.getLink.equals(existedAd.link)) {
        //如果link有变化，需要对其指向的实际url进行重抓
        val metaAd = new MetaAd
        metaAd.setEntryId(item.getEntryId)
        metaAd.setOrderId(item.getOrderId)
        metaAd.setStatus(item.getStatus.getValue)
        metaAd.setLink(item.getLink)
        metaAd.setIsTrackRealurlMode(true)
        Servers.kestrelClient.put(Constants.CRAWL_AD_QUEUE, metaAd)
        updateFields.put(FieldEnum.LINK.getFieldName, item.getLink)
      }
    }

    if (item.getDestinationOption.isDefined) {
      if (!item.getDestination.equals(existedAd.realUrl)) {
        updateFields.put(FieldEnum.REALURL.getFieldName, item.getDestination)
      }
    } else if (!StringUtils.isEmpty(existedAd.realUrl)) {
      updateFields.put(FieldEnum.REALURL.getFieldName, "")
    } else {
      //do nothing
    }

    if (item.getLocationsOption.isDefined) {
      if (item.getLocations.size > 0) {
        val locationStr = item.getLocations.asScala.mkString(",")
        if (!locationStr.equals(existedAd.location)) {
          updateFields.put(FieldEnum.LOCATION.getFieldName, locationStr)
        }
      } else {
        if (!existedAd.location.equals(ProvinceName.OTHER.getCode)) {
          updateFields.put(FieldEnum.LOCATION.getFieldName, ProvinceName.OTHER.getCode)
        }
      }
    } else {
      if (!existedAd.location.equals(ProvinceName.OTHER.getCode)) {
        updateFields.put(FieldEnum.LOCATION.getFieldName, ProvinceName.OTHER.getCode)
      }
    }

    if (item.getNetworkOption.isDefined) {
      if (item.getNetwork.size > 0) {
        val netStr = item.getNetwork.asScala.map(nw => nw.getValue.toString).mkString(",")
        if (!netStr.equals(existedAd.network)) {
          updateFields.put(FieldEnum.NETWORK.getFieldName, netStr)
        }
      }
    }

    if (item.getResourceTypeOption.isDefined) {
      if (item.getResourceType.getValue != existedAd.resourceType) {
        updateFields.put(FieldEnum.RESOURCE_TYPE.getFieldName, new java.lang.Integer(item.getResourceType.getValue))
      }
    }

    if (item.getResourceUrlOption.isDefined) {
      if (!item.getResourceUrl.equals(existedAd.resourceUrl)) {
        updateFields.put(FieldEnum.RESOURCE_URL.getFieldName, item.getResourceUrl)
      }
    }
    
    if (!item.getResourceSize.getValue.toString.equals(existedAd.resourceSize)) {
      updateFields.put(FieldEnum.RESOURCE_SIZE.getFieldName, item.getResourceSize.getValue.toString)
    }

    //如果投放在周几没有设定，则检查索引中是否是默认投放日
    if (item.getScheduleDayOption.isDefined) {
      if (item.getScheduleDay.size > 0) {
        val scheduleDaysStr = item.getScheduleDay.asScala.map(sd => sd.getValue.toString).mkString(",")
        if (!scheduleDaysStr.equals(existedAd.scheduleDay)) {
          updateFields.put(FieldEnum.SCHEDULEDAY.getFieldName, scheduleDaysStr)
        }
      } else {
        if (!existedAd.scheduleDay.equals(CalendarUtil.ALL_WEEKDAY.toString)) {
          updateFields.put(FieldEnum.SCHEDULEDAY.getFieldName, CalendarUtil.ALL_WEEKDAY.toString)
        }
      }
    } else {
      if (!existedAd.scheduleDay.equals(CalendarUtil.ALL_WEEKDAY.toString)) {
        updateFields.put(FieldEnum.SCHEDULEDAY.getFieldName, CalendarUtil.ALL_WEEKDAY.toString)
      }
    }

    //如果投放时间段没有设定，则检查是否是默认时间段
    if (item.getScheduleTimeOption.isDefined) {
      if (item.getScheduleTime.size > 0) {
        //当前只有一个时间区段，将来可能会是多个
        val scheduleTime = item.getScheduleTime.asScala.head
        val startScheduleTime = CalendarUtil.getMinute(scheduleTime.getStart)
        val endScheduleTime = CalendarUtil.getMinute(scheduleTime.getEnd)
        if (startScheduleTime != existedAd.startScheduleTime) {
          updateFields.put(FieldEnum.START_SCHEDULETIME.getFieldName, new java.lang.Integer(startScheduleTime))
        }
        if (endScheduleTime != existedAd.endScheduleTime) {
          updateFields.put(FieldEnum.END_SCHEDULETIME.getFieldName, new java.lang.Integer(endScheduleTime))
        }
      } else {
        if (existedAd.startScheduleTime != CalendarUtil.MIN_MINUTES_OF_DAY) {
          updateFields.put(FieldEnum.START_SCHEDULETIME.getFieldName, new java.lang.Integer(CalendarUtil.MIN_MINUTES_OF_DAY))
        }
        if (existedAd.endScheduleTime != CalendarUtil.MAX_MINUTES_OF_DAY) {
          updateFields.put(FieldEnum.END_SCHEDULETIME.getFieldName, new java.lang.Integer(CalendarUtil.MAX_MINUTES_OF_DAY))
        }
      }
    } else {
      if (existedAd.startScheduleTime != CalendarUtil.MIN_MINUTES_OF_DAY) {
        updateFields.put(FieldEnum.START_SCHEDULETIME.getFieldName, new java.lang.Integer(CalendarUtil.MIN_MINUTES_OF_DAY))
      }
      if (existedAd.endScheduleTime != CalendarUtil.MAX_MINUTES_OF_DAY) {
        updateFields.put(FieldEnum.END_SCHEDULETIME.getFieldName, new java.lang.Integer(CalendarUtil.MAX_MINUTES_OF_DAY))
      }
    }

    if (item.getTitleOption.isDefined) {
      if (!item.getTitle.equals(existedAd.title)) {
        updateFields.put(FieldEnum.TITLE.getFieldName, item.getTitle)
      }
    }

    if (item.getChannelsOption.isDefined) {
      if (!item.getChannels.isEmpty) {
        val channelStr = item.getChannels.asScala.map(channel => LinkParser.formatDomain(channel)).mkString(",")
        if (!channelStr.equals(existedAd.channel)){
          updateFields.put(FieldEnum.CHANNEL.getFieldName, channelStr)  
        }
      } else {
        updateFields.put(FieldEnum.CHANNEL.getFieldName, BLANK_CHANNEL)
      }
    } else {
      updateFields.put(FieldEnum.CHANNEL.getFieldName, BLANK_CHANNEL)
    }
    
    if (item.getOrderFrequency > 0) {
      Servers.asnyc.asnyc(item.getOrderId.toString) {
        limitAdRedisClient.updateLimitAdSet(List(item.getOrderId.toString), new OrderLevel)
        limitAdRedisClient.updateLimitAdShowTimes(item.getOrderId.toString, item.getOrderFrequency.toDouble, new OrderLevel)
      }
    } else {
      Servers.asnyc.asnyc(item.getOrderId.toString) {
        limitAdRedisClient.deleteAdFromLimitAdSet(List(item.getOrderId.toString), new OrderLevel)
        limitAdRedisClient.deleteLimitAdShowTimes(item.getOrderId.toString, new OrderLevel)
      }
    }
    
    if (item.getEntryFrequency > 0) {
      Servers.asnyc.asnyc(item.getEntryId.toString) {
        limitAdRedisClient.updateLimitAdSet(List(item.getEntryId.toString), new EntryLevel)
        limitAdRedisClient.updateLimitAdShowTimes(item.getEntryId.toString, item.getEntryFrequency.toDouble, new EntryLevel)
      }
    } else {
      Servers.asnyc.asnyc(item.getEntryId.toString) {
        limitAdRedisClient.deleteAdFromLimitAdSet(List(item.getEntryId.toString), new EntryLevel)
        limitAdRedisClient.deleteLimitAdShowTimes(item.getEntryId.toString, new EntryLevel)
      }
    }

    updateFields
  }

}