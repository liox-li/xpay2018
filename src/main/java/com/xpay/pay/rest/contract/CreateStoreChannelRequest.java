package com.xpay.pay.rest.contract;

import com.xpay.pay.model.StoreChannel.ChinaUmsProps;
import com.xpay.pay.model.StoreChannel.PaymentGateway;

public class CreateStoreChannelRequest {
	private String extStoreId;
	private PaymentGateway paymentGateway;
	private String extStoreName;
	private Long agentId;
	private ChinaUmsProps chinaUmsProps;
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
	public String getExtStoreName() {
		return extStoreName;
	}
	public void setExtStoreName(String extStoreName) {
		this.extStoreName = extStoreName;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public ChinaUmsProps getChinaUmsProps() {
		return chinaUmsProps;
	}
	public void setChinaUmsProps(ChinaUmsProps chinaUmsProps) {
		this.chinaUmsProps = chinaUmsProps;
	}
}
