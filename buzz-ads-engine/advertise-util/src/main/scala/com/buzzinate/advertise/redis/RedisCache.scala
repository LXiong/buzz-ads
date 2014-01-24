package com.buzzinate.advertise.redis

import com.buzzinate.advertise.util.Loggable

import redis.clients.jedis.ShardedJedisPool
import redis.clients.jedis.ShardedJedis
import collection.JavaConverters._

class RedisCache(pool: ShardedJedisPool) extends Loggable {
  def getCache[T](key: String)(implicit mt: Manifest[T]): Option[(T, Long)] = {
    val json = RedisCache.use(pool) { jedis => jedis.get(key) }
    if (json == null || "nil".equals(json)) None
    else {
      val e = JEntry.parseJson(json, mt.erasure).asInstanceOf[JEntry[T]]
      Some(e.value -> e.cacheTime)
    }
  }
  
  def putCache[T](key: String, value: T)(implicit mt: Manifest[T]): Unit = {
    val e = new JEntry(value)
    val json = e.toJson
    RedisCache.use(pool) { jedis =>
      jedis.set(key, json)
    }
  }

  def putCache[T](key: String, value: T, expireInSecs: Int)(implicit mt: Manifest[T]): Unit = {
    val e = new JEntry(value)
    val json = e.toJson
    RedisCache.use(pool) { jedis =>
      jedis.set(key, json)
      jedis.expire(key, expireInSecs)
    }
  }
  
  def getCache(key: String): String = {
    val value = RedisCache.use(pool) { jedis => jedis.get(key) }
    if ("nil".equals(value)){ null }
    else {
      value
    }
  }
  
  def putCache(key: String, value: String): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.set(key, value)
    }
  }

  def putCache(key: String, value: String, expireInSecs: Int): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.set(key, value)
      jedis.expire(key, expireInSecs)
    }
  }

  def updateExpire(key: String, expireInSecs: Int): Unit = {
    RedisCache.use(pool) { jedis =>
      jedis.expire(key, expireInSecs)
    }
  }

  def getCacheList[T](key: String)(implicit mt: Manifest[T]): Option[(java.util.List[T], Long)] = {
    val json = RedisCache.use(pool) { jedis => jedis.get(key) }
    if (json == null || "nil".equals(json)) None
    else {
      val e = JEntryList.parseJson(json, mt.erasure).asInstanceOf[JEntryList[T]]
      Some(e.values -> e.cacheTime)
    }
  }

  def putCacheList[T](key: String, values: java.util.List[T])(implicit mt: Manifest[T]): Unit = {
    val e = new JEntryList(values)
    val json = e.toJson
    RedisCache.use(pool) { jedis =>
      jedis.set(key, json)
    }
  }

  def putCacheList[T](key: String, values: java.util.List[T], expireInSecs: Int)(implicit mt: Manifest[T]): Unit = {
    val e = new JEntryList(values)
    val json = e.toJson
    RedisCache.use(pool) { jedis =>
      jedis.set(key, json)
      jedis.expire(key, expireInSecs)
    }
  }
  
  def batchPutCache[T](keyValues: List[(String, T)])(implicit mt: Manifest[T]): Unit = {
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      keyValues map { case(key, value) =>
        val e = new JEntry(value)
        val json = e.toJson
        p.set(key, json)
      }
      p.sync
    } 
  }
  
  def batchPutCache[T](keyValues: List[(String, T)], expireInSecs: Int)(implicit mt: Manifest[T]): Unit = {
    RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      keyValues map { case(key, value) =>
        val e = new JEntry(value)
        val json = e.toJson
        p.set(key, json)
        p.expire(key, expireInSecs)
      }
      p.sync
    } 
  }

  def batchGetCacheList[T](keys: List[String])(implicit mt: Manifest[T]): List[(String, List[T], Long)] = {
    val key2jsons = RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      val rs = keys map { key =>
        key -> p.get(key)
      }
      p.sync

      rs map {
        case (key, res) =>
          key -> res.get
      } filterNot {
        case (key, json) =>
          json == null || json == "nil"
      }
    }
    key2jsons map {
      case (key, json) =>
        val e = JEntryList.parseJson(json, mt.erasure).asInstanceOf[JEntryList[T]]
        (key, e.values.asScala.toList, e.cacheTime)
    }
  }
  
  def batchGetCache[T](keys: List[String])(implicit mt: Manifest[T]): List[(String, T)] = {
    val key2value = RedisCache.use(pool) { jedis =>
      val p = jedis.pipelined
      val rs = keys map { key =>
        key -> p.get(key)
      }
      p.sync

      rs map {
        case (key, res) =>
          key -> res.get
      } filterNot {
        case (key, json) =>
          json == null || json == "nil"
      }
    }
    key2value map {
      case (key, json) =>
        val e = JEntry.parseJson(json, mt.erasure).asInstanceOf[JEntry[T]]
        (key, e.value)
    }
  }
  
}

object RedisCache {
  def use[T](pool: ShardedJedisPool)(f: ShardedJedis => T): T = {
    var returned = false
    val jedis = pool.getResource
    try {
      f(jedis)
    } catch {
      case e => {
        pool.returnBrokenResource(jedis)
        returned = true
        throw e
      }
    } finally {
      if (!returned) pool.returnResource(jedis)
    }
  }
}
