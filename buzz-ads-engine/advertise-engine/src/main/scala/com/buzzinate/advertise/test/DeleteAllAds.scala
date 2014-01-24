package com.buzzinate.advertise.test

import scala.collection.JavaConverters.seqAsJavaListConverter

import com.buzzinate.advertise.elasticsearch.client.Client
import com.buzzinate.advertise.util.Constants._
import com.buzzinate.advertise.util.Config

object DeleteAllAds {
  def main(args: Array[String]): Unit = {
    val prop = Config.getConfig("config.properties")
    println(prop.getList(ELASTIC_SEARCH_HOSTS_KEY))
    val client = new Client(prop.getList(ELASTIC_SEARCH_HOSTS_KEY).asJava, prop.getString(ELASTIC_SEARCH_CLUSTER_NAME))
    val idList = new java.util.ArrayList[String]
    for(i <- 1 to 500){
      idList.add(i.toString)
    }
    client.bulkDelete(idList)
    println("buldDeleteDone")
  }

}