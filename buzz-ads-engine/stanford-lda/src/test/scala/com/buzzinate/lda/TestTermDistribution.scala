package com.buzzinate.lda

import scalanlp.io._
import scalanlp.stage._
import scalanlp.stage.text._
import scalanlp.text.tokenize._
import scalanlp.pipes.Pipes.global._
import edu.stanford.nlp.tmt.stage._
import edu.stanford.nlp.tmt.model.lda._
import edu.stanford.nlp.tmt.model.llda._;
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer

object TestTermDistribution {
  def main(args: Array[String]): Unit = {
    SimpleChineseTokenizer()
    val modelPath = file("lda-63d0cf9a-30-3f724522")
    val model = LoadCVB0LDA(modelPath)
    val index = model.termIndex.get.apply("电脑")
    val distri = model.infer("道德,制度,权力,思想,毛泽东,汉奸,人类,精神")
    distri.foreach(println)
    
    

    
   //for (i <- 0 until model.topicIndex.size){
//    println(model.topicIndex.get.get(0)) 
  // }
    
    
//    
//    val score = model.getTopicTermDistribution(0).apply(index)
//    println(score)
  }

}