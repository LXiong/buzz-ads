package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.List;

import org.safehaus.uuid.UUID;

/**
 * 
 * @author Johnson
 *
 */
public class PublisherSiteConfig implements Serializable {

    private static final long serialVersionUID = -5711538540792598789L;
    
    private String uuid;
    private String blackKeywords = "";
    private List<PublisherBlackDomain> blackDomains;
    
    public PublisherSiteConfig() {}
    
    public PublisherSiteConfig(String uuid) {
        this.uuid = uuid;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    /**
     * Used by Hibernate to get UUID from database.
     * @return
     */
    public byte[] getUuidBytes() {
        if (this.uuid != null) {
            UUID uuidx = new UUID(this.uuid);
            return uuidx.asByteArray();
        } else {
            return null;
        }
    }
    
    /**
     * Used by Hibernate to set UUID in database.
     * @param uuid
     */
    public void setUuidBytes(byte[] uuid) {
        if (uuid != null) {
            UUID uuidx = new UUID(uuid);
            this.uuid = uuidx.toString();
        } else {
            this.uuid = null;
        }
    }
    
    public String getBlackKeywords() {
        return blackKeywords;
    }
    
    public void setBlackKeywords(String blackKeywords) {
        this.blackKeywords = blackKeywords;
    }
    
    public List<PublisherBlackDomain> getBlackDomains() {
        return blackDomains;
    }
    
    public void setBlackDomains(List<PublisherBlackDomain> blackDomains) {
        this.blackDomains = blackDomains;
    }
    
}
