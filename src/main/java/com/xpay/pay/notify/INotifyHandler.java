package com.xpay.pay.notify;

import com.xpay.pay.controller.PayNotifyServlet.NotifyResponse;

public interface INotifyHandler {
	NotifyResponse handleNotification(String content);
	
	boolean validateSignature(String content);
	
	String getContentType();
	
	String getCharacterEncoding();
}
