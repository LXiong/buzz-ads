package com.buzzinate.buzzads.core.util;

import java.text.DecimalFormat;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 * 
 */
public final class FormatUtil {
    
    private static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    
    private FormatUtil() { }

    public static String getDouble(Long amount) {
        return doubleformat.format((double) amount / 100);
    }
    
    public static String getDouble(Integer amount) {
        return doubleformat.format((double) amount / 100);
    }
    
    public static double getDoubleNoStr(Integer amount) {
        return (double) amount / 100;
    }
}
