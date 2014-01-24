package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 交易认证状态。0-未认证，1-已认证，2-否认证
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-23
 */
public enum TradeConfirmEnum implements IntegerValuedEnum {

    NO_CONFIRM(0), CONFIRM_OK(1), CONFIRM_NOT(2);

    private int value;

    TradeConfirmEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.value;
    }
    
    public static TradeConfirmEnum findByValue(int param) {
        TradeConfirmEnum[] values = values();
        for (TradeConfirmEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return TradeConfirmEnum.NO_CONFIRM;
    }
}
