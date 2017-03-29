package com.xpay.pay.models;

public class StoreChannel {
	private String extStoreId;
	private PaymentGateway paymentGateway;

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
		MIAOFU(1);

		int id;

		PaymentGateway(int id) {
			this.id = id;
		}

		int getId() {
			return id;
		}
	}
}
