package com.xpay.pay.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

public class OAuth1HttpClient {
	private OAuthConsumer oAuthConsumer;

	public OAuth1HttpClient(String consumerKey, String consumerSecret) {
		setupContext(consumerKey, consumerSecret);
	}

	public void setupContext(String consumerKey, String consumerSecret) {
		this.oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
				consumerSecret);
		oAuthConsumer
				.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
	}

	public void authorize(HttpRequestBase httpRequest) throws Exception {
		oAuthConsumer.sign(httpRequest);
	}

	public String executeGetRequest(String customURIString, String methodType) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");

		HttpRequestBase httpRequest = null;
		URI uri = null;

		try {
			uri = new URI(customURIString);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		if ("GET".equals(methodType)) {
			httpRequest = new HttpGet(uri);
		} else if ("POST".equals(methodType)) {
			httpRequest = new HttpPost(uri);
		}

		httpRequest.addHeader("content-type", "application/json");
		httpRequest.addHeader("Accept", "application/json");

		try {
			authorize(httpRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		HttpResponse httpResponse = null;
		try {
			HttpHost target = new HttpHost(uri.getHost(), -1, uri.getScheme());
			httpResponse = client.execute(target, httpRequest);
			System.out.println("Connection status : "
					+ httpResponse.getStatusLine());

			InputStream inputStraem = httpResponse.getEntity().getContent();

			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStraem, writer, "UTF-8");
			String output = writer.toString();

			return output;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void main(String args[]) throws Exception {
		String key = "b471565ef7394b439c00ea47052e";
		String secret = "93039FAF4719BCA16CF51DA9D86D8BCD";
		String url = "http://106.14.47.193/xpay/rest/v1/pay/unifiedorder?storeId=T20170412143221368&payChannel=2&totalFee=10&orderTime=1212&deviceId=1213&sellerOrderNo=1214&attach=atach";
		OAuth1HttpClient httpClient = new OAuth1HttpClient(key, secret);
		String result = httpClient.executeGetRequest(url, "POST");
		System.out.println(result);
	}
}
