package com.buzzinate.advertise.elasticsearch.api;

/**
 * 搜索时返回的命中广告
 * 
 * @author feeling
 * 
 */
public class HitAd {
	public String entryId;
	public String campaignId;
	public String orderId;
	public String title;
	public String link;
	public String displayUrl;
	public String description;
	public String resourceUrl;
	public Double bidPrice;
	public Integer bidType;
	public Double score;

	public HitAd() {
	}

	public HitAd(String entryId, String campaignId, String orderId, String title, String link, String displayUrl, String description, String resourceUrl, Double bidPrice, Integer bidType) {
		this.entryId = entryId;
		this.campaignId = campaignId;
		this.orderId = orderId;
		this.title = title;
		this.link = link;
		this.displayUrl = displayUrl;
		this.resourceUrl = resourceUrl;
		this.bidPrice = bidPrice;
		this.bidType = bidType;
	}

	@Override
	public String toString() {
		return entryId + " /" + score + " [title=" + title + ", campaignId=" + campaignId + ", orderId=" + orderId + ", link=" + link + ", displayUrl=" + displayUrl + ", resourceUrl="
				+ resourceUrl + ", description=" + description + ", bicPrice=" + bidPrice + ", bidType=" + bidType + "]";
	}

}
