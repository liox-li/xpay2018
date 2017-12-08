package com.xpay.pay.util;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class IDGenerator {
	// X,3 - appId, 4 - storeId, 17 - yyyyMMddHHmmssSSS, 3 - random
	public static final String TimePattern17 = "yyyyMMddHHmmssSSS";
	public static final String TimePattern14 = "yyyyMMddHHmmss";
	public static final String TimePatternDate = "yyyy-MM-dd";
	public static final String TimePatternTime = "yyyy-MM-dd HH:mm:ss";
	private static final char X = 'X';
	private static final char S = 'S';

	public static String buildOrderNo(int appId, long storeId) {
		StringBuffer sb = new StringBuffer();
		sb.append(X);
		sb.append(StringUtils.leftPad(String.valueOf(appId), 3, "0"));
		sb.append(StringUtils.leftPad(String.valueOf(storeId), 4, "0"));
		sb.append(formatNow(TimePattern17));
		sb.append(randomNum(3));
		return sb.toString();
	}
	
	public static String buildRechargeOrderNo(int appId, long storeId) {
		StringBuffer sb = new StringBuffer();
		sb.append(S);
		sb.append(StringUtils.leftPad(String.valueOf(appId), 3, "0"));
		sb.append(StringUtils.leftPad(String.valueOf(storeId), 4, "0"));
		sb.append(formatNow(TimePattern17));
		sb.append(randomNum(3));
		return sb.toString();
	}
	
	public static String buildShortOrderNo() {
		StringBuffer sb = new StringBuffer();
		sb.append(X);
		sb.append(formatNow(TimePattern14));
		sb.append(randomNum(5));
		return sb.toString();
	}

	private static final char T = 'T';

	public static String buildStoreCode() {
		StringBuffer sb = new StringBuffer();
		sb.append(T);
		sb.append(formatNow(TimePattern14));
		sb.append(randomNum(3));
		return sb.toString();
	}

	private static final String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";

	public static final String buildKey(int length) {
		Random secureRandomProvider = new SecureRandom();
		char[] buffer = new char[length];
		for (int idx = 0; idx < buffer.length; ++idx)
			buffer[idx] = symbols.charAt(secureRandomProvider.nextInt(symbols
					.length()));
		return new String(buffer);
	}
	
	public static final String buildAuthKey() {
		return UUID.randomUUID().toString();
	}

	public static final String buildAuthSecret() {
		return  buildKey(88);
	}

	public static final String buildQrCode(String appId) {
		StringBuffer sb = new StringBuffer();
		sb.append(appId);
		sb.append(formatNow(TimePattern17));
		sb.append(randomNum(7));
		return sb.toString();
	}
	
	public static String formatTime() {
		return formatNow(TimePatternTime);
	}
	
	public static String formatDate() {
		return formatNow(TimePatternDate);
	}
	
	public static String formatDate(String pattern, String time) {
		SimpleDateFormat timeFormat = new SimpleDateFormat(pattern);
		try {
			Date date = timeFormat.parse(time);
			SimpleDateFormat dateFormat = new SimpleDateFormat(TimePatternDate);
			return dateFormat.format(date);
		} catch (ParseException e) {
			return formatDate();
		}
	}
	
	public static String formatTime(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public static String formatNow(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(new Date());
	}

	private static String randomNum(int len) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
