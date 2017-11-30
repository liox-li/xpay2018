package com.xpay.pay.proxy.chinaumsv3;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.CHINAUMSALIPAY;

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

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.chinaums.ChinaUmsResponse;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.JsonUtils;

@Component
public class ChinaUmsV3Proxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.ChinaUmsV3Config;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	private static final String appSecret = config.getProperty("provider.app.secret");
	private static final String appName = config.getProperty("provider.app.name");
	private static final String tId = config.getProperty("provider.tid");
	private static final String instMid = config.getProperty("provider.inst.mid");
	
	@Autowired
	RestTemplate chinaUmsProxy;
	
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		String url = baseEndpoint;
		
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			request.setGatewayOrderNo(IDGenerator.buildQrCode(appId));
			ChinaUmsV3Request chinaUmsRequest = this.toChinaUmsRequest(CHINAUMSALIPAY.UnifiedOrder(), request);
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("unifiedOrder POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsV3Response chinaUmsResponse = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsV3Response.class).getBody();
			logger.info("unifiedOrder result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);
			response.getBill().setGatewayOrderNo(request.getGatewayOrderNo());
		} catch (RestClientException e) {
			logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	@Override
	public PaymentResponse query(PaymentRequest request) {
		String url = baseEndpoint;
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			ChinaUmsV3Request chinaUmsRequest = this.toChinaUmsRequest(CHINAUMSALIPAY.Query(),request);
			
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("query POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsV3Response chinaUmsResponse = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsV3Response.class).getBody();
	//		logger.info("query result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + " "+chinaUmsResponse.getBillStatus() + ", took "
	//				+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);
		} catch (RestClientException e) {
			logger.info("query failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	
	@Override
	public PaymentResponse refund(PaymentRequest request) {
		String url = baseEndpoint;
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			ChinaUmsV3Request chinaUmsRequest = this.toChinaUmsRequest(CHINAUMSALIPAY.Refund(),request);
			chinaUmsRequest.setRefundAmount(chinaUmsRequest.getTotalAmount());
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("refund POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsV3Response chinaUmsResponse = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsV3Response.class).getBody();
			logger.info("refund result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);
		} catch (RestClientException e) {
			logger.info("refund failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	private ChinaUmsV3Request toChinaUmsRequest(String method, PaymentRequest request) {
		ChinaUmsV3Request chinaUmsRequest = new ChinaUmsV3Request();
		chinaUmsRequest.setMsgSrc(appName);
		chinaUmsRequest.setMsgType(method);
		chinaUmsRequest.setRequestTimestamp(IDGenerator.formatTime());
		chinaUmsRequest.setMerOrderId(request.getGatewayOrderNo());
		String[] strArrays = request.getExtStoreId().split(",");
		if(strArrays.length==1) {
			chinaUmsRequest.setMid(request.getExtStoreId());
			chinaUmsRequest.setTid(tId);
		} else {
			chinaUmsRequest.setMid(strArrays[0]);
			chinaUmsRequest.setTid(strArrays[1]);
		}
		chinaUmsRequest.setInstMid(instMid);
		chinaUmsRequest.setTradeType("APP");
		chinaUmsRequest.setGoods(request.getGoods());
		chinaUmsRequest.setOrderDesc(request.getSubject());
		if(request.getTotalFee()!=null) {
			chinaUmsRequest.setTotalAmount(String.valueOf((int)(request.getTotalFee()*100)));
		}
		chinaUmsRequest.setNotifyUrl(request.getNotifyUrl());
		chinaUmsRequest.setReturnUrl(request.getReturnUrl());
		return chinaUmsRequest;
	}
	
	private List<KeyValuePair> getKeyPairs(ChinaUmsV3Request chinaUmsRequest) {
		if (chinaUmsRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();
		
		if (StringUtils.isNotBlank(chinaUmsRequest.getMsgSrc())) {
			keyPairs.add(new KeyValuePair("msgSrc", chinaUmsRequest.getMsgSrc()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getMsgType())) {
			keyPairs.add(new KeyValuePair("msgType", chinaUmsRequest.getMsgType()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getRequestTimestamp())) {
			keyPairs.add(new KeyValuePair("requestTimestamp", chinaUmsRequest.getRequestTimestamp()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getMerOrderId())) {
			keyPairs.add(new KeyValuePair("merOrderId", chinaUmsRequest.getMerOrderId()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getMid())) {
			keyPairs.add(new KeyValuePair("mid", chinaUmsRequest.getMid()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getTid())) {
			keyPairs.add(new KeyValuePair("tid", chinaUmsRequest.getTid()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getInstMid())) {
			keyPairs.add(new KeyValuePair("instMid", chinaUmsRequest.getInstMid()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getTradeType())) {
			keyPairs.add(new KeyValuePair("tradeType", chinaUmsRequest.getTradeType()));
		}
		if(chinaUmsRequest.getGoods()!=null) {
			keyPairs.add(new KeyValuePair("goods", JsonUtils.toJson(chinaUmsRequest.getGoods())));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getOrderDesc())) {
			keyPairs.add(new KeyValuePair("orderDesc", chinaUmsRequest.getOrderDesc()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getTotalAmount())) {
			keyPairs.add(new KeyValuePair("totalAmount", chinaUmsRequest.getTotalAmount()));
		}
		if (StringUtils.isNotBlank(chinaUmsRequest.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("notifyUrl", chinaUmsRequest.getNotifyUrl()));
		}
		if(StringUtils.isNotBlank(chinaUmsRequest.getReturnUrl())) {
			keyPairs.add(new KeyValuePair("returnUrl", chinaUmsRequest.getReturnUrl()));
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
		String params = builder.build().toString().substring(1)+appSecret;
		logger.debug("sorted params: "+params);
		String md5 = CryptoUtils.md5(params);
		logger.debug("md5 upper: "+md5.toUpperCase());
		return md5 == null? null:md5.toUpperCase();
	}

	private PaymentResponse toPaymentResponse(ChinaUmsV3Request chinaUmsRequest, ChinaUmsV3Response chinaUmsResponse) {
		if (chinaUmsResponse == null || !ChinaUmsResponse.SUCCESS.equals(chinaUmsResponse.getErrCode())
				|| StringUtils.isBlank(chinaUmsResponse.getQrCode())) {
			String code = chinaUmsResponse == null ? NO_RESPONSE : chinaUmsResponse.getErrCode();
			String msg = chinaUmsResponse == null ? "No response" : chinaUmsResponse.getErrMsg();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(chinaUmsResponse.getQrCode());
		bill.setOrderNo(chinaUmsRequest.getMerOrderId());
		bill.setOrderStatus(OrderStatus.NOTPAY);
		response.setBill(bill);
		return response;
	}

	public static OrderStatus toOrderStatus(String billStatus) {
		if("PAID".equals(billStatus)) {
			return OrderStatus.SUCCESS;
		} else if("UNPAID".equals(billStatus)) {
			return OrderStatus.NOTPAY;
		}else if("REFUND".equals(billStatus)) {
			return OrderStatus.REFUND;
		}else if("CLOSED".equals(billStatus)) {
			return OrderStatus.CLOSED;
		} else {
			return OrderStatus.NOTPAY;
		}
	}
}
