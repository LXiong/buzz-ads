package com.buzzinate.advertise.server

import org.apache.thrift.protocol.TBinaryProtocol

import com.buzzinate.advertise.util.Constants.DATA_ACCESS_SERVER_HOSTS
import com.buzzinate.advertise.util.Constants.KESTREL_HOST
import com.buzzinate.advertise.util.Config
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices.FinagledClient
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices
import com.buzzinate.common.util.kestrel.KestrelClient
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import com.twitter.util.Duration

object Servers extends Loggable {

  lazy val prop = Config.getConfig("config.properties")

  lazy val kestrelHost = prop.getString(KESTREL_HOST)
  lazy val kestrelClient = new KestrelClient(kestrelHost, 5)

  lazy val finagleClient = createFinagleClient()

  private def createFinagleClient(): FinagledClient = {
    lazy val service = ClientBuilder.safeBuild(
      ClientBuilder.get().
        hosts(prop.getString(DATA_ACCESS_SERVER_HOSTS)).
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