package com.xpay.pay.proxy.ips.withdrawal.req;

import com.xpay.pay.proxy.ips.common.RequestHead;

/**
 * Created by sunjian on Date: 2017/12/26 下午8:58
 * Description:
 */
public class WithdrawalReqXml {

  private RequestHead head;

  private Body body;

  public RequestHead getHead() {
    return head;
  }

  public void setHead(RequestHead head) {
    this.head = head;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }
}