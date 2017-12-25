package com.xpay.pay.proxy.ips.useropen;

/**
 * Created by sunjian on Date: 2017/12/25 下午4:21
 * Description:
 */
public class IpsResponse {

  private String argMerCode;

  private String rspCode;

  private String rspMsg;

  private String p3DesXmlPara;

  public String getArgMerCode() {
    return argMerCode;
  }

  public void setArgMerCode(String argMerCode) {
    this.argMerCode = argMerCode;
  }

  public String getRspCode() {
    return rspCode;
  }

  public void setRspCode(String rspCode) {
    this.rspCode = rspCode;
  }

  public String getRspMsg() {
    return rspMsg;
  }

  public void setRspMsg(String rspMsg) {
    this.rspMsg = rspMsg;
  }

  public String getP3DesXmlPara() {
    return p3DesXmlPara;
  }

  public void setP3DesXmlPara(String p3DesXmlPara) {
    this.p3DesXmlPara = p3DesXmlPara;
  }
}