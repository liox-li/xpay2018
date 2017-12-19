package com.xpay.pay.proxy.ips.query.ips.rsp;

/**
 * Created by sunjian on Date: 2017/12/18 下午5:14
 * Description:
 */
public class Body {

  private String merBillNo;

  private String ipsBillNo;

  private String ipsTradeNo;

  private String tradeType;

  private String currency;

  private String amount;

  private String merBillDate;

  private String ipsTradeTime;

  private String attach;

  private String status;

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getIpsBillNo() {
    return ipsBillNo;
  }

  public void setIpsBillNo(String ipsBillNo) {
    this.ipsBillNo = ipsBillNo;
  }

  public String getIpsTradeNo() {
    return ipsTradeNo;
  }

  public void setIpsTradeNo(String ipsTradeNo) {
    this.ipsTradeNo = ipsTradeNo;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getMerBillDate() {
    return merBillDate;
  }

  public void setMerBillDate(String merBillDate) {
    this.merBillDate = merBillDate;
  }

  public String getIpsTradeTime() {
    return ipsTradeTime;
  }

  public void setIpsTradeTime(String ipsTradeTime) {
    this.ipsTradeTime = ipsTradeTime;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}