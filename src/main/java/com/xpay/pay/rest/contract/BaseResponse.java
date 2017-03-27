/**
 * Copyright (c) 2014, Blackboard Inc. All Rights Reserved.
 */
package com.xpay.pay.rest.contract;

import com.xpay.pay.ApplicationConstants;

/**
 * ClassName: BaseResponse Function: Simple Response when error happens
 *
 * @Date: Sep 1, 2014 05:05:20 PM
 */
@SuppressWarnings("rawtypes")
public class BaseResponse<DTO> {
	public static final BaseResponse OK = new BaseResponse();
	public static final BaseResponse NOT_FOUND = new BaseResponse(ApplicationConstants.STATUS_NOT_FOUND,"Not found");
	public static final BaseResponse BAD_REQUEST = new BaseResponse(ApplicationConstants.STATUS_BAD_REQUEST, "Bad request");
	public static final BaseResponse UNAUTHORIZED = new BaseResponse(ApplicationConstants.STATUS_UNAUTHORIZED, "Unauthorized");
	public static final BaseResponse FORBIDDEN = new BaseResponse(ApplicationConstants.STATUS_FORBIDDEN, "Forbidden");
	public static final BaseResponse SERVER_ERROR = new BaseResponse(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR,"Internal server error");
	protected Integer status = ApplicationConstants.STATUS_OK;
	protected String code;
	protected String message;
	protected String developerMessage;
	protected DTO data;

	public BaseResponse() {
	}

	public BaseResponse(Integer status) {
		this.status = status;
	}
	
	public BaseResponse(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public DTO getData() {
		return data;
	}
	public void setData(DTO data) {
		this.data = data;
	}
}
