package com.buzzinate.advertise.render

import scala.collection.JavaConverters.seqAsJavaListConverter

import org.apache.commons.lang.StringUtils

import com.buzzinate.advertise.elasticsearch.api.HitAd
import com.buzzinate.buzzads.thrift.AdItem

object RenderAd {

  def convert(docs: List[HitAd]): java.util.List[AdItem] = {
    docs map { doc => toAdItem(doc) } asJava
  }

  private def toAdItem(ad: HitAd): AdItem = {
    val builder = new AdItem.Builder
    builder.url(ad.link)
    builder.adEntryId(ad.entryId.toInt)
    if (!StringUtils.isEmpty(ad.title)) {
      builder.title(ad.title)
    }
    if (!StringUtils.isEmpty(ad.resourceUrl)) {
      builder.pic(ad.resourceUrl)
    }
    if (!StringUtils.isEmpty(ad.displayUrl)) {
      builder.displayUrl(ad.displayUrl)
    }
    if (!StringUtils.isEmpty(ad.description)) {
      builder.description(ad.description)
    }
    builder.build
  }
}