package com.xpay.pay.exception;


public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 5980518154787111827L;

	protected int status;

	protected String code;

	protected String developerMessage;

	public ApplicationException(int status, String code, String message) {
		super(message);
		this.status = status;
		this.code = code;
		this.developerMessage = "/";
	}

	public ApplicationException(int status, String code, String message, Throwable e) {
		super(message, e);
		this.status = status;
		this.code = code;
		this.developerMessage = CommonExceptionsHandler.printExceptionStackTrace(e);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
}