package com.buzzinate.advertise.redis.json;

import java.io.Serializable;

/**
 * 
 * @author feeling
 *
 */
public class CampaignPriority implements Serializable{
	
	static final long serialVersionUID = -7034897190745966919L;
	
	public String campaignId;
	public double priority;
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public double getPriority() {
		return priority;
	}
	public void setPriority(double priority) {
		this.priority = priority;
	}
}
