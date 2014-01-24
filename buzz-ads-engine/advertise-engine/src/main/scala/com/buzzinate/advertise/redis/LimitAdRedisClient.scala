package com.buzzinate.advertise.redis

import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.mutable.ListBuffer
import com.buzzinate.advertise.redis.json.PreferAd
import com.buzzinate.advertise.server.Message.AdLevel
import com.buzzinate.advertise.util.Loggable
import RedisClient.remainSecsInDay
import redis.clients.jedis.ShardedJedisPool
import com.buzzinate.advertise.util.Constants

/**
 * 广告投放限制的相关redis操作（比如某些广告设定为，针对每个cookie，每日投放量不能超过指定次数;特定合作放会有定向投放指标，就是某些合作方的广告只能投放到指定广告）
 */
class LimitAdRedisClient(pool: ShardedJedisPool) extends Loggable {
  import RedisClient._
  import LimitAdRedisClient._

  private val redisCache = new RedisCache(pool)

  /**
   * 将cookieId 对应的优先投放广告插入redis
   */
  def updatePreferAd(cookieId: String, ad: PreferAd): Unit = {
    val e = new JEntry(ad)
    //此处不需要cacheTime
    e.cacheTime = 0l
    val json = e.toJson
    RedisCache.use(pool) { jedis =>
      jedis.sadd(preferAdPrefix + cookieId, json)
      jedis.expire(preferAdPrefix + cookieId, remainSecsInDay)
    }
  }

  /**
   * 根据cookieId,获得对它优先投放的广告
   */
  def getPreferAd(cookieId: String)(implicit mt: Manifest[PreferAd]): List[PreferAd] = {
    RedisCache.use(pool) { jedis =>
      val preferAds = jedis.smembers(preferAdPrefix + cookieId).asScala.toList.map { json =>
        val e = JEntry.parseJson(json, mt.erasure).asInstanceOf[JEntry[PreferAd]]
        e.value
      }
      return preferAds
    }
    Nil
  }

  /**
   * 存入一个广告id，和它最多被一个用户看到几次
   */
  def updateLimitAdShowTimes(adId: String, limitTimes: Double, adLevel: AdLevel): Unit = {
    redisCache.putCache(adLimitShowTimePrefix + adLevel.toString + adId, limitTimes.toString, remainSecsInDay)
  }

  /**
   * 删除一个广告和其对应投放次数，它可能之前被限定为有限次数投放，但现在限定解除
   */
  def deleteLimitAdShowTimes(adId: String, adLevel: AdLevel): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.del(adLimitShowTimePrefix + adLevel.toString + adId)
    }
  }

  /**
   * 更新广告集合,它们均被每一个用户看到的次数都是被限定的
   */
  def updateLimitAdSet(adIds: List[String], adLevel: AdLevel): Unit = {
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      adIds map { id =>
        p.sadd(limitAdSetPrefix + adLevel.toString, id)
      }
      p.sync
      jedis.expire(limitAdSetPrefix + adLevel.toString, remainSecsInDay)
    }
  }

  /**
   * 得到做定向投放的广告
   */
  def getAudienceAds(): List[String] = {
    RedisCache.use(pool) { jedis =>
      return jedis.smembers(audienceAdPrefix).asScala.toList
    }
    Nil
  }

  /**
   * 更新做做定向投放的广告
   */
  def updateAudienceAds(entryIds: List[String]): Unit = {
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      entryIds map { id =>
        p.sadd(audienceAdPrefix, id)
      }
      p.sync
      jedis.expire(audienceAdPrefix, Constants.SECS_ONE_DAY)
    }
  }

  /**
   * 在限制投放次数的广告集合中根据ad id来删除一些广告
   */
  def deleteAdFromLimitAdSet(adIds: List[String], adLevel: AdLevel): Unit = {
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      adIds map { id =>
        p.srem(limitAdSetPrefix + adLevel.toString, id)
      }
      p.sync
    }
  }

  /**
   * 更新指定的一个cookie对应的广告集合，这个集合里的广告在当日不会被该用户再看到
   */
  def updateCookie2BlackAdSet(cookie: String, adIds: List[String], adLevel: AdLevel): Unit = {
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      adIds map { id =>
        p.sadd(cookie2blackAdSetPrefix + adLevel.toString + cookie, id)
      }
      p.sync
      jedis.expire(cookie2blackAdSetPrefix + adLevel.toString + cookie, remainSecsInDay)
    }
  }

  /**
   * 得到一个cookie对应的广告集合，这个集合里的广告都不会在当日被该用户看到
   */
  def getCookie2BlackAdSet(cookie: String, adLevel: AdLevel): List[String] = {
    var blackAds = List[String]()
    RedisCache.use(pool) { jedis =>
      blackAds = jedis.smembers(cookie2blackAdSetPrefix + adLevel.toString + cookie).asScala.toList
    }
    blackAds
  }

  /**
   * 输入一个广告集合，返回它的一个子集合，这个子集合里的广告都是对用户浏览次数有限定的
   */
  def getLimitShowAd(adIds: List[String], adLevel: AdLevel): List[String] = {
    val limitShowAds = ListBuffer[String]()
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      val rs = adIds map { id =>
        id -> p.sismember(limitAdSetPrefix + adLevel.toString, id)
      }
      p.sync
      rs map {  case (id, res) =>
          if (res.get) {
            limitShowAds.append(id)
          }
      }
    }
    limitShowAds.result
  }

  /**
   * 输入一个cookie和广告集合（一般情况下，这是当前一个用户在此次浏览行为中看到的广告的子集合，这个子集合中的广告均是被限定浏览次数的广告），返回这些广告都被这个用户看到了几次
   */
  def increByCookieAdId(cookie: String, adIds: List[String], adLevel: AdLevel): Map[String, Double] = {
    var id2Times = Map[String, Double]()
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      val rs = adIds map { id =>
        id -> p.zincrby(cookielimitAdTimesPrefix + adLevel.toString + cookie, 1.0, id)
      }
      p.sync
      rs map { case (id, res) =>
          id2Times += id -> res.get
      }
      jedis.expire(cookielimitAdTimesPrefix + adLevel.toString + cookie, remainSecsInDay)
    }
    id2Times
  }

  /**
   * 输入一个cookie和一些广告id以及这些广告分别被这个cookie看了几次，返回一个广告集合，这个集合中的广告均是已满足限制条件，今后不应该再被这个cookie看到的广告
   */
  def getBlackAdId(cookie: String, ad2showTimes: Map[String, Double], adLevel: AdLevel): Set[String] = {
    var blackAdIds = Set[String]()
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      val rs = ad2showTimes map { ad2times =>
        ad2times -> p.get(adLimitShowTimePrefix + adLevel.toString + ad2times._1)
      }
      p.sync
      rs map { case ((adid, times), res) =>
          if (times >= res.get.toDouble) {
            blackAdIds += adid
          }
      }
    }
    blackAdIds
  }
}

object LimitAdRedisClient {
  val adLimitShowTimePrefix = "ad:limitshow:times:"
  val limitAdSetPrefix = "ad:limitshow:adset:"
  val cookielimitAdTimesPrefix = "ad:limitshow:cookie:times:"
  val cookie2blackAdSetPrefix = "ad:limitshow:cookie:blackadset:"
  val preferAdPrefix = "ad:limitshow:cookie:prefer:"
  val audienceAdPrefix = "ad:limitshow:audience:adentryset:"
}