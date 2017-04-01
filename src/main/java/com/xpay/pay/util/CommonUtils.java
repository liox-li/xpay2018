package com.xpay.pay.util;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

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
}
