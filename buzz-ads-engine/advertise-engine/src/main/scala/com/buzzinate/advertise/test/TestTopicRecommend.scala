package com.buzzinate.advertise.test

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import com.buzzinate.advertise.algorithm.TopicRecommend
import com.buzzinate.advertise.server.Servers
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer
import edu.stanford.nlp.tmt.stage.LoadCVB0LDA
import scalanlp.pipes.Pipes.global.file
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.advertise.util.KeywordUtil
import com.buzzinate.httpext._
import com.buzzinate.httpext.conversions._
import com.buzzinate.extract.HtmlExtractor
import com.buzzinate.common.util.ip.ProvinceName
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum
import edu.stanford.nlp.tmt.stage.LoadGibbsLDA

object TestTopicRecommend extends Loggable {

  val urls = List("http://www.022net.com/2013/1-2/455758122245529.html",
    "http://www.022net.com/2013/1-2/455266122282996.html",
    "http://www.022net.com/2013/1-8/50153318227683.html",
    "http://www.022net.com/2011/11-14/532740243253990.html",
    "http://www.022net.com/2013/1-7/476222172286337.html",
    "http://www.022net.com/2012/12-7/471340173351928.html")

  def testWithlinks(): Unit = {
    //val recomm = new TopicRecommend()
    val modelFile = file("ldaModel/gibbs-lda-63d0cf9a-200-f7633cde")
    SimpleChineseTokenizer
    val ldaModel = LoadGibbsLDA(modelFile)
    urls.foreach { url =>
      val resp = Crawl.http.when(sc => true)(req(url) as_resp)
      val a = HtmlExtractor.extract(resp.html, url)
      val dist = ldaModel.infer(a.snippets.mkString(","))
      //      val start = System.currentTimeMillis
      //      var ress = recomm.recommend(url, a.rawTitle, a.metaKeywords, AdEntryTypeEnum.IMAGE, "" , 10, "testUid", ProvinceName.OTHER, 0, "")
      //      println("cost " + (System.currentTimeMillis - start) + " ms")
      //      println("Query => " + a.rawTitle + " => " + a.title)
      //      ress.foreach(res => println("     " + res.title + " => " + res.link + " => " + res.entryId + " => " + res.score))
    }
  }
  def main(args: Array[String]): Unit = {
    testWithlinks
  }
}