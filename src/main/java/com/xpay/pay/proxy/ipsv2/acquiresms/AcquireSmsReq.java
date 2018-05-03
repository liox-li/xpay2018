package com.xpay.pay.proxy.ipsv2.acquiresms;

import com.xpay.pay.proxy.ips.common.RspHead;
import com.xpay.pay.proxy.ipsv2.common.Body;

public class AcquireSmsReq {
	
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
