package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-2-26
 */
public enum AdEntrySizeEnum implements IntegerValuedEnum {
    SIZE80x80(1, "80*80", 1), SIZE660x90(2, "660*90", 1), SIZE610x100(3, "610*100", 1), SIZE300x250(4, "300*250", 3),
    SIZE300x100(5, "300*100", 1), SIZE200x200(6, "200*200", 1), SIZE468X60(7, "468*60", 1);
    private final int code;
    private final String text;
    //尺寸所属版位类型，1为banner，2为multimedia, 3为banner+multimedia
    private final int type;

    AdEntrySizeEnum(int value, String text, int type) {
        this.code = value;
        this.text = text;
        this.type = type;
    }

    public static AdEntrySizeEnum findByValue(int param) {
        AdEntrySizeEnum[] values = values();
        for (AdEntrySizeEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return null;
    }

    public static AdEntrySizeEnum findByText(String width, String height) {
        String key = width + "*" + height;
        for (AdEntrySizeEnum e : values()) {
            if (StringUtils.equalsIgnoreCase(key, e.getText())) {
                return e;
            }
        }
        return AdEntrySizeEnum.SIZE80x80;
    }

    /**
     * Banner类型的版位尺寸筛选器
     *
     * @return
     */
    public static Map<AdEntrySizeEnum, String> bannerSizeSelector() {
        Map<AdEntrySizeEnum, String> adEntrySizeEnumStringMap = new HashMap<AdEntrySizeEnum, String>(values().length);
        for (AdEntrySizeEnum value : values()) {
            if ((value.getType() & 0x01) == 1)
                adEntrySizeEnumStringMap.put(value, value.getText());
        }
        return adEntrySizeEnumStringMap;
    }

    /**
     * Multimedia类型的版位尺寸筛选器
     *
     * @return
     */
    public static Map<AdEntrySizeEnum, String> mmSizeSelector() {
        Map<AdEntrySizeEnum, String> adEntrySizeEnumStringMap = new HashMap<AdEntrySizeEnum, String>(values().length);
        for (AdEntrySizeEnum value : values()) {
            if ((value.getType() & 0x02) == 2)
                adEntrySizeEnumStringMap.put(value, value.getText());
        }
        return adEntrySizeEnumStringMap;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }
}
