package com.xpay.pay.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.xpay.pay.model.Order;
import com.xpay.pay.notify.INotifyHandler;
import com.xpay.pay.notify.NotifyHandlerFactory;
import com.xpay.pay.service.NotifyService;


public class PayNotifyServlet extends HttpServlet {
	private static final long serialVersionUID = -4617898543988945707L;

	protected final Logger logger = LogManager.getLogger("AccessLog");

	@Autowired
	protected NotifyHandlerFactory factory;
	@Autowired
	protected NotifyService notifyService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String uri = StringUtils.isBlank(request.getQueryString())?request.getRequestURI(): request.getRequestURI()+"?"+request.getQueryString();
		INotifyHandler notifyHandler = factory.getNotifyHandler(uri);
		
		NotifyResponse notResp = null;
		try {
			request.setCharacterEncoding(notifyHandler.getCharacterEncoding());
			String content = "";
			if(request.getContentLength()>0) {
				byte[] buffer = new byte[request.getContentLength()];
				IOUtils.readFully(request.getInputStream(), buffer);
				content = new String(buffer);
				logger.info("Notify from " + uri + " content: " + content);
			} else {
				logger.info("Notify from " + uri);
			}
			notResp = notifyHandler.handleNotification(uri, content);
			Order order = notResp == null ? null : notResp.getOrder();
			notifyService.notify(order);
		} catch (Exception e) {
			logger.error("notify failed ", e);
		} finally {
			if(notResp!=null) {
				if(!notResp.isRedirect) {
					response.setCharacterEncoding(notifyHandler.getCharacterEncoding());
					response.setHeader("Content-type", notifyHandler.getContentType());
					response.getWriter().write(notResp.getResp());
				} else {
					response.setCharacterEncoding(notifyHandler.getCharacterEncoding());
					response.setHeader("Content-type", "text/html;charset=UTF-8");
					String redirectUrl = notResp.getOrder().getReturnUrl();
					response.sendRedirect(redirectUrl);

				}
			}
		}
	}

	public static class NotifyResponse {
		private String resp;
		private boolean isRedirect = false;
		private Order order;

		public NotifyResponse(String resp) {
			this.resp = resp;
		}

		public NotifyResponse(String resp, Order order) {
			this.resp = resp;
			this.order = order;
		}

		public String getResp() {
			return resp;
		}

		public void setResp(String resp) {
			this.resp = resp;
		}

		public boolean isRedirect() {
			return isRedirect;
		}

		public void setRedirect(boolean isRedirect) {
			this.isRedirect = isRedirect;
		}

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
		}
	}
}