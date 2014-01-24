package com.buzzinate.advertise.api

import scala.collection.JavaConverters.seqAsJavaListConverter
import com.buzzinate.advertise.algorithm.Recommend
import com.buzzinate.advertise.redis.RedisClient
import com.buzzinate.advertise.render.RenderAd
import com.buzzinate.advertise.server.Servers
import com.buzzinate.advertise.util.KeywordUtil
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.buzzads.ip.IPUtils
import com.buzzinate.buzzads.thrift.AdItem
import com.buzzinate.buzzads.thrift.AdParam
import com.buzzinate.common.util.ip.ProvinceName
import com.twitter.util.ExceptionalFunction0
import com.buzzinate.buzzads.thrift.AdEntrySizeEnum
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum

class AdvertiseServerOp(param: AdParam, recommender: Recommend) extends ExceptionalFunction0[java.util.List[AdItem]] with Loggable {

  val redisClient = new RedisClient(Servers.jedisPool)

  def applyE(): java.util.List[AdItem] = {
    val url = param.getUrl    
    val title = if (param.getTitleOption.isDefined && null != param.getTitle) param.getTitle else ""
    val keywords = if (param.getKeywordsOption.isDefined && null != param.getKeywords) param.getKeywords else ""
    val entryType = if (null != param.getResourceType) param.getResourceType else AdEntryTypeEnum.IMAGE
    val resourceSize = if (param.getResourceSize != null) param.getResourceSize.getValue.toString else AdEntrySizeEnum.SIZE80X80.getValue.toString
    val count = if (param.getCount < 1) 1 else param.getCount
    val userid = if (null == param.getUserid) "" else param.getUserid
    val ip = if (param.getIpOption.isDefined && null != param.getIp) param.getIp else ""
    val netWork = param.getNetWork.getValue
    val uuid = if (param.getUuidOption.isDefined && null != param.getUuid) param.getUuid else ""
      
    var isBlackUrl = false
    var i = 0
    val blackChannels = redisClient.getBlackChannel
    while (i < blackChannels.size && !isBlackUrl) {
      if (url.startsWith(blackChannels.apply(i))) {
        info("channel is blacked => " + blackChannels.apply(i) + " url => " + url)
        isBlackUrl = true
      }
      i += 1
    }
    if (isBlackUrl) {
      return List[AdItem]().asJava
    } 

    var province = ProvinceName.OTHER
    try {
      province = ProvinceName.findByValue(IPUtils.getProvinceCode(ip))
    } catch {
      case e: Exception => {
        warn("can't recognize this ip : " + ip)
        province = ProvinceName.OTHER
      }
    }
    val startTime = System.currentTimeMillis

    val metaKeywords = KeywordUtil.splitMetaKeywords(keywords)
    val result = recommender.recommend(url, title, metaKeywords, entryType, resourceSize, count, userid, province, netWork, uuid)
    val serveInfos = new StringBuilder
    serveInfos.append(url).append(", ip => ").append(ip).append(", network => ").append(netWork).append(", title => ").append(title).append(", resourceSize => ").append(resourceSize).append(", keywords => ").append(keywords).append(", entryType => ").append(entryType.name).append(", count => ").append(count).append(", resultsSize => ").append(result.size).append(", province => ").append(province.name).append(", userid => ").append(userid).append(", uuid => ").append(uuid).append(", cost => " + (System.currentTimeMillis - startTime))
    info(serveInfos.toString)
    val res = RenderAd.convert(result)
    res
  }
}