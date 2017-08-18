package com.xpay.pay.proxy.kefu;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.KEFU;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.JsonUtils;

@Component
public class KeFuProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.KeFuConfig;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	private static final String appSecret = config.getProperty("provider.app.secret");
	
	@Autowired
	RestTemplate keFuProxy;
	
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		String url = baseEndpoint;
		
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			KeFuRequest keFuRequest = this.toKeFuRequest(KEFU.UnifiedOrder(),request);
			List<KeyValuePair> keyPairs = this.getKeyPairs(keFuRequest);
			String sign = this.signature(keyPairs, appSecret);
			keFuRequest.setSign(sign);
			logger.info("unifiedOrder POST: " + url+", body "+JsonUtils.toJson(keFuRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map= this.toFormMap(keFuRequest);
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			KeFuResponse keFuResponse = keFuProxy.exchange(url, HttpMethod.POST, httpEntity, KeFuResponse.class).getBody();
			logger.info("unifiedOrder result: " + keFuResponse.getRespCode() + " "+keFuResponse.getRespInfo() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(keFuResponse);
		} catch (RestClientException e) {
			logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	@Override
	public PaymentResponse query(PaymentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PaymentResponse refund(PaymentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private KeFuRequest toKeFuRequest(String method, PaymentRequest request) {
		KeFuRequest keFuRequest = new KeFuRequest();
		keFuRequest.setCustomerId(request.getExtStoreId());
		keFuRequest.setChannelFlag(this.channel2Flag(request.getPayChannel()));
		keFuRequest.setAmount(request.getTotalFee());
		keFuRequest.setNotifyUrl(request.getNotifyUrl());
		keFuRequest.setGoodsName(request.getSubject());
		keFuRequest.setPay_number(request.getOrderNo());
		keFuRequest.setUserid(appId);
		keFuRequest.setOrderCode(method);
		
		return keFuRequest;
	}
	
	private PaymentResponse toPaymentResponse(KeFuResponse keFuResponse) {
		if (keFuResponse == null || !KeFuResponse.SUCCESS.equals(keFuResponse.getRespCode())
				|| StringUtils.isBlank(keFuResponse.getPayUrl())) {
			String code = keFuResponse == null ? NO_RESPONSE : keFuResponse.getRespCode();
			String msg = keFuResponse == null ? "No response" : keFuResponse.getRespInfo();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(keFuResponse.getPayUrl());
		bill.setOrderNo(keFuResponse.getPay_number());
		bill.setGatewayOrderNo(keFuResponse.getOrderId());
		bill.setOrderStatus(OrderStatus.NOTPAY);
		response.setBill(bill);
		return response;
	}
	
	private List<KeyValuePair> getKeyPairs(KeFuRequest paymentRequest) {
		if (paymentRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(paymentRequest.getCustomerId())) {
			keyPairs.add(new KeyValuePair("customerId", paymentRequest.getCustomerId()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getChannelFlag())) {
			keyPairs.add(new KeyValuePair("channelFlag", paymentRequest.getChannelFlag()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getAmount())) {
			keyPairs.add(new KeyValuePair("amount", paymentRequest.getAmount()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("notifyUrl", paymentRequest.getNotifyUrl()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getGoodsName())) {
			keyPairs.add(new KeyValuePair("goodsName", paymentRequest.getGoodsName()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getUserid())) {
			keyPairs.add(new KeyValuePair("userid", paymentRequest.getUserid()));
		}
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
		String params = builder.build().toString().substring(1);
		logger.debug("sorted params: "+params);
		String md5 = CryptoUtils.md5KeFu(params, appSecret);
		logger.debug("md5 upper: "+md5.toUpperCase());
		return md5 == null? null:md5.toUpperCase();
	}
	
	private MultiValueMap<String, String> toFormMap(KeFuRequest paymentRequest) {
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		if (StringUtils.isNotBlank(paymentRequest.getCustomerId())) {
			map.add("customerId", paymentRequest.getCustomerId());
		}
		if (StringUtils.isNotBlank(paymentRequest.getChannelFlag())) {
			map.add("channelFlag", paymentRequest.getChannelFlag());
		}
		if (StringUtils.isNotBlank(paymentRequest.getAmount())) {
			map.add("amount", paymentRequest.getAmount());
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			map.add("notifyUrl", paymentRequest.getNotifyUrl());
		}
		if (StringUtils.isNotBlank(paymentRequest.getGoodsName())) {
			map.add("goodsName", paymentRequest.getGoodsName());
		}
		if (StringUtils.isNotBlank(paymentRequest.getUserid())) {
			map.add("userid", paymentRequest.getUserid());
		}
		if (StringUtils.isNotBlank(paymentRequest.getSign())) {
			map.add("sign ", paymentRequest.getSign());
		}
		if (StringUtils.isNotBlank(paymentRequest.getPay_number())) {
			map.add("pay_number", paymentRequest.getPay_number());
		}
		if (StringUtils.isNotBlank(paymentRequest.getOrderCode())) {
			map.add("orderCode", paymentRequest.getOrderCode());
		}
		return map;
		
	}
	
	private String channel2Flag(PayChannel payChannel) {
		if(PayChannel.WECHAT.equals(payChannel)) {
			return "02";
		} else {
			return "01";
		}
	}
}
