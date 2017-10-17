package com.xpay.pay.notify;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

import com.xpay.pay.proxy.chinaums.ChinaUmsProxy;

public class UPayNotifyHandler extends AbstractNotifyHandler {
	
	@Override
	protected NotifyBody extractNotifyBody(String url, String content) {
		String orderNo = "";
		String extOrderNo = "";
		String status = "";
		String targetOrderNo = "";
		String totalFee = "";
		try {
			orderNo = url.substring(url.lastIndexOf("/")+1);
			orderNo = orderNo.substring(0, orderNo.indexOf("?"));

			String decoded = URLDecoder.decode(url, "utf-8");
			String[] params = decoded.split("&");

			for (String param : params) {
				String[] pair = param.split("=");
				String key = pair[0];
				if ("trade_no".equals(key)) {
					extOrderNo = pair[1];
				} else if ("order_status".equals(key)) {
					status = pair[1];
				} else if ("total_amount".equals(key)) {
					totalFee = pair[1];
				} else if ("targetOrderId".equals(key)) {
					targetOrderNo = pair[1];
				}
			}
		} catch (Exception e) {
			
		}
		return StringUtils.isBlank(orderNo)?null:new NotifyBody(orderNo, extOrderNo, ChinaUmsProxy.toOrderStatus(status), totalFee, targetOrderNo);
	}

	private static final String SUCCESS_STR = "SUCCESS";
	@Override
	protected String getSuccessResponse() {
		return SUCCESS_STR;
	}

	private static final String FAIL_STR = "FAILED";
	@Override
	protected String getFailedResponse() {
		return FAIL_STR;
	}
}
