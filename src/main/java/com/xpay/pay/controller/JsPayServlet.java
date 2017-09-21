package com.xpay.pay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.xpay.pay.model.Order;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.miaofu.MiaoFuProxy;
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
		if(!OrderStatus.NOTPAY.equals(order.getStatus())) {
			response.sendError(400, "Order already paid");
			return;
		} 
		if(PaymentGateway.MIAOFU.equals(order.getStoreChannel().getPaymentGateway())) {
			PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
			String jsUrl = miaoFuProxy.getJsUrl(paymentRequest);
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.sendRedirect(jsUrl);
		} else {
			response.sendError(404, "Order not found");
		}
	}
}
