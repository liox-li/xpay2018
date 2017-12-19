package com.xpay.pay.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.springframework.security.crypto.codec.Base64;

/**
 * RSA签名验签类
 */
public class RSASignature {
	/**
	 * 签名算法
	 */
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA签名
	 *
	 * @param content    待签名数据
	 * @param privateKey 商户私钥
	 * @param encode     字符集编码
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String encode) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes()));

			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(encode));

			byte[] signed = signature.sign();

			return new String(Base64.encode(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String sign(String content, byte[] priBytes, String encode) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(priBytes);
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(encode));
			byte[] signed = signature.sign();
			return new String(Base64.encode(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sign(String content, byte[] priBytes) {
		return sign(content, priBytes, "utf-8");
	}

	/**
	 * 密钥签名
	 *
	 * @param content
	 * @param privateKey
	 * @return
	 */
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes());
			byte[] signed = signature.sign();
			return new String(Base64.encode(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA验签名检查
	 *
	 * @param content   待签名数据
	 * @param sign      签名值
	 * @param publicKey 分配给开发商公钥
	 * @param encode    字符集编码
	 * @return 布尔值
	 */
	public static boolean doCheck(String content, String sign, String publicKey, String encode) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey.getBytes());
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(encode));

			boolean bverify = signature.verify(Base64.decode(sign.getBytes()));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * RSA验签名检查
	 *
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @return
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey.getBytes());
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes());

			boolean bverify = signature.verify(Base64.decode(sign.getBytes()));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean doCheck(String content, String sign, byte[] pubBytes) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = pubBytes;
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes());

			boolean bverify = signature.verify(Base64.decode(sign.getBytes()));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) {
		String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMGcgED+NirUgyy8"
				+ "e8JBgao4Sp2kWKfXb+625wt5AE+9IqYOGU/d9UM/9k8K61hiutFuJjCX+wPsooKv"
				+ "HWtFxXW4SdaSvkSBk851cm4rHV21BHuh7FmgJf6ppxo6hMiowiUYlOuakWOg72N5"
				+ "vTufM75vxg4SN2TB03bnBRoN1AqfAgMBAAECgYEAjjm4IYZdJkNshR2E0GOsq+2k"
				+ "Kn/DPLDPIg4sVcoyh2EKJe/vejMz643+E3yS/B+y2wxOc54BoJVhoByhwN7FOPhh"
				+ "GWdyLS449St4Ko50ftiXhrcIb34alhIwWM+qBoC7DLEmf8Xt1VsSJTpO8TI3UAg/"
				+ "415CbAwGGOEC1lGZ1gECQQDr0SPQiZJYbbdqtztwLvjTzSPRzXnZogW6cDkzZdgc"
				+ "n/fIuWD9pdRupCFTrit0fBgcor5CuVtQweml9DJ8YDWfAkEA0i6duviRVsx45GKQ"
				+ "6mZzZr8+700z38B9ZlA2J82TaFKUcu5FIz35QRx1IrwIfNEWVw1SFesWHGMBcNcC"
				+ "4xoLAQJANWiRnvp5fbOjtfS37omE65trgGn0LflHKNmf6ucnEhyc5wYtVGVJfqGH"
				+ "tsccqm4sm9e0FbKbXuf8PgzXnlm/6wJBAMeCSmTNSCCtTN/1k/t8LnTMeq/qh6AP"
				+ "9rik0RMtN4xiPtltxSDz1eLxYVsMQ74/NsaBNXbqxP1DGX4ECpCKCwECQQC/9Hcf"
				+ "ZQLIyeCVauesjLddQVNYXJoTWW88JQn2gYd7sxUYb/mToGtm4D9NatYAs0/4NQXo"
				+ "yup4xBO2lD02Ri18";
		System.out.println(privateKey);
		String sign = sign("aaaaa", privateKey);

		String publicKey = ""
				+ "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBnIBA/jYq1IMsvHvCQYGqOEqd"
				+ "pFin12/utucLeQBPvSKmDhlP3fVDP/ZPCutYYrrRbiYwl/sD7KKCrx1rRcV1uEnW"
				+ "kr5EgZPOdXJuKx1dtQR7oexZoCX+qacaOoTIqMIlGJTrmpFjoO9jeb07nzO+b8YO"
				+ "EjdkwdN25wUaDdQKnwIDAQAB";
		System.out.println(publicKey);
		System.out.println(doCheck("aaaaa", sign, publicKey));
	}


}