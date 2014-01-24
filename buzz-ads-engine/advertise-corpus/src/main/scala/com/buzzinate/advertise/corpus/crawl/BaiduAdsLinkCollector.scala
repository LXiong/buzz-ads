package com.buzzinate.advertise.corpus.crawl
import scala.io.Source
import com.buzzinate.advertise.util.ResourceLoader
import java.net.URLEncoder
import com.buzzinate.httpext.Crawl
import com.buzzinate.httpext.req
import com.buzzinate.httpext._
import com.buzzinate.httpext.conversions._
import collection.JavaConverters._
import org.jsoup.Jsoup
import scala.collection.mutable.ListBuffer
import com.buzzinate.advertise.corpus.util.Utils

class BaiduAdsLinkCollector extends AdsLinkCollector {

  val baiduPrefix = "http://www.baidu.com/s?wd=" //%CE%C4%D1%A7
  val termsPath = "/home/feeling/terms.txt"
  val matchAttr = "size=\"-1\" color=\"#008000\""

  def getAdsLinks(): Set[String] = {
    val terms = ResourceLoader.loadResourceFile("terms.txt")
    val adsLinks = new ListBuffer[String]

    terms.foreach { term =>
      val url = baiduPrefix + URLEncoder.encode(term)
      try {
        val resp = Crawl.http.when(sc => true)(req(url) as_resp)
        Thread.sleep(500)
        val doc = Jsoup.parse(resp.html, url)
        val adsPart = doc.getElementsByAttributeValueContaining("size", "-1")
        val ads = adsPart.iterator().asScala
        ads.foreach { ad =>
          try {
            val node = ad.childNode(0)
            if (node.childNodes.isEmpty()) {
              val attr = ad.attributes()
              val adLink = node.toString()
              if (attr.toString().trim == matchAttr && !adLink.contains("baidu")) {
                adsLinks.append(Utils.parseAdLink(adLink))
                println(adLink)
              }
            }
          } catch {
            case e: Exception => {
            }
          }
        }
      } catch {
        case e: Exception => {
        }
      }
      println("")
    }
    adsLinks.result.toSet
  }
}

object BaiduAdsLInkCollector {
  def main(args: Array[String]): Unit = {
    val adsLinkColl = new BaiduAdsLinkCollector
    adsLinkColl.getAdsLinks()
  }
}