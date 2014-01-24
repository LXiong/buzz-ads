package com.buzzinate.advertise.listener

import java.util.concurrent.TimeUnit
import java.util.HashMap

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.mutable.ListBuffer

import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum
import com.buzzinate.advertise.kestrel.TrackedUrlInfo
import com.buzzinate.advertise.redis.json.CampaignPriority
import com.buzzinate.advertise.redis.json.SiteConfig
import com.buzzinate.advertise.redis.RedisClient
import com.buzzinate.advertise.server.Message.CheckEvent
import com.buzzinate.advertise.server.Message.CheckIndex
import com.buzzinate.advertise.server.Message.CheckUpdateField
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Constants.ADEVENT_QUEUE
import com.buzzinate.advertise.util.Constants.BUDGET_WARN_PRIORITY
import com.buzzinate.advertise.util.Constants.CHANNEL_EVENT_QUEUE
import com.buzzinate.advertise.util.Constants.SITE_CONFIG_EVENT_QUEUE
import com.buzzinate.advertise.util.Constants.URL_TRACK_QUEUE
import com.buzzinate.advertise.util.LinkParser
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdCriteria
import com.buzzinate.buzzads.data.thrift.TPagination
import com.buzzinate.buzzads.enums.AdStatusEnum
import com.buzzinate.buzzads.enums.IdType
import com.buzzinate.buzzads.event.AdBaseEvent
import com.buzzinate.buzzads.event.AdCreateEvent
import com.buzzinate.buzzads.event.AdDeleteEvent
import com.buzzinate.buzzads.event.AdDisableEvent
import com.buzzinate.buzzads.event.AdEnableEvent
import com.buzzinate.buzzads.event.AdModifyEvent
import com.buzzinate.buzzads.event.AdPauseEvent
import com.buzzinate.buzzads.event.AdSuspendEvent
import com.buzzinate.buzzads.event.CampaignBudgetModifyEvent
import com.buzzinate.buzzads.event.CampaignBudgetWarnEvent
import com.buzzinate.buzzads.event.ChannelBaseEvent
import com.buzzinate.buzzads.event.ChannelCloseEvent
import com.buzzinate.buzzads.event.ChannelDeletedEvent
import com.buzzinate.buzzads.event.ChannelFrozenEvent
import com.buzzinate.buzzads.event.ChannelOpenEvent
import com.buzzinate.buzzads.event.PublisherSiteConfigEvent

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.util.Duration

class EventListener extends Actor with Loggable {

  val redisClient = new RedisClient(Servers.jedisPool)

  val budgetWarnPriority = Servers.prop.getString(BUDGET_WARN_PRIORITY).toDouble

