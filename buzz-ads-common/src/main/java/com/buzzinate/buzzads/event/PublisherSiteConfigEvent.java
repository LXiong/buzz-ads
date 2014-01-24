package com.buzzinate.buzzads.event;

import java.io.Serializable;

import com.buzzinate.buzzads.enums.PublisherSiteConfigType;

/**
 * 
 * @author Johnson
 * 站长广告过滤配置事件
 *
 */
public class PublisherSiteConfigEvent implements Serializable {

    
    private static final long serialVersionUID = -9022332431257617618L;
    
    private String uuid;
    private PublisherSiteConfigType type;
    
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public PublisherSiteConfigType getType() {
        return type;
    }
    public void setType(PublisherSiteConfigType type) {
        this.type = type;
    }
    
    

}
