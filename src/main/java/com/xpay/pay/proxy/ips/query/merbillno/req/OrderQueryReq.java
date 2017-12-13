package com.xpay.pay.proxy.ips.query.merbillno.req;

import com.xpay.pay.proxy.ips.common.ReqHead;

/**
 * Created by sunjian on Date: 2017/12/13 下午2:01
 * Description:
 */
public class OrderQueryReq {

  private ReqHead head;

  private Body body;

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