package com.xpay.pay.service;

import static com.xpay.pay.proxy.IPaymentProxy.SUCCESS;
import static com.xpay.pay.proxy.IPaymentProxy.NO_RESPONSE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.models.Bill;
import com.xpay.pay.models.Order;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.TradeBean;

@Service
public class PaymentService {
	@Autowired
	private IPaymentProxy paymentProxy;

	public Bill unifiedOrder(Order order) {
		PaymentRequest request = toPaymentRequest(order);

		PaymentResponse response = paymentProxy.unifiedOrder(request);

		Bill bill = toBill(order, response);
		Assert.notBlank(bill.getCodeUrl(), ApplicationConstants.STATUS_BAD_GATEWAY, NO_RESPONSE, response.getMsg());
		return bill;
	}

	public Bill query(Order order) {
		PaymentRequest request = toPaymentRequest(order);

		PaymentResponse response = paymentProxy.query(request);

		Bill bill = toBill(order, response);
		return bill;
	}

	private PaymentRequest toPaymentRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code(order.getStoreId());
		request.setDev_id(order.getDeviceId());
		request.setOper_id(order.getOperator());
		request.setPay_channel(order.getPayChannel());
		request.setAmount(order.getTotalFee());
		request.setRaw_data(order.getAttach());
		request.setDown_trade_no(order.getSellerOrderNo());
		request.setSubject(order.getOrderSubject());
		request.setGood_details(order.getOrderItems());
		return request;
	}

	private Bill toBill(Order order, PaymentResponse response) {
		if (response == null || !SUCCESS.equals(response.getCode())
				|| response.getData() == null) {
			String code =response == null?NO_RESPONSE:response.getCode();
			String msg = response == null?"No response":response.getMsg();
			throw new GatewayException(code, msg);
		}
		TradeBean trade = response.getData();
		Bill bill = new Bill();
		bill.setOrder(order);
		bill.setCodeUrl(trade.getCode_url());
		bill.setPrepayId(trade.getPrepay_id());
		return bill;
	}
}
