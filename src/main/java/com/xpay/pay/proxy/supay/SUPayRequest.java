package com.xpay.pay.proxy.supay;

/**
 * Created by sunjian on Date: 2017/12/14 下午11:35
 * Description:
 */
public class SUPayRequest {

  private String merOrderId;

  private String merchantNo;

  private String sign;

  private String serverCode;

  private String version;

  private String params;

  private String date;

  public String getMerOrderId() {
    return merOrderId;
  }

  public void setMerOrderId(String merOrderId) {
    this.merOrderId = merOrderId;
  }

  public String getMerchantNo() {
    return merchantNo;
  }

  public void setMerchantNo(String merchantNo) {
    this.merchantNo = merchantNo;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getServerCode() {
    return serverCode;
  }

  public void setServerCode(String serverCode) {
    this.serverCode = serverCode;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}