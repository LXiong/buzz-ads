package com.buzzinate.advertise.crawl

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import com.buzzinate.advertise.batch.Batch
import com.buzzinate.advertise.batch.BatchContent
import com.buzzinate.advertise.kestrel.MetaAd
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Constants
import com.buzzinate.advertise.util.Loggable
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.InvalidActorNameException
import akka.actor.Props
import akka.util.Duration
import java.util.concurrent.TimeUnit

case class KestrelSourceId(partition: Int, id: java.lang.Long)

class Crawler(batches: List[Batch]) extends Actor with Loggable {

  private val batchWorker = context.actorOf(Props(new BatchWorker(batches)), "advertise-batchworker")
  private val contents = new ListBuffer[Content]
  private val crawlThread = 5
  private val batchSize = 10
  private val crawlerWorkers = new HashMap[Long, ActorRef]

  val pending = new HashMap[String, KestrelSourceId]
  var lastFlush = 0l
  val flushInterval = 60 * 1000l

  def receive = {

    //不断的从抓取队列中获得要抓取的广告信息
    case CheckQueue => {
      var adCount = 0
      var isEmptyQueue = false
      while (adCount <= 50 && !isEmptyQueue) {
        val obj = Servers.kestrelClient.get(Constants.CRAWL_AD_QUEUE)
        if (obj == null) {
          info("empty queue")
          isEmptyQueue = true
        } else if (obj.isInstanceOf[MetaAd]) {
          val item = obj.asInstanceOf[MetaAd]
          reqCrawl(item)
          adCount += 1
        } else {
          warn("unrecognized queue element => " + obj.getClass.getName)
        }
      }
      if(System.currentTimeMillis > lastFlush + flushInterval){
    	self ! Flush
      }
      
      Crawler.system.scheduler.scheduleOnce(Duration.apply(10, TimeUnit.SECONDS)) {
        self ! CheckQueue
      }
    }

    case CrawlResult(content) => {
      if (content.state == State.OK) contents += content
      if (contents.size > batchSize) self ! Flush
    }

    case Flush => {
      info("flush size => " + contents.size)
      if (contents.size > 0) {
        batchWorker ! ContentList(contents.result)
        contents.clear
      }
      lastFlush = System.currentTimeMillis
    }
  }

  def reqCrawl(item: MetaAd): Unit = {
    val partition = (System.currentTimeMillis() % crawlThread.toLong)
    try {
      val cw = crawlerWorkers.getOrElseUpdate(partition, context.actorOf(Props(new CrawlWorker(self)), "crawlWorker-" + partition))
      cw ! CrawlRequest(item)
    } catch {
      case e: InvalidActorNameException => error("invalid partition " + partition, e)
    }
  }
}

object Crawler extends Loggable {
  val system = ActorSystem("buzz-CrawlSystem-ads")
  def main(args: Array[String]): Unit = {

    val batches = List(new BatchContent)
    val buzzAdsCrawler = system.actorOf(Props(new Crawler(batches)), "buzz-ads-crawler")
    buzzAdsCrawler ! CheckQueue

  }
}