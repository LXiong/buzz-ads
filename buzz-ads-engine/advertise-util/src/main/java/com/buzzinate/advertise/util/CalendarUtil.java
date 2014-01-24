package com.buzzinate.advertise.util;

import java.util.Calendar;
import java.util.TimeZone;

import com.buzzinate.buzzads.enums.WeekDay;

public class CalendarUtil {
	
	public static final int MIN_MINUTES_OF_DAY = 0;
	public static final int MAX_MINUTES_OF_DAY = 24 * 60;
	
	//Jan 1, 2000
	public static final long MIN_START_TIME = 946614223849l;
	//Jan 1, 3000
	public static final long MAX_END_TIME = 32503609423849l;
	
	public static final Integer ALL_WEEKDAY = 0;
	public static int getMinute(Calendar calendar){
		int min = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour * 60 + min;
	}
	
	public static int getMinute(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		calendar.setTimeInMillis(time);
		return getMinute(calendar);
	}
	
	public static Integer getWeekDay(Calendar calendar){
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case Calendar.MONDAY:
		    return WeekDay.MONDAY.getCode();

		case Calendar.TUESDAY:
		    return WeekDay.TUESDAY.getCode();

		case Calendar.WEDNESDAY:
		    return WeekDay.WEDNESDAY.getCode();

		case Calendar.THURSDAY:
		    return WeekDay.THURSDAY.getCode();
		
		case Calendar.FRIDAY:
		    return WeekDay.FRIDAY.getCode();
		    
		case Calendar.SATURDAY:
		    return WeekDay.SATURDAY.getCode();
		
		case Calendar.SUNDAY:
			return WeekDay.SUNDAY.getCode();
		
		default :
			return ALL_WEEKDAY;

		}
	}
	
	public static void main(String[] args){
		Calendar calendar = Calendar.getInstance();
		System.out.println(getMinute(0l));
	}
}
