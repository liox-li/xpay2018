package com.xpay.pay.proxy.haoda;

public class ZSResponse {
	private String merchantId;//商户号
	private String orderDate;//8位订单日期
	private String orderId;//商户订单号
	private String orderAmount;//订单金额
	private String orderType;//订单类型
	
	private String qrcCode;//二维码支付链接- 扫码支付时有值
	private String wxPrepayId;//微信预下单id - 微信h5时有值
	private String wxPayUrl;//微信h5支付url
	private String jdPayContent;//京东支付内容域
	private String alipayUrl;//支付宝支付url
	
	private String payStatus;//支付状态 PROCESSING,SUCCESS,FAIL
	private String systemOrderId;//平台订单号
	private String respCode;//交易应答码
	private String respMessage;//交易应答信息
	private String sign;//
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getQrcCode() {
		return qrcCode;
	}
	public void setQrcCode(String qrcCode) {
		this.qrcCode = qrcCode;
	}
	public String getWxPrepayId() {
		return wxPrepayId;
	}
	public void setWxPrepayId(String wxPrepayId) {
		this.wxPrepayId = wxPrepayId;
	}
	
	public String getWxPayUrl() {
		return wxPayUrl;
	}
	public void setWxPayUrl(String wxPayUrl) {
		this.wxPayUrl = wxPayUrl;
	}
	public String getJdPayContent() {
		return jdPayContent;
	}
	public void setJdPayContent(String jdPayContent) {
		this.jdPayContent = jdPayContent;
	}
	public String getAlipayUrl() {
		return alipayUrl;
	}
	public void setAlipayUrl(String alipayUrl) {
		this.alipayUrl = alipayUrl;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getSystemOrderId() {
		return systemOrderId;
	}
	public void setSystemOrderId(String systemOrderId) {
		this.systemOrderId = systemOrderId;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	
	

}
