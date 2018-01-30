package com.xpay.pay.proxy.ips.wxpay.notify;

/**
 * Created by sunjian on Date: 2017/12/13 下午1:41
 * Description:
 */
public class Body {

  private String merBillNo;
  private String merCode;
  private String account;
  private String ipsBillNo;
  private String ipsBillTime;
  private String ordAmt;
  private String retEncodeType;
  private String status;

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getMerCode() {
    return merCode;
  }

  public void setMerCode(String merCode) {
    this.merCode = merCode;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getIpsBillNo() {
    return ipsBillNo;
  }

  public void setIpsBillNo(String ipsBillNo) {
    this.ipsBillNo = ipsBillNo;
  }

  public String getIpsBillTime() {
    return ipsBillTime;
  }

  public void setIpsBillTime(String ipsBillTime) {
    this.ipsBillTime = ipsBillTime;
  }

  public String getOrdAmt() {
    return ordAmt;
  }

  public void setOrdAmt(String ordAmt) {
    this.ordAmt = ordAmt;
  }

  public String getRetEncodeType() {
    return retEncodeType;
  }

  public void setRetEncodeType(String retEncodeType) {
    this.retEncodeType = retEncodeType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}