package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author zyeming
 *
 */
public enum PublisherContactStausEnum implements IntegerValuedEnum {
    NORMAL(0), FROZEN(1);

    private int value;

    PublisherContactStausEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return value;
    }
    
    public static PublisherContactStausEnum findByValue(int param) {
        PublisherContactStausEnum[] values = values();
        for (PublisherContactStausEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return PublisherContactStausEnum.NORMAL;
    }

}
