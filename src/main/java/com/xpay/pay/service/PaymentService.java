package com.xpay.pay.service;

import static com.xpay.pay.proxy.IPaymentProxy.NO_RESPONSE;
import static com.xpay.pay.proxy.IPaymentProxy.SUCCESS;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.models.Bill;
import com.xpay.pay.models.Order;
import com.xpay.pay.models.OrderDetail;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.TradeBean;

@Service
public class PaymentService {
	@Autowired
	private IPaymentProxy paymentProxy;

	public Order createOrder(String storeId, PayChannel channel,
			String deviceId, String ip, String totalFee, String orderTime,
			String sellerOrderNo, String attach, String notifyUrl,
			OrderDetail orderDetail) {
		Order order = new Order();
		order.setStoreId(storeId);
		order.setPayChannel(channel);
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setTotalFee(totalFee);
		order.setOrderTime(orderTime);
		order.setSellerOrderNo(sellerOrderNo);
		order.setAttach(attach);
		order.setNotifyUrl(notifyUrl);
		order.setOrderDetail(orderDetail);
		order.setOrderNo(UUID.randomUUID().toString());
		return order;
	}

	public Bill unifiedOrder(Order order) {
		PaymentRequest request = toPaymentRequest(order);

		PaymentResponse response = paymentProxy.unifiedOrder(request);

		Bill bill = toBill(order, response);
		Assert.notBlank(bill.getCodeUrl(),
				ApplicationConstants.STATUS_BAD_GATEWAY, NO_RESPONSE,
				response.getMsg());
		return bill;
	}

	public Bill query(String orderNo) {
		return null;
	}

	private PaymentRequest toPaymentRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code(order.getStoreId());
		request.setDev_id(order.getDeviceId());
		request.setPay_channel(order.getPayChannel());
		request.setAmount(order.getTotalFee());
		request.setRaw_data(order.getAttach());
		request.setDown_trade_no(order.getOrderNo());
		if (order.getOrderDetail() != null) {
			request.setOper_id(order.getOrderDetail().getOperator());
			request.setSubject(order.getOrderDetail().getOrderSubject());
			request.setGood_details(order.getOrderDetail().getOrderItems());
		}
		return request;
	}

	private Bill toBill(Order order, PaymentResponse response) {
		if (response == null || !SUCCESS.equals(response.getCode())
				|| response.getData() == null) {
			String code = response == null ? NO_RESPONSE : response.getCode();
			String msg = response == null ? "No response" : response.getMsg();
			throw new GatewayException(code, msg);
		}
		TradeBean trade = response.getData();
		Bill bill = new Bill();
		bill.setCodeUrl(trade.getCode_url());
		bill.setPrepayId(trade.getPrepay_id());
		bill.setOrderNo(order.getOrderNo());
		bill.setGatewayOrderNo(trade.getTrade_no());
		bill.setOrderStatus(trade.getTrade_status());
		bill.setOrder(order);
		return bill;
	}
}
