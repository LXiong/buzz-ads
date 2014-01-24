package com.buzzinate.advertise.elasticsearch.api;

import java.util.ArrayList;
import java.util.List;

import com.buzzinate.buzzads.thrift.AdEntryTypeEnum;
import com.buzzinate.common.util.ip.ProvinceName;

public class AdQuery {
	private String url = "";
	//id之间用逗号区隔
	private String entryIds = "";
	private List<Integer> cats = new ArrayList<Integer>();
	private String topicDistribution = "";
	private List<String> blackIds = new ArrayList<String>();
	private List<String> blackOrders = new ArrayList<String>();
	private List<String> blackCampaigns = new ArrayList<String>();
	private List<String> blackAdvertisers = new ArrayList<String>();
	private String blackWords = "";
	private String blackDomains = "";
	private String blackRealUrls = "";
	private AdEntryTypeEnum entryType = AdEntryTypeEnum.IMAGE;
	private String resourceSize = ""; 
	private ProvinceName userProvince = ProvinceName.OTHER;
	private int netWork;
	private int max;
	
	public String getUrl() {
		return url;
	}
	public AdQuery setUrl(String url) {
		this.url = url;
		return this;
	}
	public String getEntryIds() {
		return entryIds;
	}
	public AdQuery setEntryIds(String entryIds) {
		this.entryIds = entryIds;
		return this;
	}
	public List<Integer> getCats() {
		return cats;
	}
	public AdQuery setCats(List<Integer> cats) {
		this.cats = cats;
		return this;
	}
	public String getTopicDistribution() {
		return topicDistribution;
	}
	public AdQuery setTopicDistribution(String topicDistribution) {
		this.topicDistribution = topicDistribution;
		return this;
	}
	public List<String> getBlackIds() {
		return blackIds;
	}
	public AdQuery setBlackIds(List<String> blackIds) {
		this.blackIds = blackIds;
		return this;
	}
	public List<String> getBlackOrders() {
		return blackOrders;
	}
	public AdQuery setBlackOrders(List<String> blackOrders) {
		this.blackOrders = blackOrders;
		return this;
	}
	public List<String> getBlackCampaigns() {
		return blackCampaigns;
	}
	public AdQuery setBlackCampaigns(List<String> blackCampaigns) {
		this.blackCampaigns = blackCampaigns;
		return this;
	}
	public List<String> getBlackAdvertisers() {
		return blackAdvertisers;
	}
	public AdQuery setBlackAdvertisers(List<String> blackAdvertisers) {
		this.blackAdvertisers = blackAdvertisers;
		return this;
	}
	public String getBlackWords() {
		return blackWords;
	}
	public AdQuery setBlackWords(String blackWords) {
		this.blackWords = blackWords;
		return this;
	}
	public String getBlackDomains() {
		return blackDomains;
	}
	public AdQuery setBlackDomains(String blackDomains) {
		this.blackDomains = blackDomains;
		return this;
	}
	public String getBlackRealUrls() {
		return blackRealUrls;
	}
	public AdQuery setBlackRealUrls(String blackRealUrls) {
		this.blackRealUrls = blackRealUrls;
		return this;
	}
	public AdEntryTypeEnum getEntryType() {
		return entryType;
	}
	public AdQuery setEntryType(AdEntryTypeEnum entryType) {
		this.entryType = entryType;
		return this;
	}
	public String getResourceSize() {
		return resourceSize;
	}
	public AdQuery setResourceSize(String resourceSize) {
		this.resourceSize = resourceSize;
		return this;
	}
	public ProvinceName getUserProvince() {
		return userProvince;
	}
	public AdQuery setUserProvince(ProvinceName userProvince) {
		this.userProvince = userProvince;
		return this;
	}
	public int getNetWork() {
		return netWork;
	}
	public AdQuery setNetWork(int netWork) {
		this.netWork = netWork;
		return this;
	}
	public int getMax() {
		return max;
	}
	public AdQuery setMax(int max) {
		this.max = max;
		return this;
	}	
}
