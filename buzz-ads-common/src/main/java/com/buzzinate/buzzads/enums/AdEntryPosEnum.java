package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-6-13
 * Time: 下午4:38
 * 广告投放位置的枚举值
 */
public enum AdEntryPosEnum implements IntegerValuedEnum {
    BOTTOM_LEFT(1, "左下浮窗"),BOTTOM_RIGHT(2, "右下浮窗"), UNKNOWN(9, "未知");
    private final int code;
    private String text;

    private AdEntryPosEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static Map<AdEntryPosEnum, String> getSelector() {
        Map<AdEntryPosEnum, String> adEntryPosEnumStringMap = new HashMap<AdEntryPosEnum, String>(values().length);
        for (AdEntryPosEnum value : values()) {
            if (value != UNKNOWN) {
                adEntryPosEnumStringMap.put(value, value.getText());
            }
        }
        return adEntryPosEnumStringMap;
    }

    /**
     * find enum by code.
     * @param code
     * @return
     */
    public static AdEntryPosEnum findAdEntryPosEnumByCode(int code) {
        for (AdEntryPosEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getCode() {
        return code;
    }
}
