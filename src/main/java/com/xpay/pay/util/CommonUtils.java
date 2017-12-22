package com.xpay.pay.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.web.util.UriComponentsBuilder;

public class CommonUtils {

	public static final String buildQueryParams(List<KeyValuePair> keyPairs, String signKey, String sign, String encodeKeys) {
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		for (KeyValuePair pair : keyPairs) {
			String value = pair.getValue();
			if(StringUtils.indexOf(encodeKeys, pair.getKey())>=0) {
				value = urlEncode(pair.getValue());
			} 
			builder.queryParam(pair.getKey(), value);
		}
		builder.queryParam(signKey, sign);
		return builder.build().toString();
	}
	
	
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
	
	public static int toInt(Long val) {
		if(val == null || val > Integer.MAX_VALUE || val<Integer.MIN_VALUE) {
			return 0;
		}
		return val.intValue();
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
	
	public static boolean isOneOf(String source, String target, String sep) {
		if(StringUtils.isBlank(source) || StringUtils.isBlank(target) || StringUtils.isBlank(sep)) {
			return false;
		}
		
		String[] strs = source.split(sep);
		for(String str : strs) {
			if(target.equals(str)) {
				return true;
			}
		}
		return false;
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
	
	public static String getDomainName(String url) {
		try {
			URI uri = new URI(url);
			String domain = uri.getHost();
			return domain.startsWith("www.") ? domain.substring(4) : domain;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
}
