package com.buzzinate.advertise.tools

import com.buzzinate.advertise.util.Loggable
import com.buzzinate.advertise.util.ResourceLoader

object TempAdUtil extends Loggable{
  val adPriorities = loadPriorAds()

  def loadPriorAds(): Map[String, Double] = {
    var adId2Priority = Map[String, Double]()
    val lines = ResourceLoader.loadResourceFile("priorAds")
    lines.foreach { line =>
      val splits = line.split(",")
      if (splits.length == 2) {
        val id = splits.apply(0)
        val priority = splits.apply(1).toDouble
        adId2Priority += id -> priority
        info("priority ad => " + id + " => priority : " + priority)
      }
    }
    adId2Priority
  }
  
  def main(args: Array[String]): Unit = {
    println(adPriorities.getOrElse("1", 1.0))
  }
}