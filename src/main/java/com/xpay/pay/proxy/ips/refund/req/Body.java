package com.xpay.pay.proxy.ips.refund.req;

/**
 * Created by sunjian on Date: 2017/12/13 下午2:32
 * Description:
 */
public class Body {

  private String orgMerTime;

  private String orgMerBillNo;

  private String billAmount;

  private String refundAmount;

  private String refundMemo;

  private String merBillNo;

  public String getOrgMerTime() {
    return orgMerTime;
  }

  public void setOrgMerTime(String orgMerTime) {
    this.orgMerTime = orgMerTime;
  }

  public String getOrgMerBillNo() {
    return orgMerBillNo;
  }

  public void setOrgMerBillNo(String orgMerBillNo) {
    this.orgMerBillNo = orgMerBillNo;
  }

  public String getBillAmount() {
    return billAmount;
  }

  public void setBillAmount(String billAmount) {
    this.billAmount = billAmount;
  }

  public String getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(String refundAmount) {
    this.refundAmount = refundAmount;
  }

  public String getRefundMemo() {
    return refundMemo;
  }

  public void setRefundMemo(String refundMemo) {
    this.refundMemo = refundMemo;
  }

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }
}