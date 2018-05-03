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
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreGoods;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;
import com.xpay.pay.service.StoreGoodsService;
import com.xpay.pay.service.StoreService;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;

public class GoodsQrCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 5131564714596607060L;
	protected final Logger logger = LogManager.getLogger("AccessLog");
	public static final String QR_CODE_PREFIX = AppConfig.XPayConfig.getProperty("qrcode.endpoint", "http://www.zmpay.top/xpay/qrcode/");

	@Autowired
	protected StoreGoodsService goodsService;
	
	@Autowired
	protected PaymentService paymentService;
	
	@Autowired
	protected StoreService storeService;
	
	@Autowired
	protected OrderService orderService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		String parameters = StringUtils.isBlank(request.getQueryString()) ? "" : "?" + request.getQueryString();
		logger.info("GoodsQrCode: " + path + parameters);
		
		String uri = request.getRequestURI();
		String goodsCode = uri.substring(uri.lastIndexOf("/") + 1);
		StoreGoods goods = goodsService.findByCode(goodsCode);
		
		if (goods == null) {
			response.sendError(400, "无效商品");
			return;
		}
		String uid = request.getParameter("uid");
		if(StringUtils.isBlank(uid)) {
			response.sendError(400, "uid不能为空");
		    return;
		}
		Order customerOrder = orderService.findPaidBySellerOrderNo(uid);
		if(customerOrder!=null) {
			if(StringUtils.isNotBlank(customerOrder.getReturnUrl())) {
				response.sendRedirect(customerOrder.getReturnUrl());
			} else {
				response.sendError(400, "订单已存在");
			}
			return;
		}

		String storeIdStr = request.getParameter("storeId");
		long storeId = goods.getStoreId();
		if(StringUtils.isNotBlank(storeIdStr)) {
			storeId = CommonUtils.toLong(storeIdStr);
			storeId = storeId>0?storeId:goods.getStoreId();
		}
		Store store = storeService.findById(storeId);
		
		// recharget order	
		String orderNo = request.getParameter("orderNo");
		if(!validateRechargeOrder(orderNo, uid)) {
			response.sendError(400, "订单已存在");
			return;
		}
		
		Order order = paymentService.createGoodsOrder(store, goods, uid, orderNo);
		if(order == null || StringUtils.isBlank(order.getCodeUrl())) {
			response.sendError(400, "无效商品");
		    return;
		}
		
		if(isRechargetOrder(storeId, goods)) {
			long agentId = CommonUtils.toLong(request.getParameter("agentId"));
			storeService.rechargeOrder(agentId, store, goods, order.getOrderNo());
		}
		response.sendRedirect(order.getCodeUrl());
	}
	
	private boolean isRechargetOrder(long storeId, StoreGoods goods) {
		return (storeId != 1L && goods!=null && goods.getStoreId() ==1L);
	}
	
	private boolean validateRechargeOrder(String orderNo, String sellerOrderNo) {
		if(StringUtils.isNotBlank(orderNo)) {
			Order order = null;
			try {
				order = orderService.findActiveByOrderNo(orderNo);
			} catch(Exception e) {
				
			}
			return order==null;
		} 
		return true;
	}
}
