package com.xpay.pay.proxy.txf;

public class TxfResponse {
	public static final String SUCCESS = "0000";
	private String res;
	private String orderNo;
	private String money;
	private String agentOrgno;
	private String channel;
	private String msg;
	private String url;
	private String spbillno;
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
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
	public String getAgentOrgno() {
		return agentOrgno;
	}
	public void setAgentOrgno(String agentOrgno) {
		this.agentOrgno = agentOrgno;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSpbillno() {
		return spbillno;
	}
	public void setSpbillno(String spbillno) {
		this.spbillno = spbillno;
	}
}
