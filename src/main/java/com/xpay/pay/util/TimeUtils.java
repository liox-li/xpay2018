package com.xpay.pay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TimeUtils {
	public static final String TimePatternDate = "yyyy-MM-dd";
	public static final String TimePatternTime = "yyyy-MM-dd HH:mm:ss";
	public static final String TimeShortPatternTime = "yyyyMMddHHmmss";
	
	public static String formatTime(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public static Date parseTime(String timeStr, String pattern) {
		if(StringUtils.isAnyBlank(timeStr, pattern)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			return dateFormat.parse(timeStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static boolean isNowDayTime() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		return hour>9 && hour<22;
	}
	
	public static Date beginOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date endOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	
	public static int daysBetween(Date startDate, Date endDate) {
		if(startDate == null || endDate == null) {
			return Integer.MAX_VALUE;
		}
		long difference = Math.abs(endDate.getTime() - startDate.getTime());
	    long differenceDates = difference / (24 * 60 * 60 * 1000);
	    return (int)differenceDates;
	}
}
