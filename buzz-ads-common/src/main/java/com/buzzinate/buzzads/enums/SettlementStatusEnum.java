package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 站长结算状态
 * @author zyeming
 *
 */

public enum SettlementStatusEnum implements IntegerValuedEnum {
    // 未付账
    UNPAID(0),
    // 已付账
    PAID(1);
    
    private int value;

    SettlementStatusEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.value;
    }
    
    public static SettlementStatusEnum findByValue(int param) {
        SettlementStatusEnum[] values = values();
        for (SettlementStatusEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return SettlementStatusEnum.UNPAID;
    }
}
