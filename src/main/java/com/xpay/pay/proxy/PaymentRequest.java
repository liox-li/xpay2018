package com.xpay.pay.proxy;

import java.util.Arrays;

public class PaymentRequest {
	private String busi_code;
	private String dev_id;
	private String oper_id;
	private PayChannel pay_channel; 
	private String amount;
	private String undiscountable_amount;
	private String raw_data;
	private String auth_code;
	private String down_trade_no;
	private String trade_no;
	private TradeNoType trade_no_type;
	private String subject;
	private GoodBean[] good_details;
	
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

	public String getOper_id() {
		return oper_id;
	}

	public void setOper_id(String oper_id) {
		this.oper_id = oper_id;
	}

	public PayChannel getPay_channel() {
		return pay_channel;
	}

	public void setPay_channel(PayChannel pay_channel) {
		this.pay_channel = pay_channel;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUndiscountable_amount() {
		return undiscountable_amount;
	}

	public void setUndiscountable_amount(String undiscountable_amount) {
		this.undiscountable_amount = undiscountable_amount;
	}

	public String getRaw_data() {
		return raw_data;
	}

	public void setRaw_data(String raw_data) {
		this.raw_data = raw_data;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public String getDown_trade_no() {
		return down_trade_no;
	}

	public void setDown_trade_no(String down_trade_no) {
		this.down_trade_no = down_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public TradeNoType getTrade_no_type() {
		return trade_no_type;
	}

	public void setTrade_no_type(TradeNoType trade_no_type) {
		this.trade_no_type = trade_no_type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public GoodBean[] getGood_details() {
		return good_details;
	}

	public void setGood_details(GoodBean[] good_details) {
		this.good_details = good_details;
	}

	public enum PayChannel {
		ALL(0), ALIPAY(1), WECHAT(2), CASH(3), CREDITCARD(4);
		
		String id;
		
		PayChannel(int id) {
			this.id = String.valueOf(id);
		}

		public static PayChannel fromValue(String val) {
			PayChannel[] channels = PayChannel.values();
			return Arrays.stream(channels).filter(x -> x.getId().equals(val)).findFirst().orElse(null);
		}
		
		public String getId() {
			return id;
		}
	}
	
	public enum Method {
		MicroPay("pay", "micropay"), UnifiedOrder("pay", "unifiedorder"), Query("pay", "query"), Refund("pay", "refund");;
		
		String module;
		String method;
		
		Method(String module, String method) {
			this.module = module;
			this.method = method;
		}
	}
	
	public enum TradeNoType {
		MiaoFu(1), Gateway(2);
		
		int id;
		
		TradeNoType(int id) {
			this.id = id;
		}
	}
	
	public class GoodBean {
		private String id;
		private String name;
		private String category;
		private String url;
		private String amount;
		private String subject;
		private int quantity;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
	}
}
