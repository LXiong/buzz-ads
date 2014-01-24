package com.buzzinate.advertise.test.priority
import com.buzzinate.advertise.test.server.Servers
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum

object UpdateDistribution {
	def main(args: Array[String]): Unit = {
	  val entryId = Servers.updateEntryId
	  val maxDist = Array.make(200, 0.9999)
	  val updateFields = new java.util.HashMap[String, Object]
	  val topicStr = getTopicDistStr(maxDist)
	  updateFields.put(FieldEnum.TOPIC_DISTRIBUTION.getFieldName, topicStr)
	  Servers.esClient.update(entryId, updateFields)
	}
	
	def getTopicDistStr(distribution: Array[Double]): String = {
    var counter = 0
    distribution.map { dist =>
      val counterProb =
        if (dist == 0.0) {
          counter + "|" + 1.0
        } else {
          counter + "|" + dist
        }
      counter += 1
      counterProb
    }.mkString(" ")
  }
}