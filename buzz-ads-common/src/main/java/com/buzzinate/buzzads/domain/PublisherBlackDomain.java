package com.buzzinate.buzzads.domain;

import java.io.Serializable;

import org.safehaus.uuid.UUID;

import com.buzzinate.buzzads.enums.PublisherSiteConfigType;


public class PublisherBlackDomain implements Serializable {

    private static final long serialVersionUID = 8932873247187739386L;
    
    private int id;
    private String uuid;
    private PublisherSiteConfigType type;
    private String url;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public PublisherSiteConfigType getType() {
        return type;
    }

    public void setType(PublisherSiteConfigType type) {
        this.type = type;
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PublisherBlackDomain other = (PublisherBlackDomain) obj;
        if (!uuid.equals(other.getUuid()) || !url.equals(other.getUrl()))
            return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + uuid.hashCode();
        result = prime * result + url.hashCode();
        return result;
    }

}
