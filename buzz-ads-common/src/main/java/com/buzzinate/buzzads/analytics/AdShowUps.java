package com.buzzinate.buzzads.analytics;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.buzzinate.buzzads.enums.AdNetworkEnum;


/**
 * Ad show up object.
 * 
 * @author zyeming
 *
 */
public class AdShowUps implements Serializable {
    
    private static final long serialVersionUID = 3258550471834491329L;
    
    private AdNetworkEnum network;
    private List<Long> adEntryIds;
    private String sourceUrl;
    private String publisherUuid;
    private Date createAt;
    private String cookieId;
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

    public List<Long> getAdEntryIds() {
        return adEntryIds;
    }
    
    public void setAdEntryIds(List<Long> adEntryIds) {
        this.adEntryIds = adEntryIds;
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
