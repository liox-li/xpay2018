package com.xpay.pay.proxy.ips.queryorders.req;

import com.xpay.pay.proxy.ips.common.RequestHead;

public class QueryOrdersReqXml {
	
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
