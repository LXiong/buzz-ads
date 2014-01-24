package com.buzzinate.buzzads.domain.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 支付状态:0-未支付，1-已支付，2-未知，99-无支付状态状态。
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-23
 */
public enum PaidStatusEnum implements IntegerValuedEnum {

    NO_PAID(0), OK_PAID(1), UNKNOWN_PAID(2), NO_PAID_INFO(99);

    private int value;

    PaidStatusEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.value;
    }

    public static PaidStatusEnum findByValue(int param) {
        PaidStatusEnum[] values = values();
        for (PaidStatusEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return PaidStatusEnum.NO_PAID;
    }
}
