package com.buzzinate.buzzads.data.converter;

import java.util.EnumSet;
import java.util.Set;

import com.buzzinate.buzzads.data.thrift.TWeekDay;
import com.buzzinate.buzzads.enums.WeekDay;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 6, 2013 3:36:47 PM
 * 
 */
public final class WeekDayConverter {
    private WeekDayConverter() {
    }
    public static Set<TWeekDay> toThrift(Set<WeekDay> scheduleDay) {
        if (scheduleDay == null)
            return null;
        Set<TWeekDay> res = EnumSet.noneOf(TWeekDay.class);
        for (WeekDay day : scheduleDay) {
            res.add(TWeekDay.findByValue(day.getCode()));
        }
        return res;
    }

    public static Set<WeekDay> fromThrift(Set<TWeekDay> scheduleDay) {
        if (scheduleDay == null)
            return null;
        Set<WeekDay> res = EnumSet.noneOf(WeekDay.class);
        for (TWeekDay day : scheduleDay) {
            res.add(WeekDay.findByValue(day.getValue()));
        }
        return res;
    }
}
