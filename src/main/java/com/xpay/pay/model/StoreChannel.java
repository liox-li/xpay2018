package com.xpay.pay.model;

public class StoreChannel {
	private long id;
	private String extStoreId;
	private PaymentGateway paymentGateway;
	
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

	public enum PaymentGateway {
		MIAOFU("jspay", "query", "refund"), SWIFTPASS("unified.trade.pay", "unified.trade.query", "unified.trade.refund"), CHINAUMS("yuedan.getQRCode", "yuedan.query", "yuedan.refund"), RUBIPAY("unified.trade.pay", "unified.trade.query", "unified.trade.refund")
		, BAIFU("trade.weixin.gzpay", "query", "unified.trade.refund"), JUZHEN("070201", "070101", ""), CHINAUMSV2("bills.getQRCode", "bills.query", "bills.refund"), KEFU("msBank_WeChatPay", "msBank_ScanPayQuery", "");
		
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
