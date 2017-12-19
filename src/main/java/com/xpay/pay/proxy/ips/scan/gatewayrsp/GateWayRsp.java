package com.xpay.pay.proxy.ips.scan.gatewayrsp;

import com.xpay.pay.proxy.ips.common.RspHead;

/**
 * Created by sunjian on Date: 2017/12/13 上午10:10
 * Description:
 */
public class GateWayRsp {

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