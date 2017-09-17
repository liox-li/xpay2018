package com.xpay.pay.notify;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;

@Service
public class MiaoFuNotifyHandler extends AbstractNotifyHandler {

	@Override
	protected NotifyBody extractNotifyBody(String url, String content) {
		String billNo = "";
		String extOrderNo = "";
		String totalFee = "";
		OrderStatus status = OrderStatus.NOTPAY;
		try {
			String paramStr = url.substring(url.indexOf("?")+1);
			String decoded = URLDecoder.decode(paramStr, "utf-8");
			String[] params = decoded.split("&");
			
			for (String param : params) {
				String[] pair = param.split("=");
				String key = pair[0];
				if ("down_trade_no".equals(key)) {
					billNo = pair[1];
				} else if ("trade_status".equals(key)) {
					status = "ok".equalsIgnoreCase(pair[1])?OrderStatus.SUCCESS:status;
				} else if ("total_amount".equals(key)) {
					totalFee = String.valueOf((int)(Float.valueOf(pair[1])*100));
				} else if ("trade_no".equals(key)) {
					extOrderNo = pair[1];
				}
			}
		} catch (Exception e) {
			
		}
		return StringUtils.isBlank(billNo)?null:new NotifyBody(billNo, extOrderNo, status, totalFee, null);

	}

	@Override
	protected String getSuccessResponse() {
		return null;
	}

	@Override
	protected String getFailedResponse() {
		return null;
	}
	
	@Override
	public boolean isRedrect() {
		return true;
	}

}
