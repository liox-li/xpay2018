package com.xpay.pay.proxy.ips.transfer.rsp;

/**
 * Created by sunjian on Date: 2017/12/26 下午8:39
 * Description:
 */
public class Body {

  private String ipsBillNo;
  private String tradeId;
  private String ipsFee;
  private String tradeState;

  public String getIpsBillNo() {
    return ipsBillNo;
  }

  public void setIpsBillNo(String ipsBillNo) {
    this.ipsBillNo = ipsBillNo;
  }

  public String getTradeId() {
    return tradeId;
  }

  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  public String getIpsFee() {
    return ipsFee;
  }

  public void setIpsFee(String ipsFee) {
    this.ipsFee = ipsFee;
  }

  public String getTradeState() {
    return tradeState;
  }

  public void setTradeState(String tradeState) {
    this.tradeState = tradeState;
  }
}