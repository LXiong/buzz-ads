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
public enum AdNetworkEnum implements IntegerValuedEnum {
    LEZHI(0, "乐知", "lezhi"), BSHARE(1, "bShare", "bshare"), BUZZADS(2, "buzzads", "buzzads"), WJF(3, "微积分", "wjf"), MULTIMEDIA(4, "富媒体", "richmedia");
    private int code;
    private String text;
    private String abbr;

    AdNetworkEnum(int value, String text, String abbr) {
        this.code = value;
        this.text = text;
        this.abbr = abbr;
    }

    public static AdNetworkEnum findByValue(int param) {
        AdNetworkEnum[] values = values();
        for (AdNetworkEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return AdNetworkEnum.LEZHI;
    }

    public static Map<AdNetworkEnum, String> networkSelector() {
        Map<AdNetworkEnum, String> networkSelector = new HashMap<AdNetworkEnum, String>(values().length);
        networkSelector.put(LEZHI, LEZHI.getText());
        return networkSelector;
    }

    public static AdNetworkEnum findByAbbr(String param) {
        AdNetworkEnum[] values = values();
        for (AdNetworkEnum e : values) {
            if (StringUtils.equals(e.getAbbr(), param)) {
                return e;
            }
        }
        return AdNetworkEnum.BUZZADS;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getAbbr() {
        return abbr;
    }


}
