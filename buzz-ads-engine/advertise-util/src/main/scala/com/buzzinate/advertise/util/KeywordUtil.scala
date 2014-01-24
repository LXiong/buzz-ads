package com.buzzinate.advertise.util

import collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import com.buzzinate.advertise.model.KeywordInfo
import org.ansj.splitWord.Analysis
import org.ansj.splitWord.analysis.ToAnalysis
import java.io.StringReader
import org.ansj.domain.Term
import java.util.ArrayList
import org.ansj.util.recognition.NatureRecognition
import com.buzzinate.nlp.util.TextUtil
import org.ansj.splitWord.Segment
import org.ansj.library.TwoWordLibrary
import java.text.SimpleDateFormat
import java.util.Date
import com.buzzinate.nlp.util.DictUtil

object KeywordUtil {

  
  def refineKeywords(title: String): List[KeywordInfo] = {
    Segment.split(title).asScala.toList filter { term =>
      val usefulpos = term.getNatrue.natureStr.startsWith("n") && term.getNatrue.natureStr != null || term.getNatrue == "userDefine"
      usefulpos && term.getName.length >= 2
    } map { term =>
      KeywordInfo(term.getName, 1, 2)
    }
  }

  def extractKeywords(title: String, keywords: List[KeywordInfo]): List[KeywordInfo] = {
    val keywordset = keywords.map(kw => kw.word).toSet
    val lowerCaseTitle = title.trim.toLowerCase
    keywords ++ getNounKeywords(lowerCaseTitle).filterNot(w2f => keywordset.contains(w2f._1)).map(w2f => KeywordInfo(w2f._1, w2f._2, KeywordInfo.TITLE))
  }

  def getNounKeywords(title: String): Map[String, Int] = {
    val wordList = new ListBuffer[String]
    
    var prevTerm: Term = null
    var prevUseful = false
    Segment.split(title).asScala.toList foreach { term =>
      val word = term.getName
      val isUsefulPos = !DictUtil.isUseless(term)
      if (isUsefulPos) {
        if (word.length > 1) wordList += word
        if (word.length == 1 && prevUseful) wordList += (prevTerm.getName + word)
      }
      prevTerm = term
      prevUseful = isUsefulPos
    }
    
    wordList.result.groupBy(word => word).map{w2l => 
      val (word, list) = w2l
      word -> list.size
    }
  }
  
  def splitMetaKeywords(metaKeywords: String): List[String] = {
    splitMetaKeywords(metaKeywords.split("[,，]").toList)
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
      val word = sb.toString.trim.toLowerCase
      if (word.size > 1) words += word
    }
    words.result
  }

  // calculate the idf from ansj dictionary
  def dictidf(word: String): Double = DictUtil.splitIdf(word)
}