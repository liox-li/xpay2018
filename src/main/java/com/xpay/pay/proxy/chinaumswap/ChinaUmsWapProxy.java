package com.xpay.pay.proxy.chinaumswap;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.CHINAUMSWAP;

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

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.chinaums.ChinaUmsResponse;
import com.xpay.pay.proxy.chinaumsh5.ChinaUmsH5Request;
import com.xpay.pay.proxy.chinaumsh5.ChinaUmsH5Response;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.JsonUtils;

@Component
public class ChinaUmsWapProxy implements IPaymentProxy {
	
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.ChinaUmsH5V2Config;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String jsPayEndpoint = config.getProperty("provider.jspay.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	private static final String appSecret = config.getProperty("provider.app.secret");
	private static final String appName = config.getProperty("provider.app.name");
	private static final String tId = config.getProperty("provider.tid");
	private static final String instMid = config.getProperty("provider.inst.mid");

	@Autowired
	RestTemplate chinaUmsProxy;
	
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		String url = DEFAULT_JSAPI_URL + request.getOrderNo();
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(url);
		bill.setOrderStatus(OrderStatus.NOTPAY);
		bill.setGatewayOrderNo(IDGenerator.buildQrCode(appId));
		response.setBill(bill);
		return response;
	}

	public String getJsUrl(PaymentRequest request) {
		ChinaUmsH5Request chinaUmsH5Request = this.toChinaUmsH5Request(CHINAUMSWAP.UnifiedOrder(), request);
		List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsH5Request);
		String sign = CryptoUtils.signQueryParams(keyPairs, null, appSecret);
		String queryParams = CommonUtils.buildQueryParams(keyPairs, "sign", sign, "orderDesc");
		String jsUrl = jsPayEndpoint + queryParams;
		logger.info("Redirect to: " + jsUrl);
		return jsUrl;
	}
	
	@Override
	public PaymentResponse query(PaymentRequest request) {
		String url = baseEndpoint;
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			ChinaUmsH5Request chinaUmsH5Request = this.toChinaUmsH5Request(CHINAUMSWAP.Query(),request);
			
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsH5Request);
			String sign = CryptoUtils.signQueryParams(keyPairs, null, appSecret);
			chinaUmsH5Request.setSign(sign);
			logger.info("query POST: " + url+", body "+JsonUtils.toJson(chinaUmsH5Request));
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsH5Request, headers);
			ChinaUmsH5Response chinaUmsH5Response = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsH5Response.class).getBody();
			logger.info("query result: " + chinaUmsH5Response.getErrCode() + " "+chinaUmsH5Response.getErrMsg() + " "+chinaUmsH5Response.getStatus() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(request, chinaUmsH5Response);
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
			ChinaUmsH5Request chinaUmsH5Request = this.toChinaUmsH5Request(CHINAUMSWAP.Refund(),request);
			chinaUmsH5Request.setRefundAmount(chinaUmsH5Request.getTotalAmount());
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsH5Request);
			String sign = CryptoUtils.signQueryParams(keyPairs, null, appSecret);
			chinaUmsH5Request.setSign(sign);
			logger.info("refund POST: " + url+", body "+JsonUtils.toJson(chinaUmsH5Request));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsH5Request, headers);
			ChinaUmsH5Response chinaUmsH5Response = chinaUmsProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsH5Response.class).getBody();
			logger.info("refund result: " + chinaUmsH5Response.getErrCode() + " "+JsonUtils.toJson(chinaUmsH5Response) + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(request, chinaUmsH5Response);
		} catch (RestClientException e) {
			logger.info("refund failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	
	private List<KeyValuePair> getKeyPairs(ChinaUmsH5Request paymentRequest) {
		if (paymentRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(paymentRequest.getMsgSrc())) {
			keyPairs.add(new KeyValuePair("msgSrc", paymentRequest.getMsgSrc()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMsgType())) {
			keyPairs.add(new KeyValuePair("msgType", paymentRequest.getMsgType()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getRequestTimestamp())) {
			keyPairs.add(new KeyValuePair("requestTimestamp", paymentRequest.getRequestTimestamp()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMerOrderId())) {
			keyPairs.add(new KeyValuePair("merOrderId", paymentRequest.getMerOrderId()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMid())) {
			keyPairs.add(new KeyValuePair("mid", paymentRequest.getMid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTid())) {
			keyPairs.add(new KeyValuePair("tid", paymentRequest.getTid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getInstMid())) {
			keyPairs.add(new KeyValuePair("instMid", paymentRequest.getInstMid()));
		}
		if(paymentRequest.getGoods()!=null) {
			keyPairs.add(new KeyValuePair("goods", JsonUtils.toJson(paymentRequest.getGoods())));
		}
		if (StringUtils.isNotBlank(paymentRequest.getOrderDesc())) {
			keyPairs.add(new KeyValuePair("orderDesc", paymentRequest.getOrderDesc()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getTotalAmount())) {
			keyPairs.add(new KeyValuePair("totalAmount", paymentRequest.getTotalAmount()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getRefundAmount())) {
			keyPairs.add(new KeyValuePair("refundAmount", paymentRequest.getRefundAmount()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("notifyUrl", paymentRequest.getNotifyUrl()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getReturnUrl())) {
			keyPairs.add(new KeyValuePair("returnUrl", paymentRequest.getReturnUrl()));
		}
		return keyPairs;
	}
	
	
	private ChinaUmsH5Request toChinaUmsH5Request(String method, PaymentRequest request) {
		ChinaUmsH5Request chinaUmsH5Request = new ChinaUmsH5Request();
		chinaUmsH5Request.setMsgSrc(appName);
		chinaUmsH5Request.setMsgType(method);
		chinaUmsH5Request.setRequestTimestamp(IDGenerator.formatTime());
		chinaUmsH5Request.setMerOrderId(request.getGatewayOrderNo());
		chinaUmsH5Request.setMid(request.getExtStoreId());
		chinaUmsH5Request.setTid(tId);
		chinaUmsH5Request.setInstMid(instMid);
		chinaUmsH5Request.setGoods(request.getGoods());
		chinaUmsH5Request.setOrderDesc(request.getSubject());
		if(StringUtils.isNotBlank(request.getTotalFee())) {
			chinaUmsH5Request.setTotalAmount(String.valueOf((int)(request.getTotalFeeAsFloat()*100)));
		}
		chinaUmsH5Request.setNotifyUrl(request.getNotifyUrl());
		chinaUmsH5Request.setReturnUrl(DEFAULT_JSAPI_URL + PAYED + "/" + request.getOrderNo());
		return chinaUmsH5Request;
	}
	
	private PaymentResponse toPaymentResponse(PaymentRequest paymentRequest, ChinaUmsH5Response chinaUmsResponse) {
		if (chinaUmsResponse == null || !ChinaUmsResponse.SUCCESS.equals(chinaUmsResponse.getErrCode())
				|| StringUtils.isBlank(chinaUmsResponse.getTargetOrderId())) {
			String code = chinaUmsResponse == null ? NO_RESPONSE : chinaUmsResponse.getErrCode();
			String msg = chinaUmsResponse == null ? "No response" : chinaUmsResponse.getErrMsg();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setOrderNo(paymentRequest.getOrderNo());
		bill.setGatewayOrderNo(paymentRequest.getGatewayOrderNo());
		bill.setTargetOrderNo(chinaUmsResponse.getTargetOrderId());
		bill.setOrderStatus(toOrderStatus(chinaUmsResponse.getStatus()));
		response.setBill(bill);
		return response;
	}
	
	public static OrderStatus toOrderStatus(String billStatus) {
		if("PAID".equals(billStatus) || "TRADE_SUCCESS".equals(billStatus)) {
			return OrderStatus.SUCCESS;
		} else if("UNPAID".equals(billStatus)) {
			return OrderStatus.NOTPAY;
		}else if("REFUND".equals(billStatus) || "TRADE_REFUND".equals(billStatus)) {
			return OrderStatus.REFUND;
		}else if("CLOSED".equals(billStatus)) {
			return OrderStatus.CLOSED;
		} else {
			return OrderStatus.NOTPAY;
		}
	}
}
