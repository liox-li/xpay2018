package com.xpay.pay.proxy;

import java.util.Arrays;

import com.xpay.pay.util.AppConfig;

public interface IPaymentProxy {
	public static final String NO_RESPONSE = "-100";
	public static final int DEFAULT_TIMEOUT = 3000;

	public static final String PAYED = "POST";
	public static final String TOPAY = "TOPAY";
	public static final String DEFAULT_JSAPI_URL = AppConfig.XPayConfig.getProperty("jsapi.endpoint");
	public static final String DEFAULT_H5API_URL = AppConfig.XPayConfig.getProperty("h5api.endpoint");
	public static final String EXT_H5API_URL = AppConfig.XPayConfig.getProperty("h5api.ext.endpoint");

	public PaymentResponse unifiedOrder(PaymentRequest request);
	
	public PaymentResponse query(PaymentRequest request);
	
	public PaymentResponse refund(PaymentRequest request);
	
	public default void init(String props){
		return ;
	}
	public enum PayChannel {
		ALL(0),//
		ALIPAY(1),//
		WECHAT(2),//
		CASH(3), //
		CREDITCARD(4),//
		XIAOWEI(5), //
		XIAOWEI_H5(6);//
		
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
	
	public enum PayType{
		WECHAT_SCAN,//微信正扫
	    WECHAT_APP,//微信app支付
	    WECHAT_GZH,//微信公众号
	    ALIPAY,//支付宝统一下单
		ALIPAY_SCAN,//支付宝正扫
		APPLE_PAY,//苹果支付
		
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
