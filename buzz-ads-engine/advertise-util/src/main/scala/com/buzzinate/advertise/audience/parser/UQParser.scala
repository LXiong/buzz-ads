package com.buzzinate.advertise.audience.parser

import com.alibaba.fastjson.JSON
import scala.collection.JavaConverters._
import com.alibaba.fastjson.JSONObject

object UQParser {
  case class UQItem(cookieId: String, campaignId: String, entryId: String, cap: Integer)
  
  def parseJson(text: String): UQItem = {
    val json = JSON.parseObject(text)
    val cookieId = json.keySet.asScala.head
    val valueStr = json.getString(cookieId)
    
    val kvs = JSON.parseObject(valueStr)
    val campaignId = kvs.getString("campaignId")
    val entryId = kvs.getString("entryId")
    val cap = kvs.getInteger("cap")
    
    new UQItem(cookieId, campaignId, entryId, cap)
  }
  
  def toJson(item: UQItem): String = {
    val json = new JSONObject
    val valueJson = new JSONObject
    valueJson.put("campaignId", item.campaignId)
    valueJson.put("entryId", item.entryId)
    valueJson.put("cap", item.cap)
    json.put(item.cookieId, JSON.toJSON(valueJson))
    json.toJSONString
  }

  def main(args: Array[String]): Unit = {
    val json = """
       {"Sse3kk3sx":{"campaignId":"34","entryId":"89","cap":7}}
      """
    val item = new UQItem("Sse3kk3sx", "34", "89", 7)
    println(toJson(item))
    println(parseJson(json))
  }
//  def toJson(pageInfo: PageInfo): String = {
//    val json = new JSONObject
//
//    val keywordsJson = new JSONObject
//
//    val keywords = new ListBuffer[JSONObject]
//    pageInfo.keywords foreach { keyword =>
//      val keywordJson = new JSONObject
//      keywordJson.put("word", keyword.word)
//      keywordJson.put("freq", keyword.freq)
//      keywordJson.put("field", keyword.field)
//      keywords.append(keywordJson)
//    }
//
//    val categories = new ListBuffer[JSONObject]
//    pageInfo.categories foreach { category =>
//      val categoryJson = new JSONObject
//      categoryJson.put("cat", category.cat)
//      categoryJson.put("score", NumberUtil.shortenDouble(category.score, 5))
//      categories.append(categoryJson)
//    }
//
//    json.put("keywords", JSON.toJSON(keywords.result.asJava))
//    json.put("category", JSON.toJSON(categories.result.asJava))
//    
//    json.toJSONString
//  }
}