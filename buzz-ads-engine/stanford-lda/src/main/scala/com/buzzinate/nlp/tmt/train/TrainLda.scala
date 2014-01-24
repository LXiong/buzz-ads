package com.buzzinate.nlp.tmt.train

import scalanlp.io._
import scalanlp.stage._
import scalanlp.stage.text._
import scalanlp.text.tokenize._
import scalanlp.pipes.Pipes.global._
import edu.stanford.nlp.tmt.stage._
import edu.stanford.nlp.tmt.model.lda._
import edu.stanford.nlp.tmt.model.llda._;
import com.buzzinate.nlp.tmt.tokenizer.SimpleChineseTokenizer

object TrainLda {
  def main(args: Array[String]): Unit = {

    val source = CSVFile("/home/feeling/adsCorpus.csv") ~> IDColumn(1)
  //  SimpleEnglishTokenizer
    val tokenizer = {
      SimpleChineseTokenizer() ~> // tokenize on space and punctuation
        MinimumLengthFilter(2) // take terms with >=3 characters
    }

    println("start prepare text")

    val text = {
      source ~> // read from the source file
        Column(2) ~> // select column containing text
        TokenizeWith(tokenizer) ~> // tokenize with tokenizer above
        TermCounter() ~> // collect counts (needed below)
        TermMinimumDocumentCountFilter(4) ~> // filter terms in <4 docs
        TermDynamicStopListFilter(30) ~> // filter out 30 most common terms
        DocumentMinimumLengthFilter(5) // take only docs with >=5 terms
    }

    // turn the text into a dataset ready to be used with LDA
    println("start prepare dataset")
    val dataset = LDADataset(text)
    println("prepare dataset done")

    for (numTopics <- 100 until 300 by 50) {
      val params = LDAModelParams(numTopics, dataset = dataset,
        topicSmoothing = 0.01, termSmoothing = 0.01)

      // Name of the output model folder to generate
      val gibbsModelPath = file("gibbs-lda-" + dataset.signature + "-" + params.signature)
  //    val ldaModelPath = file("lda-" + dataset.signature + "-" + params.signature)

      try {
        TrainGibbsLDA(params, dataset, output = gibbsModelPath, maxIterations = 500)
      } catch {
        case e: Exception => println(numTopics + " gibbs " + e.getMessage)
      }
//      try {
//        TrainCVB0LDA(params, dataset, output = ldaModelPath, maxIterations = 1000)
//      } catch {
//        case e: Exception => println(numTopics + " cvb " + e.getMessage)
//      }
    }

  }
}