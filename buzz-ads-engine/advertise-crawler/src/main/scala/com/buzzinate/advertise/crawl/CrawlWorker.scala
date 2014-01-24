package com.buzzinate.advertise.crawl

import java.io.ByteArrayOutputStream
import java.io.IOException

import org.apache.commons.lang.StringUtils

import com.buzzinate.advertise.util.DomainNames
import com.buzzinate.advertise.util.LinkParser
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.advertise.util.URLCanonicalizer
import com.buzzinate.extract.HtmlExtractor
import com.buzzinate.httpext.conversions.toHandlerVerbs
import com.buzzinate.httpext.Crawl
import com.buzzinate.httpext.req
import com.mortennobel.imagescaling.AdvancedResizeOp
import com.mortennobel.imagescaling.ResampleOp

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import dispatch.Request.toRequestVerbs
import javax.imageio.ImageIO

case class CrawlNext(last: Long)

class CrawlWorker(master: ActorRef) extends Actor with Loggable {
  def receive = {

    case CrawlRequest(item) => {
      if(!StringUtils.isEmpty(item.getLink)){
        val url = LinkParser.removeIllChar(item.getLink)
      val content = try {
        val resp = Crawl.http.when(sc => true)(req(url) as_resp)
        if (resp.ok) {
          val baseurl = resp.lastRedirectedUri.getOrElse(url)
          val ec = extract(baseurl, resp.html, resp.header)
          val state = State.OK
          info("item : " + item.getEntryId + " crawled ok")
          Content(url, state, resp, ec, item)
        } else Content(url, State.NotOK, resp, null, item)
      } catch {
        case e: IOException => {
          error("Could not crawl " + url + " - " + e.getMessage)
          Content(url, State.IOError, null, null, item)
        }
        case e: Exception => {
          error("Could not crawl " + url + " - " + e.getMessage)
          Content(url, State.Error, null, null, item)
        }
      }
      master ! CrawlResult(content)
      }
      
    }
  }

  def extract(url: String, html: String, header: Map[String, Set[String]]): ExtractedContent = {

    try {
      val a = HtmlExtractor.extract(html, url)
      var time = if (a.time > 0) a.time else System.currentTimeMillis
      val imgsrc = if (a.thumbnail == null) null else URLCanonicalizer.getCanonicalURL(a.thumbnail, url)
      val thumbnail = if (imgsrc != null && imgsrc.startsWith("http://")) {
        try {
          val (img, thumbnailTime) = crawlImage(imgsrc, url)
          time = math.min(time, thumbnailTime)
          img
        } catch {
          case e: Exception => {
            warn("could not crawl image " + imgsrc + ", error=" + e.getMessage)
            null
          }
        }
      } else null
      ExtractedContent(a.canonicalUrl, a.title, a.rawTitle, a.snippets, a.metaKeywords, thumbnail)
    } catch {
      case e: Exception => {
        warn("extract Exception => " + url)
        return null
      }
    }
  }

  def crawlImage(imgsrc: String, url: String): (Thumbnail, Long) = {
    val (format, img, thumbnailTime) = Crawl.http((req(imgsrc) <:< Map("Referer" -> url)) as_image)
    if (CrawlWorker.isGoodThumbnail(img.getWidth, img.getHeight)) {
      val w = img.getWidth
      val h = img.getHeight
      val cut = if (w > h) img.getSubimage((w - h) / 2, 0, h, h) else img.getSubimage(0, 0, w, w)
      val resampleOp = new ResampleOp(100, 100)
      resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal)
      val small = resampleOp.filter(cut, null)
      val baos = new ByteArrayOutputStream
      ImageIO.write(small, format, baos)
      (Thumbnail(imgsrc, format, baos.toByteArray), thumbnailTime)
    } else (null, System.currentTimeMillis)
  }
}

object CrawlWorker {
  def isGoodThumbnail(width: Int, height: Int) = width >= 100 && height >= 100
  
  def main(args: Array[String]): Unit = {
    val url = "http://count.chanet.com.cn/click.cgi?a=461014&d=315042&u={uuid}_{adEntryId}&e=&url="
    val realUrl = LinkParser.removeIllChar(url)
    val testUrl = "http://video.sina.com.cn/movie/zongyi/"
    println(DomainNames.safeGetHost(testUrl))
    val resp = Crawl.http.when(sc => true)(req(realUrl) as_resp)
    val a = HtmlExtractor.extract(resp.html, realUrl)
    println(resp.lastRedirectedUri)
    println(a.rawTitle)
    println(a.title)
  }
}