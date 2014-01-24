package com.buzzinate.buzzads.domain.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * Operate Type
 *
 * @author james.chen
 * @since 2012-6-25
 */
public enum OperateType implements IntegerValuedEnum {
    ADD(1), MODIFY(2), DELETE(3), VIEW(4);

    private final int code;

    private OperateType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
