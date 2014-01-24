package com.buzzinate.advertise.corpus.crawl

import scala.collection.mutable.ListBuffer
import com.buzzinate.httpext._
import com.buzzinate.httpext.conversions._
import collection.JavaConverters._
import java.text.SimpleDateFormat
import java.text.ParseException
import com.buzzinate.advertise.util.Loggable
import java.util.Locale
import java.io.IOException
import java.io.File
import com.buzzinate.advertise.util.DomainNames
import com.buzzinate.extract.HtmlExtractor
import com.buzzinate.advertise.util.URLCanonicalizer
import scala.collection.mutable.Queue
import java.util.concurrent.TimeUnit
import com.buzzinate.advertise.util.LinkParser
import org.apache.commons.lang.StringUtils
import org.jsoup.nodes.Document
import scala.collection.mutable.HashMap
import java.util.regex.Pattern
import com.buzzinate.util.TextUtil
import org.jsoup.Jsoup
import com.buzzinate.keywords.LezhiKeywordsExtractor
import com.buzzinate.advertise.util.Constants._
import scala.io.Source
import com.buzzinate.advertise.util.HashMapUtil
import com.buzzinate.advertise.util.DoublePriorityQueue
import com.buzzinate.advertise.util.ResourceLoader
import java.io.BufferedWriter
import java.io.FileWriter

case class ExtractedContent(canonicalUrl: String, title: String, rawTitle: String, snippets: List[String], keywords: List[String])

object CorpusCrawler {

  val stopWordSet = Set("有限公司", "旗舰店", "淘宝网", "皇冠店", "专卖店", "淘货铺", "旗舰", "淘宝", "公司", "官方", "著名", "商品", "皇冠", "信息", "专卖", "正品", "包邮", "产品", "网站")

  val crawlHeight = 1
  val crawlWidth = 20
  val topNKeywords = 50
  val FILTER_PARAMS = Array(
    Pattern.compile("^blz-.*$"), // lezhi track
    Pattern.compile("^bsh_.*$"), // bshare track
    Pattern.compile("^utm_.*$"), // google track
    Pattern.compile("^bdclkid$"), // baidu track
    Pattern.compile("^jtss.*$"), // jiathis track
    Pattern.compile("^request_locale$"),
    Pattern.compile("^lang$"),
    Pattern.compile("^lange$"),
    Pattern.compile("^langpair$"))

  def main(args: Array[String]): Unit = {
    val adsLinks = ResourceLoader.loadResourceFile("adsLinks.txt")
    val adsCorpusFile = new File("adsCorpus.csv")
    val bw = new BufferedWriter(new FileWriter(adsCorpusFile))
    var counter = 0
    val crawler = new CorpusCrawler(crawlWidth, crawlHeight, topNKeywords)
    adsLinks.foreach { adLink =>
      //val keywords = crawler.getKeywords(adLink)
      val content = crawler.getContent(adLink)
      if (!content.isEmpty) {
        bw.append(counter.toString).append(",").append(content).append("\n")
        counter += 1
      }
      bw.flush
    }
    bw.close()
  }
}

class CorpusCrawler(crawlWidth: Int, crawlHeight: Int, topNKeywords: Int) {

  def getSublink2Keywords(response: Response, url: String, sitePrefix: String, depth: Int): Map[(String, Int), List[(String, Int)]] = {
    val realKeywords = LezhiKeywordsExtractor.extract(url, response.html).map(k => (k.word, k.freq))
    var links2keywords = Map() + ((url, depth) -> realKeywords)
    if (depth > 0) {
      val doc = Jsoup.parse(response.html, url)
      val hrefs = extractLinks(url, doc, href => DomainNames.safeGetHost(href) == sitePrefix)
      hrefs.slice(0, crawlWidth).foreach { href =>
        val link = href._1
        try {
          val resp = Crawl.http.when(sc => true)(req(link) as_resp)
          if (resp.ok) {
            links2keywords ++= getSublink2Keywords(resp, link, sitePrefix, depth - 1)
          }
        } catch {
          case e: Exception => {
          }
        }
      }
    }
    links2keywords
  }

  def getSublinkContent(response: Response, url: String, sitePrefix: String, depth: Int): String = {
    val ec = extract(url, response.html, response.header)
    var content = ec.snippets.mkString(".")
    if (depth > 0) {
      val doc = Jsoup.parse(response.html, url)
      val hrefs = extractLinks(url, doc, href => DomainNames.safeGetHost(href) == sitePrefix && href.toLowerCase.indexOf("download") == -1 && href.toLowerCase.indexOf("pdf") == -1)
      hrefs.slice(0, crawlWidth).foreach { href =>
        val link = href._1
        try {
          val resp = Crawl.http.when(sc => true)(req(link) as_resp)
          if (resp.ok) {
            content += getSublinkContent(resp, link, sitePrefix, depth - 1)
          }
        } catch {
          case e: Exception => {
          }
        }
      }
    }
    content
  }

  def getContent(adUrl: String): String = {
    try {
      val resp = Crawl.http.when(sc => true)(req(adUrl) as_resp)
      if (resp.ok) {
        val baseurl = resp.lastRedirectedUri.getOrElse(adUrl)
        val sitePrefix = DomainNames.safeGetHost(baseurl)
        //val ec = extract(baseurl, resp.html, resp.header)
        getSublinkContent(resp, baseurl, sitePrefix, crawlHeight).replaceAll("\t", ".").replaceAll("\n", ".").replaceAll("\\s+", ".").replaceAll(",", ".").replaceAll("\"", ".").trim()
      } else {
        ""
      }
    } catch {
      case e: Exception => {
        ""
      }
    }

  }

