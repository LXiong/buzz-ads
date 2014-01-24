package com.buzzinate.buzzads.enums;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 广告创意类型:文字链、图片、js、图文
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-23
 */
public enum AdEntryTypeEnum implements IntegerValuedEnum {
    TEXT(0), 
    IMAGE(1), 
    Flash(2), 
    //PICTEXT(3), 
    UNKNOWN(99);

    private int value;

    AdEntryTypeEnum(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return this.value;
    }
    
    public static AdEntryTypeEnum findByValue(int param) {
        AdEntryTypeEnum[] values = values();
        for (AdEntryTypeEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return AdEntryTypeEnum.UNKNOWN;
    }
    
    /**
     * Gets a Map for a selector html element.
     * @return
     */
    public static List<String> getSelector() {
        List<String> typeSelector = new LinkedList<String>();
        for (AdEntryTypeEnum type : AdEntryTypeEnum.values()) {
            typeSelector.add(type.name());
        }
        return typeSelector;
    }

    public static Map<AdEntryTypeEnum, String> adEntryTypeSelector() {
        Map<AdEntryTypeEnum, String> adEntryTypeEnumStringMap = new HashMap<AdEntryTypeEnum, String>(values().length);
        for (AdEntryTypeEnum adEntryTypeEnum : values()) {
            adEntryTypeEnumStringMap.put(adEntryTypeEnum, adEntryTypeEnum.name());
        }
        return adEntryTypeEnumStringMap;
    }
}
