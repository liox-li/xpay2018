package com.xpay.pay.proxy.ips.transfer.rsp;

/**
 * Created by sunjian on Date: 2017/12/26 下午8:39
 * Description:
 */
public class Head {

  private String version;
  private String signature;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}