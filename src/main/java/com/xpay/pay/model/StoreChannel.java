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
		MIAOFU(1), SWIFTPASS(2), CHINAUMS(3), RUBIPAY(4);
		
		int bailStoreId;
		PaymentGateway(int bailStoreId) {
			this.bailStoreId = bailStoreId;
		}
		
		public int getBailStoreId() {
			return this.bailStoreId;
		}
		
	}
}
