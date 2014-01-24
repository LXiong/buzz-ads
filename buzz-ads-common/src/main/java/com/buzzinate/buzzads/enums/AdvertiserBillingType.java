package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 11:39:10 AM
 */
public enum AdvertiserBillingType implements IntegerValuedEnum {
    DEBIT_DAY(0), REFILL_RECHARGE(1), ADJUSTMENT(2);

    private int value;

    AdvertiserBillingType(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return value;
    }

    public static AdvertiserBillingType findByValue(int value) {
        AdvertiserBillingType[] values = values();
        for (AdvertiserBillingType type : values) {
            if (type.getCode() == value) {
                return type;
            }
        }
        return AdvertiserBillingType.DEBIT_DAY;
    }
}
