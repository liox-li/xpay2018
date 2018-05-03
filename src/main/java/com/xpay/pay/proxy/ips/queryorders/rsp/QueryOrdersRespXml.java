package com.xpay.pay.proxy.ips.queryorders.rsp;

import com.xpay.pay.proxy.ips.common.ResponseHead;

public class QueryOrdersRespXml {
	
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
