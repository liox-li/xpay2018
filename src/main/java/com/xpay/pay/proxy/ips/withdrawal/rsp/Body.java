package com.xpay.pay.proxy.ips.withdrawal.rsp;

/**
 * Created by sunjian on Date: 2017/12/26 下午9:01
 * Description:
 */
public class Body {

  private String ipsBillNo;
  private String merBillNo;
  private String customerCode;
  private String amount;
  private String tradeState;
  private String failMsg;

  public String getIpsBillNo() {
    return ipsBillNo;
  }

  public void setIpsBillNo(String ipsBillNo) {
    this.ipsBillNo = ipsBillNo;
  }

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getCustomerCode() {
    return customerCode;
  }

  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getTradeState() {
    return tradeState;
  }

  public void setTradeState(String tradeState) {
    this.tradeState = tradeState;
  }

  public String getFailMsg() {
    return failMsg;
  }

  public void setFailMsg(String failMsg) {
    this.failMsg = failMsg;
  }
}