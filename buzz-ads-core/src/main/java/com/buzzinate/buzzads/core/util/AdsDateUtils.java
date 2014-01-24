package com.buzzinate.buzzads.core.util;

import java.util.Calendar;
import java.util.Date;

import com.buzzinate.common.util.DateTimeUtil;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-1-15
 */
public final class AdsDateUtils {
    
    private AdsDateUtils() {
        
    }

    @SuppressWarnings("unused")
    public static String lastMonFirstDay() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String months = "";
        String days = "";
        if (month > 1) {
            month--;
        } else {
            year--;
            month = 12;
        }
        if (!(String.valueOf(month).length() > 1)) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }
        if (!(String.valueOf(day).length() > 1)) {
            days = "0" + day;
        } else {
            days = String.valueOf(day);
        }
        String firstDay = "" + year + "-" + months + "-01";
        String[] lastMonth = new String[2];
        lastMonth[0] = firstDay;
        return firstDay;
    }

    public static String lastMonLastDay() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String months = "";
        String days = "";
        if (month > 1) {
            month--;
        } else {
            year--;
            month = 12;
        }
        if (!(String.valueOf(month).length() > 1)) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }
        if (!(String.valueOf(day).length() > 1)) {
            days = "0" + day;
        } else {
            days = String.valueOf(day);
        }
        String lastDay = "" + year + "-" + months + "-" + days;
        String[] lastMonth = new String[2];
        lastMonth[1] = lastDay;
        return lastDay;
    }
    
    public static Date lastWeekFirstDay() {
        Calendar calendar = Calendar.getInstance();          
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;        
        int offset = 1 - dayOfWeek;        
        calendar.add(Calendar.DATE, offset - 7);              
        return calendar.getTime();
    }
    
    public static Date lastWeekLastDay() {
        Calendar calendar = Calendar.getInstance();        
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;        
        int offset = 7 - dayOfWeek;        
        calendar.add(Calendar.DATE, offset - 7);
        return calendar.getTime();
    }
    
    public static int getCurrentQuarter() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        if (month == 1 || month == 2 || month == 3) {
            return 1;
        } else if (month == 4 || month == 5 || month == 6) {
            return 2;
        } else if (month == 7 || month == 8 || month == 9) {
            return 3;
        } else {
            return 4;
        } 
    }
    
    public static Date getFirstDayOfLastQuater() {
        int current = getCurrentQuarter();
        Calendar now = Calendar.getInstance();
        if (current == 2) {
            now.set(now.get(Calendar.YEAR), 0, 1);
        } else if (current == 3) {
            now.set(now.get(Calendar.YEAR), 3, 1);
        } else if (current == 4) {
            now.set(now.get(Calendar.YEAR), 6, 1);
        } else {
            now.set(now.get(Calendar.YEAR) - 1, 9, 1);
        }
        return DateTimeUtil.getDateDay(now.getTime());
    }
    
    public static Date getLastDayOfLastQuater() {
        int current = getCurrentQuarter();
        Calendar now = Calendar.getInstance();
        if (current == 2) {
            now.set(now.get(Calendar.YEAR), 2, 31);
        } else if (current == 3) {
            now.set(now.get(Calendar.YEAR), 5, 30);
        } else if (current == 4) {
            now.set(now.get(Calendar.YEAR), 8, 30);
        } else {
            now.set(now.get(Calendar.YEAR) - 1 , 11, 31);
        }
        return DateTimeUtil.getDateDay(now.getTime());
    }
}
