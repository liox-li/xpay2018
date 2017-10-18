package com.xpay.pay.notify;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.upay.UPayProxy;
import com.xpay.pay.proxy.upay.UPayResponse;
import com.xpay.pay.proxy.upay.UPayResponse.UPayData;
import com.xpay.pay.util.JsonUtils;

@Service
public class UPayNotifyHandler extends AbstractNotifyHandler {
	
	@Override
	protected NotifyBody extractNotifyBody(String url, String content) {
		String orderNo = "";
		String extOrderNo = "";
		String status = "";
		String targetOrderNo = "";
		String totalFee = "";
		try {
			UPayResponse notObject = JsonUtils.fromJson(content, UPayResponse.class);
			if(notObject!=null && notObject.isSuccess()) {
				UPayData data = notObject.getBiz_response().getData();
				orderNo = data.getClient_sn();
				extOrderNo = data.getTrade_no();
				status = data.getOrder_status();
				totalFee = data.getTotal_amount();
			}
		} catch (Exception e) {
		}
		return StringUtils.isBlank(orderNo)?null:new NotifyBody(orderNo, extOrderNo, UPayProxy.toOrderStatus(status), totalFee, targetOrderNo);
	}

	private static final String SUCCESS_STR = "success";
	@Override
	protected String getSuccessResponse() {
		return SUCCESS_STR;
	}

	private static final String FAIL_STR = "failed";
	@Override
	protected String getFailedResponse() {
		return FAIL_STR;
	}
	
}
