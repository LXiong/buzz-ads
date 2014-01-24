package com.buzzinate.advertise.corpus.crawl

import com.buzzinate.advertise.util.ResourceLoader
import scala.collection.mutable.ListBuffer
import java.net.URLEncoder
import com.buzzinate.httpext.Crawl
import com.buzzinate.httpext.req
import com.buzzinate.httpext._
import com.buzzinate.httpext.conversions._
import collection.JavaConverters._
import org.jsoup.Jsoup
import com.buzzinate.advertise.corpus.util.Utils

class SougouAdsLinkCollector extends AdsLinkCollector {

  val sougouPrefix = "http://www.sogou.com/web?query="

  def getAdsLinks(): Set[String] = {
    val terms = ResourceLoader.loadResourceFile("terms.txt")
    val adsLinks = new ListBuffer[String]

    terms.foreach { term =>
      val url = sougouPrefix + URLEncoder.encode(term)
      try {
        val resp = Crawl.http.when(sc => true)(req(url) as_resp)        
        Thread.sleep(100)
        val doc = Jsoup.parse(resp.html, url)
        val adsEles = doc.getElementsByAttributeValueContaining("class", "sponsored").iterator().asScala ++ doc.getElementsByAttributeValueContaining("class", "right").iterator().asScala
        adsEles.foreach{ adsEle => 
          val ads = adsEle.getElementsByTag("cite").iterator().asScala
          ads.foreach { ad =>
          try {

            val node = ad.childNode(0)
            if (node.childNodes.isEmpty()) {
              val adLink = node.toString()
              if (!adLink.contains("sougo")) {
                val parsedAdLink = Utils.parseAdLink(adLink)
                adsLinks.append(parsedAdLink)
                println(parsedAdLink)
              }
            }
          } catch {
            case e: Exception => {
              println(e.getMessage())
            }
          }
        }
        }
        
      } catch {
        case e: Exception => {
          println(e.getMessage())
        }
      }
      println("")
    }
    adsLinks.result.toSet
  }
}

object SougouAdsLinkCollector {
  def main(args: Array[String]): Unit = {
    val collector = new SougouAdsLinkCollector
    collector.getAdsLinks

  }
}