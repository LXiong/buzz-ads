package com.buzzinate.advertise.util

import collection.JavaConverters._
import collection.JavaConversions._
import scala.collection.JavaConverters._
import com.buzzinate.advertise.util.Constants._
import java.net.URL
import scala.collection.mutable.ListBuffer

object LinkParser extends Loggable {

  val adIdPlaceholder = "{adEntryId}"
    
  val linkSuffix = "u={uuid}_{adEntryId}"
    
  def checkLinkSuffix(linkUrl: String): String = {
    if(linkUrl.contains(linkSuffix)){
      return linkUrl
    } else {
      if(linkSuffix.contains("?")){
        linkUrl + "&" + linkSuffix
      } else {
        linkUrl + "?" + linkSuffix
      }
    }
  }
  
  def formatDomain(domain: String): String = {    
    if(org.apache.commons.lang.StringUtils.isEmpty(domain)){
      return domain
    }
    var formatDomain = domain.toLowerCase
    if (!domain.startsWith("http://")){
      formatDomain = "http://" + formatDomain
    }
    if (!formatDomain.endsWith("/")) {
      formatDomain += "/"
    }
    formatDomain
  }
  
  def getChannels(url: String): java.util.List[String] = {
    if(org.apache.commons.lang.StringUtils.isEmpty(url)){
      return List(url)
    }
    
    var bareUrl = url.toLowerCase.replaceFirst("http://", "").replaceFirst("https://", "")
    if (bareUrl.endsWith("/")) {
      bareUrl = bareUrl.substring(0, bareUrl.size - 1)
    }
    
    val channels = ListBuffer[String]()
    var sitePrefix = "http://"
    bareUrl.split("/").foreach { s => 
      sitePrefix = sitePrefix + s + "/"
      channels.append(sitePrefix)
    }
    if(!bareUrl.startsWith("www.")) {
      val domain = bareUrl.split("/").apply(0)
      val splits = domain.split("\\.")
      if (splits.size > 0) {
        if (splits.size == 2) {
          channels.append("http://www." + domain + "/")
        } else if (splits.size > 2) {
          channels.append("http://" + domain.replaceFirst(splits.apply(0), "www") + "/")
          channels.append("http://" + domain.replaceFirst(splits.apply(0)+".", "") + "/")
        } else {
          //do nothing
        }
      }
    } else {
      sitePrefix = "http://"
      bareUrl = bareUrl.substring(4)
      bareUrl.split("/").foreach { s => 
      sitePrefix = sitePrefix + s + "/"
      channels.append(sitePrefix)
    }
    }
    channels.toList.asJava
  }

  def replaceAdId(adId: String, linkUrl: String): String = {
    org.apache.commons.lang.StringUtils.replace(linkUrl, adIdPlaceholder, adId)
  }
  
  def removeIllChar(linkUrl: String): String = {
    linkUrl.replaceAll("[{,}]", "")
  }

  def main(args: Array[String]): Unit = {
//    val linkUrl = "http://count.chanet.com.cn/click.cgi?a=461014&d=315042&u={uuid}_{adEntryId}&e=&url="
//      println(removeIllChar(linkUrl))
//    val adId = "12"
//    println(replaceAdId(adId, linkUrl))
    getChannels("http://life.gmw.cn/2013-07/31/content_8460556.htm").foreach(println)
    println
    getChannels("http://chinadaily.cn/2013-07/03/content_16716374.htm").foreach(println)
   // println
   // getChannels("http://news.ifeng.com/21234_h.html").foreach(println)
    println(formatDomain("c03.optimix.asia"))
//    println(LinkParser.replaceAdId("435", LinkParser.checkLinkSuffix("www.baidu.com")))
//    val linkUrl = "http://c03.optimix.asia/imageviews?opxCREATIVEID=3245&opxPLACEMENTID=763&opxCREATIVEASSETID=13501&opxMODE=1&opxURL=http://www.bole.com/zt/jhzt/"
//      println(replaceAdId("188", checkLinkSuffix(linkUrl)))
  }

}