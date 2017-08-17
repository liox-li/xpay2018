package com.xpay.pay.model;

public class StoreChannel {
	private long id;
	private long storeId;
	private String extStoreId;
	private PaymentGateway paymentGateway;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
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

	public enum PaymentGateway {
		MIAOFU(1, "unifiedorder", "query", "refund"), SWIFTPASS(2, "unified.trade.pay", "unified.trade.query", "unified.trade.refund"), CHINAUMS(3, "yuedan.getQRCode", "yuedan.query", "yuedan.refund"), RUBIPAY(4, "unified.trade.pay", "unified.trade.query", "unified.trade.refund")
		, BAIFU(5, "trade.weixin.gzpay", "query", "unified.trade.refund"), JUZHEN(6, "070201", "070101", ""), CHINAUMSV2(7, "bills.getQRCode", "bills.query", "bills.refund"), KEFU(8, "msBank_WeChatPay", "msBank_D0Query", "");
		
		int bailStoreId;
		String unifiedOrder;
		String query;
		String refund;
		PaymentGateway(int bailStoreId, String unifiedOrder, String query, String refund) {
			this.bailStoreId = bailStoreId;
			this.unifiedOrder = unifiedOrder;
			this.query = query;
			this.refund = refund;
		}
		
		public int getBailStoreId() {
			return this.bailStoreId;
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
