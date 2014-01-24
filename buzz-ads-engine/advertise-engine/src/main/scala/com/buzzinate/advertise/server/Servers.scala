package com.buzzinate.advertise.server

import scala.collection.JavaConverters.seqAsJavaListConverter
import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.thrift.protocol.TBinaryProtocol
import com.buzzinate.advertise.async.Async
import com.buzzinate.advertise.async.AsyncWorker
import com.buzzinate.advertise.async.Invoke
import com.buzzinate.advertise.elasticsearch.client.Client
import com.buzzinate.advertise.listener.AudienceListener
import com.buzzinate.advertise.listener.EventListener
import com.buzzinate.advertise.monitor.AdStatsMonitor
import com.buzzinate.advertise.monitor.FrozenChannelMonitor
import com.buzzinate.advertise.monitor.HeartbeatMonitor
import com.buzzinate.advertise.monitor.IndexMonitor
import com.buzzinate.advertise.server.Message._
import com.buzzinate.advertise.util.Constants._
import com.buzzinate.advertise.util.Config
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices.FinagledClient
import com.buzzinate.buzzads.data.thrift.TAdCriteria
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria
import com.buzzinate.common.util.kestrel.KestrelClient
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import com.twitter.util.Duration
import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props
import redis.clients.jedis.JedisShardInfo
import redis.clients.jedis.ShardedJedisPool
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HTablePool
import org.apache.hadoop.hbase.client.HTableInterface
import com.buzzinate.advertise.log.LogCollector

object Servers extends Loggable {
  lazy val prop = Config.getConfig("config.properties")

  lazy val system = ActorSystem("Buzz-Ads-Engine-System")

  lazy val asnyc = new Async("Buzz-Ads-Engine-Async", system)

  lazy val monitNode = prop.getBoolean(MONIT_NODE, true)
  lazy val listenEventNode = prop.getBoolean(LISTEN_EVENT_NODE, true)
  
  lazy val isMatchAudienceCategory = prop.getBoolean(MATCH_AUDIENCE_CATEGORY, true)

  lazy val esCluster = prop.getString(ELASTIC_SEARCH_CLUSTER_NAME)
  lazy val esClient = new Client(prop.getList(ELASTIC_SEARCH_HOSTS_KEY).asJava, esCluster)

  val kestrelHost = prop.getString(KESTREL_HOST)
  var kestrelClient = new KestrelClient(kestrelHost, 5)

  val hTableReference = prop.getInt(HTABLE_REFERENCE, 1000)
  val hTablePool = createHTablePool(hTableReference)

  val adDataHosts = prop.getString(DATA_ACCESS_SERVER_HOSTS)
  var adDataAccessClient = createFinagleClient

  lazy val jedisPool = createJedisPool(prop.getList(REDIS_HOSTS_KEY), 500)

  lazy val adsThriftPort = prop.getInt(ADS_THRIFT_PORT, 32124)
  lazy val adsThriftTimeout = prop.getInt(ADS_THRIFT_TIMEOUT, 5)
  lazy val adsThriftThreads = prop.getInt(ADS_THRIFT_THREADS, 500)

  lazy val topnTopic = Servers.prop.getInt(TOP_TOPIC, 3)
  lazy val ldaModelPath = prop.getString(LDA_MODEL_PATH)

  lazy val ctrAttGravity = prop.getString(CTR_ATTENUATION_GRAVITY).toDouble

  lazy val indexMonitor = system.actorOf(Props(new IndexMonitor))
  lazy val adStatsMonitor = system.actorOf(Props(new AdStatsMonitor))
  lazy val heartBeatMonitor = system.actorOf(Props(new HeartbeatMonitor))
  lazy val frozenChannelMonitor = system.actorOf(Props(new FrozenChannelMonitor))
  
  val logCollector = system.actorOf(Props(new LogCollector))
  
  lazy val eventListener = system.actorOf(Props(new EventListener))
  lazy val audienceListener = system.actorOf(Props(new AudienceListener))

  def createHTablePool(hTableReference: Int): HTablePool = {
    val conf = HBaseConfiguration.create()
    info("connection zookeeper => " + conf.get("hbase.zookeeper.quorum"))
    new HTablePool(conf, hTableReference)
  }

  def createFinagleClient(): FinagledClient = {
    lazy val service = ClientBuilder.safeBuild(
      ClientBuilder.get().
        hosts(adDataHosts).
        codec(ThriftClientFramedCodec.get()).
        // Retry number, only applies to recoverable errors
        retries(3).
        requestTimeout(Duration.fromSeconds(5)).
        // Connection pool core size
        hostConnectionCoresize(100).
        // Connection pool max size
        hostConnectionLimit(600).
        // The amount of idle time for which a connection is cached in pool.
        hostConnectionIdleTime(Duration.fromSeconds(30)).
        // The maximum number of connection requests that are queued when the 
        // connection concurrency exceeds the connection limit.
        hostConnectionMaxWaiters(500));
    new TAdDataAccessServices.FinagledClient(
      service, new TBinaryProtocol.Factory());
  }

  private def createJedisPool(hosts: List[String], maxActive: Int) = {
    val shards = hosts map { host =>
      new JedisShardInfo(host)
    }
    val poolConfig = new GenericObjectPool.Config
    poolConfig.testWhileIdle = true
    poolConfig.maxActive = maxActive
    poolConfig.timeBetweenEvictionRunsMillis = 1000L * 120
    new ShardedJedisPool(poolConfig, shards.asJava)
  }

  def monitIndex(): Unit = {
    val asyncWorker = system.actorOf(Props(new AsyncWorker), "index-monitor")
    val cBuilder = new TAdCriteria.Builder
    val criteria = cBuilder.build
    val f = (x: Int) => {
      indexMonitor ! CheckIndex(criteria, true, true)
    }
    asyncWorker ! Invoke(f, System.currentTimeMillis)
  }

  def monitAdStats(): Unit = {
    val asyncWorker = system.actorOf(Props(new AsyncWorker), "adStats-monitor")
    val cBuilder = new TAdStatsCriteria.Builder
    val criteria = cBuilder.build
    val f = (x: Int) => {
      adStatsMonitor ! CheckAdStats(criteria, true)
    }
    asyncWorker ! Invoke(f, System.currentTimeMillis)
  }
  
  def monitFrozenChannels(): Unit = {
    val asyncWorker = system.actorOf(Props(new AsyncWorker), "frozenChannel-monitor")
    val cBuilder = new TAdStatsCriteria.Builder
    val criteria = cBuilder.build
    val f = (x: Int) => {
      frozenChannelMonitor ! CheckFrozenChannel(true)
    }
    asyncWorker ! Invoke(f, System.currentTimeMillis)
  }

  def monitHeartBeat(): Unit = {
    val asyncWorker = system.actorOf(Props(new AsyncWorker), "heartBeat-monitor")
    val f = (x: Int) => {
      heartBeatMonitor ! HeartBeat
    }
    asyncWorker ! Invoke(f, System.currentTimeMillis)
  }
  
  def listenEvent(): Unit = {
    val asyncWorker = system.actorOf(Props(new AsyncWorker), "event-listener")
    val f = (x: Int) => {
      eventListener ! CheckEvent
    }
    asyncWorker ! Invoke(f, System.currentTimeMillis)
  }
  
  def listenAudience(): Unit = {
    val asyncWorker = system.actorOf(Props(new AsyncWorker), "audience-listener")
    val f = (x: Int) => {
      audienceListener ! CheckEvent
    }
    asyncWorker ! Invoke(f, System.currentTimeMillis)
  }

}