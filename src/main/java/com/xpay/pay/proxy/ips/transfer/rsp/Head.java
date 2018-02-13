package com.xpay.pay.proxy.ips.transfer.rsp;

/**
 * Created by sunjian on Date: 2017/12/26 下午8:39
 * Description:
 */
public class Head {

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