package com.buzzinate.advertise.tools
import com.buzzinate.nlp.util.TextUtil
import scala.collection.mutable.ListBuffer
import org.apache.commons.lang.StringUtils
import com.buzzinate.advertise.model.KeywordInfo
import com.buzzinate.advertise.util.KeywordUtil
import com.buzzinate.advertise.algorithm.TopicRecommend
import com.buzzinate.advertise.util.DoublePriorityQueue
import scala.collection.JavaConverters._

object LdaUtil {

  val te = new com.buzzinate.nlp.util.TitleExtractor
  val defaultDist = Array.make(TopicRecommend.ldaModel.numTopics, 1.0 / TopicRecommend.ldaModel.numTopics)
  
  def uniformDistribution(weight: Double): String = {
    val dist = Array.make(TopicRecommend.ldaModel.numTopics, weight)
    TopicRecommend.getTopicDistStr(dist)
  }
  
  def getTopicDistribution(title: String, keywords: String, topTopicSize: Int, weighted: Double = 1.0): (String, String) = {

    val filledTitle = TextUtil.fillText(te.extract(title))

    val processedKeywords =
      if (StringUtils.isEmpty(keywords)) {
        List[KeywordInfo]()
      } else {
        splitMetaKeywords(keywords.split("[,，]").toList).map { mk =>
          StringUtils.trim(mk).toLowerCase
        }.map { w =>
          val field = if (filledTitle.contains(TextUtil.fillWord(w))) KeywordInfo.META_TITLE else KeywordInfo.META
          KeywordInfo(w, 1, field)
        }
      }
    val refinedKeywords = KeywordUtil.extractKeywords(filledTitle, processedKeywords).map(keyword => keyword.word).mkString(",")
    val distribution = TopicRecommend.ldaModel.infer(refinedKeywords)

    if (!distribution.apply(0).isNaN) {
      (TopicRecommend.getTopicDistStr(distribution, weighted), getTopTopicStr(distribution, topTopicSize))
    } else {
      (TopicRecommend.getTopicDistStr(defaultDist, weighted), "")
    }

  }

  def splitMetaKeywords(metaKeywords: List[String]): List[String] = {
    val words = new ListBuffer[String]
    for (metaKeyword <- metaKeywords) {
      val sb = new StringBuffer
      for (ch <- metaKeyword) {
        if (Character.isLetterOrDigit(ch)) sb.append(ch)
        else {
          val word = sb.toString.trim
          if (word.size > 1) words += word
          sb.setLength(0)
        }
      }
      val word = sb.toString.trim
      if (word.size > 1) words += word
    }
    words.result
  }

  def getTopTopicStr(distribution: Array[Double], topN: Int): String = {
    val tdpq = new DoublePriorityQueue[Int](topN)
    var counter = 0
    distribution foreach { prob =>
      tdpq.add(prob, counter)
      counter += 1
    }
    val topTopics = tdpq.entries

    topTopics.asScala.map { kv =>
      val index = kv.value
      index
    }.mkString(",")
  }
}