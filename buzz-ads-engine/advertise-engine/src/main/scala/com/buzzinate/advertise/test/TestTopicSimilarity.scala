package com.buzzinate.advertise.test

import edu.stanford.nlp.tmt.model.lda.CVB0LDA
import edu.stanford.nlp.tmt.stage._
import scalanlp.pipes.Pipes.global.file
import com.buzzinate.advertise.server.Servers
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer
import com.buzzinate.advertise.algorithm.TopicRecommend

object TestTopicSimilarity {

  val penaltyfactor = 10000

  def main(args: Array[String]): Unit = {
    val modelFile = file(Servers.ldaModelPath)
    var id2keywords = Map[String, String]()
    SimpleChineseTokenizer
    println("startLoad")
    val ldaModel = LoadGibbsLDA(modelFile)
    println("loadDone")
    //val testText = List("adf","阿里地方集散地","阿斯兰地方口气我人","；了你库存薪资在v从哦")
    val dist1 = ldaModel.infer("美女,欧美,欧美,欧美,欧美,性感,女郎,女郎,女郎,商品赠,100,蜕变,加赠,电动,按摩枕,康宁")
    val dist2 = ldaModel.infer("乐扣,开台,商品赠,100,蜕变,加赠,电动,按摩枕,康宁锅,康宁,用户,10,全新,购物送,新生,时尚购,赠上,全新开,27,上加,购物,优质,商品,glass,满额,按摩,时尚")
    println(getKLDistance(dist1, dist2))

  }
  def getKLDistance(pDistribution: Array[Double], qDistribution: Array[Double]): Double = {
    if (pDistribution.isEmpty || qDistribution.isEmpty || pDistribution.length != qDistribution.length) {
      return Double.MaxValue
    }
    var distance = 0.0
    for (i <- 0 until pDistribution.length) {
      val px = pDistribution.apply(i)
      val qx = qDistribution.apply(i)
      if (px > 0.0) {
        if (qx > 0.0) {
          distance += (px * (Math.log(px) - Math.log(qx)))
        } else {
          distance += penaltyfactor
        }
      }
    }
    distance
  }
}