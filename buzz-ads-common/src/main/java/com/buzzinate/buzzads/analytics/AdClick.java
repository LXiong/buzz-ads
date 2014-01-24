package com.buzzinate.buzzads.analytics;

import java.io.Serializable;
import java.util.Date;

import com.buzzinate.buzzads.enums.AdNetworkEnum;


/**
 * Ad click object
 * 
 * @author zyeming
 *
 */
public class AdClick implements Serializable {
    
    private static final long serialVersionUID = -8327966024247335235L;
    
    private AdNetworkEnum network;
    private long adEntryId;
    private String sourceUrl;
    private String publisherUuid;
    private Date createAt;
    private String cookieId = "";
    private String ip;
    private String ua;

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public long getAdEntryId() {
        return adEntryId;
    }
    
    public void setAdEntryId(long adEntryId) {
        this.adEntryId = adEntryId;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }
    
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    public String getPublisherUuid() {
        return publisherUuid;
    }
    
    public void setPublisherUuid(String publisherUuid) {
        this.publisherUuid = publisherUuid;
    }
    
    public Date getCreateAt() {
        return createAt;
    }
    
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
    
    public String getCookieId() {
        return cookieId;
    }
    
    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    

}
