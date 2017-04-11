package com.xpay.pay.proxy.notify;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.xpay.pay.model.App;
import com.xpay.pay.proxy.OAuth1ApiBinding;
import com.xpay.pay.rest.contract.OrderResponse;

@Component
public class NotifyProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final int DEFAULT_CONNECT_TIMEOUT = 2000;
	private static final int DEFAULT_READ_TIMEOUT = 3000;

	
	public String notify(String url, App app, OrderResponse request) {
		logger.info("notify POST: " + url);
		String response = "fail";
		long l = System.currentTimeMillis();
		try {
			RestTemplate restTemplate = initRestTemplte(app);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();
			logger.info("notify result: " + response + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
		} catch (Exception e) {
			logger.info("notify failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	
	
	private RestTemplate initRestTemplte(App app) {
		OAuth1ApiBinding binding = new OAuth1ApiBinding(app.getKey(), app.getSecret());
		RestTemplate restTemplate = binding.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
		s.setReadTimeout(DEFAULT_READ_TIMEOUT);

		restTemplate.setRequestFactory(s);
		return restTemplate;
	}
}
