package com.xpay.pay.proxy.ips.withdrawal.req;

/**
 * Created by sunjian on Date: 2017/12/26 下午8:59
 * Description:
 */
public class Body {

  private String merBillNo;

  private String customerCode;

  private String pageUrl;

  private String s2sUrl;

  private String bankCard;

  private String bankCode;

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

  public String getPageUrl() {
    return pageUrl;
  }

  public void setPageUrl(String pageUrl) {
    this.pageUrl = pageUrl;
  }

  public String getS2sUrl() {
    return s2sUrl;
  }

  public void setS2sUrl(String s2sUrl) {
    this.s2sUrl = s2sUrl;
  }

  public String getBankCard() {
    return bankCard;
  }

  public void setBankCard(String bankCard) {
    this.bankCard = bankCard;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }
}