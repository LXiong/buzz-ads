package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-28
 * Time: 下午12:22
 * 合作伙伴
 */
public enum PartnerEnum implements IntegerValuedEnum {
    UQ(1), UNKNOWN(99);
    private final int code;


    private PartnerEnum(int code) {
        this.code = code;
    }

    public static PartnerEnum getPartnerEnumByCode(int code) {
        for (PartnerEnum partner : values()) {
            if (partner.getCode() == code) {
                return partner;
            }
        }
        return UNKNOWN;
    }

    @Override
    public int getCode() {
        return code;
    }
}
