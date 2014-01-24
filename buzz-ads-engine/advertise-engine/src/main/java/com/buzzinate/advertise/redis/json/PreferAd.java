package com.buzzinate.advertise.redis.json;

import java.io.Serializable;

/**
 * 
 * @author feeling
 *
 */
public class PreferAd implements Serializable{
	
	static final long serialVersionUID = -7034897190545966969L;
	
	public String campaignId;
	public String entryId;
	public String advertiser;
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	public String getAdvertiser() {
		return advertiser;
	}
	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}
	
}