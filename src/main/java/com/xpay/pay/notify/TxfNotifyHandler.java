package com.xpay.pay.notify;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.txf.TxfProxy;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.JsonUtils;

@Service
public class TxfNotifyHandler extends AbstractNotifyHandler {
	
	@Override
	protected NotifyBody extractNotifyBody(String url, String content) {
		String billNo = "";
		String status = "";
		String targetOrderNo = "";
		String totalFee = "";
		try {
			String[] params = content.split("&");
			String body = "";
			String sign = "";
			for (String param : params) {
				String[] pair = param.split("=");
				String key = pair[0];
				if ("params".equals(key)) {
					String str = CommonUtils.replaceBlank(pair[1]);
					logger.info("params: "+str);
					body = CryptoUtils.base64Decode(str);
				} else if ("sign".equals(key)) {
					sign = pair[1];
				}
			}
			TxfNotifyBody jsonNotify = JsonUtils.fromJson(body, TxfNotifyBody.class);
			if(jsonNotify!=null) {
				billNo = jsonNotify.getOrderNo();
				status = jsonNotify.getStatus();
				totalFee = jsonNotify.getMoney();
				targetOrderNo = jsonNotify.getSpbillno();
			}
		} catch (Exception e) {
			logger.error("TxfNotifyHandler extractNotifyBody "+content, e);
		}
		return StringUtils.isBlank(billNo)?null:new NotifyBody(billNo, null, TxfProxy.toOrderStatus(status), totalFee, targetOrderNo);
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
	
	public static class TxfNotifyBody {
		private String orderNo;
		private String memo;
		private String status;
		private String money;
		private String agentOrgno;
		private String channel;
		private String spbillno;
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getMoney() {
			return money;
		}
		public void setMoney(String money) {
			this.money = money;
		}
		public String getAgentOrgno() {
			return agentOrgno;
		}
		public void setAgentOrgno(String agentOrgno) {
			this.agentOrgno = agentOrgno;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getSpbillno() {
			return spbillno;
		}
		public void setSpbillno(String spbillno) {
			this.spbillno = spbillno;
		}
	}
}
