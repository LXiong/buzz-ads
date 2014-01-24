package com.buzzinate.advertise.monitor

import java.util.concurrent.TimeUnit

import scala.collection.JavaConverters.asScalaBufferConverter

import com.buzzinate.advertise.redis.RedisClient
import com.buzzinate.advertise.server.Message.CheckFrozenChannel
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.LinkParser
import com.buzzinate.advertise.util.Loggable

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.util.Duration

class FrozenChannelMonitor extends Actor with Loggable {
  val redisClient = new RedisClient(Servers.jedisPool)

  def receive = {

    //获取冻结媒体列表，并放入redis
    case CheckFrozenChannel(monitMode) => {
      try {
        val frozenChannel = Servers.adDataAccessClient.findAllFrozenList.get.asScala.toList
        val frozenDomains = frozenChannel.map(channel => LinkParser.formatDomain(channel.getDomain))
        if (frozenDomains.size > 0) {
          info("[FrozenChannelMonitor]: frozenChannels size => " + frozenDomains.size)
          Servers.asnyc.asnyc(System.currentTimeMillis.toString) {
            redisClient.flushBlackChannel(frozenDomains)
          }	
        }
      } catch {
        case e: Exception => {
          error("FrozenChannelMonitor Exception => " + e.getMessage())
          e.getStackTrace().foreach(trace => error(trace.toString()))
        }
      }
      if (monitMode) {
        Servers.system.scheduler.scheduleOnce(Duration.apply(1, TimeUnit.HOURS)) {
          self ! CheckFrozenChannel(monitMode)
        }
      }
    }
  }
}