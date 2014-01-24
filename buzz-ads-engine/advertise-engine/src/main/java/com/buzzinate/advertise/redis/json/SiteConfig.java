package com.buzzinate.advertise.redis.json;

import java.io.Serializable;

/**
 * 
 * @author feeling
 *
 */
public class SiteConfig implements Serializable{
	
	static final long serialVersionUID = -7034897190745866939L;
	
	public String blackKeywords = "";
	public String blackDomains = "";
	public String blackRealUrls = "";
	
	public String getBlackKeywords() {
		return blackKeywords;
	}
	public void setBlackKeywords(String blackKeywords) {
		this.blackKeywords = blackKeywords;
	}
	public String getBlackDomains() {
		return blackDomains;
	}
	public void setBlackDomains(String blackDomains) {
		this.blackDomains = blackDomains;
	}
	public String getBlackRealUrls() {
		return blackRealUrls;
	}
	public void setBlackRealUrls(String blackRealUrls) {
		this.blackRealUrls = blackRealUrls;
	}
	
	
	
	
}
