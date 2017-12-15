package com.xpay.pay.proxy.ips.common;

/**
 * Created by sunjian on Date: 2017/12/12 下午11:01
 * Description:
 */
public class ReqHead {

  private String version;
  private String merCode;
  private String merName;
  private String account;
  private String msgId;
  private String reqDate;
  private String signature;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getMerCode() {
    return merCode;
  }

  public void setMerCode(String merCode) {
    this.merCode = merCode;
  }

  public String getMerName() {
    return merName;
  }

  public void setMerName(String merName) {
    this.merName = merName;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
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