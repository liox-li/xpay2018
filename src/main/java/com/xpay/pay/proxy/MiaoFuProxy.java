package com.xpay.pay.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.xpay.pay.proxy.PayRequest.Method;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.JsonUtils;

@Component
public class MiaoFuProxy implements IPayProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	@Autowired
	RestTemplate miaofuProxy;
	private static final AppConfig config = AppConfig.MiaoFuCponfig;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	private static final String appSecret = config.getProperty("provider.app.secret");

	@Override
	public PayResponse microPay(PayRequest payRequest) {
		String url = buildUrl(Method.MicroPay, payRequest);
		long l = System.currentTimeMillis();
		PayResponse response = null;
		try {
			HttpEntity<?> request = null;
			response = miaofuProxy.exchange(url, HttpMethod.POST, request, PayResponse.class).getBody();
			logger.info("get careers by career result: " + response.getCode() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
		} catch (RestClientException e) {
			logger.info("microPay failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	
	@Override
	public PayResponse unifiedOrder(PayRequest payRequest) {
		String url = buildUrl(Method.UnifiedOrder, payRequest);
		System.out.println("unifiedOrder POST: " + url);
		long l = System.currentTimeMillis();
		PayResponse response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			response = miaofuProxy.exchange(url, HttpMethod.POST, httpEntity, PayResponse.class).getBody();
			logger.info("unifiedOrder result: " + response.getCode() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
		} catch (RestClientException e) {
			logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	
	private String buildUrl(Method method, PayRequest payRequest) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseEndpoint).path("/"+method.module+"/"+method.method);
		List<KeyValuePair> keyPairs = getKeyPairs(payRequest);
		for(KeyValuePair pair : keyPairs) {
			builder.queryParam(pair.getKey(), pair.getValue());
		}
		String sign = signature(keyPairs, appSecret);
		builder.queryParam("sign", sign);
		String url = builder.build().encode().toString();	
		return url;
	}
	
	private List<KeyValuePair> getKeyPairs(PayRequest payRequest) {
		if(payRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();
		
		if(StringUtils.isNotBlank(payRequest.getBusi_code())) {
			keyPairs.add(new KeyValuePair("busi_code", payRequest.getBusi_code()));
		}
		if(StringUtils.isNotBlank(payRequest.getDev_id())) {
			keyPairs.add(new KeyValuePair("dev_id", payRequest.getDev_id()));
		}
		if(StringUtils.isNotBlank(payRequest.getOper_id())) {
			keyPairs.add(new KeyValuePair("oper_id", payRequest.getOper_id()));
		}
		if(payRequest.getPay_channel()!=null) {
			keyPairs.add(new KeyValuePair("pay_channel", payRequest.getPay_channel().id));
		}
		if(StringUtils.isNotBlank(payRequest.getAmount())) {
			keyPairs.add(new KeyValuePair("amount", String.valueOf(payRequest.getAmount())));
		}
		if(StringUtils.isNotBlank(payRequest.getUndiscountable_amount())) {
			keyPairs.add(new KeyValuePair("undiscountable_amount", payRequest.getUndiscountable_amount()));
		}
		if(StringUtils.isNotBlank(payRequest.getRaw_data())) {
			keyPairs.add(new KeyValuePair("raw_data", payRequest.getRaw_data()));
		}
		if(StringUtils.isNotBlank(payRequest.getAuth_code())) {
			keyPairs.add(new KeyValuePair("auth_code", payRequest.getAuth_code()));
		}
		if(StringUtils.isNotBlank(payRequest.getDown_trade_no())) {
			keyPairs.add(new KeyValuePair("down_trade_no", payRequest.getDown_trade_no()));
		}
		if(StringUtils.isNotBlank(payRequest.getSubject())) {
			keyPairs.add(new KeyValuePair("subject", payRequest.getSubject()));
		}
		if(payRequest.getGood_details()!=null) {
			keyPairs.add(new KeyValuePair("good_details", JsonUtils.toJson(payRequest.getGood_details())));
		}
		keyPairs.add(new KeyValuePair("app_id", appId));
		keyPairs.add(new KeyValuePair("timestamp", String.valueOf(System.currentTimeMillis()/1000)));
		keyPairs.add(new KeyValuePair("version", "v3"));

		return keyPairs;
	}

	private String signature(List<KeyValuePair> keyPairs, String appSecret) {
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		for(KeyValuePair pair : keyPairs) {
			builder.queryParam(pair.getKey(), pair.getValue());
		}
		builder.queryParam("APP_SECRET", appSecret);
		String params = builder.build().encode().toString().substring(1);
		String md5 = CryptoUtils.md5(params);
		
		return md5 == null? null:md5.toUpperCase();
		
	}
}
