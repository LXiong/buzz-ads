package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 事件通知类型:活动、订单、广告
 * 
 * @author Martin
 * 
 */
public enum IdType implements IntegerValuedEnum {
    CAMPAIGN(0), 
    ORDER(1), 
    ENTRY(2);

    private int value;

    IdType(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return this.value;
    }
    
    public static IdType findByValue(int param) {
        IdType[] values = values();
        for (IdType e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return IdType.ENTRY;
    }
    
}
