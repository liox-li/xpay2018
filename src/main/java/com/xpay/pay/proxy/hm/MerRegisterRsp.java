package com.xpay.pay.proxy.hm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sunjian on Date: 2018/2/9 下午2:35
 * Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerRegisterRsp {

  private String merCode;
  private String statusCode;
  private String statusMsg;

  public String getMerCode() {
    return merCode;
  }

  public void setMerCode(String merCode) {
    this.merCode = merCode;
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
}