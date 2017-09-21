package com.xpay.pay.proxy.kekepay;

/**
 * Created by sunjian on Date: 2017/9/21 下午6:07
 * Description:
 */
public class KekePayQueryResponse {
  public static final String SUCCESS = "0000";
  private String resultCode;
  private String outTradeNo;
  private String orderStatus;
  private String orderPrice;
  private String trxNo;
  private String completeDate;//yyyyMMddHHmmss
  private String errMsg;
  private String sign;

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
  }

  public String getTrxNo() {
    return trxNo;
  }

  public void setTrxNo(String trxNo) {
    this.trxNo = trxNo;
  }

  public String getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(String completeDate) {
    this.completeDate = completeDate;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }
}