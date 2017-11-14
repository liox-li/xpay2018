package com.xpay.pay.proxy.upay;

public class UPayRequest {
	private String client_sn;
	private String terminal_sn;
	private String payway;
	private String subject;
	private String total_amount;
	private String operator;
	private String return_url;
	private String notify_url;
	private String refund_request_no;
	private String refund_amount;
	public String getClient_sn() {
		return client_sn;
	}
	public void setClient_sn(String client_sn) {
		this.client_sn = client_sn;
	}
	public String getTerminal_sn() {
		return terminal_sn;
	}
	public void setTerminal_sn(String terminal_sn) {
		this.terminal_sn = terminal_sn;
	}
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getRefund_request_no() {
		return refund_request_no;
	}
	public void setRefund_request_no(String refund_request_no) {
		this.refund_request_no = refund_request_no;
	}
	public String getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}
	
}
