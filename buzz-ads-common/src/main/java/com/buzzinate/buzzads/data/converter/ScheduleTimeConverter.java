package com.buzzinate.buzzads.data.converter;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalTime;

import com.buzzinate.buzzads.data.thrift.TScheduleTime;
import com.buzzinate.buzzads.domain.ScheduleTime;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Feb 26, 2013 3:27:10 PM
 * 
 */
public final class ScheduleTimeConverter {
    private ScheduleTimeConverter() {
    }

    /**
     * Convert from thrift object to ScheduleTime domain object
     * 
     * @param tItem
     * @return
     */
    public static Set<ScheduleTime> fromThrift(Set<TScheduleTime> tScheduleTime) {
        if (tScheduleTime == null)
            return null;
        Set<ScheduleTime> res = new HashSet<ScheduleTime>();
        for (TScheduleTime st : tScheduleTime) {
            ScheduleTime item = new ScheduleTime();
            item.setStart(LocalTime.fromMillisOfDay(st.getStart()));
            item.setEnd(LocalTime.fromMillisOfDay(st.getEnd()));
            res.add(item);
        }
        return res;
    }
    /**
     * Convert ScheduleTime domain object to thrift object
     * 
     * @param tItem
     * @return
     */
    public static Set<TScheduleTime> toThrift(Set<ScheduleTime> scheduleTime) {
        if (scheduleTime == null)
            return null;
        Set<TScheduleTime> res = new HashSet<TScheduleTime>();
        for (ScheduleTime st : scheduleTime) {
            TScheduleTime item = new TScheduleTime.Builder().start(LocalTime.parse(st.getStartStr()).getMillisOfDay())
                    .end(LocalTime.parse(st.getEndStr()).getMillisOfDay()).build();
            res.add(item);
        }
        return res;
    }
}
