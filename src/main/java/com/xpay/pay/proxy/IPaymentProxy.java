package com.xpay.pay.proxy;

import java.util.Arrays;

public interface IPaymentProxy {
	public static final String NO_RESPONSE = "-100";
	public PaymentResponse unifiedOrder(PaymentRequest request);
	
	public PaymentResponse query(PaymentRequest request);
	
	public PaymentResponse refund(PaymentRequest request);
	
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
	
	public enum TradeNoType {
		MiaoFu(1), Gateway(2);
		
		int id;
		
		TradeNoType(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
}
