package com.buzzinate.buzzads.util;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.DateTimeUtil;



/**
 * 
 * @author zyeming
 *
 */
public final class DateRangeUtil {

    private DateRangeUtil() { }
    
    /**
     * Gets the start and end dates in an array of Date objects: startDate, endDate.
     * This is used for statistics charting.
     * NOTE: This function adds a day to the endDate in order for statistics charting to include the last date.
     * For example: 10-01-2010 to 10-09-2010 will return 10-01-2010 to 10-10-2010.
     * 
     * use (dateStartDate.compareTo(dateEndDate) >= 0) to check if the returned dates are valid...
     * 
     * @param dateStart
     * @param dateEnd
     * @param isAdmin
     *         If the user is an admin user
     * @param allowSameDay
     *         if true, it will not add a day to the dateEnd
     * @return
     */
    public static Date[] getStartAndEndDates(Date dateStart, Date dateEnd, boolean isAdmin) {
        
        int defaultDays = ConfigurationReader.getInt("stats.default.view.days");
        int maxDays = 0;
        if (isAdmin) {
            maxDays = ConfigurationReader.getInt("stats.max.admin.view.days");
        } else {
            maxDays = ConfigurationReader.getInt("stats.max.view.days");
        }
        
        Date dateEndDate;
        Date dateStartDate;
        if (isValidDateRange(dateStart, dateEnd)) {
            dateEndDate = dateEnd;
            // make sure it's in valid range
            dateStartDate = DateTimeUtil.subtractDays(dateEndDate, maxDays);
            if (dateStart.compareTo(dateStartDate) > 0) {
                dateStartDate = dateStart;
            }
        } else {
            // if invalid, using default date range
            dateEndDate = DateTimeUtil.getCurrentDateDay();
            dateStartDate = DateTimeUtil.subtractDays(dateEndDate, defaultDays);
        }
        
        // add to the end of the day so it'll include the last day
        dateEndDate = DateUtils.addSeconds(dateEndDate, 3600 * 23 + 3599);
        return new Date[] {dateStartDate, dateEndDate};
    }
    
    
    private static boolean isValidDateRange(Date start, Date end) {
        return start != null && end != null && 
                        end.compareTo(start) >= 0 && 
                        end.compareTo(DateTimeUtil.getCurrentDateDay()) <= 0;
    }
    
    
    public static boolean isValidOrderDate(Date start, Date end, Date campStart, Date campEnd) {
        assert start != null;
        assert campStart != null;
        
        Date current = DateTimeUtil.getCurrentDateDay();
        // Order的结束日期应该在开始日期以及当前日期之后
        if (end != null && (start.after(end) || current.after(end))) {
            return false;
        }
        
        // Order的日期应该在Campaign的日期范围内
        if (campStart.after(start)) {
            return false;
        }
        if (campEnd == null) {
            return true;
        } 
        if (end == null) {
            return false;
        } 
        
        return !campEnd.before(end);
    }
    
}