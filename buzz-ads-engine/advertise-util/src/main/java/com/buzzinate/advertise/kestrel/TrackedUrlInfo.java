package com.buzzinate.advertise.kestrel;

import java.io.Serializable;

public class TrackedUrlInfo implements Serializable{
	
	private static final long serialVersionUID = -3470744999237594359L;
	
	private int entryId;
	private String realUrl;
	
	public TrackedUrlInfo(){
		
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public String getRealUrl() {
		return realUrl;
	}

	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}	
	
}
