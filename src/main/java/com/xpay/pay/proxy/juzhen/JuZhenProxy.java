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
	private static final String publicKeyPath = config
			.getProperty("provider.public.key.path");
	private static final String privateKeyPath = config
			.getProperty("provider.private.key.path");
	private static final String keystorePass = "!@34%^";
	private static final String pfxPass = "123pay";

	private static final String encoding = "UTF-8";

	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		String extOrderNo = IDGenerator.buildShortOrderNo();
		String msgInfo = IDGenerator.formatNow(IDGenerator.TimePattern14) + "|"
				+ this.formatAmount(request.getTotalFeeAsFloat()) + "|"
				+ request.getNotifyUrl() + "|" + request.getSubject();
		logger.info("msgInfo: " + msgInfo);
		
		String signature = "";
		try {
			signature = JuSignature.sign(msgInfo, encoding, publicKeyPath,
					pfxPass);
			logger.info("sign: " + signature);
		} catch (Exception e1) {
			logger.error("Sign failed", e1);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("merId", request.getExtStoreId());
		map.put("tradeCode", PaymentGateway.JUZHEN.UnifiedOrder());
		map.put("orderId", extOrderNo);
		map.put("msg", msgInfo);
		map.put("signature", signature);
		String response = "";
		try {
			long l = System.currentTimeMillis();
			response = HttpPost.https(baseEndpoint, map, privateKeyPath,
					publicKeyPath, keystorePass, pfxPass);
			boolean success = JuSignature.validateSign(response, encoding,
					publicKeyPath, pfxPass);
			if (success) {
				logger.info("order response: " + response + ", took "
						+ (System.currentTimeMillis() - l) + "ms");
				return toPaymentResponse(response, extOrderNo);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResponse refund(PaymentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private PaymentResponse toPaymentResponse(String str, String extOrderNo) {
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
		bill.setOrderNo(extOrderNo);
		bill.setOrderStatus(OrderStatus.NOTPAY);
		response.setBill(bill);
		return response;
	}

	private String formatAmount(float amount) {
		int amt = (int) (amount * 100);
		String result = String.valueOf(amt);
		if (result.length() < 15) {
			result = StringUtils.leftPad(result, 15, "0");
		}
		return result;
	}

}
