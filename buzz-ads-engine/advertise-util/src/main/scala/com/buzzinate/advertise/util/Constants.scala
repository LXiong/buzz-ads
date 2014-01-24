package com.buzzinate.advertise.util

object Constants {

  val KESTREL_HOST = "kestrel.hosts"
    
  val MONGO_HOST = "mongo.hosts"
  
  val CRAWL_AD_QUEUE = "ad_engine_crawl"
  val URL_TRACK_QUEUE = "ad_engine_trackUrl"
  val ADEVENT_QUEUE = "ad_event"
  val SITE_CONFIG_EVENT_QUEUE = "site_config";
  val CHANNEL_EVENT_QUEUE = "channel_event";
  val AUDIENCE_EVENT_QUEUE = "audience_event"
    
  val MONIT_NODE = "monit.node"
  val LISTEN_EVENT_NODE = "listenevent.node"
    
  val MATCH_AUDIENCE_CATEGORY = "match_audience_Category"
    
  val REDIS_HOSTS_KEY = "redis.hosts"
    
  val ELASTIC_SEARCH_HOSTS_KEY = "elastic.search.hosts"
  val ELASTIC_SEARCH_CLUSTER_NAME = "elastic.search.cluster"
    
  val DATA_ACCESS_SERVER_HOSTS = "data.access.server.hosts"
    
  val HTABLE_REFERENCE = "htable.reference"
    
  val ADS_THRIFT_THREADS = "ads.thrift.threads"  
  val ADS_THRIFT_PORT = "ads.thrift.port"
  val ADS_THRIFT_TIMEOUT = "ads.thrift.timeout"
  
  val LDA_MODEL_PATH = "lda.model.path"
    
  val TOP_TOPIC = "recommender.top.topics"
  val SEARCH_EXPAND_COEFFICIENT = "search.expand.coefficient"
  val BUDGET_WARN_PRIORITY = "budget.warn.priority"
  val CTR_ATTENUATION_GRAVITY = "ctr.attenuation.gravity"
    
  val PRIORITY_CPM = "priority.cpm"
  val PRIORITY_CPC = "priority.cpc"
  val PRIORITY_CPT = "priority.cpt"
  val PRIORITY_CPD = "priority.cpd"
  
  val BLANK_CATEGORY = "none"
  val BLANK_CHANNEL = "none"
     
  val SECS_ONE_DAY = 60 * 60 * 24
  val SECS_ONE_HOUR = 60 * 60
}