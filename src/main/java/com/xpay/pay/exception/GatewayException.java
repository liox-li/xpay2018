package com.xpay.pay.exception;

import com.xpay.pay.ApplicationConstants;

public class GatewayException extends ApplicationException {

	private static final long serialVersionUID = -4669030034336752683L;

	public GatewayException(String code, String message) {
		super(ApplicationConstants.STATUS_BAD_GATEWAY, code, message);
	}
	
	public GatewayException(String code, String message, Throwable e) {
		super(ApplicationConstants.STATUS_BAD_GATEWAY, code, message, e);
	}
}
