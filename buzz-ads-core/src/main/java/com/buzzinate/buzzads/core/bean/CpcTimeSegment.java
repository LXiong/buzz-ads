package com.buzzinate.buzzads.core.bean;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * Cpc time segment. Each segment have 4 hours, numbered as 1 to 6.
 * @author zyeming
 *
 */
public final class CpcTimeSegment {
    
    private static final int SEGMENT_MINUTES = ConfigurationReader.getInt("cpc.segment.minutes", 240);
    private static final int SEGMENT_PER_DAY = 24 * 60 / SEGMENT_MINUTES;
    
    
    private final Date day;
    private final int segment;
    
    public CpcTimeSegment(Date day, int segment) {
        this.day = day;
        this.segment = segment;
    }

    public Date getDay() {
        return day;
    }

    public int getSegment() {
        return segment;
    }
    
    public String toCacheKey() {
        return DateTimeUtil.formatDate(day) + ":" + segment;
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + segment;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CpcTimeSegment other = (CpcTimeSegment) obj;
        if (day == null) {
            if (other.day != null)
                return false;
        } else if (!day.equals(other.day))
            return false;
        if (segment != other.segment)
            return false;
        return true;
    }

    
    /**
     * Return the next segment of this one
     * @return
     */
    public static CpcTimeSegment getNextSegment(CpcTimeSegment ts) {
        Date day = ts.getDay();
        int segment = ts.getSegment() + 1;
        if (segment > SEGMENT_PER_DAY) {
            segment = 1;
            day = DateTimeUtil.plusDays(day, 1);
        }
        return new CpcTimeSegment(day, segment);
    }
    
    /**
     * Return the previous cpc time segment
     * @return
     */
    public static CpcTimeSegment getPreviousSegment() {
        // 回到上个时间段
        DateTime previous = new DateTime().minusMinutes(SEGMENT_MINUTES);
        LocalDate day = new LocalDate(previous);
        int minute = previous.getMinuteOfDay();
        int segment = minute / SEGMENT_MINUTES + 1;
        return new CpcTimeSegment(day.toDate(), segment);
    }
    
    
    /**
     * Return the current cpc time segment
     * @return
     */
    public static CpcTimeSegment getCurrentSegment() {
        DateTime current = new DateTime();
        LocalDate day = new LocalDate(current);
        int minute = current.getMinuteOfDay();
        int segment = minute / SEGMENT_MINUTES + 1;
        return new CpcTimeSegment(day.toDate(), segment);
    }
    
    /**
     * Return the corresponding segment of date
     * @param date
     * @return
     */
    public static CpcTimeSegment getSegment(Date date) {
        DateTime dt = new DateTime(date);
        LocalDate day = new LocalDate(dt);
        int minute = dt.getMinuteOfDay();
        int segment = minute / SEGMENT_MINUTES + 1;
        return new CpcTimeSegment(day.toDate(), segment);
    }
}
