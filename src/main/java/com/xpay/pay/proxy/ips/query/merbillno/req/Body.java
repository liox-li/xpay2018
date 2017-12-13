package com.xpay.pay.proxy.ips.query.merbillno.req;

/**
 * Created by sunjian on Date: 2017/12/13 下午2:02
 * Description:
 */
public class Body {

  private String merBillNo;

  private String date;

  private String amount;

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }
}