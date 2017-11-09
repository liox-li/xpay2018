package com.xpay.pay.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.ApplicationException;
import com.xpay.pay.exception.AuthException;
import com.xpay.pay.model.App;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.service.AppService;
import com.xpay.pay.util.JsonUtils;

public class OAuthFilter implements Filter {
	protected final Logger logger = LogManager.getLogger(OAuthFilter.class);
	
	@Autowired
	protected AppService appService;
	private static final SimpleOAuthValidator validator=new SimpleOAuthValidator();
	private static AtomicLong lastReleaseTime = new AtomicLong(System.currentTimeMillis());
	private static final long releasePeriod = 3600000;  // one hour

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		try {
			String token = httpRequest.getHeader(ApplicationConstants.HEADER_ACCESS_TOKEN);
			if(StringUtils.isNotBlank(token) && token.length()>=32) {
				App app = checkToken(token);
				if(app!=null) {
					request.setAttribute(ApplicationConstants.ATTRIBUTE_APP, app);
					chain.doFilter(request, response);
				} else {
					throw new AuthException("401", String.format("Unauthorized, please check your token: %s", token));
				}
			} else {
				String oauth = httpRequest.getHeader(ApplicationConstants.HEADER_OAUTH);
				App app = checkApp(oauth);
				if (app != null) {
					request.setAttribute(ApplicationConstants.ATTRIBUTE_APP, app);
					checkOAuth(httpRequest, app);
					chain.doFilter(request, response);
				} else {
					throw new AuthException("401", "Unauthorized, please check your key/secret");
				}
			}
		} catch (ApplicationException e) {
			logger.error("Unauthorized request", e);
			printErrorResponse(response, e);
		} finally {
			if(System.currentTimeMillis()- lastReleaseTime.get()>releasePeriod) {
				validator.releaseGarbage();
				lastReleaseTime.set(System.currentTimeMillis());
			}
		}
	}

	private App checkToken(String token) {
		String aToken = token.substring(0,32);
		String appKey = token.substring(32);
		App app = appService.findByToken(aToken);
		if(app!=null) {
			return app.getKey().replace("-", "").equals(appKey)?app: null;
		}
		return app;
	}

	@Override
	public void destroy() {
	}

	private static final String OAUTH_PREFIX = "OAuth ";
	private static final String CONSUMER_KEY = "oauth_consumer_key";

	private App checkApp(String oauth) {
		if (!StringUtils.startsWith(oauth, OAUTH_PREFIX)) {
			return null;
		}
		String[] keyPairs = oauth.substring(6).split(",");
		KeyValuePair consumerKey = Arrays
				.stream(keyPairs)
				.map(x -> {
					String[] strArr = x.trim().split("=");
					return new KeyValuePair(strArr[0], strArr[1].substring(1,
							strArr[1].length() - 1));
				}).filter(k -> CONSUMER_KEY.equals(k.getKey())).findAny()
				.orElse(null);
		if (consumerKey == null || StringUtils.isBlank(consumerKey.getValue())) {
			return null;
		}
		App app = appService.findByKey(consumerKey.getValue());
		return app;
	}
	
	private void checkOAuth(HttpServletRequest httpRequest, App app) {
		OAuthMessage message=OAuthServlet.getMessage(httpRequest,null);
		OAuthConsumer consumer=new OAuthConsumer(null, app.getKey(), app.getSecret(), null);
		consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
		OAuthAccessor accessor=new OAuthAccessor(consumer);
		
		try {
			validator.validateMessage(message,accessor);
		} catch (Exception e) {
			throw new AuthException("401", e.getMessage());
		}
	}

	private void printErrorResponse(ServletResponse response,
			ApplicationException exception) throws JsonProcessingException, IOException {
		BaseResponse<?> errorResponse = new BaseResponse<Object>();
		errorResponse.setStatus(exception.getStatus());
		errorResponse.setMessage("The request was rejected because authentication failed.");
		errorResponse.setCode(exception.getCode());

		if (response instanceof HttpServletResponse) {
			((HttpServletResponse) response).setContentType("application/json");
			((HttpServletResponse) response).setStatus(exception.getStatus());
		}

		response.getOutputStream().println(JsonUtils.toJson(errorResponse));
	}

}
