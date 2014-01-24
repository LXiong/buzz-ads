package com.buzzinate.buzzads.domain;

import java.io.Serializable;

import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> 
 * Feb 26, 2013 2:47:07 PM
 * 
 */
public class ScheduleTime implements Serializable {
    private static final long serialVersionUID = -72333025157333408L;

    private LocalTime start;
    private LocalTime end;
    
    public ScheduleTime() {
    }
    
    public ScheduleTime(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }
   
    public void setEnd(LocalTime end) {
        this.end = end;
    }
    
    public String getTimeStr() {
        if (start == null || end == null)
            return null;
        
        StringBuilder sb = new StringBuilder(start.toString(ISODateTimeFormat.hourMinute()));
        sb.append("-").append(end.toString(ISODateTimeFormat.hourMinute()));
        return sb.toString();
    }
    
    public String getStartStr() {
        if (start != null)
            return start.toString(ISODateTimeFormat.hourMinute());
        return null;
    }
    
    public String getEndStr() {
        if (end != null)
            return end.toString(ISODateTimeFormat.hourMinute());
        return null;
    }
}
