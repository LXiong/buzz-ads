package com.buzzinate.advertise.crawl

import com.buzzinate.advertise.batch.Batch
import com.buzzinate.advertise.util.Loggable

import akka.actor.Actor

case class ContentList(contents: List[Content])

class BatchWorker(batches: List[Batch]) extends Actor with Loggable {
    def receive = {
      case ContentList(contents) => {
        info("flush contents: " + contents.size)
        for (batch <- batches) batch.batch(contents)
        val urls = contents.map(x => x.url)
      }
    }
}