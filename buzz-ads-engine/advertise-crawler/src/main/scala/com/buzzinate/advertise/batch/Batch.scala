package com.buzzinate.advertise.batch
import com.buzzinate.advertise.crawl.Content

trait Batch {
  def batch(contents: List[Content]): Unit
}