  def receive = {

    case CheckEvent => {
      var noMessage = false
      while (!noMessage) {

        var noChannelEventMsg = true
        var noUrlTrackedMsg = true
        var noSiteConfigMsg = true
        var noAdEventMsg = false
        
        val channelEvent = Servers.kestrelClient.get(CHANNEL_EVENT_QUEUE)
        
        if (null != channelEvent) {
          if (channelEvent.isInstanceOf[ChannelDeletedEvent]) {
            processOpenChannel(channelEvent.asInstanceOf[ChannelDeletedEvent])
            noChannelEventMsg = false
          } else if (channelEvent.isInstanceOf[ChannelFrozenEvent]) {
            processUpdateBlackChannel(channelEvent.asInstanceOf[ChannelFrozenEvent])
            noChannelEventMsg = false
          } else if (channelEvent.isInstanceOf[ChannelOpenEvent]){
            processOpenChannel(channelEvent.asInstanceOf[ChannelOpenEvent])
            noChannelEventMsg = false
          } else {
            if (channelEvent.isInstanceOf[ChannelCloseEvent]) {
              info("close channel => " + channelEvent.asInstanceOf[ChannelCloseEvent].getDomain)
            } else {
              warn("wrong channelEvent : " + channelEvent.getClass.getName)
              // do nothing
            }
          }
        }

        val urlTrackedInfoObj = Servers.kestrelClient.get(URL_TRACK_QUEUE)

        if (null != urlTrackedInfoObj) {
          if (urlTrackedInfoObj.isInstanceOf[TrackedUrlInfo]) {
            info("Event Listener: get => " + urlTrackedInfoObj.getClass.getName)
            val urlTrackedInfo = urlTrackedInfoObj.asInstanceOf[TrackedUrlInfo]
            Servers.indexMonitor ! CheckUpdateField(urlTrackedInfo.getEntryId.toString, FieldEnum.REALURL.getFieldName, urlTrackedInfo.getRealUrl)
            noUrlTrackedMsg = false
          }
        }

        val siteConfigEvent = Servers.kestrelClient.get(SITE_CONFIG_EVENT_QUEUE)

        if (null != siteConfigEvent) {
          if (siteConfigEvent.isInstanceOf[PublisherSiteConfigEvent]) {
            info("Event Listener: get => " + siteConfigEvent.getClass.getName)
            processUpdateSiteconfigEvent(siteConfigEvent.asInstanceOf[PublisherSiteConfigEvent])
            noSiteConfigMsg = false
          }
        }

        val adEvent = Servers.kestrelClient.get(ADEVENT_QUEUE)

        if (null == adEvent) {
          noAdEventMsg = true
        } else if (adEvent.isInstanceOf[AdCreateEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdCreateEvent(adEvent.asInstanceOf[AdCreateEvent])
        } else if (adEvent.isInstanceOf[AdDeleteEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdDeleteEvent(adEvent.asInstanceOf[AdDeleteEvent])
        } else if (adEvent.isInstanceOf[AdDisableEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdDisableEvent(adEvent.asInstanceOf[AdDisableEvent])
        } else if (adEvent.isInstanceOf[AdEnableEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdEnableEvent(adEvent.asInstanceOf[AdEnableEvent])
        } else if (adEvent.isInstanceOf[AdModifyEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdModifyEvent(adEvent.asInstanceOf[AdModifyEvent])
        } else if (adEvent.isInstanceOf[AdPauseEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdPauseEvent(adEvent.asInstanceOf[AdPauseEvent])
        } else if (adEvent.isInstanceOf[AdSuspendEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processAdSuspendEvent(adEvent.asInstanceOf[AdSuspendEvent])
        } else if (adEvent.isInstanceOf[CampaignBudgetModifyEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processCampaignBudgetModifyEvent(adEvent.asInstanceOf[CampaignBudgetModifyEvent])
        } else if (adEvent.isInstanceOf[CampaignBudgetWarnEvent]) {
          info("Event Listener: get => " + adEvent.getClass.getName)
          processCampaignBudgetWarnEvent(adEvent.asInstanceOf[CampaignBudgetWarnEvent])
        } else {
          warn("wrong event : " + adEvent.getClass.getName)
          noAdEventMsg = true
        }
        noMessage = noUrlTrackedMsg && noSiteConfigMsg && noAdEventMsg && noChannelEventMsg
        if (noMessage) {
          Servers.system.scheduler.scheduleOnce(Duration.apply(5, TimeUnit.SECONDS)) {
            self ! CheckEvent
          }
        }
      }
    }
  }

  private def processAdCreateEvent(event: AdCreateEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val cBuilder = new TAdCriteria.Builder
      if (event.getType.getCode == IdType.CAMPAIGN.getCode) {
        cBuilder.campaignId(event.getId)
        val criteria = cBuilder.build
        Servers.indexMonitor ! CheckIndex(criteria, false, false)
        info("create campaign => " + event.getId)
      } else if (event.getType.getCode == IdType.ORDER.getCode) {
        cBuilder.orderId(event.getId)
        val criteria = cBuilder.build
        Servers.indexMonitor ! CheckIndex(criteria, false, false)
        info("create order => " + event.getId)
      } else if (event.getType.getCode == IdType.ENTRY.getCode) {
        cBuilder.entryId(event.getId)
        val criteria = cBuilder.build
        Servers.indexMonitor ! CheckIndex(criteria, false, false)
        info("create entry => " + event.getId)
      } else {
        warn("unRecognize event type " + event.getType)
      }

    }
  }

  private def processAdDeleteEvent(event: AdDeleteEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val entryIds = getEntryIds(event, AdStatusEnum.DELETED)
      Servers.esClient.bulkDelete(entryIds.asJava)
      info("delete => " + event.getId())
    }
  }

  private def processAdDisableEvent(event: AdDisableEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val entryIds = getEntryIds(event, AdStatusEnum.DISABLED)
      entryIds foreach { entryId =>
        val source = new HashMap[String, Object]();
        source.put(FieldEnum.STATUS.getFieldName, new Integer(AdStatusEnum.DISABLED.getCode()))
        Servers.esClient.update(entryId, source)
        info("disable => " + entryId)
      }
    }
  }

  private def processAdEnableEvent(event: AdEnableEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val entryIds = getEntryIds(event, AdStatusEnum.ENABLED)
      entryIds foreach { entryId =>
        val source = new HashMap[String, Object]();
        source.put(FieldEnum.STATUS.getFieldName, new Integer(AdStatusEnum.ENABLED.getCode()))
        Servers.esClient.update(entryId, source)
        info("enable => " + entryId)
      }
    }
  }

  private def processAdPauseEvent(event: AdPauseEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val entryIds = getEntryIds(event, AdStatusEnum.PAUSED)
      entryIds foreach { entryId =>
        val source = new HashMap[String, Object]();
        source.put(FieldEnum.STATUS.getFieldName, new Integer(AdStatusEnum.PAUSED.getCode()))
        Servers.esClient.update(entryId, source)
        info("pause => " + entryId)
      }
    }
  }

  private def processAdSuspendEvent(event: AdSuspendEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val entryIds = getEntryIds(event, AdStatusEnum.SUSPENDED)
      entryIds foreach { entryId =>
        val source = new HashMap[String, Object]()
        source.put(FieldEnum.STATUS.getFieldName, new Integer(AdStatusEnum.SUSPENDED.getCode()))
        Servers.esClient.update(entryId, source)
        info("suspend => " + entryId)
      }
    }
  }

  private def processAdModifyEvent(event: AdModifyEvent): Unit = {
    Servers.asnyc.asnyc(event.getId.toString) {
      val cBuilder = new TAdCriteria.Builder
      if (event.getType.getCode == IdType.CAMPAIGN.getCode) {
        cBuilder.campaignId(event.getId)
        val criteria = cBuilder.build
        Servers.indexMonitor ! CheckIndex(criteria, false, false)
        info("modify campaign => " + event.getId())
      } else if (event.getType.getCode == IdType.ORDER.getCode) {
        cBuilder.orderId(event.getId)
        val criteria = cBuilder.build
        Servers.indexMonitor ! CheckIndex(criteria, false, false)
        info("modify order => " + event.getId())
      } else if (event.getType.getCode == IdType.ENTRY.getCode) {
        cBuilder.entryId(event.getId())
        val criteria = cBuilder.build
        Servers.indexMonitor ! CheckIndex(criteria, false, false)
        info("modify entry => " + event.getId())
      } else {
        warn("unRecognize event type " + event.getType())
      }
    }
  }

  private def processCampaignBudgetModifyEvent(event: CampaignBudgetModifyEvent): Unit = {
    val asnycKey = System.currentTimeMillis.toString
    info("campaign budget Modify, campaign ids: => " + event.getCampaignIds.asScala.mkString(","))
    Servers.asnyc.asnyc(asnycKey) {
      val cbs = Servers.adDataAccessClient.findCampaignBudgets(event.getCampaignIds).get.asScala.toList
      if (cbs.size > 0) {
        redisClient.updateCampaignBalanceRatio(cbs)
      }
    }
  }

  private def processCampaignBudgetWarnEvent(event: CampaignBudgetWarnEvent): Unit = {
    val asnycKey = System.currentTimeMillis.toString
    info("campaign budget warn, campaign ids: => " + event.getCampaignIds.asScala.mkString(","))
    Servers.asnyc.asnyc(asnycKey) {
      val cps = event.getCampaignIds.asScala.toList.map { campaignId =>
        val cp = new CampaignPriority
        cp.campaignId = campaignId.toString
        cp.priority = budgetWarnPriority
        cp
      }
      redisClient.updateCampaignPriority(cps)
    }
  }

  private def processUpdateSiteconfigEvent(event: PublisherSiteConfigEvent): Unit = {
    val asnycKey = System.currentTimeMillis.toString
    info("update siteconfig ! uuid => " + event.getUuid)
    Servers.asnyc.asnyc(asnycKey) {
      val siteConfig = new SiteConfig
      try {
        val tSiteConfig = Servers.adDataAccessClient.getPublisherSiteConfig(event.getUuid).get

        if (tSiteConfig.getBlackKeywordsOption.isDefined) {
          siteConfig.setBlackKeywords(tSiteConfig.getBlackKeywords)
          info("update siteconfig ! BlackKeywords => " + tSiteConfig.getBlackKeywords)
        }
        if (tSiteConfig.getBlackDomainsOption.isDefined) {
          siteConfig.setBlackDomains(tSiteConfig.getBlackDomains.asScala.mkString(","))
          info("update siteconfig ! BlackDomains => " + tSiteConfig.getBlackDomains.asScala.mkString(","))
        }
        if (tSiteConfig.getBlackEntryLinksOption.isDefined) {
          siteConfig.setBlackRealUrls(tSiteConfig.getBlackEntryLinks.asScala.mkString(","))
          info("update siteconfig ! BlackEntryLinks => " + tSiteConfig.getBlackEntryLinks.asScala.mkString(","))
        }
        
        redisClient.updateSiteConfig(event.getUuid, siteConfig)
      } catch {
        case e: Exception => {
          info("update siteconfig ! the siteconfig of " + event.getUuid + " is empty")
          e.getStackTrace().foreach(trace => error(trace.toString()))
          //即使获取不到这个uuid对应的siteconfig，则将一个空的siteConfig存如redis，防止每次推荐请求都调用adDataAccessClient.getPublisherSiteConfig
          redisClient.updateSiteConfig(event.getUuid, siteConfig)
        }
      }
    }
  }
  
  private def processUpdateBlackChannel(event: ChannelBaseEvent): Unit = {
    info("update blackChannes => " + event.getDomain)
    Servers.asnyc.asnyc(event.getUuid) {  
      redisClient.updateBlackChannel(LinkParser.formatDomain(event.getDomain))
    }
  }
  
  private def processOpenChannel(event: ChannelBaseEvent): Unit = {
    info("openChannel => " + event.getDomain)
    Servers.asnyc.asnyc(event.getUuid) {  
      redisClient.openChannel(LinkParser.formatDomain(event.getDomain))
    }
  }

  private def getEntryIds(baseEvent: AdBaseEvent, adStatus: AdStatusEnum): List[String] = {
    val cBuilder = new TAdCriteria.Builder
    if (baseEvent.getType.getCode == IdType.CAMPAIGN.getCode) {
      cBuilder.campaignId(baseEvent.getId)
    } else if (baseEvent.getType.getCode == IdType.ORDER.getCode) {
      cBuilder.orderId(baseEvent.getId)
    } else if (baseEvent.getType.getCode == IdType.ENTRY.getCode) {
      cBuilder.entryId(baseEvent.getId)
    } else {
      warn("unRecognize event type " + baseEvent.getType())
    }
    val criteria = cBuilder.build
    var isLastPage = false
    var start = 0
    val count = 200
    val pBuilder = new TPagination.Builder
    pBuilder.count(count.shortValue)
    val entryIds = ListBuffer[String]()
    while (!isLastPage) {
      pBuilder.start(start.shortValue)
      info("find items by event, event code: " + baseEvent.getType.getCode + " ; event id : " + baseEvent.getId)
      val adItems = Servers.adDataAccessClient.findAdItems(criteria, pBuilder.build).get().asScala.toList
      info("find adItems size : " + adItems.size)
      if (adItems.size < count) {
        isLastPage = true
      }
      val ids = adItems filter { item =>
        info("this event status : " + adStatus.getCode)
        info("this item status : " + item.getStatus.getValue + " ==> item id " + item.getEntryId)
        item.getStatus().getValue == adStatus.getCode
      } map (item => item.getEntryId.toString)
      entryIds.appendAll(ids)
      start += count
    }
    entryIds.result
  }
}