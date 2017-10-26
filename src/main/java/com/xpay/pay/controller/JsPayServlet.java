package com.xpay.pay.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.xpay.pay.model.Order;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.chinaumsh5.ChinaUmsH5Proxy;
import com.xpay.pay.proxy.chinaumswap.ChinaUmsWapProxy;
import com.xpay.pay.proxy.kekepay.KekePayProxy;
import com.xpay.pay.proxy.miaofu.MiaoFuProxy;
import com.xpay.pay.proxy.upay.UPayProxy;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;

public class JsPayServlet extends HttpServlet {
	private static final long serialVersionUID = 4657984343307637580L;
	protected final Logger logger = LogManager.getLogger("AccessLog");
	@Autowired
	protected OrderService orderService;
	@Autowired
	protected PaymentService paymentService;
	@Autowired
	protected MiaoFuProxy miaoFuProxy;
	@Autowired
	protected ChinaUmsH5Proxy chinaUmsH5Proxy;
	@Autowired
	protected ChinaUmsWapProxy chinaUmsH5V2Proxy;
	@Autowired
	protected UPayProxy upayProxy;
	@Autowired
	protected KekePayProxy kekePayProxy;

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
		String orderNo = uri.substring(uri.lastIndexOf("/")+1);
		
		Order order = orderService.findActiveByOrderNo(orderNo);
		if(order==null) {
			response.sendError(404, "Order not found");
			return;
		}
		String path = request.getPathInfo();
		String parameters = StringUtils.isBlank(request.getQueryString())?"":"?"+request.getQueryString();
		logger.info("Jspay: "+path + parameters);
		if(PaymentGateway.CHINAUMSH5.equals(order.getStoreChannel().getPaymentGateway())) {
			if(!path.startsWith("/"+IPaymentProxy.PAYED)) {
				if(!OrderStatus.NOTPAY.equals(order.getStatus())) {
					response.sendError(400, "Order already paid");
					return;
				} 
				PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
				paymentRequest.setGatewayOrderNo(order.getExtOrderNo());
				String jsUrl = chinaUmsH5Proxy.getJsUrl(paymentRequest);
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				response.sendRedirect(jsUrl);
			} else {
				String status = request.getParameter("status");
				OrderStatus orderStatus = ChinaUmsH5Proxy.toOrderStatus(status);
				String returnUrl = order.getReturnUrl()+"?status="+orderStatus.getValue();
				logger.info("Return to: "+returnUrl);
				response.sendRedirect(returnUrl);
			}
		} else if(PaymentGateway.CHINAUMSWAP.equals(order.getStoreChannel().getPaymentGateway())) {
			if(!path.startsWith("/"+IPaymentProxy.PAYED)) {
				if(!OrderStatus.NOTPAY.equals(order.getStatus())) {
					response.sendError(400, "Order already paid");
					return;
				} 
				PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
				paymentRequest.setGatewayOrderNo(order.getExtOrderNo());
				String jsUrl = chinaUmsH5V2Proxy.getJsUrl(paymentRequest);
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				response.sendRedirect(jsUrl);
			} else {
				String status = request.getParameter("status");
				OrderStatus orderStatus = ChinaUmsH5Proxy.toOrderStatus(status);
				String returnUrl = order.getReturnUrl()+"?status="+orderStatus.getValue();
				logger.info("Return to: "+returnUrl);
				response.sendRedirect(returnUrl);
			}
		} else if(PaymentGateway.UPAY.equals(order.getStoreChannel().getPaymentGateway())) {
			if(!path.startsWith("/"+IPaymentProxy.PAYED)) {
				if(!OrderStatus.NOTPAY.equals(order.getStatus())) {
					response.sendError(400, "Order already paid");
					return;
				} 
				PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
				String jsUrl = upayProxy.getJsUrl(paymentRequest);
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				response.sendRedirect(jsUrl);
			} else {
				String status = request.getParameter("status");
				OrderStatus orderStatus = UPayProxy.toOrderStatus(status);
				String returnUrl = order.getReturnUrl()+"?status="+orderStatus.getValue();
				logger.info("Return to: "+returnUrl);
				response.sendRedirect(returnUrl);
			}
		} else if(PaymentGateway.MIAOFU.equals(order.getStoreChannel().getPaymentGateway())) {
			PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
			String jsUrl = miaoFuProxy.getJsUrl(paymentRequest);
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.sendRedirect(jsUrl);
		} else if(PaymentGateway.KEKEPAY.equals(order.getStoreChannel().getPaymentGateway())){
			if(uri.contains(IPaymentProxy.TOPAY)){
				PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
				response.sendRedirect(kekePayProxy.getJsUrl(paymentRequest));
			}else if(uri.contains(IPaymentProxy.PAYED)){
				response.sendRedirect(order.getReturnUrl());
			}
		} else {
			response.sendError(404, "Order not found");
		}
	}
}
