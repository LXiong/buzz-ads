package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-15
 * Time: 下午6:32
 * 媒体类别
 */
public enum ChannelStyleEnum implements IntegerValuedEnum {
    STYLE_120_120(0, "120*120"), UNKNOWN(1, "未知");
    private int code;
    private String text;

    private ChannelStyleEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static ChannelStyleEnum findChannelStyleByCode(int code) {
        for (ChannelStyleEnum item : values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public static Map<ChannelStyleEnum, String> channelStyleSelector() {
        Map<ChannelStyleEnum, String> channelStyleEnumStringMap = new HashMap<ChannelStyleEnum, String>(values().length);
        for (ChannelStyleEnum channelStyleEnum : values()) {
            channelStyleEnumStringMap.put(channelStyleEnum, channelStyleEnum.getText());
        }
        channelStyleEnumStringMap.remove(UNKNOWN);
        return channelStyleEnumStringMap;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
