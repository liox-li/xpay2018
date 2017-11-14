package com.xpay.pay.proxy.upay;

import org.apache.commons.lang3.StringUtils;

public class UPayResponse {
	private String result_code;
	private String error_code;
	private String error_message;
	private UPayBizResponse biz_response;
	
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	public UPayBizResponse getBiz_response() {
		return biz_response;
	}
	public void setBiz_response(UPayBizResponse biz_response) {
		this.biz_response = biz_response;
	}
	
	public boolean isSuccess() {
		return "200".equals(this.result_code) 
				&& this.getBiz_response()!=null
				&& this.getBiz_response().getData() !=null
				&& StringUtils.isNotBlank(this.getBiz_response().getData().getSn());

	}
	
	public static class UPayBizResponse {
		private String result_code;
		private String error_code;
		private String error_message;
		private UPayData data;
		public String getResult_code() {
			return result_code;
		}
		public void setResult_code(String result_code) {
			this.result_code = result_code;
		}
		public String getError_code() {
			return error_code;
		}
		public void setError_code(String error_code) {
			this.error_code = error_code;
		}
		public String getError_message() {
			return error_message;
		}
		public void setError_message(String error_message) {
			this.error_message = error_message;
		}
		public UPayData getData() {
			return data;
		}
		public void setData(UPayData data) {
			this.data = data;
		}
	}
	
	public static class UPayData {
		private String sn;
		private String client_sn;
		private String client_tsn;
		private String trade_no;
		private String finish_time;
		private String channel_finish_time;
		private String status;
		private String order_status;
		private String payway;
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
		public String getClient_tsn() {
			return client_tsn;
		}
		public void setClient_tsn(String client_tsn) {
			this.client_tsn = client_tsn;
		}
		public String getTrade_no() {
			return trade_no;
		}
		public void setTrade_no(String trade_no) {
			this.trade_no = trade_no;
		}
		public String getFinish_time() {
			return finish_time;
		}
		public void setFinish_time(String finish_time) {
			this.finish_time = finish_time;
		}
		public String getChannel_finish_time() {
			return channel_finish_time;
		}
		public void setChannel_finish_time(String channel_finish_time) {
			this.channel_finish_time = channel_finish_time;
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
		public String getPayway() {
			return payway;
		}
		public void setPayway(String payway) {
			this.payway = payway;
		}
		public String getTotal_amount() {
			return total_amount;
		}
		public void setTotal_amount(String total_amount) {
			this.total_amount = total_amount;
		}
	}
}
