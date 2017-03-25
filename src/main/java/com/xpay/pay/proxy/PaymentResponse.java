package com.xpay.pay.proxy;

import com.xpay.pay.proxy.PaymentRequest.PayChannel;

public class PaymentResponse {
	private String code;
	private TradeBean data;
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public TradeBean getData() {
		return data;
	}


	public void setData(TradeBean date) {
		this.data = date;
	}

	public class TradeBean {
		private String trade_no;
		private TradeStatus trade_status;
		private PayType pay_type;
		private PayChannel pay_channel;
		private int total_amount;
		private int seller_amount;
		private int seller_coupon;
		private int buyer_amount;
		private int chn_coupon;
		private int undiscount_amount;
		private String buyer_id;
		private String store_name;
		private String busi_code;
		private String dev_id;
		private String down_trade_no;
		private String up_trade_no;
		private String raw_data;
		private String oper_id;
		private String subject;
		private String time_end;
		private String refund_time;
		private String code_url;
		private String prepay_id;

		public String getTrade_no() {
			return trade_no;
		}

		public void setTrade_no(String trade_no) {
			this.trade_no = trade_no;
		}

		public TradeStatus getTrade_status() {
			return trade_status;
		}

		public void setTrade_status(TradeStatus trade_status) {
			this.trade_status = trade_status;
		}

		public PayType getPay_type() {
			return pay_type;
		}

		public void setPay_type(PayType pay_type) {
			this.pay_type = pay_type;
		}

		public PayChannel getPay_channel() {
			return pay_channel;
		}

		public void setPay_channel(PayChannel pay_channel) {
			this.pay_channel = pay_channel;
		}

		public int getTotal_amount() {
			return total_amount;
		}

		public void setTotal_amount(int total_amount) {
			this.total_amount = total_amount;
		}

		public int getSeller_amount() {
			return seller_amount;
		}

		public void setSeller_amount(int seller_amount) {
			this.seller_amount = seller_amount;
		}

		public int getSeller_coupon() {
			return seller_coupon;
		}

		public void setSeller_coupon(int seller_coupon) {
			this.seller_coupon = seller_coupon;
		}

		public int getBuyer_amount() {
			return buyer_amount;
		}

		public void setBuyer_amount(int buyer_amount) {
			this.buyer_amount = buyer_amount;
		}

		public int getChn_coupon() {
			return chn_coupon;
		}

		public void setChn_coupon(int chn_coupon) {
			this.chn_coupon = chn_coupon;
		}

		public int getUndiscount_amount() {
			return undiscount_amount;
		}

		public void setUndiscount_amount(int undiscount_amount) {
			this.undiscount_amount = undiscount_amount;
		}

		public String getBuyer_id() {
			return buyer_id;
		}

		public void setBuyer_id(String buyer_id) {
			this.buyer_id = buyer_id;
		}

		public String getStore_name() {
			return store_name;
		}

		public void setStore_name(String store_name) {
			this.store_name = store_name;
		}

		public String getBusi_code() {
			return busi_code;
		}

		public void setBusi_code(String busi_code) {
			this.busi_code = busi_code;
		}

		public String getDev_id() {
			return dev_id;
		}

		public void setDev_id(String dev_id) {
			this.dev_id = dev_id;
		}

		public String getDown_trade_no() {
			return down_trade_no;
		}

		public void setDown_trade_no(String down_trade_no) {
			this.down_trade_no = down_trade_no;
		}

		public String getUp_trade_no() {
			return up_trade_no;
		}

		public void setUp_trade_no(String up_trade_no) {
			this.up_trade_no = up_trade_no;
		}

		public String getRaw_data() {
			return raw_data;
		}

		public void setRaw_data(String raw_data) {
			this.raw_data = raw_data;
		}

		public String getOper_id() {
			return oper_id;
		}

		public void setOper_id(String oper_id) {
			this.oper_id = oper_id;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getTime_end() {
			return time_end;
		}

		public void setTime_end(String time_end) {
			this.time_end = time_end;
		}

		public String getRefund_time() {
			return refund_time;
		}

		public void setRefund_time(String refund_time) {
			this.refund_time = refund_time;
		}

		public String getCode_url() {
			return code_url;
		}

		public void setCode_url(String code_url) {
			this.code_url = code_url;
		}

		public String getPrepay_id() {
			return prepay_id;
		}

		public void setPrepay_id(String prepay_id) {
			this.prepay_id = prepay_id;
		}
	}

	public enum TradeStatus {
		SUCCESS, REFUND, REVOKED, NOTPAY, USERPAYING
	}

	public enum PayType {
		MICROPAY, NATIVE, JSAPI
	}
}
