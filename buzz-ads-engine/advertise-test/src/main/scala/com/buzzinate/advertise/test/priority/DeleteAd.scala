package com.buzzinate.advertise.test.priority

import com.buzzinate.advertise.test.server.Servers
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum
import scala.collection.JavaConverters._

object DeleteAd {
	def main(args: Array[String]): Unit = {
	  val deleteEntryIds = Servers.deleteEntryId.split(",")
	  Servers.esClient.bulkDelete(deleteEntryIds.toList.asJava)
	  println("deleteDone: " + deleteEntryIds)
	}
}