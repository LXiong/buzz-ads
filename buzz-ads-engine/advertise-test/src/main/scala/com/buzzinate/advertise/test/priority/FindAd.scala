package com.buzzinate.advertise.test.priority
import com.buzzinate.advertise.test.server.Servers
import scala.collection.JavaConverters._
import com.buzzinate.advertise.util.DomainNames
import com.buzzinate.buzzads.data.thrift.TPagination
import com.buzzinate.buzzads.data.thrift.TAdCriteria
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.seqAsJavaListConverter

object FindAd {
  def main(args: Array[String]): Unit = {
    val cBuilder = new TAdCriteria.Builder
    cBuilder.entryId(Servers.updateEntryId.toInt)
    val criteria = cBuilder.build
    val pBuilder = new TPagination.Builder
    pBuilder.start(Servers.adDataStart.shortValue)
    pBuilder.count(Servers.adDataCount.shortValue)
    try {
      val adItems = Servers.adDataAccessClient.findAdItems(criteria, pBuilder.build).get().asScala.toList
      println("[SUCCESS] get " + adItems.size + " items from data service " + Servers.adDataHosts)
    } catch {
      case e: Exception => {
        println("[ERROR] can't get ads items from data service : " + Servers.adDataHosts)
      }
    }

  }
}