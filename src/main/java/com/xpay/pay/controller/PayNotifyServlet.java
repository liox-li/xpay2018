package com.xpay.pay.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
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
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.chinaums.ChinaUmsProxy;
import com.xpay.pay.proxy.juzhen.JuZhenNotification;
import com.xpay.pay.proxy.juzhen.JuZhenProxy;
import com.xpay.pay.proxy.notify.NotifyProxy;
import com.xpay.pay.proxy.rubipay.RubiPayProxy;
import com.xpay.pay.proxy.swiftpass.SwiftpassProxy;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.OrderResponse;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.XmlUtils;

public class PayNotifyServlet extends HttpServlet {
	private static final long serialVersionUID = -4617898543988945707L;

	protected final Logger logger = LogManager.getLogger("AccessLog");

	@Autowired
	protected OrderService orderService;
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
		String uri = request.getRequestURI();

		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		NotifyResponse notResp = null;
		Order order = null;
		try {
			request.setCharacterEncoding("utf-8");
			byte[] buffer = new byte[request.getContentLength()];
			IOUtils.readFully(request.getInputStream(), buffer);
			String content = new String(buffer);
			logger.info("Notify from " + uri + " content: " + content);

			if (uri.contains(PaymentGateway.SWIFTPASS.name().toLowerCase())) {
				notResp = handleSwiftpassNotification(content);
			} else if (uri.contains(PaymentGateway.CHINAUMS.name()
					.toLowerCase())) {
				notResp = handleChinaUmsNotification(content);
			} else if (uri
					.contains(PaymentGateway.RUBIPAY.name().toLowerCase())) {
				notResp = handleRubiPayNotification(content);
			} else if (uri.contains(PaymentGateway.JUZHEN.name().toLowerCase())) {
				notResp = handleJuZhenNotification(content);
			}
			order = notResp == null ? null : notResp.getOrder();
			if (order != null && StringUtils.isNotBlank(order.getNotifyUrl())
					&& order.isSettle()) {
				notify(order);
			}
		} catch (Exception e) {
			logger.error("notify failed ", e);
		} finally {
			response.getWriter().write(notResp.getResp());
		}
	}

	private NotifyResponse handleSwiftpassNotification(String content)
			throws Exception {
		Order order = null;
		String respString = "fail";
		if (StringUtils.isNotBlank(content)) {
			Map<String, String> map = XmlUtils.fromXml(content.getBytes(),
					"utf-8");
			if (map.containsKey("sign")) {
				boolean checkSignature = CryptoUtils.checkSignature(map,
						SwiftpassProxy.appSecret, "sign", "key");
				if (!checkSignature) {
					logger.info("check signature failed");
					respString = "fail";
				} else {
					String status = map.get("status");
					if (status != null && "0".equals(status)) {
						String result_code = map.get("result_code");
						if (PaymentResponse.SUCCESS.equals(result_code)) {
							String orderNo = map.get("out_trade_no");
							String totalFee = map.get("total_fee");
							order = orderService.findActiveByOrderNo(orderNo);
							if (order != null
									&& CommonUtils.toInt(totalFee) == (int) (order
											.getTotalFeeAsFloat() * 100)) {
								order.setStatus(OrderStatus.SUCCESS);
								orderService.update(order);
								respString = "success";
							}
						}
					}

				}
			}
		}
		NotifyResponse response = new NotifyResponse(respString, order);
		return response;
	}

	private NotifyResponse handleChinaUmsNotification(String content) {
		Order order = null;
		String respString = "fail";

		if (StringUtils.isNotBlank(content)) {
			String billNo = "";
			String status = "";
			try {
				String decoded = URLDecoder.decode(content, "utf-8");
				String[] params = decoded.split("&");
				for (String param : params) {
					String[] pair = param.split("=");
					String key = pair[0];
					if ("billNo".equals(key)) {
						billNo = pair[1];
					} else if ("billStatus".equals(key)) {
						status = pair[1];
					}
				}
				order = orderService.findActiveByExtOrderNo(billNo);
				if (order != null) {
					order.setStatus(ChinaUmsProxy.toOrderStatus(status));
					orderService.update(order);
					respString = "success";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		NotifyResponse response = new NotifyResponse(respString, order);
		return response;
	}

	private NotifyResponse handleRubiPayNotification(String content)
			throws Exception {
		Order order = null;
		String respString = "fail";
		if (StringUtils.isNotBlank(content)) {
			Map<String, String> map = XmlUtils.fromXml(content.getBytes(),
					"utf-8");
			if (map.containsKey("sign")) {
				boolean checkSignature = CryptoUtils.checkSignature(map,
						RubiPayProxy.appSecret, "sign", "key");
				if (!checkSignature) {
					logger.info("check signature failed");
					respString = "fail";
				} else {
					String status = map.get("status");
					if (status != null && "0".equals(status)) {
						String result_code = map.get("result_code");
						if (PaymentResponse.SUCCESS.equals(result_code)) {
							String orderNo = map.get("out_trade_no");
							String totalFee = map.get("total_fee");
							order = orderService.findActiveByOrderNo(orderNo);
							if (order != null
									&& CommonUtils.toInt(totalFee) == (int) (order
											.getTotalFeeAsFloat() * 100)) {
								order.setStatus(OrderStatus.SUCCESS);
								orderService.update(order);
								respString = "success";
							}
						}
					}

				}
			}
		}
		NotifyResponse response = new NotifyResponse(respString, order);
		return response;
	}

	private static final String sucJuZhenRespString = "{'respCode':'00000', 'respInfo': 'OK'}";
	private static final String errorJuZhenRespString = "{'respCode':'20000', 'respInfo': 'Failed'}";
	private NotifyResponse handleJuZhenNotification(String content) {
		Order order = null;
		String respString = errorJuZhenRespString;
		if (StringUtils.isNotBlank(content)) {
			JuZhenNotification notification = JsonUtils.fromJson(content,
					JuZhenNotification.class);
			if (notification != null
					&& StringUtils.isNoneBlank(notification.getSignature(),
							notification.getOrderId(),
							notification.getTransAmt())) {
				String orderId = notification.getOrderId();
				String totalFee = notification.getTransAmt();

				order = orderService.findActiveByExtOrderNo(orderId);

				if (order != null
						&& totalFee.equals(JuZhenProxy.formatAmount(order
								.getTotalFeeAsFloat()))) {
					order.setStatus(OrderStatus.SUCCESS);
					orderService.update(order);
					respString = sucJuZhenRespString;
				}
			}
		}
		logger.info("handle response result: "+respString);
		NotifyResponse response = new NotifyResponse(respString, order);
		return response;
	}

	private static final Executor executor = Executors.newFixedThreadPool(50);
	private void notify(final Order order) {
		CompletableFuture.runAsync(() -> {
			OrderResponse notification = new OrderResponse();
			notification.setOrderNo(order.getOrderNo());
			notification.setSellerOrderNo(order.getSellerOrderNo());
			notification.setStoreId(String.valueOf(order.getStoreId()));
			notification.setCodeUrl(order.getCodeUrl());
			notification.setPrepayId(order.getPrepayId());
			notification.setTokenId(order.getTokenId());
			notification.setOrderStatus(order.getStatus().getValue());
			notification.setAttach(order.getAttach());
			BaseResponse response = null;
			for (int i=0;i<3;i++) {
				try {
					response = notifyProxy.notify(order.getNotifyUrl(), order.getApp(),
						notification);
					if(response != null && response.getStatus()==ApplicationConstants.STATUS_OK) {
						return;
					}
					Thread.sleep(30000);
				} catch(Exception e) {
					
				}
			}
		}, executor);
	}

	public static class NotifyResponse {
		private String resp;
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

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
		}
	}
}