package com.xpay.pay.proxy.juzhen;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.JsonUtils;

@Component
public class JuZhenProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final AppConfig config = AppConfig.JuZhenConfig;
	private static final String baseEndpoint = config
			.getProperty("provider.endpoint");
	public static final String publicKeyPath = config
			.getProperty("provider.public.key.path");
	public static final String privateKeyPath = config
			.getProperty("provider.private.key.path");
	public static final String keystorePass = "!@34%^";
	public static final String pfxPass = "123pay";

	public static final String encoding = "UTF-8";

	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		String extOrderNo = IDGenerator.buildShortOrderNo();
		String msgInfo = IDGenerator.formatNow(IDGenerator.TimePattern14) + "|"
				+ formatAmount(request.getTotalFeeAsFloat()) + "|"
				+ request.getNotifyUrl() + "|" + request.getSubject();
		logger.info("Order msgInfo: " + msgInfo);
		
		String signature = "";
		try {
			signature = JuSignature.sign(msgInfo, encoding, publicKeyPath,
					pfxPass);
			logger.info("sign: " + signature);

			Map<String, String> map = new HashMap<String, String>();
			map.put("merId", request.getExtStoreId());
			map.put("tradeCode", PaymentGateway.JUZHEN.UnifiedOrder());
			map.put("orderId", extOrderNo);
			map.put("msg", msgInfo);
			map.put("signature", signature);

			String response = "";
			long l = System.currentTimeMillis();
			response = HttpPost.https(baseEndpoint, map, privateKeyPath,
					publicKeyPath, keystorePass, pfxPass);
			boolean success = JuSignature.validateSign(response, encoding,
					publicKeyPath, pfxPass);
			logger.info("order response: " + response + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			if (success) {
				return toPaymentResponse(request, response, extOrderNo);
			} else {
				logger.error("Verify sign failed");
			}
		} catch (Exception e) {
			logger.error("Unified order failed", e);
		}
		return null;
	}

	@Override
	public PaymentResponse query(PaymentRequest request) {
		String extOrderNo = request.getGatewayOrderNo();
		String msgInfo = IDGenerator.formatNow(IDGenerator.TimePattern14) + "|"+extOrderNo+"|"+PaymentGateway.JUZHEN.Query();
		logger.info("Query msgInfo: " + msgInfo);
		
		String signature = "";
		try {
			signature = JuSignature.sign(msgInfo, encoding, publicKeyPath,
					pfxPass);
			logger.info("sign: " + signature);

			Map<String, String> map = new HashMap<String, String>();
			map.put("merId", request.getExtStoreId());
			map.put("tradeCode", PaymentGateway.JUZHEN.Query());
			map.put("orderId", extOrderNo);
			map.put("msg", msgInfo);
			map.put("signature", signature);

			String response = "";
			long l = System.currentTimeMillis();
			response = HttpPost.https(baseEndpoint, map, privateKeyPath,
					publicKeyPath, keystorePass, pfxPass);
			boolean success = JuSignature.validateSign(response, encoding,
					publicKeyPath, pfxPass);
			logger.info("Query response: " + response + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			if (success) {
				return toPaymentResponse(request, response, extOrderNo);
			} else {
				logger.error("Verify sign failed");
			}
		} catch (Exception e) {
			logger.error("Query order failed", e);
		}
		return null;
	}

	@Override
	public PaymentResponse refund(PaymentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private PaymentResponse toPaymentResponse(PaymentRequest request, String str, String extOrderNo) {
		JuZhenResponse resp = JsonUtils.fromJson(str, JuZhenResponse.class);
		if (resp == null || !JuZhenResponse.SUCCESS.equals(resp.getRespCode())
				|| StringUtils.isBlank(resp.getCodeUrl())) {
			String code = resp == null ? NO_RESPONSE : resp.getRespCode();
			String msg = resp == null ? "No response" : resp.getRespInfo();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(resp.getCodeUrl());
		bill.setPrepayId(resp.getPrepayId());
		bill.setOrderNo(request.getOrderNo());
		bill.setGatewayOrderNo(extOrderNo);
		bill.setOrderStatus(toOrderStatus(resp.getOrdStatus()));
		response.setBill(bill);
		return response;
	}
	
	private OrderStatus toOrderStatus(String ordStatus) {
		if(StringUtils.isBlank(ordStatus)) {
			return OrderStatus.NOTPAY;
		}
		if(ordStatus.startsWith("0")) {
			return OrderStatus.SUCCESS;
		} else if(ordStatus.startsWith("1") || ordStatus.startsWith("2")) {
			return OrderStatus.PAYERROR;
		} else {
			return OrderStatus.NOTPAY;
		}
	}

	public static final  String formatAmount(float amount) {
		int amt = (int) (amount * 100);
		String result = String.valueOf(amt);
		if (result.length() < 15) {
			result = StringUtils.leftPad(result, 15, "0");
		}
		return result;
	}

}
