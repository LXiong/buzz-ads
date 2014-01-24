package com.buzzinate.adx.enums;

/**
 * @author james.chen
 * date 2013-7-31
 */
public enum DisplayStatus {

    DISPLAY(1), UNKNOW(-1);

    private int code;

    private DisplayStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

