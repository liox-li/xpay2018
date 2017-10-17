package com.xpay.pay.proxy.upay;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.CHINAUMSH5;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.stereotype.Component;

import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.chinaumsh5.ChinaUmsH5Request;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.CryptoUtils;

@Component
public class UPayProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	
//	private static final AppConfig config = AppConfig.UPayConfig;
//	private static final String baseEndpoint = config.getProperty("provider.endpoint");
//	private static final String jsPayEndpoint = config.getProperty("provider.jspay.endpoint");
//	private static final String appId = config.getProperty("provider.app.id");
//	private static final String appSecret = config.getProperty("provider.app.secret");
	private static final String DEFAULT_JSAPI_URL = AppConfig.XPayConfig.getProperty("jsapi.endpoint");
	
	public PaymentResponse unifiedOrder(PaymentRequest request) {
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
//	ChinaUmsH5Request chinaUmsH5Request = this.toUPayRequest(CHINAUMSH5.UnifiedOrder(), request);
//		List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsH5Request);
//		String sign = CryptoUtils.signQueryParams(keyPairs, null, appSecret);
//		String queryParams = CommonUtils.buildQueryParams(keyPairs, "sign", sign, "orderDesc");
//		String jsUrl = jsPayEndpoint + queryParams;
		String jsUrl = "";
		logger.info("Redirect to: " + jsUrl);
		return jsUrl;
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
