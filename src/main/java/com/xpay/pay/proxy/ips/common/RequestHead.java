package com.xpay.pay.proxy.ips.common;

/**
 * Created by sunjian on Date: 2017/12/25 下午4:16
 * Description:
 */
public class RequestHead {

  private String version;

  private String reqIp;

  private String reqDate;

  private String signature;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getReqIp() {
    return reqIp;
  }

  public void setReqIp(String reqIp) {
    this.reqIp = reqIp;
  }

  public String getReqDate() {
    return reqDate;
  }

  public void setReqDate(String reqDate) {
    this.reqDate = reqDate;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}