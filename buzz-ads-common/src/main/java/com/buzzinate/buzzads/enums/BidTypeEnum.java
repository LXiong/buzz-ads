package com.buzzinate.buzzads.enums;


import java.util.LinkedHashMap;
import java.util.Map;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 订单计费类型
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-22
 */
public enum BidTypeEnum implements IntegerValuedEnum {

    CPM(0), CPA(1), CPS(2), CPC(3), CPT(4), CPD(5);
    private int value;

    BidTypeEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.value;
    }
    
    public static BidTypeEnum findByValue(int param) {
        BidTypeEnum[] values = values();
        for (BidTypeEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return BidTypeEnum.CPC;
    }

    public static Map<BidTypeEnum, String> bidTypeSelector() {
        Map<BidTypeEnum, String> bidTypeEnumStringMap = new LinkedHashMap<BidTypeEnum, String>(values().length);
        bidTypeEnumStringMap.put(null, "全部");
        for (BidTypeEnum item : values()) {
            bidTypeEnumStringMap.put(item, item.name());
        }
        return bidTypeEnumStringMap;

    }

}
