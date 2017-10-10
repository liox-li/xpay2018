package com.xpay.pay.proxy.chinaumsh5;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.stereotype.Component;

import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.JsonUtils;

@Component
public class ChinaUmsH5Proxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.ChinaUmsH5Config;
	private static final String baseEndpoint = config.getProperty("provider.endpoint");
	private static final String jsPayEndpoint = config.getProperty("provider.jspay.endpoint");
	private static final String appId = config.getProperty("provider.app.id");
	private static final String appSecret = config.getProperty("provider.app.secret");
	private static final String appName = config.getProperty("provider.app.name");
	private static final String tId = config.getProperty("provider.tid");
	private static final String instMid = config.getProperty("provider.inst.mid");
	private static final String DEFAULT_JSAPI_URL = AppConfig.XPayConfig.getProperty("jsapi.endpoint");

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
		ChinaUmsH5Request chinaUmsH5Request = this.toChinaUmsH5Request(request);
		List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsH5Request);
		String queryParams = CryptoUtils.signParams(keyPairs, "sign", null, appSecret);
		return jsPayEndpoint + "?" + queryParams;
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
		keyPairs.add(new KeyValuePair("totalAmount", String.valueOf(paymentRequest.getTotalAmount())));
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("notifyUrl", paymentRequest.getNotifyUrl()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getReturnUrl())) {
			keyPairs.add(new KeyValuePair("returnUrl", paymentRequest.getReturnUrl()));
		}
		return keyPairs;
	}
	
	
	private ChinaUmsH5Request toChinaUmsH5Request(PaymentRequest request) {
		ChinaUmsH5Request chinaUmsH5Request = new ChinaUmsH5Request();
		chinaUmsH5Request.setMsgSrc(appName);
		chinaUmsH5Request.setMsgType("WXPay.jsPay");
		chinaUmsH5Request.setRequestTimestamp(IDGenerator.formatTime());
		chinaUmsH5Request.setMerOrderId(request.getGatewayOrderNo());
		chinaUmsH5Request.setMid(request.getExtStoreId());
		chinaUmsH5Request.setTid(tId);
		chinaUmsH5Request.setInstMid(instMid);
		chinaUmsH5Request.setGoods(request.getGoods());
		chinaUmsH5Request.setOrderDesc(request.getSubject());
		if(StringUtils.isNotBlank(request.getTotalFee())) {
			chinaUmsH5Request.setTotalAmount((int)(request.getTotalFeeAsFloat()*100));
		}
		chinaUmsH5Request.setNotifyUrl(request.getNotifyUrl());
		chinaUmsH5Request.setReturnUrl(request.getReturnUrl());
		return chinaUmsH5Request;
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
