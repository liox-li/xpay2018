package com.xpay.pay.proxy.ips.wxpay.payreq;

/**
 * Created by sunjian on Date: 2018/1/30 下午10:13
 * Description:
 */
public class Body {

  private String merBillno = "";
  private GoodsInfo goodsInfo;
  private String merType = "";
  private String subMerCode = "";
  private String ordAmt = "";
  private String ordTime = "";
  private String merchantUrl = "";
  private String serverUrl = "";
  private String billEXP = "";
  private String reachBy = "";
  private String reachAddress = "";
  private String currencyType = "";
  private String attach = "";
  private String retEncodeType = "";

  public String getMerBillno() {
    return merBillno;
  }

  public void setMerBillno(String merBillno) {
    this.merBillno = merBillno;
  }

  public GoodsInfo getGoodsInfo() {
    return goodsInfo;
  }

  public void setGoodsInfo(GoodsInfo goodsInfo) {
    this.goodsInfo = goodsInfo;
  }

  public String getMerType() {
    return merType;
  }

  public void setMerType(String merType) {
    this.merType = merType;
  }

  public String getSubMerCode() {
    return subMerCode;
  }

  public void setSubMerCode(String subMerCode) {
    this.subMerCode = subMerCode;
  }

  public String getOrdAmt() {
    return ordAmt;
  }

  public void setOrdAmt(String ordAmt) {
    this.ordAmt = ordAmt;
  }

  public String getOrdTime() {
    return ordTime;
  }

  public void setOrdTime(String ordTime) {
    this.ordTime = ordTime;
  }

  public String getMerchantUrl() {
    return merchantUrl;
  }

  public void setMerchantUrl(String merchantUrl) {
    this.merchantUrl = merchantUrl;
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  public String getBillEXP() {
    return billEXP;
  }

  public void setBillEXP(String billEXP) {
    this.billEXP = billEXP;
  }

  public String getReachBy() {
    return reachBy;
  }

  public void setReachBy(String reachBy) {
    this.reachBy = reachBy;
  }

  public String getReachAddress() {
    return reachAddress;
  }

  public void setReachAddress(String reachAddress) {
    this.reachAddress = reachAddress;
  }

  public String getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getRetEncodeType() {
    return retEncodeType;
  }

  public void setRetEncodeType(String retEncodeType) {
    this.retEncodeType = retEncodeType;
  }
}