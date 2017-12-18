package com.xpay.pay.proxy.ips.query.ips.rsp;

import com.xpay.pay.proxy.ips.common.RspHead;

/**
 * Created by sunjian on Date: 2017/12/18 下午5:14
 * Description:
 */
public class TradeQueryRsp {

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