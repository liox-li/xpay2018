package com.xpay.pay.exception;

import com.xpay.pay.ApplicationConstants;

public class DBException extends ApplicationException {

	private static final long serialVersionUID = -7864837566884808072L;

	public DBException(String code, String message) {
		super(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR, code, message);
	}
	
	public DBException(String code, String message, Throwable e) {
		super(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR, code, message, e);
	}
}
