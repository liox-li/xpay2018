package com.xpay.pay.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.ApplicationException;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.models.Bill;
import com.xpay.pay.models.Order;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.OrderDetail;
import com.xpay.pay.service.PaymentService;

@RestController
@RequestMapping("/v1/pay")
public class PaymentRestService {
	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = "/unifiedorder ", method = RequestMethod.POST)
	public BaseResponse<Bill> unifiedOrder(
			@RequestParam String storeId,
			@RequestParam PayChannel payChannel, // ALIPAY(1), WECHAT(2)
			@RequestParam String totalFee, // <=3000yuan
			@RequestParam String orderTime, // yyyyMMddHHmmss
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false) String notifyUrl,
			@RequestBody(required = false) OrderDetail orderDetail) {

		Order order = new Order();
		order.setStoreId(storeId);
		order.setPayChannel(payChannel);
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setTotalFee(totalFee);
		order.setOrderTime(orderTime);
		order.setNotifyUrl(notifyUrl);
		if (orderDetail != null) {
			order.setStoreName(orderDetail.getStoreName());
			order.setOperator(orderDetail.getOperator());
			order.setSellerOrderNo(orderDetail.getSellerOrderNo());
			order.setOrderSubject(orderDetail.getOrderSubject());
			order.setOrderDesc(orderDetail.getOrderDesc());
			order.setOrderItems(orderDetail.getOrderItems());
			order.setAttach(orderDetail.getAttach());
		}
		BaseResponse<Bill> response = new BaseResponse<Bill>();
		try {
			Bill bill = paymentService.unifiedOrder(order);
			response.setData(bill);
		} catch (GatewayException e) {
			response.setStatus(ApplicationConstants.STATUS_BAD_GATEWAY);
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
		} catch (ApplicationException e) {
			response.setStatus(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR);
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/query ", method = RequestMethod.GET)
	public BaseResponse<Bill> query(
			@RequestParam String storeId,
			@RequestParam String orderNo,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip) {
		Order order = new Order();
		order.setStoreId(storeId);
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setOrderNo(orderNo);
		// order.setPayChannel(payChannel);
		Bill bill = paymentService.query(order);
		BaseResponse<Bill> response = new BaseResponse<Bill>();
		response.setData(bill);
		return response;
	}

}
