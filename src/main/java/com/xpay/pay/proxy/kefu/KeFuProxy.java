package com.xpay.pay.proxy.kefu;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.KEFU;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
			String params = this.toParams(keFuRequest);
			String respStr = HttpClientUtil.sendPostRequest(url, params);
			KeFuResponse keFuResponse = JsonUtils.fromJson(respStr, KeFuResponse.class);
			logger.info("unifiedOrder result: " + keFuResponse.getRespCode() + " "+keFuResponse.getRespInfo() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(keFuResponse);
		} catch (Exception e) {
			logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw new GatewayException("503", "unifiedOrder failed", e);
		}
		return response;
	}
	@Override
	public PaymentResponse query(PaymentRequest request) {
		String url = baseEndpoint;
		
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			KeFuRequest keFuRequest = this.toKeFuQueryRequest(KEFU.Query(),request);
			List<KeyValuePair> keyPairs = this.getKeyPairs(keFuRequest);
			String sign = this.signature(keyPairs, appSecret);
			keFuRequest.setSign(sign);
			logger.info("Query POST: " + url+", body "+JsonUtils.toJson(keFuRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String params = this.toParams(keFuRequest);
			String respStr = HttpClientUtil.sendPostRequest(url, params);
			KeFuResponse keFuResponse = JsonUtils.fromJson(respStr, KeFuResponse.class);
			logger.info("Query result: " + keFuResponse.getRespCode() + " "+keFuResponse.getRespInfo() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(keFuResponse);
		} catch (Exception e) {
			logger.info("Query failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw new GatewayException("503", "Query failed", e);
		}
		return response;
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
		keFuRequest.setAmount(String.valueOf(request.getTotalFee()));
		keFuRequest.setOrderId(request.getOrderNo());
		keFuRequest.setNotifyUrl(request.getNotifyUrl());
		keFuRequest.setGoodsName(request.getSubject());
		keFuRequest.setPay_number(request.getOrderTime());
		keFuRequest.setUserid(appId);
		keFuRequest.setOrderCode(method);
		
		return keFuRequest;
	}
	
	private KeFuRequest toKeFuQueryRequest(String method, PaymentRequest request) {
		KeFuRequest keFuRequest = new KeFuRequest();
		keFuRequest.setCustomerId(request.getExtStoreId());
		keFuRequest.setOrderId(request.getGatewayOrderNo());
		keFuRequest.setUserid(appId);
		keFuRequest.setOrderCode(method);
		
		return keFuRequest;
	}
	
	private PaymentResponse toPaymentResponse(KeFuResponse keFuResponse) {
		if (keFuResponse == null || !KeFuResponse.SUCCESS.equals(keFuResponse.getRespCode())) {
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
		bill.setTargetOrderNo(keFuResponse.getOutOrderNo());
		bill.setOrderStatus(toOrderStatus(keFuResponse.getTransStatus()));
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
		if (StringUtils.isNotBlank(paymentRequest.getOrderId())) {
			keyPairs.add(new KeyValuePair("orderId", paymentRequest.getOrderId()));
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
	
	private String toParams(KeFuRequest paymentRequest) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		if (StringUtils.isNotBlank(paymentRequest.getCustomerId())) {
			builder.queryParam("customerId", paymentRequest.getCustomerId());
		}
		if (StringUtils.isNotBlank(paymentRequest.getOrderId())) {
			builder.queryParam("orderId", paymentRequest.getOrderId());
		}
		if (StringUtils.isNotBlank(paymentRequest.getChannelFlag())) {
			builder.queryParam("channelFlag", paymentRequest.getChannelFlag());
		}
		if (StringUtils.isNotBlank(paymentRequest.getAmount())) {
			builder.queryParam("amount", paymentRequest.getAmount());
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			builder.queryParam("notifyUrl", paymentRequest.getNotifyUrl());
		}
		if (StringUtils.isNotBlank(paymentRequest.getGoodsName())) {
			builder.queryParam("goodsName", paymentRequest.getGoodsName());
		}
		if (StringUtils.isNotBlank(paymentRequest.getUserid())) {
			builder.queryParam("userid", paymentRequest.getUserid());
		}
		if (StringUtils.isNotBlank(paymentRequest.getSign())) {
			builder.queryParam("sign", paymentRequest.getSign());
		}
		if (StringUtils.isNotBlank(paymentRequest.getPay_number())) {
			builder.queryParam("pay_number", paymentRequest.getPay_number());
		}
		if (StringUtils.isNotBlank(paymentRequest.getOrderCode())) {
			builder.queryParam("orderCode", paymentRequest.getOrderCode());
		}
		return builder.build().toString().substring(1);
		
	}
	
	private String channel2Flag(PayChannel payChannel) {
		if(PayChannel.WECHAT.equals(payChannel)) {
			return "02";
		} else if(PayChannel.ALIPAY.equals(payChannel)) {
			return "01";
		} else {
			return "";
		}
	}
	
	public static OrderStatus toOrderStatus(String status) {
		if("0".equals(status)) {
			return OrderStatus.SUCCESS;
		} else if("1".equals(status)) {
			return OrderStatus.PAYERROR;
		} else {
			return OrderStatus.NOTPAY;
		}
 	}
}
