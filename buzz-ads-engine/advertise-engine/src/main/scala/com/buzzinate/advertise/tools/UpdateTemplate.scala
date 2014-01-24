package com.buzzinate.advertise.tools

import scala.collection.JavaConverters.seqAsJavaListConverter

import com.buzzinate.advertise.elasticsearch.client.Client
import com.buzzinate.advertise.util.Constants._
import com.buzzinate.advertise.util.Config

object UpdateTemplate {

  def main(args: Array[String]): Unit = {
    val name = "buzzadsvtwo_tpl"
    val tpl =
      """{
    "template" : "buzzadsvtwo_*",
    "settings" : {
        "number_of_shards" : 1
    },
    "mappings" : {
        "ad" : {
          "properties" : {
    		"entryId" : {"type" : "string", "index" : "not_analyzed", "omit_norms": "true", "store": "yes"},
    		"groupId" : {"type" : "string", "index" : "not_analyzed", "omit_norms": "true", "store": "yes"},
    		"campaignId" : {"type" : "string", "index" : "not_analyzed", "omit_norms": "true", "store": "yes"},
    		"advertiserId" : {"type" : "string", "index" : "not_analyzed", "omit_norms": "true", "store": "yes"},
    		"title" : {"type" : "string", "index" : "no", "omit_norms": "true", "store": "yes"},
    		"keyword" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"topicDistribution" : {"type" : "string", "index_analyzer" : "distribution_analyzer", "index_options": "positions", "omit_norms": "true", "store": "yes"},
    		"topTopicDistribution" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"scheduleDay" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"startTime" : {"type" : "long", "index" : "not_analyzed", "store": "yes"},
    		"endTime" : {"type" : "long", "index" : "not_analyzed", "store": "yes"},
    		"startScheduleTime" : {"type" : "integer", "index" : "not_analyzed", "store": "yes"},
    		"endScheduleTime" : {"type" : "integer", "index" : "not_analyzed", "store": "yes"},
    		"link" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
            "realUrl" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"resourceType" : {"type" : "integer", "index" : "not_analyzed", "store": "yes"},
    		"resourceUrl" : {"type" : "string", "index" : "no", "omit_norms": "true", "store": "yes"},
            "resourceSize" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"displayUrl" : {"type" : "string", "index" : "no", "omit_norms": "true", "store": "yes"},
    		"description" : {"type" : "string", "index" : "no", "omit_norms": "true", "store": "yes"},
    		"network" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"audienceMainCat" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},	
    		"audienceSubCat" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},	
    		"audienceLevel3Cat"  : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"adMainCat"  : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"adSubCat"  : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
    		"adLevel3Cat"  : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},    
    		"bidType" : {"type" : "integer", "index" : "not_analyzed", "store": "yes"},
    		"bidPrice": {"type" : "double", "index" : "no", "store": "yes"},
    		"status": {"type" : "integer", "index" : "not_analyzed", "store": "yes"},
    		"location": {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
            "channel" : {"type" : "string", "index_analyzer" : "comma_analyzer", "index_options": "docs", "omit_norms": "true", "store": "yes"},
            "lastModified" : {"type" : "long", "store": "yes"}
	  }
        }
    }
}"""

    val prop = Config.getConfig("config.properties")
    println(prop.getList(ELASTIC_SEARCH_HOSTS_KEY))
    val client = new Client(prop.getList(ELASTIC_SEARCH_HOSTS_KEY).asJava, prop.getString(ELASTIC_SEARCH_CLUSTER_NAME))
    println("update template: " + client.updateTemplate(name, tpl))
    client.close
  }
}