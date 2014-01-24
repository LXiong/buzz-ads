package com.buzzinate.adx.enums;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-7-10
 * Time: 上午9:31
 * 竞价结果枚举
 */
public enum BidStatus {
    SUCCESS(1), TIMEOUT(0), FAILURE(-1), SEGEMNT_FAIL(2), NO_BID(3);

    private int code;

    private BidStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
