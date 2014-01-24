package com.buzzinate.advertise.listener

import java.util.concurrent.TimeUnit

import com.buzzinate.advertise.audience.parser.UQParser.UQItem
import com.buzzinate.advertise.audience.parser.UQParser
import com.buzzinate.advertise.redis.json.PreferAd
import com.buzzinate.advertise.redis.LimitAdRedisClient
import com.buzzinate.advertise.server.Message.CheckEvent
import com.buzzinate.advertise.server.Message.EntryLevel
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Constants.AUDIENCE_EVENT_QUEUE
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.enums.PartnerEnum
import com.buzzinate.buzzads.event.AudienceFileReceivedEvent

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.util.Duration

/**
 * 接收合作方的投放规则并做相应逻辑处理
 */
class AudienceListener extends Actor with Loggable {
  val limitAdRedisClient = new LimitAdRedisClient(Servers.jedisPool)
  def receive = {
    case CheckEvent => {
      var noMessage = false
      while (!noMessage) {
        val event = Servers.kestrelClient.get(AUDIENCE_EVENT_QUEUE)
        if (null != event) {
          if (event.isInstanceOf[AudienceFileReceivedEvent]) {
            val audienceEvent = event.asInstanceOf[AudienceFileReceivedEvent]
            val parserId = audienceEvent.getPartnerEnumCode
            val advertiserId = audienceEvent.getAdvertiserId
            val jsonVal = audienceEvent.getData
            if (parserId == PartnerEnum.UQ.getCode) {
              try {
                val item = UQParser.parseJson(jsonVal)
                info("AudienceListener: parserId => " + parserId + " advertiserId => " + advertiserId + " item => " + item.toString)
                Servers.asnyc.asnyc(item.cookieId) {
                  processUQItem(item, advertiserId.toString)
                }
              } catch {
                case e: Exception => {
                  error("AudienceListener: Error Message from UQ => " + jsonVal)
                }
              }
            } else {
              error("AudienceListener: unrecognize parserid => " + parserId)
            }
          } else {
            noMessage = true
          }
        } else {
          noMessage = true
        }

        if (noMessage) {
          Servers.system.scheduler.scheduleOnce(Duration.apply(5, TimeUnit.SECONDS)) {
            self ! CheckEvent
          }
        }
      }
    }
  }

  def processUQItem(item: UQItem, advertiserId: String): Unit = {
    limitAdRedisClient.updateLimitAdSet(List(item.entryId), new EntryLevel)
    limitAdRedisClient.updateLimitAdShowTimes(item.entryId.toString, item.cap.toDouble, new EntryLevel)
    val preferAd = new PreferAd
    preferAd.setCampaignId(item.campaignId)
    preferAd.setEntryId(item.entryId)
    preferAd.setAdvertiser(advertiserId)
    limitAdRedisClient.updatePreferAd(item.cookieId, preferAd)
    limitAdRedisClient.updateAudienceAds(List(item.entryId))
  }
}