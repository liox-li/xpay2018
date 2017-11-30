package com.xpay.pay.proxy.chinaumsv2;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.CHINAUMSV2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.xpay.pay.proxy.chinaums.ChinaUmsRequest;
import com.xpay.pay.proxy.chinaums.ChinaUmsResponse;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.TimeUtils;

@Component
public class ChinaUmsV2Proxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.ChinaUmsV2Config;
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
			ChinaUmsRequest chinaUmsRequest = this.toChinaUmsRequest(CHINAUMSV2.UnifiedOrder(),request);
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("unifiedOrder POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsResponse chinaUmsResponse = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsResponse.class).getBody();
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
			ChinaUmsRequest chinaUmsRequest = this.toChinaUmsRequest(CHINAUMSV2.Query(),request);
			
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("query POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsResponse chinaUmsResponse = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsResponse.class).getBody();
			logger.info("query result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + " "+chinaUmsResponse.getBillStatus() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
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
			ChinaUmsRequest chinaUmsRequest = this.toChinaUmsRequest(CHINAUMSV2.Refund(),request);
			chinaUmsRequest.setRefundAmount(chinaUmsRequest.getTotalAmount());
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("refund POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsResponse chinaUmsResponse = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsResponse.class).getBody();
			logger.info("refund result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);
		} catch (RestClientException e) {
			logger.info("refund failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	private ChinaUmsRequest toChinaUmsRequest(String method, PaymentRequest request) {
		ChinaUmsRequest chinaUmsRequest = new ChinaUmsRequest();
		chinaUmsRequest.setMsgSrc(appName);
		String[] strArrays = request.getExtStoreId().split(",");
		if(strArrays.length==1) {
			chinaUmsRequest.setMid(request.getExtStoreId());
			chinaUmsRequest.setTid(tId);
		} else {
			chinaUmsRequest.setMid(strArrays[0]);
			chinaUmsRequest.setTid(strArrays[1]);
		}
		chinaUmsRequest.setInstMid(instMid);
		chinaUmsRequest.setBillNo(request.getGatewayOrderNo());
		chinaUmsRequest.setRequestTimeStamp(IDGenerator.formatTime());
		if(CHINAUMSV2.UnifiedOrder().equals(method)) {
			chinaUmsRequest.setBillDate(IDGenerator.formatDate());
			chinaUmsRequest.setExpireTime(this.getExpireTime());
		} else {
			chinaUmsRequest.setBillDate(IDGenerator.formatDate(IDGenerator.TimePattern14, request.getOrderTime()));
		}
		chinaUmsRequest.setBillDesc(request.getSubject());
		if(request.getTotalFee()!=null) {
			chinaUmsRequest.setTotalAmount(String.valueOf((int)(request.getTotalFee()*100)));
		}
		chinaUmsRequest.setGoods(request.getGoods());
		chinaUmsRequest.setNotifyUrl(request.getNotifyUrl());
		chinaUmsRequest.setReturnUrl(request.getReturnUrl());
		chinaUmsRequest.setMsgType(method);
//		chinaUmsRequest.setSystemId(appId);
		return chinaUmsRequest;
	}
	
	private List<KeyValuePair> getKeyPairs(ChinaUmsRequest paymentRequest) {
		if (paymentRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(paymentRequest.getMsgSrc())) {
			keyPairs.add(new KeyValuePair("msgSrc", paymentRequest.getMsgSrc()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getRequestTimeStamp())) {
			keyPairs.add(new KeyValuePair("requestTimeStamp", paymentRequest
					.getRequestTimeStamp()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMid())) {
			keyPairs.add(new KeyValuePair("mid", paymentRequest.getMid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTid())) {
			keyPairs.add(new KeyValuePair("tid", paymentRequest.getTid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBillNo())) {
			keyPairs.add(new KeyValuePair("billNo", paymentRequest.getBillNo()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBillDate())) {
			keyPairs.add(new KeyValuePair("billDate", paymentRequest.getBillDate()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBillDesc())) {
			keyPairs.add(new KeyValuePair("billDesc", paymentRequest.getBillDesc()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTotalAmount())) {
			keyPairs.add(new KeyValuePair("totalAmount", paymentRequest.getTotalAmount()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getSign())) {
			keyPairs.add(new KeyValuePair("sign", paymentRequest.getSign()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("notifyUrl", paymentRequest.getNotifyUrl()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getInstMid())) {
			keyPairs.add(new KeyValuePair("instMid", paymentRequest.getInstMid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMsgType())) {
			keyPairs.add(new KeyValuePair("msgType", paymentRequest.getMsgType()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getRefundAmount())) {
			keyPairs.add(new KeyValuePair("refundAmount", paymentRequest.getRefundAmount()));
		}
		if(paymentRequest.getGoods()!=null) {
			keyPairs.add(new KeyValuePair("goods", JsonUtils.toJson(paymentRequest.getGoods())));
		}
		if(StringUtils.isNotBlank(paymentRequest.getReturnUrl())) {
			keyPairs.add(new KeyValuePair("returnUrl", paymentRequest.getReturnUrl()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getExpireTime())) {
			keyPairs.add(new KeyValuePair("expireTime", paymentRequest.getExpireTime()));
		}
		
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});
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

	private PaymentResponse toPaymentResponse(ChinaUmsRequest chinaUmsRequest, ChinaUmsResponse chinaUmsResponse) {
		if (chinaUmsResponse == null || !ChinaUmsResponse.SUCCESS.equals(chinaUmsResponse.getErrCode())
				|| StringUtils.isBlank(chinaUmsResponse.getBillQRCode())) {
			String code = chinaUmsResponse == null ? NO_RESPONSE : chinaUmsResponse.getErrCode();
			String msg = chinaUmsResponse == null ? "No response" : chinaUmsResponse.getErrMsg();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(chinaUmsResponse.getBillQRCode());
		bill.setOrderNo(chinaUmsRequest.getBillNo());
		bill.setGatewayOrderNo(chinaUmsResponse.getQrCodeId());
		bill.setOrderStatus(toOrderStatus(chinaUmsResponse.getBillStatus()));
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

	private String getExpireTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 30);
		Date date = c.getTime();
		return TimeUtils.formatTime(date, TimeUtils.TimePatternTime);
	}

}
