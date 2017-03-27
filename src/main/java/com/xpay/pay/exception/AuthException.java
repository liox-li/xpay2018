package com.xpay.pay.exception;

import com.xpay.pay.ApplicationConstants;

public class AuthException extends ApplicationException{

	private static final long serialVersionUID = -6652982171980104523L;

	public AuthException(String code, String message) {
		super(ApplicationConstants.STATUS_UNAUTHORIZED, code, message);
	}
	
	public AuthException(String code, String message, Throwable e) {
		super(ApplicationConstants.STATUS_UNAUTHORIZED, code, message, e);
	}
}
