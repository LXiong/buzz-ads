package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author Johnson
 *
 */
public enum AdvertiserStatusEnum implements IntegerValuedEnum {
    NORMAL(0), FROZEN(1);
    
    private int value;
    
    AdvertiserStatusEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return value;
    }
    
    public static AdvertiserStatusEnum findByValue(int value) {
        AdvertiserStatusEnum[] allStatus = values();
        for (AdvertiserStatusEnum status: allStatus) {
            if (status.getCode() == value) {
                return status;
            }
        }
        return AdvertiserStatusEnum.NORMAL;
    }

}
