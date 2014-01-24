package com.buzzinate.buzzads.payment;


/**
 * @author Jerry.Ma
 * 
 */
public final class PayConfig {


    /**
     * 其他形式支付或赠送
     */
    public static final int PAYGATE_BSHARE_INNER = -1;

    public static final int PAYGATE_NONE = 0;
    /**
     * 支付宝
     */
    public static final int PAYGATE_ALIPAY = 1;

    /**
     * 财付通
     */
    public static final int PAYGATE_TENPAY = 2;

    // ***************************************** payment status.

    // used to display all payment options.
    public static final int PAYMENT_ALL = -100;
    /**
     * payment failure.
     */
    public static final int PAYMENT_FAIL = -1;

    /**
     * wait the user payment.
     */
    public static final int PAYMENT_WAIT = 0;

    /**
     * payment successful.
     */
    public static final int PAYMENT_SUCCEED = 1;

    
    private PayConfig() {
        // no constructor
    }

}