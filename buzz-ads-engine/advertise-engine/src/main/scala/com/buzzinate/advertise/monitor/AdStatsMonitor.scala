package com.buzzinate.advertise.monitor

import java.util.concurrent.TimeUnit

import scala.collection.JavaConverters.asScalaBufferConverter

import com.buzzinate.advertise.redis.RedisClient
import com.buzzinate.advertise.server.Message.CheckAdStats
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TPagination

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.util.Duration

class AdStatsMonitor extends Actor with Loggable {
  val redisClient = new RedisClient(Servers.jedisPool)

  def receive = {

    //获取广告点击的统计信息，并放入redis
    case CheckAdStats(criteria, monitMode) => {
      try {
        var isLastPage = false
        var start = 0
        val count = 200
        val pBuilder = new TPagination.Builder
        pBuilder.count(count.shortValue)
        while (!isLastPage) {
          val page = pBuilder.start(start.shortValue).build
          val stats = Servers.adDataAccessClient.findAdStats(criteria, page).get.asScala.toList
          if (stats.size < count) {
            isLastPage = true
          }
          if (stats.size > 0) {
            Servers.asnyc.asnyc(System.currentTimeMillis.toString) {
              redisClient.updateAdCtr(stats)
            }
          }
        }
      } catch {
        case e: Exception => {
          error("AdStatsMonitor Exception => " + e.getMessage())
          e.getStackTrace().foreach(trace => error(trace.toString()))
        }
      }
      if (monitMode) {
        Servers.system.scheduler.scheduleOnce(Duration.apply(1, TimeUnit.HOURS)) {
          self ! CheckAdStats(criteria, monitMode)
        }
      }
    }
  }
}