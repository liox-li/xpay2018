package com.xpay.pay.proxy.ips.query.merbillno.rsp;

import com.xpay.pay.proxy.ips.common.RspHead;

/**
 * Created by sunjian on Date: 2017/12/13 下午2:10
 * Description:
 */
public class OrderQueryRsp {

  private RspHead head;

  private Body body;

  public RspHead getHead() {
    return head;
  }

  public void setHead(RspHead head) {
    this.head = head;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }
}