package com.xpay.pay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class AccessLogFilter implements Filter {

	private static final Logger performanceLogger = LogManager.getLogger("PerformanceLog");
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		ServletRequest requestToUse = request;
		ServletResponse responseToUse = response;
		try {
			if (request instanceof HttpServletRequest) {
				requestToUse = new ContentCachingRequestWrapper((HttpServletRequest) request);
			}
			if (response instanceof HttpServletResponse) {
				responseToUse = new ContentCachingResponseWrapper((HttpServletResponse) response);
			}
			chain.doFilter(requestToUse, responseToUse);
		} finally {
			afterRequest(requestToUse, responseToUse, start);
		}
	}

	@Override
	public void destroy() {
	}
	
	protected void afterRequest(ServletRequest request, ServletResponse response, long start) {
		// only log HttpServletRequest
		if (request instanceof ContentCachingRequestWrapper && response instanceof ContentCachingResponseWrapper) {
			try {
				ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
				ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
				
				EmbbedRequestResponseWrapper wrapper = new EmbbedRequestResponseWrapper(requestWrapper, responseWrapper, start);
				performanceLogger.info(wrapper.toShortString());
			} catch (Throwable e) {
				// do nothing
			}
		}
	}
	
	static class EmbbedRequestResponseWrapper {
		private String method;
		private String uri;
		private String client;
		private String session;
		private String user;
		private String header;
		private String payload;
		private int status;
		private String response;
		private long time;

		public EmbbedRequestResponseWrapper(ContentCachingRequestWrapper request,
				ContentCachingResponseWrapper response, long start) throws IOException {
			this.time = System.currentTimeMillis() - start;
			this.method = request.getMethod();
			this.uri = request.getRequestURI();
			String queryString = request.getQueryString();
			if (StringUtils.isNotEmpty(queryString)) {
				this.uri += "?" + queryString;
			}
			this.client = request.getRemoteAddr();
			this.user = request.getRemoteUser();
			setSession(request);
			setRequestHeader(request);
			setRequestPayload(request);
			this.status = response.getStatus();

			setResponseBody(response);
		}

		protected void setSession(ContentCachingRequestWrapper request) {
			HttpSession session = request.getSession(false);
			if (this.session != null) {
				this.session = session.getId();
			}
		}

		protected void setRequestHeader(ContentCachingRequestWrapper request) {
			StringBuilder headerSb = new StringBuilder();
			Enumeration<String> enumeration = request.getHeaderNames();
			int idx = 0;
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				if (idx == 0) {
					headerSb.append(name).append("=").append(request.getHeader(name));
				} else {
					headerSb.append(";").append(name).append("=").append(request.getHeader(name));
				}
				idx ++;
			}
			this.header = headerSb.toString();
		}

		protected void setRequestPayload(ContentCachingRequestWrapper request) {
			byte[] buf = request.getContentAsByteArray();
			if (buf.length > 0) {
				int length = buf.length;
				try {
					this.payload = new String(buf, 0, length, request.getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {
					this.payload = "[unknown]";
				}
			} else {
				// if request body is not cached (means not read before), we
				// read it from input stream
				try {
					if (request.getInputStream() != null) {
						this.payload = IOUtils.toString(request.getInputStream());
					}
				} catch (IOException e) {
					this.payload = "[unknown]";
				}
			}
		}

		protected void setResponseBody(ContentCachingResponseWrapper response) throws IOException {
			byte[] buf = response.getContentAsByteArray();
			if (buf.length > 0) {
				int length = buf.length;
				try {
					this.response = new String(buf, 0, length, response.getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {
					this.response = "[unknown]";
				}
				// copy body back to raw response
				StreamUtils.copy(buf, response.getResponse().getOutputStream());
			}
		}

		public String getMethod() {
			return method;
		}

		public String getUri() {
			return uri;
		}

		public String getClient() {
			return client;
		}

		public String getSession() {
			return session;
		}

		public String getUser() {
			return user;
		}

		public String getHeader() {
			return header;
		}

		public String getPayload() {
			return payload;
		}

		public int getStatus() {
			return status;
		}

		public String getResponse() {
			return response;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public void setClient(String client) {
			this.client = client;
		}

		public void setSession(String session) {
			this.session = session;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public void setPayload(String payload) {
			this.payload = payload;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[method=").append(method);
			sb.append(", uri=").append(uri);
			sb.append(", client=").append(client);
			sb.append(", user=").append(user);
			sb.append(", header=").append(header);
			sb.append(", payload=").append(payload);
			sb.append(", status=").append(status);
			sb.append(", time=").append(time).append("ms]");
			return sb.toString();
		}
		
		public String toShortString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[method=").append(method);
			sb.append(", uri=").append(uri);
			sb.append(", time=").append(time).append("ms]");
			return sb.toString();
		}

	}

}
