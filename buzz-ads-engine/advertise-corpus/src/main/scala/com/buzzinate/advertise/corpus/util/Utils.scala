package com.buzzinate.advertise.corpus.util

object Utils {
  
  /**
   * clean the adsLink to standard url.
   * Example:
   * 
   * www.xkm999.com		tel:400-630-6180
   * 
   */
  
  def parseAdLink(link: String): String = {
    var url = link.toLowerCase().trim().split("\\s+").apply(0).split("tel:").apply(0)
    while (url.endsWith("/")) {
      url = url.substring(0, url.length - 1)
    }
    val urlOffset = url.indexOf("www")
    if(urlOffset > 0 ){
      url = url.substring(urlOffset)
    }
    url = "http://" + url.split("/").apply(0).trim()
    url
  }
}