package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

import java.util.HashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-27
 * Time: 下午3:14
 * 支持的银行的枚举
 */
public enum BankEnum implements IntegerValuedEnum {
    ICBC(0, "工商银行"), CCB(1, "建设银行"), ABC(2, "农业银行"), BCM(3, "交通银行"),
    BOC(4, "中国银行"), CMB(5, "招商银行"), SPDB(6, "浦发银行"), CEB(7, "光大银行"),
    ECITIC(8, "中信银行"), CIB(9, "兴业银行"), HXB(10, "华夏银行"), CMBC(11, "民生银行"),
    SDB(12, "深圳发展银行"), UCB(13, "城市商业银行"), GDB(14, "广东发展银行"), PSBC(15, "邮政储蓄银行"),
    RCB(16, "农村商业银行"), CSXYS(17, "城市信用社"), NCXYS(18, "农村信用社"), NCHZYH(19, "农村合作银行"),
    UNKNOWN(99, "未知");
    private final int code;
    private final String text;

    private BankEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static BankEnum getBankEnumByCode(int code) {
        if (code < 0) {
            return UNKNOWN;
        }
        for (BankEnum bankEnum : values()) {
            if (bankEnum.code == code) {
                return bankEnum;
            }
        }
        return UNKNOWN;
    }

    public static Map<BankEnum, String> bankSelector() {
        Map<BankEnum, String> bankEnumStringMap = new HashMap<BankEnum, String>();
        for (BankEnum bankEnum : values()) {
            bankEnumStringMap.put(bankEnum, bankEnum.getText());
        }
        return bankEnumStringMap;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
