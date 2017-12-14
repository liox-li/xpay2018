package com.xpay.pay.proxy.ips.gatewayreq;

import com.xpay.pay.proxy.ips.common.ReqHead;

/**
 * Created by sunjian on Date: 2017/12/12 下午11:04
 * Description:
 */
public class GateWayReq {

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