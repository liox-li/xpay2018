package com.xpay.pay.proxy.ips.refund.rsp;

/**
 * Created by sunjian on Date: 2017/12/13 下午3:00
 * Description:
 */
public class Body {

  private String canRefundAmount;
  private String realRefundAmount;
  private String ipsBillNo;
  private String refundTradeNo;
  private String ipsTradeTime;
  private String status;

  public String getCanRefundAmount() {
    return canRefundAmount;
  }

  public void setCanRefundAmount(String canRefundAmount) {
    this.canRefundAmount = canRefundAmount;
  }

  public String getRealRefundAmount() {
    return realRefundAmount;
  }

  public void setRealRefundAmount(String realRefundAmount) {
    this.realRefundAmount = realRefundAmount;
  }

  public String getIpsBillNo() {
    return ipsBillNo;
  }

  public void setIpsBillNo(String ipsBillNo) {
    this.ipsBillNo = ipsBillNo;
  }

  public String getRefundTradeNo() {
    return refundTradeNo;
  }

  public void setRefundTradeNo(String refundTradeNo) {
    this.refundTradeNo = refundTradeNo;
  }

  public String getIpsTradeTime() {
    return ipsTradeTime;
  }

  public void setIpsTradeTime(String ipsTradeTime) {
    this.ipsTradeTime = ipsTradeTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}