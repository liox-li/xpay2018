package com.xpay.pay.proxy.swiftpass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.XmlUtils;

@Component
public class SwiftpassProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.SwirfPassConfig;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	public static final String appSecret = config.getProperty("provider.app.secret");

	@Autowired
	private RestTemplate swiftPassProxy;
	
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		long l = System.currentTimeMillis();
		try {
			SwiftpassRequest swiftRequest = toSwiftpassRequest(request);
			String sign = signature(Method.UnifiedOrder, swiftRequest, appSecret);
			swiftRequest.setSign(sign);
			List<KeyValuePair> keyPairs = this.getKeyPairs(Method.UnifiedOrder,
					swiftRequest);
			String xml = XmlUtils.toXml(keyPairs);
			StringEntity entityParams = new StringEntity(xml, "utf-8");
			
			HttpPost httpPost = new HttpPost(baseEndpoint);
			httpPost.setEntity(entityParams);
			logger.info("nativePay POST: "+baseEndpoint+", content: " + xml);
			
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			
			if(response != null && response.getEntity() != null){
				 PaymentResponse paymentResponse = toPaymentResponse(response.getEntity());
				 logger.info("nativePay result: " + paymentResponse.getCode()+" "+ paymentResponse.getMsg() + ", took "
							+ (System.currentTimeMillis() - l) + "ms");
				 return paymentResponse;
			}
		} catch (Exception e) {
			throw new GatewayException(ApplicationConstants.CODE_ERROR_JSON,e.getMessage());
		} finally {
			if(client != null) {
				try {
					client.close();
				} catch(Exception e) {
					
				}
			}
		}
		return null;
	}

	@Override
	public PaymentResponse nativePay(PaymentRequest request) {
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		long l = System.currentTimeMillis();
		try {
			SwiftpassRequest swiftRequest = toSwiftpassRequest(request);
			String sign = signature(Method.NativePay, swiftRequest, appSecret);
			swiftRequest.setSign(sign);
			List<KeyValuePair> keyPairs = this.getKeyPairs(Method.NativePay,
					swiftRequest);
			String xml = XmlUtils.toXml(keyPairs);
			StringEntity entityParams = new StringEntity(xml, "utf-8");
			
			HttpPost httpPost = new HttpPost(baseEndpoint);
			httpPost.setEntity(entityParams);
			logger.info("nativePay POST: "+baseEndpoint+", content: " + xml);
			
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			
			if(response != null && response.getEntity() != null){
				 PaymentResponse paymentResponse = toPaymentResponse(response.getEntity());
				 logger.info("nativePay result: " + paymentResponse.getCode()+" "+ paymentResponse.getMsg() + ", took "
							+ (System.currentTimeMillis() - l) + "ms");
				 return paymentResponse;
			}
		} catch (Exception e) {
			throw new GatewayException(ApplicationConstants.CODE_ERROR_JSON,e.getMessage());
		} finally {
			if(client != null) {
				try {
					client.close();
				} catch(Exception e) {
					
				}
			}
		}
		return null;
	}

	
	@Override
	public PaymentResponse query(PaymentRequest request) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public PaymentResponse refund(PaymentRequest request) {
		throw new java.lang.UnsupportedOperationException();
	}

	private SwiftpassRequest toSwiftpassRequest(PaymentRequest paymentRequest) {
		SwiftpassRequest request = new SwiftpassRequest();
		request.setMch_id(paymentRequest.getExtStoreId());
		request.setOut_trade_no(paymentRequest.getOrderNo());
		request.setDevice_info(paymentRequest.getDeviceId());
		request.setBody(paymentRequest.getSubject());
		request.setAttach(paymentRequest.getAttach());
		request.setTotal_fee((int) (paymentRequest.getTotalFeeAsFloat() * 100));
		request.setMch_create_ip(paymentRequest.getServerIp());
		request.setNotify_url(paymentRequest.getNotifyUrl());
		request.setNonce_str(IDGenerator.buildKey(10));
		return request;
	}

	private PaymentResponse toPaymentResponse(HttpEntity httpEntity) throws Exception {
		byte[] bytes = EntityUtils.toByteArray(httpEntity);
		Map<String, String> params = XmlUtils.fromXml(bytes, "utf-8");
		boolean checkSign = CryptoUtils.checkSignature(params, appSecret, "sign", "key");
		
		if(!checkSign || !PaymentResponse.SUCCESS.equals(params.get("status")) || !PaymentResponse.SUCCESS.equals(params.get("result_code"))) {
			String code = params.get("status");
			String msg = params.get("err_msg");
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setTokenId(params.get("token_id"));
		bill.setCodeUrl(params.get("code_url"));
		bill.setOrderNo(params.get("out_trade_no"));
		bill.setGatewayOrderNo(params.get("transaction_id"));
		bill.setOrderStatus(OrderStatus.NOTPAY);
		response.setBill(bill);
		return response;
	}

	private String signature(Method method, SwiftpassRequest request,
			String appSecret) {
		List<KeyValuePair> keyPairs = getKeyPairs(method, request);

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
	
	private List<KeyValuePair> getKeyPairs(Method method,
			SwiftpassRequest paymentRequest) {
		if (paymentRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(paymentRequest.getMch_id())) {
			keyPairs.add(new KeyValuePair("mch_id", paymentRequest.getMch_id()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getOut_trade_no())) {
			keyPairs.add(new KeyValuePair("out_trade_no", paymentRequest
					.getOut_trade_no()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getDevice_info())) {
			keyPairs.add(new KeyValuePair("device_info", paymentRequest
					.getDevice_info()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBody())) {
			keyPairs.add(new KeyValuePair("body", paymentRequest.getBody()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getAttach())) {
			keyPairs.add(new KeyValuePair("attach", paymentRequest.getAttach()));
		}
		keyPairs.add(new KeyValuePair("total_fee", String
				.valueOf(paymentRequest.getTotal_fee())));
		if (StringUtils.isNotBlank(paymentRequest.getMch_create_ip())) {
			keyPairs.add(new KeyValuePair("mch_create_ip", paymentRequest
					.getMch_create_ip()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotify_url())) {
			keyPairs.add(new KeyValuePair("notify_url", paymentRequest
					.getNotify_url()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getSign())) {
			keyPairs.add(new KeyValuePair("sign", paymentRequest.getSign()));
		}
		keyPairs.add(new KeyValuePair("service", method.getService()));
		keyPairs.add(new KeyValuePair("appid", appId));
		keyPairs.add(new KeyValuePair("nonce_str", paymentRequest.getNonce_str()));
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});
		return keyPairs;
	}

	@Override
	public PaymentResponse microPay(PaymentRequest request) {
		throw new java.lang.UnsupportedOperationException();
	}

}
