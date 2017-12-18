package com.xpay.pay.proxy.ips.query.merbillno.rsp;

/**
 * Created by sunjian on Date: 2017/12/13 下午2:10
 * Description:
 */
public class Body {

  private String merBillNo;
  private String ipsBillNo;
  private String tradeType;
  private String currency;
  private String amount;
  private String merBillDate;
  private String ipsBillTime;
  private String attach;
  private String status;
  private String refundAmount;
  private String refuseAmout;

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

  public String getIpsBillTime() {
    return ipsBillTime;
  }

  public void setIpsBillTime(String ipsBillTime) {
    this.ipsBillTime = ipsBillTime;
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

  public String getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(String refundAmount) {
    this.refundAmount = refundAmount;
  }

  public String getRefuseAmout() {
    return refuseAmout;
  }

  public void setRefuseAmout(String refuseAmout) {
    this.refuseAmout = refuseAmout;
  }
}