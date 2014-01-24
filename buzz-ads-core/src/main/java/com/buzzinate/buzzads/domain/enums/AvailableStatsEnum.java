package com.buzzinate.buzzads.domain.enums;

import java.util.HashMap;
import java.util.Map;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author Johnson
 *
 */
public enum AvailableStatsEnum implements IntegerValuedEnum {
    VIEWS(1), CLICKS(2), CPCCLICKS(3), CPMVIEWS(4), CTR(5), COST(6);
    
    private int value;
    
    AvailableStatsEnum(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return this.value;
    }
    
    public static AvailableStatsEnum findByValue(int param) {
        AvailableStatsEnum[] values = values();
        for (AvailableStatsEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return null;
    }
    
    public static Map<Integer, String> charOverViewType() {
        return getTypeMap(new AvailableStatsEnum[]{VIEWS, CLICKS, COST});
    }
    
    public static Map<Integer, String> charCpcType() {
        return getTypeMap(new AvailableStatsEnum[]{VIEWS, CLICKS, CPCCLICKS, CTR, COST});
    }
    
    public static Map<Integer, String> charCpmType() {
        return getTypeMap(new AvailableStatsEnum[]{VIEWS, CLICKS, CPMVIEWS, COST});
    }
    
    public static Map<Integer, String> adminCharViewType() {
        Map<Integer, String> viewMap = new HashMap<Integer, String>();
        AvailableStatsEnum[] values = values();
        for (AvailableStatsEnum e : values) {
            viewMap.put(Integer.valueOf(e.getCode()), getAdminCnName(e));
        }
        viewMap.remove(CPCCLICKS.getCode());
        viewMap.remove(CPMVIEWS.getCode());
        return viewMap;
    }
    
    public static String getAdminCnName(AvailableStatsEnum name) {
        switch (name) {
        case VIEWS:
            return "展现量";
        case CLICKS:
            return "点击量";
        case CTR:
            return "点击率";
        case CPCCLICKS:
            return "有效点击量";
        case CPMVIEWS:
            return "有效展示量";
        default:
            return "收入";
        }
    }
    
    public static String getCnName(AvailableStatsEnum name) {
        switch (name) {
        case VIEWS:
            return "原始展示量";
        case CLICKS:
            return "原始点击量";
        case CTR:
            return "有效点击率";
        case CPCCLICKS:
            return "有效点击量";
        case CPMVIEWS:
            return "有效展示量";
        default:
            return "支出";
        }
    }
    
    private static Map<Integer, String> getTypeMap(AvailableStatsEnum[] values) {
        Map<Integer, String> viewMap = new HashMap<Integer, String>();
        for (AvailableStatsEnum e : values) {
            viewMap.put(Integer.valueOf(e.getCode()), getCnName(e));
        }
        return viewMap;
    }

}
