package com.buzzinate.advertise.elasticsearch.client;

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
import com.buzzinate.advertise.elasticsearch.api.AdQuery;
import com.buzzinate.advertise.elasticsearch.api.HitAd;
import com.buzzinate.advertise.elasticsearch.api.Query;
import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum;
import com.buzzinate.advertise.elasticsearch.parser.AdQueryParser;
import com.buzzinate.advertise.elasticsearch.parser.EntryQueryParser;
import com.buzzinate.advertise.util.CalendarUtil;
import com.buzzinate.advertise.util.Constants;
import com.buzzinate.advertise.util.LinkParser;
import com.buzzinate.advertise.util.StringUtils;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum;
import com.buzzinate.common.util.ip.ProvinceName;

/**
 * buzzads-indexer提供对外服务的客户端
 * 
 * @author feeling
 * 
 */
public class Client {

	private static final String TYPE = "ad";
	private static final String AD_INDEX = "buzzadsvtwo_ad";
	public static final String[] fields = new String[] { FieldEnum.ENTRY_ID.getFieldName(), FieldEnum.CAMPAIGN_ID.getFieldName(),
			FieldEnum.GROUP_ID.getFieldName(), FieldEnum.TITLE.getFieldName(), FieldEnum.LINK.getFieldName(), FieldEnum.DISPLAY_URL.getFieldName(),
			FieldEnum.DESCRIPTION.getFieldName(), FieldEnum.RESOURCE_URL.getFieldName(), FieldEnum.BID_PRICE.getFieldName(), FieldEnum.BID_TYPE.getFieldName() };

	private final org.elasticsearch.client.Client client;

	public Client(String host, String clusterName) {
		this(Arrays.asList(host), clusterName);
	}

	public Client(List<String> hosts, String clusterName) {
		this(hosts, 9300, clusterName);
	}

	public Client(List<String> hosts, int port, String clusterName) {
		Settings settings = setNodename(ImmutableSettings.settingsBuilder()).put("http.enabled", "false").put("transport.tcp.port", "9300-9400")
				.put("discovery.zen.ping.unicast.hosts", StringUtils.join(hosts, ",")).build();

		Node node = NodeBuilder.nodeBuilder().clusterName(clusterName).client(true).settings(settings).node();
		client = node.client();
	}

	public String state() {
		return client.admin().cluster().state(new ClusterStateRequest()).actionGet().getState().toString();
	}

	public boolean updateTemplate(String name, String template) {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest(name).source(template);
		return client.admin().indices().putTemplate(request).actionGet().isAcknowledged();
	}

	private ImmutableSettings.Builder setNodename(ImmutableSettings.Builder builder) {
		String uuid = UUID.randomBase64UUID();
		try {
			return builder.put("node.name", InetAddress.getLocalHost().getHostName() + "-" + uuid);
		} catch (UnknownHostException e) {
			return builder.put("node.name", uuid);
		}
	}

	public Map<String, Ad> get(List<String> adIds) {
		MultiGetRequestBuilder mget = client.prepareMultiGet();
		for (String adId : adIds) {
			mget.add(AD_INDEX, TYPE, adId);
		}
		MultiGetResponse resps = mget.execute().actionGet();
		Map<String, Ad> id2ads = new HashMap<String, Ad>();
		for (MultiGetItemResponse resp : resps) {
			if (resp.getResponse() != null && resp.getResponse().isExists()) {
				Ad ad = fromSource(resp.getResponse().getSource());
				id2ads.put(ad.entryId, ad);
			}
		}
		return id2ads;
	}

	public List<String> exists(List<String> adIds) {
		List<String> existAdIds = new ArrayList<String>();
		if (adIds.isEmpty())
			return existAdIds;

		MultiGetRequestBuilder mget = client.prepareMultiGet();
		for (String adId : adIds) {
			mget.add(new MultiGetRequest.Item(AD_INDEX, TYPE, adId).fields(FieldEnum.ENTRY_ID.getFieldName()));
		}
		MultiGetResponse resps = mget.execute().actionGet();
		for (MultiGetItemResponse resp : resps) {
			if (resp.getResponse().isExists()) {
				String adId = (String) resp.getResponse().getField(FieldEnum.ENTRY_ID.getFieldName()).getValue();
				existAdIds.add(adId);
			}
		}
		return existAdIds;
	}

