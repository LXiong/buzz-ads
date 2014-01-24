/**
 * 
 */
package com.buzzinate.buzzads.payment;

import com.buzzinate.common.util.DateTimeUtil;

import java.util.Date;
import java.util.Random;

/**
 * <p>
 * This is an util class for generate payment order number.
 * </p>
 * 
 * @author Jerry.Ma
 */
public final class OrdernoGenerator {

    /**
     * order no's prefix.
     */
    public static final String ORDER_NO_PREFIX = "B#";

    // defined a lock.
    private static final byte[] LOCK = new byte[0];

    private OrdernoGenerator() {
        // ignore.
    }

    /**
     * Generate an order number for the payment.
     * 
     * @return current user's order number.
     */
    public static String generateOrderNo() {
        String orderNo = ORDER_NO_PREFIX;
        synchronized (LOCK) {
            orderNo += DateTimeUtil.formatDate(new Date(), 
                            DateTimeUtil.FMT_DATE_YYYYMMDDHHMMSS) + getRandom();
        }
        return orderNo;
    }

    /**
     * generate a random number, the number's length is 2.
     * 
     * @return the random number.
     */
    public static int getRandom() {
        final Random rad = new Random();
        final int base = 100;
        final int primeNumber = 13;
        int iRadom = rad.nextInt(base);
        if (iRadom < 10) {
            iRadom += primeNumber;
        }
        return iRadom;
    }

}
