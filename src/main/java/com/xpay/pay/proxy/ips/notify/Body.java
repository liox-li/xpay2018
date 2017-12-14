package com.xpay.pay.proxy.ips.notify;

/**
 * Created by sunjian on Date: 2017/12/13 下午1:41
 * Description:
 */
public class Body {

  private String merBillNo;
  private String currencyType;
  private String amount;
  private String date;
  private String status;
  private String msg;
  private String attach;
  private String ipsBillNo;
  private String ipsTradeNo;
  private String retEncodeType;
  private String bankBillNo;
  private String resultType;
  private String ipsBillTime;

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
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

  public String getRetEncodeType() {
    return retEncodeType;
  }

  public void setRetEncodeType(String retEncodeType) {
    this.retEncodeType = retEncodeType;
  }

  public String getBankBillNo() {
    return bankBillNo;
  }

  public void setBankBillNo(String bankBillNo) {
    this.bankBillNo = bankBillNo;
  }

  public String getResultType() {
    return resultType;
  }

  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  public String getIpsBillTime() {
    return ipsBillTime;
  }

  public void setIpsBillTime(String ipsBillTime) {
    this.ipsBillTime = ipsBillTime;
  }
}