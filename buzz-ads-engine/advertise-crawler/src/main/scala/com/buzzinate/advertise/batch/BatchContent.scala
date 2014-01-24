package com.buzzinate.advertise.batch

import scala.collection.immutable.List
import org.apache.commons.lang.StringUtils
import com.buzzinate.advertise.crawl.Content
import com.buzzinate.advertise.crawl.State
import com.buzzinate.advertise.crawl.Thumbnail
import com.buzzinate.advertise.kestrel.TrackedUrlInfo
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.up.UpYunClient
import com.buzzinate.advertise.up.UpYunUtil
import com.buzzinate.advertise.util.Constants.URL_TRACK_QUEUE
import com.buzzinate.buzzads.data.thrift.TAdItem
import com.buzzinate.buzzads.data.thrift.TResourceTypeEnum
import com.buzzinate.keywords.LezhiKeywordsExtractor
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.data.thrift.TAdStatusEnum

class BatchContent extends Batch with Loggable {

  def batch(contents: List[Content]): Unit = {
    contents filter (content => content.state == State.OK) foreach { content =>
      val item = content.item
      //将抓取到的最终跳转链接发入消息队列，供advertise-engine中的EventListener获取
      val trackedUrlInfo = new TrackedUrlInfo
      trackedUrlInfo.setEntryId(item.getEntryId)
      trackedUrlInfo.setRealUrl(content.realUrl)
      Servers.kestrelClient.put(URL_TRACK_QUEUE, trackedUrlInfo)
      val itemBuilder = new TAdItem.Builder
      itemBuilder.entryId(item.getEntryId).orderId(item.getOrderId).status(TAdStatusEnum.findByValue(item.getStatus))
      if (content.item.getIsTrackRealurlMode) {
        itemBuilder.destination(content.realUrl)
      } else {
        val ec = content.ec
        val url = ec.canonicalUrl
        val title = if (!StringUtils.isEmpty(ec.rawTitle)) ec.rawTitle else if (!StringUtils.isEmpty(ec.title)) ec.title else ""
        val realKeywords = LezhiKeywordsExtractor.extract(url, content.resp.html).map(k => k.word)
        val finalKeywords = (realKeywords.toSet | ec.keywords.toSet).mkString(",")
        val thumbnail = ec.thumbnail
        var resourceType = TResourceTypeEnum.TEXT
        var imageUrl = ""
        if (thumbnail != null) {
          resourceType = TResourceTypeEnum.IMAGE
          imageUrl = BatchContent.upload(url, thumbnail)
        }
        val now = System.currentTimeMillis

        itemBuilder.resourceType(resourceType)
        if (StringUtils.isEmpty(item.getTitle)) {
          if (!StringUtils.isEmpty(title)) {
            itemBuilder.title(title)
          }
        } else {
          itemBuilder.title(item.getTitle)
        }

        if (StringUtils.isEmpty(item.getResourceUrl)) {
          if (!StringUtils.isEmpty(imageUrl)) {
            itemBuilder.resourceUrl(imageUrl)
          }
        } else {
          itemBuilder.resourceUrl(item.getResourceUrl)
        }

        if (StringUtils.isEmpty(item.getKeywords)) {
          if (!StringUtils.isEmpty(finalKeywords)) {
            itemBuilder.keywords(finalKeywords)
          }
        } else {
          itemBuilder.keywords(item.getKeywords)
        }
      }
      //利用TAdDataAccessServices的接口，将抓取到的东西更新入buzz-ads的数据库
      val tAdItem = itemBuilder.build
      info("content.item.getIsTrackRealurlMode => " + content.item.getIsTrackRealurlMode)
      info("tAdItem.entryId => " + tAdItem.getEntryId)
      info("tAdItem.getDestination => " + tAdItem.getDestination)
      try {
        Servers.finagleClient.updateAdItem(tAdItem)
      } catch {
        case e: Exception => {
          error("finagleClient.updateAdItem Exception => " + e.getMessage())
          e.getStackTrace().foreach(trace => error(trace.toString()))
        }
      }

    }
  }
}

object BatchContent {
  val upyun = new UpYunClient("lezhi", "buzzinate", "buzzinate")
  val advertise = "advertise"

  def upload(url: String, thumbnail: Thumbnail): String = {
    val filename = "/" + UpYunUtil.md5(thumbnail.src) + "." + thumbnail.format
    val uf = upyun.upload(advertise, filename, thumbnail.data, true)
    uf.url
  }
}
