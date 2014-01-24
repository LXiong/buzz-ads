package com.buzzinate.buzzads.util;

import static com.buzzinate.common.util.DateTimeUtil.convertDate;
import static com.buzzinate.common.util.DateTimeUtil.plusDays;
import static com.buzzinate.common.util.DateTimeUtil.subtractDays;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.buzzinate.common.util.DateTimeUtil;

/**
 * 
 * @author zyeming
 *
 */
public class DateRangeUtilTest {

    @Test
    public void testGetDatesInRange() {
        Date d1 = convertDate("2012-9-18");
        Date d2 = convertDate("2012-10-1");
        Date[] dres = DateRangeUtil.getStartAndEndDates(d1, d2, false);
        Assert.assertEquals(dres[0], d1);
        Assert.assertEquals(dres[1], getEndDay(d2));
    }
    
    @Test
    public void testGetDatesOutRange() {
        Date d1 = DateTimeUtil.convertDate("2012-1-1");
        Date d2 = DateTimeUtil.convertDate("2012-10-1");
        Date[] dres = DateRangeUtil.getStartAndEndDates(d1, d2, false);
        Assert.assertEquals(dres[0], plusDays(d2, -92));
        Assert.assertEquals(dres[1], getEndDay(d2));
    }
    
    @Test
    public void testGetDatesNoEndDate() {
        Date dnow = DateTimeUtil.getCurrentDateDay();
        Date d1 = DateTimeUtil.convertDate("2012-1-1");
        Date[] dres = DateRangeUtil.getStartAndEndDates(d1, null, false);
        Assert.assertEquals(dres[0], DateTimeUtil.subtractDays(dnow, 30));
        Assert.assertEquals(dres[1], getEndDay(dnow));
    }
    
    @Test
    public void testGetDatesNoStartDate() {
        Date dnow = DateTimeUtil.getCurrentDateDay();
        Date d2 = DateTimeUtil.convertDate("2012-10-1");
        Date[] dres = DateRangeUtil.getStartAndEndDates(null, d2, false);
        Assert.assertEquals(dres[0], DateTimeUtil.subtractDays(dnow, 30));
        Assert.assertEquals(dres[1], getEndDay(dnow));
    }
    
    @Test
    public void testGetDatesAfterToday() {
        Date dnow = DateTimeUtil.getCurrentDateDay();
        Date d1 = DateTimeUtil.convertDate("2012-1-1");
        Date d2 = plusDays(dnow, 50);
        Date[] dres = DateRangeUtil.getStartAndEndDates(d1, d2, false);
        Assert.assertEquals(dres[0], subtractDays(dnow, 30));
        Assert.assertEquals(dres[1], getEndDay(dnow));
    }
    
    
    @Test
    public void testIsValidOrderDate() {
        Assert.assertFalse(DateRangeUtil.isValidOrderDate(
                        DateTimeUtil.convertDate("2013-12-2"),
                        DateTimeUtil.convertDate("2013-1-1"),
                        DateTimeUtil.convertDate("2013-1-1"),
                        DateTimeUtil.convertDate("2014-1-1")));
        
        Assert.assertTrue(DateRangeUtil.isValidOrderDate(
                        DateTimeUtil.convertDate("2013-1-2"),
                        DateTimeUtil.convertDate("2013-12-1"),
                        DateTimeUtil.convertDate("2013-1-1"),
                        DateTimeUtil.convertDate("2014-1-1")));
        
        Assert.assertFalse(DateRangeUtil.isValidOrderDate(
                        DateTimeUtil.convertDate("2013-1-2"),
                        null,
                        DateTimeUtil.convertDate("2013-1-1"),
                        DateTimeUtil.convertDate("2013-12-1")));
        
        Assert.assertTrue(DateRangeUtil.isValidOrderDate(
                        DateTimeUtil.convertDate("2013-1-2"),
                        DateTimeUtil.convertDate("2014-1-1"),
                        DateTimeUtil.convertDate("2013-1-1"),
                        null));
        
        Assert.assertFalse(DateRangeUtil.isValidOrderDate(
                        DateTimeUtil.convertDate("2013-1-1"),
                        null,
                        DateTimeUtil.convertDate("2013-1-2"),
                        null));
        

    }
    
    private Date getEndDay(Date day) {
        return DateUtils.addSeconds(day, 3600 * 23 + 3599);
    }
}
