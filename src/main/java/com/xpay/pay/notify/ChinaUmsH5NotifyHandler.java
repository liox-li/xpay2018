package com.xpay.pay.notify;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.chinaums.ChinaUmsProxy;
import com.xpay.pay.util.CommonUtils;

@Service
public class ChinaUmsH5NotifyHandler extends AbstractNotifyHandler {
	
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
				if ("merOrderId".equals(key)) {
					billNo = pair[1];
				} else if ("status".equals(key)) {
					status = pair[1];
				} else if ("totalAmount".equals(key)) {
					totalFee = pair[1];
				} else if ("targetOrderId".equals(key)) {
					targetOrderNo = pair[1];
				}
			}
		} catch (Exception e) {
			logger.error("ChinaUmsH5NotifyHandler extractNotifyBody "+content, e);
		}
		return StringUtils.isBlank(billNo)?null:new NotifyBody(null, billNo, ChinaUmsProxy.toOrderStatus(status), CommonUtils.toInt(totalFee), targetOrderNo);
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
