package com.buzzinate.nlp.tmt.tokenizer

import scalanlp.io.TextReader
import scalanlp.collection.LazyIterable
import scalanlp.serialization.{ SubtypedCompanion, TypedCompanion0 }
import scalanlp.text.tokenize.Tokenizer
import scalanlp.text.Unicode
import org.ansj.splitWord.Segment
import scala.collection.JavaConverters._
import com.buzzinate.nlp.util.WordUtil

/**
 * Simple Chinese Tokenizer extends scalanlp Tokenizer with ansj
 */
trait SimpleChineseTokenizer extends Tokenizer

object SimpleChineseTokenizer extends SubtypedCompanion[SimpleChineseTokenizer] {
  
  val uselessNatures = Set("null", "w", "m", "f", "r", "d", "p", "y", "q", "ad", "c", "u", "ud", "ug", "uj", "ul", "uv", "uz", "l", "m")
  
  prepare();

  for (cc <- List(this, Tokenizer)) {
    //cc.register[V0]("SimpleChineseTokenizer.V0");
    cc.register[V1]("SimpleChineseTokenizer.V1");
  }

  def apply() = V1();

//  /** Version 0 of the SimpleChineseTokenizer. */
//  class V0 extends SimpleChineseTokenizer {
//    override def apply(in: String): Iterable[String] = {
//      var string = in;
//      string = V0.r1.replaceAllIn(string, "");
//      string = V0.r2.replaceAllIn(string, "$1 ");
//      string = V0.r3.replaceAllIn(string, " $1");
//      string.split("\\s+");
//    }
//  }
//
//  object V0 extends TypedCompanion0[V0] {
//    prepare();
//
//    // delete word-final hyphens when followed by newlines
//    val r1 = "(?<=\\w)-\\s*\n\\s*".r;
//
//    // add spaces around non-word-internal punctuation
//    val r2 = "(?<=\\W)(\\p{P})(?! )".r;
//    val r3 = "(?! )(\\p{P})(?=\\W)".r;
//
//    private val _instance = new V0();
//    def apply() = _instance;
//
//    override def name = "SimpleChineseTokenizer.V0"
//  }

  class V1 extends SimpleChineseTokenizer {
    def apply(in: String): Iterable[String] = {

      val splits = Segment.split(in.replace('.', ' ')).asScala
      splits.filter{term =>
        val trimName = term.getName.trim
      	!uselessNatures.contains(term.getNatrue().natureStr) && trimName.length > 1 && !WordUtil.isStopword(trimName)
      }.map(term => term.getName().trim())
      
      

    }
  }

  object V1 extends TypedCompanion0[V1] {
    prepare();

    private val _instance = new V1();
    def apply() = _instance;

    override def name = "SimpleChineseTokenizer.V1";
  }

  def main(args: Array[String]): Unit = {
    val v1 = new V1
    val text = "i am a good boy.how do you think"
    val iter = v1.apply(text)
    iter.toList.foreach(println)
  }
}