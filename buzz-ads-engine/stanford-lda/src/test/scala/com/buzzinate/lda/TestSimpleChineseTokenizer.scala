package com.buzzinate.lda
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer.V1

object TestSimpleChineseTokenizer {
	def main(args: Array[String]): Unit = {
	  val v1 = new V1
    val text = "i am a good boy.how do you think"
    val iter = v1.apply(text)
    iter.toList.foreach(println)
	}
}