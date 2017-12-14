package com.xpay.pay.proxy.ips.refund.req;

import com.xpay.pay.proxy.ips.common.ReqHead;

/**
 * Created by sunjian on Date: 2017/12/13 下午2:31
 * Description:
 */
public class RefundReq {

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