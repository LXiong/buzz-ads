package com.buzzinate.advertise.api

import java.util.concurrent.Executors
import com.buzzinate.advertise.algorithm.TopicRecommend
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.enums.AdNetworkEnum
import com.buzzinate.buzzads.thrift.AdServices.FutureIface
import com.buzzinate.buzzads.thrift.AdItem
import com.buzzinate.buzzads.thrift.AdParam
import com.buzzinate.common.util.ip.ProvinceName
import com.twitter.util.Future
import com.twitter.util.ExecutorServiceFuturePool
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum

class AdvertiseServiceImpl extends FutureIface with Loggable {

  val recommender = new TopicRecommend

  info("initial buzzads engine ... " + initial)
  info("initial done")

  val futurePool = new ExecutorServiceFuturePool(Executors.newFixedThreadPool(Servers.adsThriftThreads))

  def serve(param: AdParam): Future[java.util.List[AdItem]] = {
    val op = new AdvertiseServerOp(param, recommender)
    val future: Future[java.util.List[AdItem]] = futurePool.apply(op.applyE())
    return future
  }

  def initial(): Unit = {
    info("initial es client")
    Servers.esClient
    info("initial adDataAccessClient")
    Servers.adDataAccessClient
    info("initial es client done")
    if (Servers.monitNode) {
      Servers.monitIndex
      Servers.monitAdStats
      Servers.monitFrozenChannels
    }
    if (Servers.listenEventNode) {
      Servers.listenEvent
      Servers.listenAudience
    }
    Servers.monitHeartBeat
    //加热推荐引擎从而进行初始化
    val initialRecRes = recommender.recommend("www.buzzinate.com", "擘纳广告系统", List[String]("广告"), AdEntryTypeEnum.IMAGE, "" , 5, "systemAdmin", ProvinceName.OTHER, AdNetworkEnum.LEZHI.getCode, "")
    info("initialRecRes size : " + initialRecRes.size)
  }

}