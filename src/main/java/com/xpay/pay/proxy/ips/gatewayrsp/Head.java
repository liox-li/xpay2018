package com.xpay.pay.proxy.ips.gatewayrsp;

/**
 * Created by sunjian on Date: 2017/12/13 上午10:10
 * Description:
 */
public class Head {

  private String referenceID;
  private String rspCode;
  private String rspMsg;
  private String reqDate;
  private String rspDate;
  private String signature;

  public String getReferenceID() {
    return referenceID;
  }

  public void setReferenceID(String referenceID) {
    this.referenceID = referenceID;
  }

  public String getRspCode() {
    return rspCode;
  }

  public void setRspCode(String rspCode) {
    this.rspCode = rspCode;
  }

  public String getRspMsg() {
    return rspMsg;
  }

  public void setRspMsg(String rspMsg) {
    this.rspMsg = rspMsg;
  }

  public String getReqDate() {
    return reqDate;
  }

  public void setReqDate(String reqDate) {
    this.reqDate = reqDate;
  }

  public String getRspDate() {
    return rspDate;
  }

  public void setRspDate(String rspDate) {
    this.rspDate = rspDate;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}