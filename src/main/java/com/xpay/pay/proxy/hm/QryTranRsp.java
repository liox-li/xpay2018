package com.xpay.pay.proxy.hm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sunjian on Date: 2018/2/9 下午3:01
 * Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QryTranRsp {

  private String merCode;
  private String orderNo;
  private String transDate;
  private String transStatus;
  private String transResultMsg;
  private String transAmount;
  private String actualAmount;
  private String transSeq;
  private String statusCode;
  private String statusMsg;
  private String reserved;

  public String getMerCode() {
    return merCode;
  }

  public void setMerCode(String merCode) {
    this.merCode = merCode;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getTransDate() {
    return transDate;
  }

  public void setTransDate(String transDate) {
    this.transDate = transDate;
  }

  public String getTransStatus() {
    return transStatus;
  }

  public void setTransStatus(String transStatus) {
    this.transStatus = transStatus;
  }

  public String getTransResultMsg() {
    return transResultMsg;
  }

  public void setTransResultMsg(String transResultMsg) {
    this.transResultMsg = transResultMsg;
  }

  public String getTransAmount() {
    return transAmount;
  }

  public void setTransAmount(String transAmount) {
    this.transAmount = transAmount;
  }

  public String getActualAmount() {
    return actualAmount;
  }

  public void setActualAmount(String actualAmount) {
    this.actualAmount = actualAmount;
  }

  public String getTransSeq() {
    return transSeq;
  }

  public void setTransSeq(String transSeq) {
    this.transSeq = transSeq;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg) {
    this.statusMsg = statusMsg;
  }

  public String getReserved() {
    return reserved;
  }

  public void setReserved(String reserved) {
    this.reserved = reserved;
  }
}