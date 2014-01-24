package com.buzzinate.advertise.test

import collection.JavaConverters._
import com.buzzinate.advertise.elasticsearch.client.Client
import com.buzzinate.advertise.util.Constants._
import com.buzzinate.advertise.elasticsearch.api.Ad
import com.buzzinate.advertise.server.Servers
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable.ListBuffer
import org.apache.commons.lang.StringUtils
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.Constants._
import com.buzzinate.advertise.util.DoublePriorityQueue
import com.buzzinate.advertise.util.KeywordUtil
import com.buzzinate.advertise.model.KeywordInfo
import com.buzzinate.nlp.util.TextUtil
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer
import edu.stanford.nlp.tmt.model.lda.CVB0LDA
import edu.stanford.nlp.tmt.stage._
import scalanlp.pipes.Pipes.global.file
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import java.util.Date
import com.buzzinate.advertise.util.Loggable
import java.io.BufferedWriter
import com.buzzinate.advertise.tools.TempAdUtil
import com.buzzinate.common.util.ip.ProvinceName
import java.util.HashMap
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum
import com.buzzinate.advertise.util.Config

object TestElasticSearch {

  import com.buzzinate.advertise.algorithm.TopicRecommend._

//  def testQuery(client: Client): Unit = {
//
//    val startTime = System.currentTimeMillis()
//    var counter = 0
//    while (true) {
//      val results = client.query("", List[Integer]().asJava, "3|0.7 45|0.05 88|0.1 178|0.15", List[String]().asJava, "", "", "", false, ProvinceName.OTHER, 0,5)
//      println("res size => " + results.size)
//      results.asScala.foreach(res => println(res.title))
//      counter += 1
//      println((System.currentTimeMillis() - startTime) / counter)
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    testQuery(Servers.esClient)
//  }

  def getTopicDistStr(distribution: Array[Double]): String = {
    var counter = 0
    distribution.map { dist =>
      val counterProb =
        if (dist == 0.0) {
          ""
        } else {
          counter + "|" + dist
        }
      counter += 1
      counterProb
    }.mkString(" ")
  }

}