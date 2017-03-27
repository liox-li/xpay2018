package com.xpay.pay.exception;

import static com.xpay.pay.ApplicationConstants.CODE_COMMON;
import static com.xpay.pay.ApplicationConstants.STATUS_BAD_REQUEST;
import static com.xpay.pay.ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.xpay.pay.rest.contract.BaseResponse;

@ControllerAdvice
@SuppressWarnings("rawtypes")
public class CommonExceptionsHandler {

	private static final Logger LOG = LogManager
			.getLogger(CommonExceptionsHandler.class);

	/**
	 * 
	 * mbaasExceptionHandler
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ApplicationException.class)
	public @ResponseBody ResponseEntity<BaseResponse> applicationExceptionHandler(
			ApplicationException e) {
		LOG.error(e.getMessage(), e);

		BaseResponse response = new BaseResponse();
		response.setMessage(e.getMessage());
		response.setStatus(e.getStatus());
		response.setCode(e.getCode());
		ResponseEntity<BaseResponse> responseEntity = new ResponseEntity<BaseResponse>(
				response, HttpStatus.valueOf(response.getStatus()));
		return responseEntity;
	}

	/**
	 * 
	 * httpServletRequestBindingExceptionHandler:
	 * 
	 * @param throwable
	 * @return
	 */
	@ExceptionHandler({ ServletRequestBindingException.class,
			IllegalArgumentException.class,
			HttpRequestMethodNotSupportedException.class,
			TypeMismatchException.class, HttpMessageNotReadableException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody BaseResponse httpServletRequestBindingExceptionHandler(
			Throwable throwable) {
		LOG.error(
				"Bad Request, please check your HTTP request url, header, parameter.",
				throwable);
		BaseResponse response = new BaseResponse();
		response.setCode(CODE_COMMON);
		response.setStatus(STATUS_BAD_REQUEST);
		response.setMessage("Bad Request, please check your HTTP request: "
				+ throwable.getMessage());
		return response;
	}

	/**
	 * 
	 * sqlExceptionHandler:
	 * 
	 * @param throwable
	 * @return
	 */
	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody BaseResponse handleSqlException(SQLException e) {
		LOG.error(
				"Xpay internal server error caused by invalid sql, please try later.",
				e);

		BaseResponse response = new BaseResponse();
		response.setCode(CODE_COMMON);
		response.setStatus(STATUS_INTERNAL_SERVER_ERROR);
		response.setMessage(e.getMessage());
		return response;
	}

	/**
	 * Unexpected Exception handler, returning 500 status code, we should
	 * prevent this kind of case as much as possible
	 * 
	 * @param throwable
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody BaseResponse runtimeExceptionHandler(
			Throwable throwable) {
		LOG.error("Xpay internal server error, please try later.", throwable);
		BaseResponse response = new BaseResponse();
		response.setCode(CODE_COMMON);
		response.setStatus(STATUS_INTERNAL_SERVER_ERROR);
		response.setMessage(throwable.getMessage());
		return response;
	}

	public static String printExceptionStackTrace(Throwable e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			return sw.toString();
		} finally {
			IOUtils.closeQuietly(sw);
			IOUtils.closeQuietly(pw);
		}
	}
}
