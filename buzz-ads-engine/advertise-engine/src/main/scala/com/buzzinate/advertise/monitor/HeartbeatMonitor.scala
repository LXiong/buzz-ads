package com.buzzinate.advertise.monitor

import java.util.concurrent.TimeUnit

import com.buzzinate.advertise.server.Message.HeartBeat
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdCriteria
import com.buzzinate.buzzads.data.thrift.TPagination
import com.buzzinate.common.util.kestrel.KestrelClient

import HeartbeatMonitor.criteria
import HeartbeatMonitor.pBuilder
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.util.Duration

object HeartbeatMonitor {
  val start = 0
  val count = 50
  val pBuilder = new TPagination.Builder
  pBuilder.start(start.shortValue)
  pBuilder.count(count.shortValue)
  val cBuilder = new TAdCriteria.Builder
  val criteria = cBuilder.build
}

class HeartbeatMonitor extends Actor with Loggable {
  import HeartbeatMonitor._
  def receive = {

    //心跳检测，检查使用到的一些服务是否正常
    case HeartBeat => {
      try {
        Servers.kestrelClient.get("Ad_Beat_Test_Queue")
        info("kestrelClient is alive")
      } catch {
        case e: Exception => {
          warn("kestrelClient is dead , restart ...")
          Servers.kestrelClient = new KestrelClient(Servers.kestrelHost, 5)
          info("kestrelClient restart done")
        }
      }
      try {
        try {
          Servers.adDataAccessClient.findAdItems(criteria, pBuilder.build)
          info("adDataAccessClient is alive")
        } catch {
          case e: Exception => {
            warn("adDataAccessClient is dead , restart ...")
            Servers.adDataAccessClient = Servers.createFinagleClient
            info("adDataAccessClient restart done")
          }
        }
      }
      Servers.system.scheduler.scheduleOnce(Duration.apply(1, TimeUnit.MINUTES)) {
        self ! HeartBeat
      }
    }
  }
}