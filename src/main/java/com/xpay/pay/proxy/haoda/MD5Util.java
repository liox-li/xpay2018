package com.xpay.pay.proxy.haoda;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String getMD5Str(String sMsg) {
        byte[] msg = sMsg.getBytes();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(msg);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        }
        byte[] b = messageDigest.digest();
        return new String(Base64.encodeBase64(b));
    }

    public static void main(String[] args) {
        String snr = "aaaa1111!";
        String sStr = getMD5Str(snr);
        System.out.println(sStr);
    }
}