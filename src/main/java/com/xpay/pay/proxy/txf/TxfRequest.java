package com.xpay.pay.proxy.txf;

public class TxfRequest {
	private String agentOrgno;
	private String orderNo;
	private String money;
	private String curType;
	private String returnUrl;
	private String notifyUrl;
	private String memo;
	private String attach;
	private String cardType;
	private String bankSegment;
	private String userType;
	private String channel;
	public String getAgentOrgno() {
		return agentOrgno;
	}
	public void setAgentOrgno(String agentOrgno) {
		this.agentOrgno = agentOrgno;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCurType() {
		return curType;
	}
	public void setCurType(String curType) {
		this.curType = curType;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getBankSegment() {
		return bankSegment;
	}
	public void setBankSegment(String bankSegment) {
		this.bankSegment = bankSegment;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	} 
	
	
}
