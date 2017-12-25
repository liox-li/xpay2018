package com.xpay.pay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriComponentsBuilder;

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

	public static final String sha512(String str) {
		try {
			MessageDigest messageDigest =  MessageDigest.getInstance("SHA512");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
			byte[] byteArray = messageDigest.digest();
			return bytesToHex(byteArray);
		} catch (Exception e) {
			return null;
		}
	}

	public static final String signQueryParams(List<KeyValuePair> keyPairs, String secretKey, String appSecret) {
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		for (KeyValuePair pair : keyPairs) {
			builder.queryParam(pair.getKey(), pair.getValue());
		}
		String params;
		if(StringUtils.isNotBlank(secretKey)) {
			builder.queryParam(secretKey, appSecret);
			params = builder.build().toString().substring(1);
		} else {
			params = builder.build().toString().substring(1);
			params += appSecret;
		}
		String md5 = CryptoUtils.md5(params).toUpperCase();
		return md5;
	}

	public static final String md5KeFu(String str, String key) {
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

	public static String encryptDESede(String key, String iv, String src) {
		try {
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey sec = keyFactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			IvParameterSpec IvParameters = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
			return Base64Utils.encodeToString(cipher.doFinal(src.getBytes("UTF-8")));
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

  public static String decryptDESede(String key, String iv, String src) {
    try {
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey sec = keyFactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			IvParameterSpec IvParameters = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
			return new String(cipher.doFinal(Base64Utils.decode(src.getBytes("UTF-8"))), "UTF-8");
		} catch (Exception e1) {
      e1.printStackTrace();
    }
    return null;
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
	
	public static String base64Encode(String str) {
		if(str == null) {
			return null;
		}
		Encoder encoder = Base64.getEncoder();
		try {
			return encoder.encodeToString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String base64Decode(String str) {
		if(str == null) {
			return null;
		}
		Decoder decoder = Base64.getDecoder();
		try {
			return new String(decoder.decode(str.getBytes("UTF-8")), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
