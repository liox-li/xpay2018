package com.xpay.pay.proxy.supay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sunjian on Date: 2017/12/14 下午11:47
 * Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResult {

  private String merOrderId;

  private String platformOrderId;

  private Integer amount;

  private String status;

  private String payCode;

  private String parameter1;

  private String extents;

  private String remarks;

  public String getMerOrderId() {
    return merOrderId;
  }

  public void setMerOrderId(String merOrderId) {
    this.merOrderId = merOrderId;
  }

  public String getPlatformOrderId() {
    return platformOrderId;
  }

  public void setPlatformOrderId(String platformOrderId) {
    this.platformOrderId = platformOrderId;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPayCode() {
    return payCode;
  }

  public void setPayCode(String payCode) {
    this.payCode = payCode;
  }

  public String getParameter1() {
    return parameter1;
  }

  public void setParameter1(String parameter1) {
    this.parameter1 = parameter1;
  }

  public String getExtents() {
    return extents;
  }

  public void setExtents(String extents) {
    this.extents = extents;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}