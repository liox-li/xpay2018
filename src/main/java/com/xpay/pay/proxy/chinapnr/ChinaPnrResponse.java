package com.xpay.pay.proxy.chinapnr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChinaPnrResponse {
	private String resp_code;//218002--调用成功，其他见返回码附件表
	private String resp_desc;//应答返回描述
	private String bg_bank_code;//银行返回码
	private String bg_bank_message;//银行返回描述
	private String platform_seq_id;//本平台交易唯一标识号  组成规则：8位本平台日期+10位系统流水号
	private String pay_url;//支付地址  支付宝统一下单返回参数支付宝浏览器下直接用此链接请求支付宝支付(注：目前部分通道不支持返回支付地址)
	private String token_id;//动态口令 微信公众号参数根据token_id组装如下url地址，在微信下可直接唤起支付https://pay.swiftpass.cn/pay/jspay?token_id=9a0610bc519e782e6275e8c7dd94a445&showwxtitle=1
	private String pay_info;// 支付信息
	private String order_date;//订单日期
	private String order_id;//订单号
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}
	public String getResp_desc() {
		return resp_desc;
	}
	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	}
	public String getBg_bank_code() {
		return bg_bank_code;
	}
	public void setBg_bank_code(String bg_bank_code) {
		this.bg_bank_code = bg_bank_code;
	}
	public String getBg_bank_message() {
		return bg_bank_message;
	}
	public void setBg_bank_message(String bg_bank_message) {
		this.bg_bank_message = bg_bank_message;
	}
	public String getPlatform_seq_id() {
		return platform_seq_id;
	}
	public void setPlatform_seq_id(String platform_seq_id) {
		this.platform_seq_id = platform_seq_id;
	}
	public String getPay_url() {
		return pay_url;
	}
	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}
	public String getToken_id() {
		return token_id;
	}
	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}
	public String getPay_info() {
		return pay_info;
	}
	public void setPay_info(String pay_info) {
		this.pay_info = pay_info;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	
	
	
}
