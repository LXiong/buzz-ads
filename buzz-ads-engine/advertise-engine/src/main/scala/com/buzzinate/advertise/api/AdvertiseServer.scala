package com.buzzinate.advertise.api

import java.net.InetSocketAddress

import org.apache.thrift.protocol.TBinaryProtocol

import com.buzzinate.advertise.server.Servers
import com.buzzinate.buzzads.thrift.AdServices
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.util.Duration

object AdvertiseServer {
  def main(args: Array[String]): Unit = {
    startThriftServer(Servers.adsThriftTimeout, Servers.adsThriftPort)
  }

  def startThriftServer(requestTimeout: Int, port: Int): Unit = {
    val processor = new AdvertiseServiceImpl
    ServerBuilder.safeBuild(new AdServices.FinagledService(processor,
      new TBinaryProtocol.Factory()),
      ServerBuilder.get().
        name("AdvertiseServerServices").
        codec(ThriftServerFramedCodec.get()).
        requestTimeout(Duration.fromSeconds(requestTimeout)).
        bindTo(new InetSocketAddress(port)))
  }
}