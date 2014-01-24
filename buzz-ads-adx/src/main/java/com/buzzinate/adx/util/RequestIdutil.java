package com.buzzinate.adx.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * generate RequestId
 * @author james.chen
 * date 2013-7-27
 */
public final class RequestIdutil implements Serializable {

    private static final long serialVersionUID = -1013482526202967913L;
    private static String MACHINE_ID = "1";
    private static AtomicLong SEQUENCE_VALUE = new AtomicLong(0);
    private static long SEQUENCE_MAX = 10000;

    public static long gen() {
        long value = SEQUENCE_VALUE.getAndAdd(1);
        if (value >= SEQUENCE_MAX) {
            SEQUENCE_VALUE.set(1);
        }
        return Long.valueOf(MACHINE_ID + System.currentTimeMillis() + value);
    }
}
