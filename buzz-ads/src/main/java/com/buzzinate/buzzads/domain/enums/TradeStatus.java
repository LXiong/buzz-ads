package com.buzzinate.buzzads.domain.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

import java.io.Serializable;

/**
 * Trade status enum
 *
 * @author martin
 */
public enum TradeStatus implements IntegerValuedEnum, Serializable {

    WAITPAY(0), SUCCESS(2), FAIL(-1);

    private final int code;

    private TradeStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static TradeStatus getTradeStatus(int code) {
        if (WAITPAY.getCode() == code) {
            return WAITPAY;
        } else if (SUCCESS.getCode() == code) {
            return SUCCESS;
        } else if (FAIL.getCode() == code) {
            return FAIL;
        }
        return null;
    }
}
