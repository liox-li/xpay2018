package com.xpay.pay.exception;

import static com.xpay.pay.ApplicationConstants.CODE_COMMON;
import static com.xpay.pay.ApplicationConstants.STATUS_BAD_REQUEST;
import static com.xpay.pay.ApplicationConstants.STATUS_NOT_FOUND;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class Assert {
	@SuppressWarnings("rawtypes")
	public static void notEmpty(Collection coll, String message) {
		if (CollectionUtils.isEmpty(coll)) {
			throw new ApplicationException(STATUS_NOT_FOUND, CODE_COMMON, message);
		}
	}

	public static void notEmpty(String field, int statusCode, String errorCode, String message) {
		if (StringUtils.isEmpty(field)) {
			throw new ApplicationException(statusCode, errorCode, message);
		}
	}

	public static void notBlank(String field, int statusCode, String errorCode, String message) {
		if (StringUtils.isBlank(field)) {
			throw new ApplicationException(statusCode, errorCode, message);
		}
	}
	
	public static void notBlank(String field, String message) {
		if (StringUtils.isBlank(field)) {
			throw new ApplicationException(STATUS_BAD_REQUEST, CODE_COMMON, message);
		}
	}


	/**
	 * notNull: throw exception if object is null
	 * 
	 * @param object
	 * @param message
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new ApplicationException(STATUS_BAD_REQUEST, CODE_COMMON, message);
		}
	}
	
	public static void notNull(Object obj, int statusCode, String errorCode, String message) {
		if (null == obj) {
			throw new ApplicationException(statusCode, errorCode, message);
		}
	}
	/**
	 * isTrue: throw exception if expression is not true
	 * 
	 * @param expression
	 * @param message
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new ApplicationException(STATUS_BAD_REQUEST, CODE_COMMON, message);
		}
	}
	
	/**
	 * isTrue: throw exception if expression is not true
	 * 
	 * @param expression
	 * @param message
	 */
	public static void isTrue(boolean expression, int statusCode, String errorCode,String message) {
		if (!expression) {
			throw new ApplicationException(statusCode, errorCode, message);
		}
	}

}
