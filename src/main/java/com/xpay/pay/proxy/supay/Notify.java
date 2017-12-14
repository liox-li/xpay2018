package com.xpay.pay.proxy.supay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sunjian on Date: 2017/12/15 上午12:23
 * Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notify {

  private String merOrderId;

  private String platformOrderId;

  private Integer amount;

  private String status;

  private String payCode;

  private String channelNo;

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

  public String getChannelNo() {
    return channelNo;
  }

  public void setChannelNo(String channelNo) {
    this.channelNo = channelNo;
  }
}