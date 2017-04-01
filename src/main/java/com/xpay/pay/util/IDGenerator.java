package com.xpay.pay.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class IDGenerator {
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

	private static final String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789-_";

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

	private static String formatNow(String pattern) {
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
