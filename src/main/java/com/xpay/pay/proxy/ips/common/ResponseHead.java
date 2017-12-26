package com.xpay.pay.proxy.ips.common;

/**
 * Created by sunjian on Date: 2017/12/25 下午4:24
 * Description:
 */
public class ResponseHead {

  private String reqDate;

  private String respDate;

  private String signature;

  public String getReqDate() {
    return reqDate;
  }

  public void setReqDate(String reqDate) {
    this.reqDate = reqDate;
  }

  public String getRespDate() {
    return respDate;
  }

  public void setRespDate(String respDate) {
    this.respDate = respDate;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}