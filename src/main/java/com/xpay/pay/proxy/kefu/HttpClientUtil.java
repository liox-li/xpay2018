package com.xpay.pay.proxy.kefu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * Created by dell on 2015/1/13.
 */
public class HttpClientUtil {

   
    /**
     * 封装HTTP POST方法
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String post(String url, Map paramMap) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        /** 忽略SSL证书校验*/
        try { 
            //Secure Protocol implementation.
            SSLContext ctx = SSLContext.getInstance("SSL");
            //Implementation of a trust manager for X509 certificates    
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,  
                        String string) throws CertificateException {  
                }
                public void checkServerTrusted(X509Certificate[] xcs,  
                        String string) throws CertificateException {  
                }
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }
            };  
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);  
            ClientConnectionManager ccm = httpClient.getConnectionManager();  
            //register https protocol in httpclient's scheme registry    
            SchemeRegistry sr = ccm.getSchemeRegistry();  
            sr.register(new Scheme("https", 443, ssf));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formparams = setHttpParams(paramMap);
        UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
        httpPost.setEntity(param);
        HttpResponse response = httpClient.execute(httpPost);
        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP POST方法
     * @param
     * @param （如JSON串）
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String post(String url, String data) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type","text/json; charset=utf-8");
        httpPost.setEntity(new StringEntity(URLEncoder.encode(data, "UTF-8")));
        HttpResponse response = httpClient.execute(httpPost);
        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP GET方法
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String get(String url) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(URI.create(url));
        HttpResponse response = httpClient.execute(httpGet);
        String httpEntityContent = getHttpEntityContent(response);
        httpGet.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP GET方法
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String get(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();
        List<NameValuePair> formparams = setHttpParams(paramMap);
        String param = URLEncodedUtils.format(formparams, "UTF-8");
        httpGet.setURI(URI.create(url + "?" + param));
        HttpResponse response = httpClient.execute(httpGet);
        String httpEntityContent = getHttpEntityContent(response);
        httpGet.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP PUT方法
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String put(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(url);
        List<NameValuePair> formparams = setHttpParams(paramMap);
        UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPut.setEntity(param);
        HttpResponse response = httpClient.execute(httpPut);
        String httpEntityContent = getHttpEntityContent(response);
        httpPut.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP DELETE方法
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String delete(String url) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete= new HttpDelete();
        httpDelete.setURI(URI.create(url));
        HttpResponse response = httpClient.execute(httpDelete);
        String httpEntityContent = getHttpEntityContent(response);
        httpDelete.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP DELETE方法
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws java.io.IOException
     */
    public static String delete(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete();
        List<NameValuePair> formparams = setHttpParams(paramMap);
        String param = URLEncodedUtils.format(formparams, "UTF-8");
        httpDelete.setURI(URI.create(url + "?" + param));
        HttpResponse response = httpClient.execute(httpDelete);
        String httpEntityContent = getHttpEntityContent(response);
        httpDelete.abort();
        return httpEntityContent;
    }


    /**
     * 设置请求参数
     * @param
     * @return
     */
    private static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formparams;
    }

    /**
     * 获得响应HTTP实体内容
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private static String getHttpEntityContent(HttpResponse response) throws IOException, UnsupportedEncodingException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line + "\n");
                line = br.readLine();
            }
            return sb.toString();
        }
        return "";
    }
    
    /*
	 * map转换为url
	 * */
	public static String sendPostRequest(String url, String params) throws Exception {
		URL u = null;
		HttpURLConnection con = null;
		
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
			osw.write(params);
			osw.flush();
			osw.close();
		} catch (Exception e) {
			System.out.println("与服务器连接发生错误");
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
			System.out.println("从服务器获取数据失败");
			throw new Exception("从服务器获取数据失败");
		}		
		return buffer.toString();
	}
   
	public static byte[] postBinResource(String urlStr, String request, String encode,int timeOutInSeconds) throws Exception {
        HttpURLConnection http = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            URL url = new URL(urlStr);
            http = (HttpURLConnection)url.openConnection();
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setUseCaches(false);
            http.setConnectTimeout(timeOutInSeconds*1000);//设置连接超时
            //如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
            http.setReadTimeout(timeOutInSeconds*1000);//设置读取超时
            //如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset="+encode);
            http.connect();

            outputStream = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, encode);
            osw.write(request);
            osw.flush();
            osw.close();

            if (http.getResponseCode() == 200) {
                inputStream = http.getInputStream();
                byte[] returnValue1 = IOUtils.toByteArray(inputStream);
                return returnValue1;
            }else{
                throw new RuntimeException("http read ["+http.getResponseCode()+"]");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (http != null) http.disconnect();
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
   
}
