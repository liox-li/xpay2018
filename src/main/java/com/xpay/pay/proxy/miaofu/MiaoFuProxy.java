package com.xpay.pay.proxy.miaofu;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.MIAOFU;

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
import com.xpay.pay.proxy.miaofu.MiaoFuResponse.TradeBean;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.CryptoUtils;

@Component
public class MiaoFuProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.MiaoFuConfig;
	private static final String baseEndpoint = config
			.getProperty("provider.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	private static final String appSecret = config
			.getProperty("provider.app.secret");
	private static final String jsUrl = config.getProperty("provider.jsuri");
	
	@Autowired
	RestTemplate miaofuProxy;

	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		request.setPayChannel(null);
		String url = DEFAULT_JSAPI_URL + request.getOrderNo();
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(url);
		bill.setOrderStatus(OrderStatus.NOTPAY);
		response.setBill(bill);
		return response;
	}
	
	public String getJsUrl(PaymentRequest request) {
//		request.setPayChannel(null);
//		String url = buildUrl(MIAOFU.UnifiedOrder(), request);
//		return url;

		String url = jsUrl.replace("%storeId%", request.getExtStoreId())
			.replace("%amount%", request.getTotalFee())
			.replace("%subject%", CommonUtils.urlEncode(request.getSubject()))
			.replace("%redirectUrl%", request.getNotifyUrl());
		logger.info("unified order GET: " + url);
		return url;
	}
	
	@Override
	public PaymentResponse query(PaymentRequest request) {
		request.setOrderNo(null);
		request.setTradeNoType(TradeNoType.MiaoFu);
		String url = buildUrl(MIAOFU.Query(), request);
		logger.info("query POST: " + url);
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			MiaoFuResponse miaoFuResponse = miaofuProxy.exchange(url,
					HttpMethod.POST, httpEntity, MiaoFuResponse.class)
					.getBody();
			response = toPaymentResponse(miaoFuResponse);
			logger.info("query result: " + miaoFuResponse.getCode() + " "
					+ miaoFuResponse.getMsg() + " "
					+ response.getBill().getOrderStatus() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
		} catch (RestClientException e) {
			logger.info("query failed, took "
					+ (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	@Override
	public PaymentResponse refund(PaymentRequest request) {
		request.setTotalFee(null);
		request.setOrderNo(null);
		request.setTradeNoType(TradeNoType.MiaoFu);
		String url = buildUrl(MIAOFU.Refund(), request);
		logger.info("refund POST: " + url);
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			MiaoFuResponse miaoFuResponse = miaofuProxy.exchange(url,
					HttpMethod.POST, httpEntity, MiaoFuResponse.class)
					.getBody();
			logger.info("refund result: " + miaoFuResponse.getCode() + " "
					+ miaoFuResponse.getMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(miaoFuResponse);
		} catch (RestClientException e) {
			logger.info("refund failed, took "
					+ (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	private String buildUrl(String method, PaymentRequest request) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
				baseEndpoint).path(
				"/pay/" + method);
		List<KeyValuePair> keyPairs = getKeyPairs(request);
		for (KeyValuePair pair : keyPairs) {
			builder.queryParam(pair.getKey(), pair.getValue());
		}
		String sign = signature(keyPairs, appSecret);
		builder.queryParam("sign", sign);
		String url = builder.build().toString();
		return url;
	}

	private List<KeyValuePair> getKeyPairs(PaymentRequest request) {
		if (request == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(request.getExtStoreId())) {
			keyPairs.add(new KeyValuePair("busi_code", request.getExtStoreId()));
		}
		if (StringUtils.isNotBlank(request.getDeviceId())) {
			keyPairs.add(new KeyValuePair("dev_id", request.getDeviceId()));
		}
		if (request.getPayChannel() != null) {
			keyPairs.add(new KeyValuePair("pay_channel", request
					.getPayChannel().getId()));
		}
		if (StringUtils.isNotBlank(request.getTotalFee())) {
			keyPairs.add(new KeyValuePair("amount", request.getTotalFee()));
		}
		if (StringUtils.isNotBlank(request.getAttach())) {
			keyPairs.add(new KeyValuePair("raw_data", request.getAttach()));
		}
		if (StringUtils.isNotBlank(request.getOrderNo())) {
			keyPairs.add(new KeyValuePair("down_trade_no", request.getOrderNo()));
		}
		if (StringUtils.isNotBlank(request.getGatewayOrderNo())) {
			keyPairs.add(new KeyValuePair("trade_no", request.getGatewayOrderNo()));
		}
		if (request.getTradeNoType() != null) {
			keyPairs.add(new KeyValuePair("trade_no_type", String
					.valueOf(request.getTradeNoType().getId())));
		}
		if (StringUtils.isNotBlank(request.getSubject())) {
			keyPairs.add(new KeyValuePair("subject", request.getSubject()));
		}
		if (StringUtils.isNotBlank(request.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("redirect_url", request.getNotifyUrl()));
		}
		keyPairs.add(new KeyValuePair("app_id", appId));
		keyPairs.add(new KeyValuePair("timestamp", String.valueOf(System
				.currentTimeMillis() / 1000)));
		keyPairs.add(new KeyValuePair("version", "v3"));

		return keyPairs;
	}

	private String signature(List<KeyValuePair> keyPairs, String appSecret) {
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		for (KeyValuePair pair : keyPairs) {
			builder.queryParam(pair.getKey(), pair.getValue());
		}
		builder.queryParam("APP_SECRET", appSecret);
		String params = builder.build().toString().substring(1);
		logger.debug("sorted params: " + params);
		String md5 = CryptoUtils.md5(params);
		logger.debug("md5 upper: " + md5.toUpperCase());
		return md5 == null ? null : md5.toUpperCase();
	}

	private PaymentResponse toPaymentResponse(MiaoFuResponse miaoFuResponse) {
		if (miaoFuResponse == null
				|| !MiaoFuResponse.SUCCESS.equals(miaoFuResponse.getCode())
				|| miaoFuResponse.getData() == null) {
			String code = miaoFuResponse == null ? NO_RESPONSE : miaoFuResponse
					.getCode();
			String msg = miaoFuResponse == null ? "No response"
					: miaoFuResponse.getMsg();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		TradeBean trade = miaoFuResponse.getData();
		Bill bill = new Bill();
		bill.setCodeUrl(trade.getCode_url());
		bill.setPrepayId(trade.getPrepay_id());
		bill.setOrderNo(trade.getDown_trade_no());
		bill.setGatewayOrderNo(trade.getTrade_no());
		bill.setOrderStatus(trade.getTrade_status());
		response.setBill(bill);
		return response;
	}
}
