package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author Johnson
 * 
 */
public enum AdvertiserBusinessType implements IntegerValuedEnum {
    UNKNOWN(0);
    
    private int value;
    
    AdvertiserBusinessType(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return value;
    }

    public static AdvertiserBusinessType findByValue(int value) {
        AdvertiserBusinessType[] values = values();
        for (AdvertiserBusinessType type : values) {
            if (type.getCode() == value) {
                return type;
            }
        }
        return AdvertiserBusinessType.UNKNOWN;
    }

}