	public void bulkAdd(List<Ad> ads) {
		if (ads.isEmpty())
			return;

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Ad doc : ads) {
			doc.title = StringUtils.escapeJson(doc.title);
			doc.description = StringUtils.escapeJson(doc.description);
			doc.keyword = StringUtils.escapeJson(doc.keyword);
			String json = JSON.toJSONString(doc, false);
			bulkRequest.add(client.prepareIndex(AD_INDEX, TYPE, doc.entryId).setSource(json));
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			throw new RuntimeException(bulkResponse.buildFailureMessage());
		}
	}

	public void update(String adId, Map<String, Object> source) {
		UpdateRequestBuilder updateRequest = client.prepareUpdate();
		updateRequest.setIndex(AD_INDEX).setType(TYPE).setId(adId);
		updateRequest.setDoc(source);
		updateRequest.setRefresh(true);
		updateRequest.setTimeout(TimeValue.timeValueSeconds(10l));
		updateRequest.execute().actionGet();
	}

	public void bulkDelete(List<String> ids) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (String id : ids) {
			bulkRequest.add(new DeleteRequest(AD_INDEX, TYPE, id));
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			throw new RuntimeException(bulkResponse.buildFailureMessage());
		}
	}

	public List<HitAd> query(String queryText, FilterBuilder filterBuilder, int max, String queryParserName) {
		Query q = new Query(queryText);
		Map<String, Query> querymap = new HashMap<String, Query>();
		querymap.put(queryParserName, q);

		SearchResponse resp = client.prepareSearch(AD_INDEX).setTypes(TYPE).setSearchType(SearchType.DFS_QUERY_AND_FETCH).setFilter(filterBuilder)
				.setQuery(JSON.toJSONString(querymap, false)).addFields(fields).setFrom(0).setSize(max).execute().actionGet();

		List<HitAd> docs = new ArrayList<HitAd>();
		for (SearchHit sh : resp.getHits()) {
			HitAd doc = fromField(sh.fields());
			Float score = sh.getScore();
			doc.score = score.doubleValue();
			docs.add(doc);
		}
		return docs;
	}

	public List<HitAd> query(AdQuery adQuery) {
		BoolFilterBuilder bfBuilder = FilterBuilders.boolFilter();
		bfBuilder.must(FilterBuilders.termFilter(FieldEnum.STATUS.getFieldName(), AdStatusEnum.ENABLED.getCode()));
		bfBuilder.must(FilterBuilders.termFilter(FieldEnum.NETWORK.getFieldName(), String.valueOf(adQuery.getNetWork())));

		// 按广告的entryId过滤
		if (!adQuery.getBlackIds().isEmpty()) {
			for (String blackId : adQuery.getBlackIds()) {
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.ENTRY_ID.getFieldName(), blackId));
			}
		}

		// 按广告的orderId过滤
		if (!adQuery.getBlackOrders().isEmpty()) {
			for (String blackOrder : adQuery.getBlackOrders()) {
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.GROUP_ID.getFieldName(), blackOrder));
			}
		}

		// 按广告的campaignId过滤
		if (!adQuery.getBlackCampaigns().isEmpty()) {
			for (String blackCampaign : adQuery.getBlackCampaigns()) {
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.CAMPAIGN_ID.getFieldName(), blackCampaign));
			}
		}

		// 按广告主进行过滤
		if (!adQuery.getBlackAdvertisers().isEmpty()) {
			for (String blackAdvertiser : adQuery.getBlackAdvertisers()) {
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.ADVERTISER_ID.getFieldName(), blackAdvertiser));
			}
		}

		// 按广告的audienceCategory进行过滤
		BoolFilterBuilder mainCatAudienceBuilder = FilterBuilders.boolFilter();
		mainCatAudienceBuilder.should(FilterBuilders.termFilter(FieldEnum.AUDIENCE_MAIN_CAT.getFieldName(), Constants.BLANK_CATEGORY()));
		BoolFilterBuilder subCatAudienceBuilder = FilterBuilders.boolFilter();
		subCatAudienceBuilder.should(FilterBuilders.termFilter(FieldEnum.AUDIENCE_SUB_CAT.getFieldName(), Constants.BLANK_CATEGORY()));
		if (!adQuery.getCats().isEmpty()) {
			List<Integer> mainCats = new ArrayList<Integer>();
			List<Integer> subCats = new ArrayList<Integer>();
			for (Integer cat : adQuery.getCats()) {
				if (cat >= 100) {
					subCats.add(cat);
				} else {
					mainCats.add(cat);
				}
			}

			if (!mainCats.isEmpty()) {
				for (Integer cat : mainCats) {
					mainCatAudienceBuilder.should(FilterBuilders.termFilter(FieldEnum.AUDIENCE_MAIN_CAT.getFieldName(), cat.toString()));
				}
			}

			if (!subCats.isEmpty()) {
				for (Integer cat : subCats) {
					subCatAudienceBuilder.should(FilterBuilders.termFilter(FieldEnum.AUDIENCE_SUB_CAT.getFieldName(), cat.toString()));
				}
			}
		}
		bfBuilder.must(mainCatAudienceBuilder);
		bfBuilder.must(subCatAudienceBuilder);
		
		// 按照投放频道进行过滤
		if (!org.apache.commons.lang.StringUtils.isEmpty(adQuery.getUrl())) {
			BoolFilterBuilder channelBuilder = FilterBuilders.boolFilter();
			channelBuilder.should(FilterBuilders.termFilter(FieldEnum.CHANNEL.getFieldName(), Constants.BLANK_CHANNEL()));
			for (String channel : LinkParser.getChannels(adQuery.getUrl())) {
				channelBuilder.should(FilterBuilders.termFilter(FieldEnum.CHANNEL.getFieldName(), channel));
			}
			bfBuilder.must(channelBuilder);
		}

		// 过滤黑名单中的词
		if (!org.apache.commons.lang.StringUtils.isEmpty(adQuery.getBlackWords())) {
			for (String blackWord : adQuery.getBlackWords().split(",")) {
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.KEYWORD.getFieldName(), blackWord));
			}
		}

		// 按域名进行过滤
		if (!org.apache.commons.lang.StringUtils.isEmpty(adQuery.getBlackDomains())) {
			for (String blackDomain : adQuery.getBlackDomains().split(",")) {
				bfBuilder.mustNot(FilterBuilders.prefixFilter(FieldEnum.REALURL.getFieldName(), blackDomain));
				bfBuilder.mustNot(FilterBuilders.prefixFilter(FieldEnum.LINK.getFieldName(), blackDomain));
			}
		}

		// 按广告实际链接进行过滤
		if (!org.apache.commons.lang.StringUtils.isEmpty(adQuery.getBlackRealUrls())) {
			for (String blackRealUrl : adQuery.getBlackRealUrls().split(",")) {
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.REALURL.getFieldName(), blackRealUrl));
				bfBuilder.mustNot(FilterBuilders.termFilter(FieldEnum.LINK.getFieldName(), blackRealUrl));
			}
		}

		// 按广告投放地点进行过滤
		BoolFilterBuilder locationBuilder = FilterBuilders.boolFilter();
		locationBuilder.should(FilterBuilders.termFilter(FieldEnum.LOCATION.getFieldName(), ProvinceName.OTHER.getCode()));
		if (!adQuery.getUserProvince().equals(ProvinceName.OTHER)) {
			locationBuilder.should(FilterBuilders.termFilter(FieldEnum.LOCATION.getFieldName(), adQuery.getUserProvince().getCode()));
		}
		bfBuilder.must(locationBuilder);

		// 按照resourceType进行过滤
		if (adQuery.getEntryType().getValue() != AdEntryTypeEnum.TEXT.getValue()) {
			bfBuilder.must(FilterBuilders.termFilter(FieldEnum.RESOURCE_TYPE.getFieldName(), adQuery.getEntryType().getValue()));
		} else {
			BoolFilterBuilder textImageBuilder = FilterBuilders.boolFilter();
			textImageBuilder.should(FilterBuilders.termFilter(FieldEnum.RESOURCE_TYPE.getFieldName(), AdEntryTypeEnum.TEXT.getValue()));
			textImageBuilder.should(FilterBuilders.termFilter(FieldEnum.RESOURCE_TYPE.getFieldName(), AdEntryTypeEnum.IMAGE.getValue()));
			bfBuilder.must(textImageBuilder);
		}

		// 按照投放资源的尺寸进行过滤
		if (!adQuery.getResourceSize().isEmpty()) {
			bfBuilder.must(FilterBuilders.termFilter(FieldEnum.RESOURCE_SIZE.getFieldName(), adQuery.getResourceSize()));
		}

		// 按照指定投放时间进行过滤
		Calendar calendar = Calendar.getInstance();

		BoolFilterBuilder scheduleDayFilter = FilterBuilders.boolFilter();
		scheduleDayFilter.should(FilterBuilders.termFilter(FieldEnum.SCHEDULEDAY.getFieldName(), CalendarUtil.getWeekDay(calendar).toString()));
		scheduleDayFilter.should(FilterBuilders.termFilter(FieldEnum.SCHEDULEDAY.getFieldName(), CalendarUtil.ALL_WEEKDAY.toString()));
		bfBuilder.must(scheduleDayFilter);
		int nowMins = CalendarUtil.getMinute(calendar);
		bfBuilder.must(FilterBuilders.numericRangeFilter(FieldEnum.START_SCHEDULETIME.getFieldName()).from(CalendarUtil.MIN_MINUTES_OF_DAY).to(nowMins)
				.includeLower(true).includeUpper(true));
		bfBuilder.must(FilterBuilders.numericRangeFilter(FieldEnum.END_SCHEDULETIME.getFieldName()).from(nowMins).to(CalendarUtil.MAX_MINUTES_OF_DAY)
				.includeLower(true).includeUpper(true));
		bfBuilder.must(FilterBuilders.numericRangeFilter(FieldEnum.START_TIME.getFieldName()).from(CalendarUtil.MIN_START_TIME).to(calendar.getTimeInMillis())
				.includeLower(true).includeUpper(true));
		bfBuilder.must(FilterBuilders.numericRangeFilter(FieldEnum.END_TIME.getFieldName()).from(calendar.getTimeInMillis()).to(CalendarUtil.MAX_END_TIME)
				.includeLower(true).includeUpper(true));

		bfBuilder.cache(true);
		if (!org.apache.commons.lang.StringUtils.isEmpty(adQuery.getEntryIds())) {
			return this.query(adQuery.getEntryIds(), bfBuilder, adQuery.getMax(), EntryQueryParser.NAME);
		} else {
			return this.query(adQuery.getTopicDistribution(), bfBuilder, adQuery.getMax(), AdQueryParser.NAME);
		}
	}

	private Ad fromSource(Map<String, Object> source) {
		Ad ad = new Ad();

		String entryId = (String) source.get(FieldEnum.ENTRY_ID.getFieldName());
		String groupId = (String) source.get(FieldEnum.GROUP_ID.getFieldName());
		String campaignId = (String) source.get(FieldEnum.CAMPAIGN_ID.getFieldName());
		String advertiserId = (String) source.get(FieldEnum.ADVERTISER_ID.getFieldName());
		String title = (String) source.get(FieldEnum.TITLE.getFieldName());
		String keyword = (String) source.get(FieldEnum.KEYWORD.getFieldName());
		String topicDistribution = (String) source.get(FieldEnum.TOPIC_DISTRIBUTION.getFieldName());
		String topTopicDistribution = (String) source.get(FieldEnum.TOP_TOPIC_DISTRIBUTION.getFieldName());
		long startTime = ((Number) (source.get(FieldEnum.START_TIME.getFieldName()))).longValue();
		long endTime = ((Number) (source.get(FieldEnum.END_TIME.getFieldName()))).longValue();
		int startScheduleTime = ((Number) (source.get(FieldEnum.START_SCHEDULETIME.getFieldName()))).intValue();
		int endScheduleTime = ((Number) (source.get(FieldEnum.END_SCHEDULETIME.getFieldName()))).intValue();
		String scheduleDay = (String) source.get(FieldEnum.SCHEDULEDAY.getFieldName());
		String link = (String) source.get(FieldEnum.LINK.getFieldName());
		String realUrl = (String) source.get(FieldEnum.REALURL.getFieldName());
		int resourceType = ((Number) (source.get(FieldEnum.RESOURCE_TYPE.getFieldName()))).intValue();
		String resourceUrl = (String) (source.get(FieldEnum.RESOURCE_URL.getFieldName()));
		String resourceSize = (String) (source.get(FieldEnum.RESOURCE_SIZE.getFieldName()));
		String displayUrl = (String) (source.get(FieldEnum.DISPLAY_URL.getFieldName()));
		String description = (String) (source.get(FieldEnum.DESCRIPTION.getFieldName()));
		String network = (String) (source.get(FieldEnum.NETWORK.getFieldName()));
		int bidType = ((Number) (source.get(FieldEnum.BID_TYPE.getFieldName()))).intValue();
		String audienceMainCat = (String) (source.get(FieldEnum.AUDIENCE_MAIN_CAT.getFieldName()));
		String audienceSubCat = (String) (source.get(FieldEnum.AUDIENCE_SUB_CAT.getFieldName()));
		String audienceLevel3Cat = (String) (source.get(FieldEnum.AUDIENCE_LEVEL_3_CAT.getFieldName()));
		String adMainCat = (String) (source.get(FieldEnum.AD_MAIN_CAT.getFieldName()));
		String adSubCat = (String) (source.get(FieldEnum.AD_SUB_CAT.getFieldName()));
		String adLevel3Cat = (String) (source.get(FieldEnum.AD_LEVEL_3_CAT.getFieldName()));
		double bidPrice = ((Number) (source.get(FieldEnum.BID_PRICE.getFieldName()))).doubleValue();
		int status = ((Number) (source.get(FieldEnum.STATUS.getFieldName()))).intValue();
		String location = (String) (source.get(FieldEnum.LOCATION.getFieldName()));
		String channel = (String) (source.get(FieldEnum.CHANNEL.getFieldName()));
		long lastModified = ((Number) (source.get(FieldEnum.LASTMODIFIED.getFieldName()))).longValue();

		ad.setEntryId(entryId).setGroupId(groupId).setCampaignId(campaignId).setAdvertiserId(advertiserId).setTitle(title).setKeyword(keyword)
				.setTopicDistribution(topicDistribution).setTopTopicDistribution(topTopicDistribution).setStartTime(startTime).setEndTime(endTime)
				.setStartScheduleTime(startScheduleTime).setEndScheduleTime(endScheduleTime).setScheduleDay(scheduleDay).setLink(link).setRealUrl(realUrl)
				.setResourceType(resourceType).setResourceUrl(resourceUrl).setResourceSize(resourceSize).setDisplayUrl(displayUrl).setDescription(description)
				.setNetwork(network).setBidType(bidType).setAudienceMainCat(audienceMainCat).setAudienceSubCat(audienceSubCat)
				.setAudienceLevel3Cat(audienceLevel3Cat).setAdMainCat(adMainCat).setAdSubCat(adSubCat).setAdLevel3Cat(adLevel3Cat).setBidPrice(bidPrice)
				.setStatus(status).setLocation(location).setChannel(channel).setLastModified(lastModified);

		return ad;
	}

	private HitAd fromField(Map<String, SearchHitField> fields) {
		String entryId = fields.get(FieldEnum.ENTRY_ID.getFieldName()).getValue();
		String campaignId = fields.get(FieldEnum.CAMPAIGN_ID.getFieldName()).getValue();
		String orderId = fields.get(FieldEnum.GROUP_ID.getFieldName()).getValue();

		String title = "";
		String link = "";
		String displayUrl = "";
		String description = "";
		String resourceUrl = "";
		Double bidPrice = 0.0;
		Integer bidType = 3;// the default bidType is CPC, if we can't get the
							// bidType from index
		if (fields.get(FieldEnum.TITLE.getFieldName()) != null) {
			title = fields.get(FieldEnum.TITLE.getFieldName()).getValue();
		}
		if (fields.get(FieldEnum.LINK.getFieldName()) != null) {
			link = fields.get(FieldEnum.LINK.getFieldName()).getValue();
		}
		if (fields.get(FieldEnum.DISPLAY_URL.getFieldName()) != null) {
			displayUrl = fields.get(FieldEnum.DISPLAY_URL.getFieldName()).getValue();
		}
		if (fields.get(FieldEnum.DESCRIPTION.getFieldName()) != null) {
			description = fields.get(FieldEnum.DESCRIPTION.getFieldName()).getValue();
		}
		if (fields.get(FieldEnum.RESOURCE_URL.getFieldName()) != null) {
			resourceUrl = fields.get(FieldEnum.RESOURCE_URL.getFieldName()).getValue();
		}
		if (fields.get(FieldEnum.BID_PRICE.getFieldName()) != null) {
			bidPrice = fields.get(FieldEnum.BID_PRICE.getFieldName()).getValue();
		}
		if (fields.get(FieldEnum.BID_TYPE.getFieldName()) != null) {
			bidType = fields.get(FieldEnum.BID_TYPE.getFieldName()).getValue();
		}

		return new HitAd(entryId, campaignId, orderId, title, link, displayUrl, description, resourceUrl, bidPrice, bidType);
	}

	public void close() {
		client.close();
	}

}
