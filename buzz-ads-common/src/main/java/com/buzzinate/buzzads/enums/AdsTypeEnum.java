package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author JOhnson
 * 广告投放方式
 */
public enum AdsTypeEnum implements IntegerValuedEnum {
    //均与投放
    AVERAGE(0),
    //加速投放
    ACCELERATE(1);
    
    private int code;
    
    AdsTypeEnum(int value) {
        this.code = value;
    }
    
    @Override
    public int getCode() {
        return code;
    }
    
    public static AdsTypeEnum findByValue(int param) {
        AdsTypeEnum[] values = values();
        for (AdsTypeEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return AdsTypeEnum.AVERAGE;
    }
}
