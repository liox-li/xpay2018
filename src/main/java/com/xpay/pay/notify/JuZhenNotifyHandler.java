package com.xpay.pay.notify;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.juzhen.JuZhenProxy;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.JsonUtils;

@Service
public class JuZhenNotifyHandler extends AbstractNotifyHandler {

	@Override
	protected NotifyBody extractNotifyBody(String url,String content) {
		JuZhenNotification notification = JsonUtils.fromJson(content,
				JuZhenNotification.class);
		if (notification != null && StringUtils.isNoneBlank(notification.getSignature(),
						notification.getOrderId(),
						notification.getTransAmt())) {
			String orderId = notification.getOrderId();
			String totalFee = notification.getTransAmt();
			String status = notification.getOrdStatus();
			return new NotifyBody(null, orderId, JuZhenProxy.toOrderStatus(status), CommonUtils.toInt(totalFee), "");
		}
		return null;
	}

	private static final String SUCCESS_STR = "{'respCode':'00000', 'respInfo': 'OK'}";
	@Override
	protected String getSuccessResponse() {
		return SUCCESS_STR;
	}

	private static final String FAIL_STR = "{'respCode':'20000', 'respInfo': 'Failed'}";
	@Override
	protected String getFailedResponse() {
		return FAIL_STR;
	}
	
	public static class JuZhenNotification {
		private String merId;
		private String orderId;
		private String transKey;
		private String ordStatus;
		private String transAmt;
		private String feeAmt;
		private String ordInfo;
		private String openId;
		private String bankType;
		private String signature;
		public String getMerId() {
			return merId;
		}
		public void setMerId(String merId) {
			this.merId = merId;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getTransKey() {
			return transKey;
		}
		public void setTransKey(String transKey) {
			this.transKey = transKey;
		}
		public String getOrdStatus() {
			return ordStatus;
		}
		public void setOrdStatus(String ordStatus) {
			this.ordStatus = ordStatus;
		}
		public String getTransAmt() {
			return transAmt;
		}
		public void setTransAmt(String transAmt) {
			this.transAmt = transAmt;
		}
		public String getFeeAmt() {
			return feeAmt;
		}
		public void setFeeAmt(String feeAmt) {
			this.feeAmt = feeAmt;
		}
		public String getOrdInfo() {
			return ordInfo;
		}
		public void setOrdInfo(String ordInfo) {
			this.ordInfo = ordInfo;
		}
		public String getOpenId() {
			return openId;
		}
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public String getBankType() {
			return bankType;
		}
		public void setBankType(String bankType) {
			this.bankType = bankType;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
	}
}
