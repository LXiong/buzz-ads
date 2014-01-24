package com.buzzinate.advertise.redis.json;

import java.io.Serializable;

/**
 * 
 * @author feeling
 *
 */
public class AdCtr implements Serializable{
	
	static final long serialVersionUID = -7034897190795966939L;
	
	public String adId;
	public long lastUpdateDay;
	public double ctr; // clickThroughRate
	
	public String getAdId() {
		return adId;
	}
	public void setAdId(String adId) {
		this.adId = adId;
	}
	public long getLastUpdateDay() {
		return lastUpdateDay;
	}
	public void setLastUpdateDay(long lastUpdateDay) {
		this.lastUpdateDay = lastUpdateDay;
	}
	public double getCtr() {
		return ctr;
	}
	public void setCtr(double ctr) {
		this.ctr = ctr;
	}

}
