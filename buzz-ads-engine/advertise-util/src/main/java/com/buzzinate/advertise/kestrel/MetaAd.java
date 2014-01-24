package com.buzzinate.advertise.kestrel;

import java.io.Serializable;

/**
 * this class is a serial object , put it in kestrel , wait for advertise-crawler get
 * @author feeling
 *
 */
public class MetaAd implements Serializable{
	
	private static final long serialVersionUID = -3470744999237597659L;
	
	private int entryId;
	private int orderId;
	private int status;
	private String title;
	private String keywords;
	private String resourceUrl;
	private String link;
	private Boolean isTrackRealurlMode;
	
	public MetaAd(){
		
	}
	
	public int getEntryId() {
		return entryId;
	}
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getIsTrackRealurlMode() {
		return isTrackRealurlMode;
	}

	public void setIsTrackRealurlMode(Boolean isTrackRealurlMode) {
		this.isTrackRealurlMode = isTrackRealurlMode;
	}
	
}
