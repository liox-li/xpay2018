package com.xpay.pay.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.App;

public abstract class AuthRestService {
//	@Autowired
	private HttpServletRequest request;
	
	protected App getApp() {
		App app = (App)request.getAttribute(ApplicationConstants.ATTRIBUTE_APP);
		Assert.notNull(app, "No valid app found");
		return app;
	}
}
