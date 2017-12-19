package com.xpay.pay.proxy.txf;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.JsonUtils;

@Component
public class TxfProxy implements IPaymentProxy {
	
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.TxfConfig;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String orderUri = config.getProperty("provider.order.uri");
	private static final String queryUri = config.getProperty("provider.query.uri");
	private static final String agentOrgNo = config.getProperty("provider.app.id");
	private static final String secret = config.getProperty("provider.app.secret");
	@Autowired
	RestTemplate txfProxy;
	
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		String url = baseEndpoint + orderUri;
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			TxfRequest txfRequest = this.toTxfRequest(request);
			String json = JsonUtils.toJson(txfRequest);
			String base64EncodedJson = CryptoUtils.base64Encode(json);
			String toBeMd5 = base64EncodedJson + secret;
			String sign = CryptoUtils.md5(toBeMd5);
			url = url.replace("$params$", base64EncodedJson);
			url = url.replace("%sign%", sign);
			logger.info("unifiedOrder POST: " + url+", body "+json);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			TxfResponse txfResponse = txfProxy.exchange(url, HttpMethod.POST, httpEntity, TxfResponse.class).getBody();
			logger.info("unifiedOrder result: " + txfResponse.getRes() + " "+txfResponse.getMsg()  + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(request, txfResponse);
		} catch (RestClientException e) {
			logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	private TxfRequest toTxfRequest(PaymentRequest request) {
		TxfRequest txfRequest = new TxfRequest();
		txfRequest.setAgentOrgno(agentOrgNo);
		txfRequest.setOrderNo(request.getOrderNo());
		txfRequest.setMoney(String.valueOf((int)(request.getTotalFee()*100)));
		txfRequest.setCurType("1");
		txfRequest.setReturnUrl(request.getReturnUrl());
		txfRequest.setNotifyUrl(request.getNotifyUrl());
		txfRequest.setMemo(request.getSubject());
		txfRequest.setAttach(request.getAttach());
		txfRequest.setCardType(request.getCardType());
		txfRequest.setBankSegment(request.getBankId());
		txfRequest.setUserType("1");
		txfRequest.setChannel("2");
		return txfRequest;
	}

	private PaymentResponse toPaymentResponse(PaymentRequest request, TxfResponse txfResponse) {
		if (txfResponse == null || !TxfResponse.SUCCESS.equals(txfResponse.getRes())
				|| StringUtils.isBlank(txfResponse.getUrl())) {
			String code = txfResponse == null ? NO_RESPONSE : txfResponse.getRes();
			String msg = txfResponse == null ? "No response" : txfResponse.getMsg();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setOrderNo(request.getOrderNo());
		bill.setCodeUrl(txfResponse.getUrl());
		response.setBill(bill);
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

}
