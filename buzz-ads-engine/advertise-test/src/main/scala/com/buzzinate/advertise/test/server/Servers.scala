package com.buzzinate.advertise.test.server
import com.buzzinate.advertise.util.Config
import com.buzzinate.advertise.elasticsearch.client.Client
import scala.collection.JavaConverters._
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HTablePool
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices.FinagledClient
import com.buzzinate.buzzads.data.thrift.TAdCriteria
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.util.Duration
object Servers {
	lazy val prop = Config.getConfig("config.properties")
	lazy val esClient = new Client(prop.getList("elastic.search.hosts").asJava, prop.getString("elastic.search.cluster"))
	lazy val updateEntryId = prop.getString("entry.id")
	lazy val deleteEntryId = prop.getString("delete.entry.id")
	lazy val cookieId = prop.getString("cookie.id")
	lazy val adDataHosts = prop.getString("data.access.server.hosts")
	lazy val adDataAccessClient = createFinagleClient
	lazy val adDataStart = prop.getInt("data.access.start", 0)
	lazy val adDataCount = prop.getInt("data.access.count", 50)
	lazy val conf = HBaseConfiguration.create()
    lazy val pool = new HTablePool(conf, 1000)
    lazy val table = pool.getTable("user_category")
    
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
}