package com.xpay.pay.proxy.juzhen;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;


public class HttpPost {
	public static String https(String url, Map<String, String> params,
			String caFileUrl, String pfxFileUrl, String caPwd, String pfxPwd)
			throws Exception {
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		String sendString = "";
		if (params != null) {

			for (Entry<String, String> e : params.entrySet()) {

				sb.append(e.getKey());

				sb.append("=");

				sb.append(e.getValue());

				sb.append("&");

			}

			sendString = sb.substring(0, sb.length() - 1);

		}

		System.out.println("代付系统连接:" + url);
		System.out.println("发送给代付系统信息:" + sendString);
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance("SunX509");

			KeyStore ks = KeyStore.getInstance("JKS");
			File caFile = new File(caFileUrl);
			File pfxFile = new File(pfxFileUrl);
			InputStream caInputStream = new FileInputStream(caFile);
			InputStream pfxInputStream = new FileInputStream(pfxFile);
			ks.load(caInputStream, caPwd.toCharArray());

			KeyStore temp = KeyStore.getInstance("PKCS12");
			temp.load(pfxInputStream, pfxPwd.toCharArray());

			kmf.init(temp, pfxPwd.toCharArray());
			tmf.init(ks);
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			URL url2 = new URL(url);
			HttpsURLConnection urlCon = (HttpsURLConnection) url2
					.openConnection();
			urlCon.setDoOutput(true);
			urlCon.setDoInput(true);
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			urlCon.setSSLSocketFactory(sslContext.getSocketFactory());

			OutputStream os = urlCon.getOutputStream();
			InputStream fis = new ByteArrayInputStream(
					sendString.getBytes("UTF-8"));
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] bytes = new byte[1024];
			int len = -1;
			while ((len = bis.read(bytes)) != -1) {
				os.write(bytes, 0, len);
			}
			os.flush();
			bis.close();
			fis.close();
			os.close();

			InputStream is = urlCon.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			System.out.println("返回信息:" + sb);

			br.close();
			is.close();
			urlCon.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String http(String url, Map<String, String> params) throws Exception {

		URL u = null;

		HttpURLConnection con = null;

		// 构建请求参数

		StringBuffer sb = new StringBuffer();
		String sendString = "" ;
		if (params != null) {

			for (Entry<String, String> e : params.entrySet()) {

				sb.append(e.getKey());

				sb.append("=");

				sb.append(e.getValue());
				
				sb.append("&");

			}

			sendString = sb.substring(0, sb.length() - 1);

		}
		
		System.out.println("ERP连接:" + url);
		System.out.println("发送给ERP信息:" + sb.toString());
//		logger.info("ERP连接:" + url);
//		logger.info("发送给ERP信息:" + sb.toString());

		// 尝试发送请求

		try {

			u = new URL(url);

			con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("POST");

			con.setDoOutput(true);

			con.setDoInput(true);

			con.setUseCaches(false);

			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			OutputStreamWriter osw = new OutputStreamWriter(con
					.getOutputStream(), "UTF-8");

			osw.write(sendString);

			osw.flush();

			osw.close();

		} catch (Exception e) {

			throw new Exception("与服务器连接发生错误");

		} finally {

			if (con != null) {

				con.disconnect();

			}

		}

		// 读取返回内容

		StringBuffer buffer = new StringBuffer();

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(con

			.getInputStream(), "UTF-8"));

			String temp;

			while ((temp = br.readLine()) != null) {

				buffer.append(temp);
			}

		} catch (Exception e) {

			throw new Exception("从服务器获取数据失败");

		}
		
		return buffer.toString();

	}

}
