package com.xpay.pay.util;

import java.security.MessageDigest;

public class CryptoUtils {
	public static final String md5(String str) {
		try {
			MessageDigest messageDigest = null;

			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));

			byte[] byteArray = messageDigest.digest();

			return bytesToHex(byteArray);
		} catch (Exception e) {
			return null;
		}
	}


	private static String bytesToHex(byte[] bytes) {
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
		}

		return md5StrBuff.toString();
	}
}
