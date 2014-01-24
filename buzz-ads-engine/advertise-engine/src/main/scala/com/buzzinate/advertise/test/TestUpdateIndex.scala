package com.buzzinate.advertise.test

import scala.collection.JavaConverters.seqAsJavaListConverter
import com.buzzinate.advertise.elasticsearch.client.Client
import com.buzzinate.advertise.util.Constants._
import com.buzzinate.advertise.util.Config
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.UUID;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import com.alibaba.fastjson.JSON;
import com.buzzinate.advertise.elasticsearch.api.Ad;
import com.buzzinate.advertise.elasticsearch.api.HitAd;
import com.buzzinate.advertise.elasticsearch.api.Query;
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum;
import com.buzzinate.advertise.elasticsearch.parser.AdQueryParser;
import com.buzzinate.advertise.util.CalendarUtil;
import com.buzzinate.advertise.util.StringUtils;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.common.util.ip.ProvinceName;
object TestPriority {
  def main(args: Array[String]): Unit = {
    val prop = Config.getConfig("config.properties")

    val settings = setNodename(ImmutableSettings.settingsBuilder()).put("http.enabled", "false").put("transport.tcp.port", "9300-9400")
      .put("discovery.zen.ping.unicast.hosts", StringUtils.join(prop.getList(ELASTIC_SEARCH_HOSTS_KEY).asJava, ",")).build();

    val node = NodeBuilder.nodeBuilder().clusterName(prop.getString(ELASTIC_SEARCH_CLUSTER_NAME)).client(true).settings(settings).node();
    val client = node.client()

    val updateFields = new java.util.HashMap[String, Object]
    updateFields.put(FieldEnum.KEYWORD.getFieldName, "三地方")
    updateFields.put(FieldEnum.BID_PRICE.getFieldName, new java.lang.Double(3.2))
    val updateRequest = client.prepareUpdate();
    updateRequest.setIndex("buzzads_ad").setType("ad").setId("11");
    updateRequest.setDoc(updateFields);
    updateRequest.setTimeout(TimeValue.timeValueSeconds(10l));
    updateRequest.execute().actionGet();
    println("updatedone")
  }

  def setNodename(builder: ImmutableSettings.Builder): ImmutableSettings.Builder = {
    val uuid = UUID.randomBase64UUID();
    try {
      return builder.put("node.name", InetAddress.getLocalHost().getHostName() + "-" + uuid);
    } catch {
      case e: Exception =>
        return builder.put("node.name", uuid);
    }
  }
}