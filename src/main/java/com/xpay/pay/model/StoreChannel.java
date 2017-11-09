package com.xpay.pay.model;

import com.xpay.pay.util.TimeUtils;

public class StoreChannel {
	private long id;
	private String extStoreId;
	private PaymentGateway paymentGateway;
	private long lastUseTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExtStoreId() {
		return extStoreId;
	}

	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}

	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}

	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	
	public long getLastUseTime() {
		return lastUseTime;
	}

	public void setLastUseTime(long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
	
	private static final long BLOCK_TIME_DAY= 40*1000;
	private static final long BLOCK_TIME_NIGHT= 60*1000;
	public boolean isAvailable() {
		long blockTime = TimeUtils.isNowDayTime()?BLOCK_TIME_DAY:BLOCK_TIME_NIGHT;
		return System.currentTimeMillis()-this.lastUseTime>blockTime;
	}

	public enum PaymentGateway {
		MIAOFU("jspay", "query", "refund"), 
		SWIFTPASS("unified.trade.pay", "unified.trade.query", "unified.trade.refund"), 
		CHINAUMS("yuedan.getQRCode", "yuedan.query", "yuedan.refund"), 
		CHINAUMSV2("bills.getQRCode", "bills.query", "bills.refund"), 
//		CHINAUMSH5("WXPay.jsPay", "query", "refund"),
		CHINAUMSH5("WXPay.jsPay", "query", "refund"),
		CHINAUMSWAP("WXPay.jsPay", "query", "refund"),
		UPAY("", "query", "refund"),
		RUBIPAY("unified.trade.pay", "unified.trade.query", "unified.trade.refund"),
		BAIFU("trade.weixin.gzpay", "query", "unified.trade.refund"), 
		JUZHEN("070201", "070101", ""), 
		KEFU("msBank_WeChatPay", "msBank_ScanPayQuery", ""), 
		KEKEPAY("pay","query","");
		
		String unifiedOrder;
		String query;
		String refund;
		PaymentGateway(String unifiedOrder, String query, String refund) {
			this.unifiedOrder = unifiedOrder;
			this.query = query;
			this.refund = refund;
		}
		
		public String UnifiedOrder() {
			return this.unifiedOrder;
		}
		
		public String Query() {
			return this.query;
		}
		
		public String Refund() {
			return this.refund;
		}
		
	}
}
