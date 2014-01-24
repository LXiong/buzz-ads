package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author Johnson
 *
 */
public enum PublisherContactRevMethod implements IntegerValuedEnum {
    ALIPAY(0);

    private int value;
    
    PublisherContactRevMethod(int value) {
        this.value = value;
    }

    public int getCode() {
        return value;
    }
    
    public static PublisherContactRevMethod findByValue(int param) {
        PublisherContactRevMethod[] values = values();
        for (PublisherContactRevMethod e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return PublisherContactRevMethod.ALIPAY;
    }

}
