package com.xpay.pay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	public static final String TimePatternDate = "yyyy-MM-dd";
	public static final String TimePatternTime = "yyyy-MM-dd HH:mm:ss";
	
	public static String formatTime(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public static Date parseTime(String timeStr, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			return dateFormat.parse(timeStr);
		} catch (ParseException e) {
			return null;
		}
	}
}
