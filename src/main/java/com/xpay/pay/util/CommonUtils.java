package com.xpay.pay.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.collections.CollectionUtils;

public class CommonUtils {
	public static String urlEncode(String param) 
	{
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch(Exception e) {
			return param;
		}
	}
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
	
	public static Date hourBeforeNow(int hours) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime timeout = zonedNow.minusHours(hours);
		return Date.from(timeout.toInstant());
	}
	
	public static Date parseTime(String timeStr, String pattern) {
		SimpleDateFormat timeFormat = new SimpleDateFormat(pattern);
		try {
			Date date = timeFormat.parse(timeStr);
			return date;
		} catch (ParseException e) {
			return hourBeforeNow(36);
		}
	}
	
	public static boolean isWithinHours(String timeStr, String pattern, int hours) {
		Date hourBeforeNow = hourBeforeNow(hours);
		Date date = parseTime(timeStr, pattern);
		return date.after(hourBeforeNow);
	}
	
	public static String getLocalIP() {
		try {
		    Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		    for (; n.hasMoreElements();)
		    {
		        NetworkInterface e = n.nextElement();
	
		        Enumeration<InetAddress> a = e.getInetAddresses();
		        for (; a.hasMoreElements();)
		        {
		            InetAddress addr = a.nextElement();
		            if(isValidIp(addr.getHostAddress())) {
		            	return addr.getHostAddress();
		            }
		            
		        }
		    }
		    return InetAddress.getLocalHost().getHostAddress();
		} catch(Exception e) {
			return null;
		}
	} 
	
	private static boolean isValidIp(String ip) {
		if(ip == null || ip.trim().length()==0) {
			return false;
		}
		if(ip.startsWith("127.")) {
			return false;
		}
		
		String[] ips = ip.split("\\.");
		if(ips == null || ips.length!=4) {
			return false;
		}
		return true;
	}
	
}
