package com.buzzinate.advertise.redis.json;

import java.io.Serializable;

/**
 * 
 * @author feeling
 *
 */
public class CampaignBalanceRatio implements Serializable{
	
	static final long serialVersionUID = -7034897190742966939L;
	
	public String campaignId;
	public double balanceRatio;
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public double getBalanceRatio() {
		return balanceRatio;
	}
	public void setBalanceRatio(double balanceRatio) {
		this.balanceRatio = balanceRatio;
	}
}
