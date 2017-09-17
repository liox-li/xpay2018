package com.xpay.pay.notify;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.chinaums.ChinaUmsProxy;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.util.JsonUtils;

@Service
public class ChinaUmsNotifyHandler extends AbstractNotifyHandler {
	@Autowired
	OrderService orderService;
	
	@Override
	protected NotifyBody extractNotifyBody(String url, String content) {
		String billNo = "";
		String status = "";
		String targetOrderNo = "";
		String totalFee = "";
		try {
			String decoded = URLDecoder.decode(content, "utf-8");
			String[] params = decoded.split("&");

			for (String param : params) {
				String[] pair = param.split("=");
				String key = pair[0];
				if ("billNo".equals(key)) {
					billNo = pair[1];
				} else if ("billStatus".equals(key)) {
					status = pair[1];
				} else if ("totalAmount".equals(key)) {
					totalFee = pair[1];
				} else if ("billPayment".equals(key)) {
					ChinaUmsBill bill = JsonUtils.fromJson(pair[1], ChinaUmsBill.class);
					targetOrderNo = bill == null? "": bill.getTargetOrderId();
				}
			}
		} catch (Exception e) {
			
		}
		return StringUtils.isBlank(billNo)?null:new NotifyBody(null, billNo, ChinaUmsProxy.toOrderStatus(status), totalFee, targetOrderNo);
	}

	private static final String SUCCESS_STR = "success";
	@Override
	protected String getSuccessResponse() {
		return SUCCESS_STR;
	}

	private static final String FAIL_STR = "fail";
	@Override
	protected String getFailedResponse() {
		return FAIL_STR;
	}
	
	public static class ChinaUmsBill {
		private String targetOrderId;

		public String getTargetOrderId() {
			return targetOrderId;
		}

		public void setTargetOrderId(String targetOrderId) {
			this.targetOrderId = targetOrderId;
		}
	}
}
