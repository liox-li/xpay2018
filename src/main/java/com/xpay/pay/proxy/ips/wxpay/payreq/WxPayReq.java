package com.xpay.pay.proxy.ips.wxpay.payreq;

import com.xpay.pay.proxy.ips.common.ReqHead;

/**
 * Created by sunjian on Date: 2018/1/30 下午10:12
 * Description:
 */
public class WxPayReq {

  ReqHead head;

  Body body;

  public ReqHead getHead() {
    return head;
  }

  public void setHead(ReqHead head) {
    this.head = head;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }
}