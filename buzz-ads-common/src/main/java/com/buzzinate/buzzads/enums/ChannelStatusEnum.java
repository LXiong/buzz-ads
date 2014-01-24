package com.buzzinate.buzzads.enums;

import java.util.HashMap;
import java.util.Map;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 媒体状态:1为开启，2为关闭3为冻结，4为删除
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 * 
 *         2013-5-13
 */
public enum ChannelStatusEnum implements IntegerValuedEnum {
    OPENED(0), 
    CLOSED(1), 
    FROZENED(2),
    DELETED(3), 
    UNKNOWN(99);

    private int value;

    ChannelStatusEnum(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return this.value;
    }
    
    public static ChannelStatusEnum findByValue(int param) {
        ChannelStatusEnum[] values = values();
        for (ChannelStatusEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return ChannelStatusEnum.UNKNOWN;
    }
    
    public static ChannelStatusEnum findByKey(String param) {
        ChannelStatusEnum[] values = values();
        for (ChannelStatusEnum e : values) {
            if (e.name().equals(param)) {
                return e;
            }
        }
        return ChannelStatusEnum.UNKNOWN;
    }
    
    /**
     * Gets a Map for a selector html element.
     * @return
     */
    public static Map<Integer, String> getSelector() {
        Map<Integer, String> typeSelector = new HashMap<Integer, String>();
        for (ChannelStatusEnum type : ChannelStatusEnum.values()) {
            typeSelector.put(Integer.valueOf(type.getCode()), type.name());
        }
        // 由于删除改为物理删除，所以下拉列表删除按钮需要移除
        typeSelector.remove(DELETED.getCode());
        return typeSelector;
    }

    public static Map<ChannelStatusEnum, String> statusSelector() {
        Map<ChannelStatusEnum, String> statusSelector = new HashMap<ChannelStatusEnum, String>(values().length);
        for (ChannelStatusEnum status : values()) {
            statusSelector.put(status, status.name());
        }
        statusSelector.remove(DELETED);
        return statusSelector;
    }
}

