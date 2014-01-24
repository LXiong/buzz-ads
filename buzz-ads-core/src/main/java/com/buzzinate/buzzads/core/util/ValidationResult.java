package com.buzzinate.buzzads.core.util;

/**
 * 
 * @author Johnson
 * ApI 验证结果类
 */
public class ValidationResult {
    public static final String ERRCODE = "errcode";
    public static final String ERRMSG = "errmsg";
    public static final String VALID_CODE = "0";
    public static final String INVALID_SIG_CODE = "1";
    public static final String INVALID_PARAMETER_CODE = "2";
    public static final String RESULT_NOTFOUND_CODE = "3";
    private String errcode;
    private String errmsg;

    public ValidationResult() {
        this.errcode = VALID_CODE;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


}
