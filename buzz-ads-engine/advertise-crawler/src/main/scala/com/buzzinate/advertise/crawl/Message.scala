package com.buzzinate.advertise.crawl

import com.buzzinate.advertise.kestrel.MetaAd
import com.buzzinate.httpext.Response

object State {
  val FromApi = 0
  val OK = 1
  val Other = 2
  val NotOK = 3
  val IOError = 4
  val Error = 5
}

case class Thumbnail(src: String, format: String, data: Array[Byte])
case class ExtractedContent(canonicalUrl: String, title: String, rawTitle: String, snippets: List[String], keywords: List[String], thumbnail: Thumbnail)
case class Content(url: String, state: Int, resp: Response, ec: ExtractedContent, item: MetaAd) {
  val realUrl = if (resp != null) resp.lastRedirectedUri.getOrElse(url) else url
}

sealed trait CrawlMessage
case class Flush extends CrawlMessage
case class CheckQueue extends CrawlMessage
case class CrawlResult(content: Content) extends CrawlMessage
case class CrawlRequest(item: MetaAd) extends CrawlMessage