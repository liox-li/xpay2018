package com.xpay.pay.proxy.supay;

/**
 * Created by sunjian on Date: 2017/12/14 下午11:46
 * Description:
 */
public class SUPayResponse {

  private String code;

  private String msg;

  private String date;

  private String sign;

  private String result;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}