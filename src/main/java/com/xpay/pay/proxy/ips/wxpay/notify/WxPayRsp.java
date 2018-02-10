package com.xpay.pay.proxy.ips.wxpay.notify;

import com.xpay.pay.proxy.ips.common.RspHead;

/**
 * Created by sunjian on Date: 2017/12/13 下午1:39
 * Description:
 */
public class WxPayRsp {

  RspHead head;

  Body body;

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