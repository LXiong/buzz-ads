package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-27
 * Time: 下午3:06
 * 财务类型的枚举
 */
public enum FinanceTypeEnum implements IntegerValuedEnum {
    PERSON(0, "个人"), COMPANY(1, "公司"), UNKNOWN(9, "未知");
    private final int code;
    private final String text;

    private FinanceTypeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static FinanceTypeEnum getFinanceTypeByCode(int code) {
        if (code < 0) {
            return UNKNOWN;
        }
        for (FinanceTypeEnum financeTypeEnum : values()) {
            if (financeTypeEnum.code == code) {
                return financeTypeEnum;
            }
        }
        return UNKNOWN;
    }

    public static Map<FinanceTypeEnum, String> financeTypeSelector() {
        Map<FinanceTypeEnum, String> financeTypeSelector = new HashMap<FinanceTypeEnum, String>();
        for (FinanceTypeEnum financeTypeEnum : values()) {
            financeTypeSelector.put(financeTypeEnum, financeTypeEnum.getText());
        }
        financeTypeSelector.remove(UNKNOWN);
        return financeTypeSelector;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

}
