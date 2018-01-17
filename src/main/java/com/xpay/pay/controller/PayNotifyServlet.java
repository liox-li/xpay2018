package com.xpay.pay.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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


import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.model.Order;
import com.xpay.pay.notify.INotifyHandler;
import com.xpay.pay.notify.NotifyHandlerFactory;
import com.xpay.pay.proxy.notify.NotifyProxy;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.NotificationResponse;import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.JsonUtils;


public class PayNotifyServlet extends HttpServlet {
	private static final long serialVersionUID = -4617898543988945707L;

	protected final Logger logger = LogManager.getLogger("AccessLog");

	@Autowired
	protected NotifyHandlerFactory factory;
	@Autowired
	protected NotifyProxy notifyProxy;

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
			notify(order);
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

	private static final Executor executor = Executors.newFixedThreadPool(80);
	@SuppressWarnings("rawtypes")
	private void notify(final Order order) {
		if (order != null && StringUtils.isNotBlank(order.getNotifyUrl())
				&& order.isSettle()) {
			CompletableFuture.runAsync(() -> {
				NotificationResponse notification = this.toNotResponse(order);
				if(order.getStoreChannel()!=null&& order.getStoreChannelId()>0) {
					notification.setChannelNo(order.getStoreChannelId());
				}
				String sign = CryptoUtils.signQueryParams(notification.toKeyValuePairs(), null, order.getApp().getSecret());
				notification.setSign(sign);
				BaseResponse response = null;
				boolean storeNotified = false;
				boolean proxyNotified = false;
				for (int i=0;i<3;i++) {
					try {
						if(!storeNotified) {
							response = notifyProxy.notify(order.getNotifyUrl(), order.getApp(), notification);
							if(response != null && response.getStatus()==ApplicationConstants.STATUS_OK) {
								storeNotified = true;
							}
						}
					} catch(Exception e) {
						logger.warn("notify failed", e);
					}
					
					try {
						if(!proxyNotified && StringUtils.isNotBlank(order.getStore().getProxyUrl())) {
							notification.setStoreId(order.getStore().getCode());
							notification.setStoreName(order.getStore().getName());
							notification.setChannelNo(order.getStoreChannelId());
							response = notifyProxy.notify(order.getStore().getProxyUrl(), order.getApp(), notification);
							if(response != null && response.getStatus()==ApplicationConstants.STATUS_OK) {
								proxyNotified = true;
							}
						} else {
							proxyNotified = true;
						}
					} catch(Exception e) {
						
					}
					
					if(storeNotified && proxyNotified) {
						return;
					}
					
					try {
						Thread.sleep(30000);
					} catch (Exception e) {
					}
				}
			}, executor);
		}
	}
	private NotificationResponse toNotResponse(Order order) {
		try {
			NotificationResponse notification = new NotificationResponse();
			notification.setOrderNo(order.getOrderNo());
			notification.setExtOrderNo(order.getExtOrderNo());
			notification.setTargetOrderNo(order.getTargetOrderNo());
			if(order.getGoods()!=null) {
				notification.setCodeUrl(GoodsQrCodeServlet.QR_CODE_PREFIX+order.getGoods().getCode()+"?uid="+order.getUid());
				notification.setSubject(order.getSubject());
			} else {
				notification.setSellerOrderNo(order.getSellerOrderNo());
				notification.setCodeUrl(order.getCodeUrl());
			}
			
			notification.setPrepayId(order.getPrepayId());
			notification.setTokenId(order.getTokenId());
			notification.setTotalFee(order.getTotalFee());
			notification.setOrderStatus(order.getStatus().getValue());
			notification.setAttach(order.getAttach());
			notification.setUid(order.getUid());
			return notification;
		} catch(Exception e) {
			logger.error("convert to NotResponse failed", e);
			return null;
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