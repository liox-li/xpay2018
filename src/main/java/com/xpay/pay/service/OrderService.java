package com.xpay.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.models.Bill;
import com.xpay.pay.models.Order;
import com.xpay.pay.proxy.IOrderProxy;
import com.xpay.pay.proxy.OrderRequest;
import com.xpay.pay.proxy.OrderResonse;
import com.xpay.pay.proxy.OrderResonse.TradeBean;

@Service
public class OrderService {
	@Autowired 
	private IOrderProxy orderProxy;
	
	 public Bill placeOrder(Order order) {
		 OrderRequest request = toOrderRequest(order);
		 
		 OrderResonse response = orderProxy.placeOrder(request);
		 
		 Bill bill = toBill(order, response);
		 return bill;
	 }

	private OrderRequest toOrderRequest(Order order) {
		OrderRequest request = new OrderRequest();
		request.setBusi_code(order.getStoreId());
		request.setDev_id(order.getDeviceId());
		request.setOper_id(order.getOperator());
		request.setPay_channel(order.getPayChannel());
		request.setAmount(order.getAmount());
		request.setUndiscountable_amount(order.getUndiscountableAmount());
		request.setRaw_data(order.getSellerData());
		request.setDown_trade_no(order.getOrderNum());
		request.setSubject(order.getOrderSubject());
		request.setGood_details(order.getOrderItems());
		return request;
	}

	private Bill toBill(Order order, OrderResonse response) {
		if(response == null || !response.getCode().equals("0") || response.getData()==null) {
			return null;
		}
		TradeBean trade = response.getData();
		Bill bill = new Bill();
		bill.setOrder(order);
		bill.setTradeNo(trade.getUp_trade_no());
		bill.setTradeStatus(trade.getTrade_status());
		bill.setBuyerId(trade.getBuyer_id());
		bill.setTotalAmount(trade.getTotal_amount());
		bill.setSellerAmount(trade.getSeller_amount());
		bill.setSellerCoupon(trade.getSeller_coupon());
		bill.setBuyerAmount(trade.getBuyer_amount());
		bill.setChnCoupon(trade.getChn_coupon());
		bill.setUndiscountAmount(trade.getUndiscount_amount());
		bill.setOrderTime(trade.getTime_end());
		bill.setPayUrl(trade.getCode_url());
		bill.setWechatPayId(trade.getPrepay_id());
		return bill;
	}
}
