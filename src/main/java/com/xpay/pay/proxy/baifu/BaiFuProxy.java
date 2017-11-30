package com.xpay.pay.proxy.baifu;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.BAIFU;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.xpay.pay.util.HttpClient;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.XmlUtils;

@Component
public class BaiFuProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.BaiFuConfig;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	public static final String appSecret = config.getProperty("provider.app.secret");
	private static final String pathPay = config.getProperty("provider.resource.pay"); 
	private static final String pathQuery = config.getProperty("provider.resource.query"); 

	@Autowired
	private RestTemplate baifuProxy;
	
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		BaiFuRequest baifuRequest = this.toBaiFuRequest(request);
		baifuRequest.setTrade_type(BAIFU.UnifiedOrder());
		String sign = signature(baifuRequest, appSecret);
		baifuRequest.setSign(sign);
		List<KeyValuePair> keyPairs = this.getKeyPairs(baifuRequest);
		String xmlBody = XmlUtils.toXml(keyPairs);
		String url = baseEndpoint + pathPay;  	 
		String result = HttpClient.doPost(url, xmlBody, DEFAULT_TIMEOUT);
		PaymentResponse paymentResponse = null;
		try {
			paymentResponse = toPaymentResponse(result);
		} catch (Exception e) {
			logger.error("ToPaymentResponse error", e);
		}
		return paymentResponse;
	}

	@Override
	public PaymentResponse query(PaymentRequest request) {
		BaiFuRequest baifuRequest = this.toBaiFuRequest(request);
		String sign = signature(baifuRequest, appSecret);
		baifuRequest.setSign(sign);
		List<KeyValuePair> keyPairs = this.getKeyPairs(baifuRequest);
		String xmlBody = XmlUtils.toXml(keyPairs);
		 	 
		String url = baseEndpoint + pathQuery;  	 
		String result = HttpClient.doPost(url, xmlBody, DEFAULT_TIMEOUT);
		PaymentResponse paymentResponse = null;
		try {
			paymentResponse = toPaymentResponse(result);
		} catch (Exception e) {
			logger.error("ToPaymentResponse error", e);
		}
		return paymentResponse;
	}

	@Override
	public PaymentResponse refund(PaymentRequest request) {
		throw new java.lang.UnsupportedOperationException("Refund is not supported");
	}

	private BaiFuRequest toBaiFuRequest(PaymentRequest paymentRequest) {
		BaiFuRequest request = new BaiFuRequest();
		request.setMch_id(paymentRequest.getExtStoreId());
		request.setOut_trade_no(paymentRequest.getOrderNo());
		request.setBody(paymentRequest.getSubject());
		request.setDetail(paymentRequest.getAttach());
		if(paymentRequest.getTotalFee()!=null) {
			request.setTotal_fee(String.valueOf((int) (paymentRequest.getTotalFee() * 100)));
		}
		request.setSpbill_create_ip(paymentRequest.getServerIp());
		request.setNotify_url(paymentRequest.getNotifyUrl());
		request.setNonce_str(IDGenerator.buildKey(10));
		return request;
	}
	
	private PaymentResponse toPaymentResponse(String result) throws Exception {
		Map<String, String> params = XmlUtils.fromXml(result.getBytes(), "utf-8");
		boolean checkSign = CryptoUtils.checkSignature(params, appSecret, "sign", "key");
		
		if(!checkSign || !PaymentResponse.SUCCESS.equals(params.get("status")) || StringUtils.isNotBlank(params.get("err_msg"))) {
			String code = params.get("status");
			String msg = params.get("err_msg");
			msg = StringUtils.isBlank(msg)? params.get("message"): msg;
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setTokenId(params.get("token_id"));
		bill.setCodeUrl(params.get("code_url"));
		bill.setOrderNo(params.get("out_trade_no"));
		bill.setGatewayOrderNo(params.get("transaction_id"));
		String tradeStatus = params.get("trade_state");
		OrderStatus orderStatus = StringUtils.isBlank(tradeStatus)?OrderStatus.NOTPAY:OrderStatus.valueOf(tradeStatus);
		bill.setOrderStatus(orderStatus);
		response.setBill(bill);
		return response;
	}
	
	private String signature(BaiFuRequest request, String appSecret) {
		List<KeyValuePair> keyPairs = getKeyPairs(request);

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		for (KeyValuePair pair : keyPairs) {
			builder.queryParam(pair.getKey(), pair.getValue());
		}
		builder.queryParam("key", appSecret);
		String params = builder.build().toString().substring(1);
		logger.debug("sorted params: " + params);
		String md5 = CryptoUtils.md5(params);
		logger.debug("md5 upper: " + md5.toUpperCase());
		return md5 == null ? null : md5.toUpperCase();
	}

	private List<KeyValuePair> getKeyPairs(BaiFuRequest paymentRequest) {
		if (paymentRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(paymentRequest.getMch_id())) {
			keyPairs.add(new KeyValuePair("mch_id", paymentRequest.getMch_id()));
		}
		keyPairs.add(new KeyValuePair("nonce_str", paymentRequest.getNonce_str()));
		if (StringUtils.isNotBlank(paymentRequest.getBody())) {
			keyPairs.add(new KeyValuePair("body", paymentRequest.getBody()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getDetail())) {
			keyPairs.add(new KeyValuePair("detail", paymentRequest.getDetail()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getOut_trade_no())) {
			keyPairs.add(new KeyValuePair("out_trade_no", paymentRequest
					.getOut_trade_no()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTotal_fee())) {
			keyPairs.add(new KeyValuePair("total_fee", paymentRequest.getTotal_fee()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getSpbill_create_ip())) {
			keyPairs.add(new KeyValuePair("spbill_create_ip", paymentRequest.getSpbill_create_ip()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotify_url())) {
			keyPairs.add(new KeyValuePair("notify_url", paymentRequest
					.getNotify_url()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getReturn_url())) {
			keyPairs.add(new KeyValuePair("return_url", paymentRequest.getReturn_url()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTrade_type())) {
			keyPairs.add(new KeyValuePair("trade_type", paymentRequest.getTrade_type()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getSign())) {
			keyPairs.add(new KeyValuePair("sign", paymentRequest.getSign()));
		}
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});
		return keyPairs;
	}
}
