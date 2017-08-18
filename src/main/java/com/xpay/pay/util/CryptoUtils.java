package com.xpay.pay.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CryptoUtils {
	public static final String md5(String str) {
		try {
			MessageDigest messageDigest =  MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
			byte[] byteArray = messageDigest.digest();
			return bytesToHex(byteArray);
		} catch (Exception e) {
			return null;
		}
	}
	
    public static String md5KeFu(String str, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            
            String result="";
            byte[] temp;
            temp=md5.digest(key.getBytes("UTF-8"));
            for (int i=0; i<temp.length; i++){
                result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
            
            return result;
        } catch(Exception e)
        {
        	return null;
        }
    }
	
	public static boolean checkSignature(Map<String,String> params,String key, String signKey, String keyParam){
        boolean result = false;
        if(params.containsKey(signKey)){
            String sign = params.get(signKey);
            params.remove(signKey);
            String preStr = buildPayParams(params);
            String signRecieve = CryptoUtils.md5(preStr+"&"+keyParam+"=" + key);
            result = sign.equalsIgnoreCase(signRecieve);
        }
        return result;
    }

	private static String buildPayParams(Map<String, String> payParams){
		StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            sb.append(payParams.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
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
