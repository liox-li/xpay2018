package com.xpay.pay.proxy.juzhen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Enumeration;

import org.springframework.security.crypto.codec.Base64;

public class JuSignature {


	public static String sign(String msgInfo, String encoding,String fileUrl,String pwd) {
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			// 通过SHA1进行摘要并转16进制
			byte[] signDigest = sha1X16(msgInfo, encoding);
			File pfxFile= new File(fileUrl); 
			InputStream inputStream = new FileInputStream(pfxFile);
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(inputStream,pwd.toCharArray());
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey)keyStore.getKey(keyAlias, "123pay".toCharArray());
			byteSign = Base64.encode(signBySoft(privateKey, signDigest));
			stringSign = new String(byteSign);
			return stringSign;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
    private static byte[] sha1X16(String data, String encoding) throws Exception{
        byte bytes[] = sha1(data.getBytes(encoding));
        StringBuilder sha1StrBuff = new StringBuilder();
        for(int i = 0; i < bytes.length; i++)
            if(Integer.toHexString(255 & bytes[i]).length() == 1)
                sha1StrBuff.append("0").append(Integer.toHexString(255 & bytes[i]));
            else
                sha1StrBuff.append(Integer.toHexString(255 & bytes[i]));
        try{
            return sha1StrBuff.toString().getBytes(encoding);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    private static byte[] sha1(byte data[]) throws Exception{
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(data);
        return md.digest();
    }
    
    private static byte[] signBySoft(PrivateKey privateKey, byte data[])throws Exception {
        byte result[] = null;
        Signature st = Signature.getInstance("SHA1withRSA");
        st.initSign(privateKey);
        st.update(data);
        result = st.sign();
        return result;
    }
    
    /**
     * 从返回报文中获取签名
     * @param responts 返回报文
     * @return
     */
    public static String getSign(String responts){
    	String msgs[] = responts.split("\"signature\":\"", -1);
		String signature = msgs[1].split("\"}", -1)[0];
		return signature;
    }

	/**
	 * 验证签名
	 * @param responts 返回报文
	 * @param encoding
	 * @param pubKeyUrl
	 * @param pfxPwd
	 * @return
	 */
	public static boolean  validateSign(String response, String encoding,
			String pubKeyUrl, String pfxPwd) {
		boolean success=false;
		String signature1 = JuSignature.getSign(response);
		String responts1 = response.replace(signature1, "@eidpay");
		String signature2 = JuSignature.sign(responts1, "UTF-8",pubKeyUrl,pfxPwd);
		if (signature2.equals(signature1)) {
			success=true;
		}
		return success;
		
	}
    
}
