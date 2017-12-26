package com.xpay.pay.proxy.ips.withdrawal.rsp;

import com.xpay.pay.proxy.ips.common.ResponseHead;

/**
 * Created by sunjian on Date: 2017/12/26 下午9:00
 * Description:
 */
public class WithdrawalRespXml {

  private ResponseHead head;

  private Body body;

  public ResponseHead getHead() {
    return head;
  }

  public void setHead(ResponseHead head) {
    this.head = head;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }
}