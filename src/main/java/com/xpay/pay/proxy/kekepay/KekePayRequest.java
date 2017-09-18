package com.xpay.pay.proxy.kekepay;

public class KekePayRequest {

  private String payKey;
  private String orderPrice;
  private String outTradeNo;
  private String productType;
  private String orderTime;
  private String payBankAccountNo;
  private String productName;
  private String orderIp;
  private String returnUrl;
  private String notifyUrl;
  private String sign;

  public String getPayKey() {
    return payKey;
  }

  public void setPayKey(String payKey) {
    this.payKey = payKey;
  }

  public String getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(String orderTime) {
    this.orderTime = orderTime;
  }

  public String getPayBankAccountNo() {
    return payBankAccountNo;
  }

  public void setPayBankAccountNo(String payBankAccountNo) {
    this.payBankAccountNo = payBankAccountNo;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getOrderIp() {
    return orderIp;
  }

  public void setOrderIp(String orderIp) {
    this.orderIp = orderIp;
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

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }
}