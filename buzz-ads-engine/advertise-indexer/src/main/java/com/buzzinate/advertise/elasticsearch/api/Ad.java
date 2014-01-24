package com.buzzinate.advertise.elasticsearch.api;

import com.buzzinate.advertise.util.CalendarUtil;
import com.buzzinate.advertise.util.Constants;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.ip.ProvinceName;

/**
 * This Ad class is a Document of lucene index of Elastic Search Platform. When
 * we insert a document into the index, if the not null field is null , the
 * {@link BuzzAdsIndexerIllegalAdException} would be threw.
 * 
 * @author feeling
 * 
 */
public class Ad {
	public String entryId; // not null
	public String groupId; // not null
	public String campaignId; // not null
	public String advertiserId; // not null
	public String title; // not null
	public String keyword = "";
	public String topicDistribution = "";
	public String topTopicDistribution = "";
	public long startTime = CalendarUtil.MIN_START_TIME;
	public long endTime = CalendarUtil.MAX_END_TIME;
	public int startScheduleTime = CalendarUtil.MIN_MINUTES_OF_DAY;
	public int endScheduleTime = CalendarUtil.MAX_MINUTES_OF_DAY;
	public String scheduleDay = CalendarUtil.ALL_WEEKDAY.toString();
	public String link; // not null
	public String realUrl = "";
	public int resourceType = AdEntryTypeEnum.TEXT.getCode();
	public String resourceUrl = "";
	public String resourceSize = "";
	public String displayUrl = "";
	public String description = "";
	public String network = String.valueOf(AdNetworkEnum.BSHARE.getCode());
	public int bidType = BidTypeEnum.CPC.getCode();
	public String audienceMainCat = Constants.BLANK_CATEGORY();
	public String audienceSubCat = Constants.BLANK_CATEGORY();
	public String audienceLevel3Cat = Constants.BLANK_CATEGORY();
	public String adMainCat = Constants.BLANK_CATEGORY();
	public String adSubCat = Constants.BLANK_CATEGORY();
	public String adLevel3Cat = Constants.BLANK_CATEGORY();
	public double bidPrice = 0.0;
	public int status; // not null
	public String location = ProvinceName.OTHER.getCode();
	public String channel = Constants.BLANK_CHANNEL();
	public long lastModified = System.currentTimeMillis();

	public Ad() {

	}

	public Ad setEntryId(String entryId) {
		this.entryId = entryId;
		return this;
	}

	public Ad setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public Ad setCampaignId(String campaignId) {
		this.campaignId = campaignId;
		return this;
	}

	public Ad setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
		return this;
	}

	public Ad setTitle(String title) {
		this.title = title;
		return this;
	}

	public Ad setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public Ad setTopicDistribution(String topicDistribution) {
		this.topicDistribution = topicDistribution;
		return this;
	}

	public Ad setTopTopicDistribution(String topTopicDistribution) {
		this.topTopicDistribution = topTopicDistribution;
		return this;
	}

	public Ad setScheduleDay(String scheduleDay) {
		this.scheduleDay = scheduleDay;
		return this;
	}

	public Ad setStartScheduleTime(int startScheduleTime) {
		this.startScheduleTime = startScheduleTime;
		return this;
	}

	public Ad setEndScheduleTime(int endScheduleTime) {
		this.endScheduleTime = endScheduleTime;
		return this;
	}

	public Ad setLink(String link) {
		this.link = link;
		return this;
	}
	
	public Ad setRealUrl(String realUrl) {
		this.realUrl = realUrl;
		return this;
	}

	public Ad setResourceType(int resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public Ad setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
		return this;
	}
	
	public Ad setResourceSize(String resourceSize) {
		this.resourceSize = resourceSize;
		return this;
	}

	public Ad setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
		return this;
	}

	public Ad setDescription(String description) {
		this.description = description;
		return this;
	}

	public Ad setNetwork(String network) {
		this.network = network;
		return this;
	}

	public Ad setBidType(int bidType) {
		this.bidType = bidType;
		return this;
	}

	public Ad setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
		return this;
	}

	public Ad setStatus(int status) {
		this.status = status;
		return this;
	}

	public Ad setLocation(String location) {
		this.location = location;
		return this;
	}

	public Ad setLastModified(long lastModified) {
		this.lastModified = lastModified;
		return this;
	}
	
	public Ad setAudienceMainCat(String audienceMainCat) {
		this.audienceMainCat = audienceMainCat;
		return this;
	}

	public Ad setAudienceSubCat(String audienceSubCat) {
		this.audienceSubCat = audienceSubCat;
		return this;
	}

	public Ad setAudienceLevel3Cat(String audienceLevel3Cat) {
		this.audienceLevel3Cat = audienceLevel3Cat;
		return this;
	}

	public Ad setAdMainCat(String adMainCat) {
		this.adMainCat = adMainCat;
		return this;
	}

	public Ad setAdSubCat(String adSubCat) {
		this.adSubCat = adSubCat;
		return this;
	}

	public Ad setAdLevel3Cat(String adLevel3Cat) {
		this.adLevel3Cat = adLevel3Cat;
		return this;
	}

	public Ad setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public Ad setEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}
	
	public Ad setChannel(String channel) {
		this.channel = channel;
		return this;
	}
}
