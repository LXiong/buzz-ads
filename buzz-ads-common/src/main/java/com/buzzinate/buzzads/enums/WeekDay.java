package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> 
 * Mar 6, 2013 12:37:59 PM
 * 
 */
public enum WeekDay implements IntegerValuedEnum {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

    private int code;

    WeekDay(int value) {
        this.code = value;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static WeekDay findByValue(int param) {
        WeekDay[] values = values();
        for (WeekDay e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return WeekDay.MONDAY;
    }
    
    public static String getCNView(String name) {
        if (name.equals(WeekDay.MONDAY.name())) {
            return "周一";
        } else if (name.equals(WeekDay.TUESDAY.name())) {
            return "周二";
        } else if (name.equals(WeekDay.WEDNESDAY.name())) {
            return "周三";
        } else if (name.equals(WeekDay.THURSDAY.name())) {
            return "周四";
        } else if (name.equals(WeekDay.FRIDAY.name())) {
            return "周五";
        } else if (name.equals(WeekDay.SATURDAY.name())) {
            return "周六";
        } else if (name.equals(WeekDay.SUNDAY.name())) {
            return "周日";
        } else {
            return null;
        }
    }
}