package com.xpay.pay.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class CommonUtils {
	public static String iso88591(String str) {
		try {
			return new String(str.toString().getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public static String utf8(String str) {
		try {
			return new String(str.toString().getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public static float toFloat(String val) {
		try {
			return Float.valueOf(val);
		} catch(Exception e) {
			return Float.MIN_VALUE;
		}
	}
	
	public static int toInt(String val) {
		try {
			return Integer.valueOf(val);
		} catch(Exception e) {
			return Integer.MIN_VALUE;
		}
	}
	
	public static <E> boolean in(Collection<E> coll, E e) {
		if(CollectionUtils.isEmpty(coll)) {
			return false;
		}
		return coll.contains(e);
	}

	// X,3 - appId, 4 - storeId, 17 - yyyyMMddHHmmssSSS, 3 - random
	private static final String TimePattern17 = "yyyyMMddHHmmssSSS";
	private static final String timePattern14 = "yyyyMMddHHmmss";
	private static final char X = 'X';
	public static String buildOrderNo(int appId, long storeId) {
		StringBuffer sb = new StringBuffer();
		sb.append(X);
		sb.append(StringUtils.leftPad(String.valueOf(appId), 3, "0"));
		sb.append(StringUtils.leftPad(String.valueOf(storeId), 4, "0"));
		sb.append(formatNow(TimePattern17));
		sb.append(randomNum(3));
		return sb.toString();
	}
	
	private static final char T = 'T';
	public static String buildStoreCode() {
		StringBuffer sb = new StringBuffer();
		sb.append(T);
		sb.append(formatNow(timePattern14));
		sb.append(randomNum(3));
		return sb.toString();
	}
	
	private static String formatNow(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(new Date());
	}
	
	private static String randomNum(int len) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<len;i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
