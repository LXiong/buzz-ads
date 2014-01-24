package com.buzzinate.buzzads.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-2-26
 */
public enum AdStatusEnum implements IntegerValuedEnum {

    READY(0), ENABLED(1), PAUSED(2), DISABLED(3), SUSPENDED(4), DELETED(5), VERIFYING(6), REJECTED(7);

    private int code;

    AdStatusEnum(int value) {
        this.code = value;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static AdStatusEnum findByValue(int param) {
        AdStatusEnum[] values = values();
        for (AdStatusEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return AdStatusEnum.ENABLED;
    }
    
    /**
     * This puts an "ALL" string in the returned Map in order to display
     * this on the UI in a select box.
     * 
     * @return
     */
    public static Map<AdStatusEnum, String> getSelector() {
        Map<AdStatusEnum, String> list = new LinkedHashMap<AdStatusEnum, String>();
        list.put(null, "ALL");
        for (AdStatusEnum o : AdStatusEnum.values()) {
            list.put(o, o.name());
        }
        return list;
    }

    public static Map<AdStatusEnum, String> getSelectorForAdEntryManage() {
        Map<AdStatusEnum, String> list = getSelector();
        list.remove(READY);
        list.remove(SUSPENDED);
        return list;
    }

    public static Map<AdStatusEnum, String> getSelectorForAdOrderManage() {
        Map<AdStatusEnum, String> list = getSelector();
        list.remove(READY);
        list.remove(VERIFYING);
        list.remove(REJECTED);
        list.remove(SUSPENDED);
        return list;
    }

    public static Map<AdStatusEnum, String> getSelectorForCampManage() {
        Map<AdStatusEnum, String> list = getSelector();
        list.remove(READY);
        list.remove(VERIFYING);
        list.remove(REJECTED);
        return list;
    }
    
    public static String getCnName(AdStatusEnum status) {
        switch (status) {
        case READY:
            return "就绪";
        case ENABLED:
            return "有效";
        case PAUSED:
            return "暂停";
        case DISABLED:
            return "禁用";
        case SUSPENDED:
            return "挂起";
        case DELETED:
            return "删除";
        case VERIFYING:
            return "审核中";
        default:
            return "拒绝";
        }
    }
}