  def getKeywords(adUrl: String): Set[(String, Double)] = {

    try {
      val resp = Crawl.http.when(sc => true)(req(adUrl) as_resp)
      if (resp.ok) {
        val baseurl = resp.lastRedirectedUri.getOrElse(adUrl)
        val sitePrefix = DomainNames.safeGetHost(baseurl)
        val ec = extract(baseurl, resp.html, resp.header)

        val url = ec.canonicalUrl

        val sublink2Keywords = getSublink2Keywords(resp, url, sitePrefix, crawlHeight)
        val keywordsCnt = new HashMap[String, Double] with HashMapUtil.DoubleHashMap[String]
        val docFreqCnt = new HashMap[String, Int] with HashMapUtil.IntHashMap[String]

        sublink2Keywords.foreach { s2k =>
          val ((link, depth), keywords) = ((s2k._1._1, s2k._1._2), s2k._2)
          val sumFreq = keywords.foldLeft(0.0)((sumFreq, keyword) => sumFreq + keyword._2)

          keywords.foreach { k =>
            val (keyword, freq) = (k._1, k._2 / sumFreq)
            keywordsCnt.adjustOrPut(keyword, freq, freq)
            docFreqCnt.adjustOrPut(keyword, 1, 1)
          }
        }
        val tdpq = new DoublePriorityQueue[String](topNKeywords)
        keywordsCnt.foreach { kc =>
          val (keyword, freq) = (kc._1, kc._2)
          val idf = Math.log(sublink2Keywords.size.toDouble / docFreqCnt.getOrElse(keyword, 1))
          tdpq.add(freq * idf, keyword)
        }
        val word2Score = tdpq.entries().asScala.map(entry => entry.value -> entry.key).toMap
        val title = if (!StringUtils.isEmpty(ec.rawTitle)) ec.rawTitle else if (!StringUtils.isEmpty(ec.title)) ec.title else ""
        val finalKeywords = (tdpq.values.asScala.toList ++ ec.keywords.filter(keyword => keywordsCnt.contains(keyword))).filter(word => !CorpusCrawler.stopWordSet.contains(word)).map { word =>
          var filteredWord = word
          CorpusCrawler.stopWordSet.foreach(sw => filteredWord = filteredWord.replaceAll(sw, ""))
          filteredWord
        }.toSet.filter(word => word.length > 1).map(word => word -> word2Score.getOrElse(word, 0.0)) //.mkString(".")
        println(sitePrefix + " => " + finalKeywords)
        finalKeywords

      } else {
        Set[(String, Double)]()
      }
    } catch {
      case e: Exception => {
        Set[(String, Double)]()
      }
    }
  }

  def extract(url: String, html: String, header: Map[String, Set[String]]): ExtractedContent = {

    try {
      val a = HtmlExtractor.extract(html, url)
      ExtractedContent(a.canonicalUrl, a.title, a.rawTitle, a.snippets, a.metaKeywords)
    } catch {
      case e: Exception => {

        return null
      }
    }
  }

  def extractLinks(url: String, doc: Document, urlfilter: String => Boolean): HashMap[String, String] = {
    val hrefs = new HashMap[String, String]
    val es = doc.body().getElementsByTag("a")
    for (i <- 0 until es.size()) {
      val e = es.get(i)
      val href = filter(URLCanonicalizer.getCanonicalURL(e.attr("href"), url))

      var isimg = false
      val img = e.select("img").first
      if (img != null) {
        val src = filter(URLCanonicalizer.getCanonicalURL(img.attr("src"), url))
        if (href == src) isimg = true
        else {
          val path = StringUtils.substringBeforeLast(src, "/")
          val format = StringUtils.substringAfterLast(src, ".")
          val filename = StringUtils.substring(src, path.length + 1, src.length - path.length - format.length - 1)
          val lcs = TextUtil.findLcs(filename, href)
          if (href.startsWith(path) && href.endsWith(format) && lcs.length * 3 > filename.length) isimg = true
        }
      }
      if (!isimg && isValidUrl(href) && urlfilter(href)) hrefs += href -> e.text
    }
    hrefs.remove(url)
    hrefs
  }

  def verifyPrefix(url: String, prefix: String): Boolean = url.startsWith(prefix)

  private def isValidUrl(href: String): Boolean = {
    href.startsWith("http://") && href.indexOf("http:/", 1) == -1 && href.indexOf("http%3A%2F%2F", 1) == -1
  }

  def shortenUrlPrefix(urlprefix: String) = {
    var i = urlprefix.length - 1
    var digitslash = true
    while (i >= 0 && digitslash) {
      val ch = urlprefix.charAt(i)
      digitslash = Character.isDigit(ch) || ch == '/' || ch == '-'
      i -= 1
    }
    urlprefix.substring(0, i + 2)
  }

  def filter(orgUrl: String): String = {
    val hashPos = orgUrl.indexOf('#')
    val url =
      if (hashPos > 0) orgUrl.substring(0, hashPos)
      else orgUrl

    val quesPos = url.indexOf('?')
    if (quesPos < 0) return url

    val mainUrl = url.substring(0, quesPos)
    val params = url.substring(quesPos + 1).split("&")
    val filteredParams = params.filter { p =>
      val pname = p.split('=')(0)
      CorpusCrawler.FILTER_PARAMS.forall { fp =>
        val m = fp.matcher(pname)
        !m.matches
      }
    }

    var resUrl: StringBuilder = new StringBuilder(mainUrl)
    if (!filteredParams.isEmpty) resUrl.append("?")
    filteredParams.sorted.foreach { p =>
      resUrl.append(p + "&")
    }
    if (resUrl.last == '&')
      resUrl.deleteCharAt(resUrl.size - 1)
    resUrl.toString
  }
}