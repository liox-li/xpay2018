package com.xpay.pay.notify;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.upay.UPayProxy;
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
			UPayNotification notObject = JsonUtils.fromJson(content, UPayNotification.class);
			if(notObject!=null) {
				orderNo = notObject.getClient_sn();
				extOrderNo = notObject.getSn();
				targetOrderNo = notObject.getTrade_no();
				status = notObject.getOrder_status();
				totalFee = notObject.getTotal_amount();
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
	
	public static class UPayNotification {
		private String sn;
		private String client_sn;
		private String status;
		private String order_status;
		private String trade_no;
		private String total_amount;
		
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getClient_sn() {
			return client_sn;
		}
		public void setClient_sn(String client_sn) {
			this.client_sn = client_sn;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getOrder_status() {
			return order_status;
		}
		public void setOrder_status(String order_status) {
			this.order_status = order_status;
		}
		public String getTrade_no() {
			return trade_no;
		}
		public void setTrade_no(String trade_no) {
			this.trade_no = trade_no;
		}
		public String getTotal_amount() {
			return total_amount;
		}
		public void setTotal_amount(String total_amount) {
			this.total_amount = total_amount;
		}
	}

	
}
