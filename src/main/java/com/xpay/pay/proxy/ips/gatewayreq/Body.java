package com.xpay.pay.proxy.ips.gatewayreq;

/**
 * Created by sunjian on Date: 2017/12/12 下午11:05
 * Description:
 */
public class Body {

  private String merBillNo;
  private String gatewayType;
  private String merType;
  private String subMerCode;
  private String date;
  private String currencyType;
  private String amount;
  private String lang;
  private String attach;
  private String retEncodeType;
  private String serverUrl;
  private String billEXP;
  private String goodsName;

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getGatewayType() {
    return gatewayType;
  }

  public void setGatewayType(String gatewayType) {
    this.gatewayType = gatewayType;
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

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
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

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
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

  public String getGoodsName() {
    return goodsName;
  }

  public void setGoodsName(String goodsName) {
    this.goodsName = goodsName;
  }
}