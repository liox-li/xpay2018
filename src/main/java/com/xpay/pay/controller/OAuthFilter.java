package com.xpay.pay.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.AuthException;
import com.xpay.pay.model.App;
import com.xpay.pay.service.AppService;


public class OAuthFilter implements Filter {
	@Autowired
	protected AppService appService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		String oauth = httpRequest.getHeader(ApplicationConstants.HEADER_OAUTH);
		App app = doAuth(oauth);
		if(app!=null) {
			request.setAttribute(ApplicationConstants.ATTRIBUTE_APP, app);
			chain.doFilter(request, response);
		} else {
			throw new AuthException("-1", "Unauthorized");
		}
		
	}

	@Override
	public void destroy() {
	}
	
	private static final String OAUTH_PREFIX = "OAuth ";
	private static final String CONSUMER_KEY = "oauth_consumer_key";
	private App doAuth(String oauth) {
		if(!StringUtils.startsWith(oauth, OAUTH_PREFIX)) {
			return null;
		}
		String[] keyPairs = oauth.substring(6).split(",");
		KeyValuePair consumerKey = Arrays.stream(keyPairs).map(x -> {
			String[] strArr = x.split("=");
			return new KeyValuePair(strArr[0],strArr[1]);
		}).filter(k -> CONSUMER_KEY.equals(k.getKey())).findAny().orElse(null);
		if(consumerKey == null || StringUtils.isBlank(consumerKey.getValue())) {
			return null;
		}
		App app = appService.findByKey(consumerKey.getValue());
		return app;
	}

}
