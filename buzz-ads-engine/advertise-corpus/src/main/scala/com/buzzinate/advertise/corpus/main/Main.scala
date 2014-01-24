package com.buzzinate.advertise.corpus.main

import com.buzzinate.advertise.util.ResourceLoader
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import com.buzzinate.advertise.corpus.crawl.CorpusCrawler
import com.buzzinate.advertise.corpus.lda.LdaTrainer

object Main {

  val crawlHeight = 1
  val crawlWidth = 20
  val topNKeywords = 50
  val mindf = 4
  val mostCommonFiltNum = 30
  val minTextLength = 5
  
  val adsLinkDir = "adsLinks.txt"
  val adsCorpusDir = "adsCorpus.csv"

  def main(args: Array[String]): Unit = {
//    val adsLinks = ResourceLoader.loadResourceFile(adsLinkDir)
//    //val adsCorpusFile = new File(adsCorpusDir)
//    val bw = new BufferedWriter(new FileWriter(adsCorpusDir))
//    var counter = 0
//    val crawler = new CorpusCrawler(crawlWidth, crawlHeight, topNKeywords)
//    adsLinks.foreach { adLink =>
//      //val keywords = crawler.getKeywords(adLink)
//      println(adLink)
//      val content = crawler.getContent(adLink)
//      if (!content.isEmpty) {
//        bw.append(counter.toString).append(",").append(content).append("\n")
//        counter += 1
//      }
//      if(counter % 50 == 0){
//        bw.flush
//      }
//    }
//    bw.close
    val trainer = new LdaTrainer(adsCorpusDir, mindf, mostCommonFiltNum, minTextLength)
    trainer.train
  }
}