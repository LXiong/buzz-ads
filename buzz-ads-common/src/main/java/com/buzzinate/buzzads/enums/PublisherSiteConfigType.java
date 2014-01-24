package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * Johnson
 * @author Administrator
 *
 */
public enum PublisherSiteConfigType implements IntegerValuedEnum {
    //域名过滤
    DOMAIN(0), 
    //广告实际链接过滤
    ADENTRYLINK(1),
    //关键字过滤
    KEYWORDS(2);

    private int value;
    PublisherSiteConfigType(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return value;
    }
    
    public static PublisherSiteConfigType findByValue(int val) {
        PublisherSiteConfigType[] types = values();
        for (PublisherSiteConfigType type : types) {
            if (type.getCode() == val) {
                return type;
            }
        }
        return DOMAIN;
    }

}
