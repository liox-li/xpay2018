package com.xpay.pay.proxy.hm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sunjian on Date: 2018/2/9 下午2:39
 * Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranRsp {

  private String merCode;

  private String orderNo;

  private String orderDate;

  private String orderTime;

  private String currencyCode;

  private String transDate;

  private String transStatus;

  private String transAmount;

  private String actualAmount;

  private String transSeq;

  private String merReserved;

  private String statusCode;

  private String statusMsg;

  private String creCardId;

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

  public String getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(String orderTime) {
    this.orderTime = orderTime;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
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

  public String getMerReserved() {
    return merReserved;
  }

  public void setMerReserved(String merReserved) {
    this.merReserved = merReserved;
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

  public String getCreCardId() {
    return creCardId;
  }

  public void setCreCardId(String creCardId) {
    this.creCardId = creCardId;
  }
}