package com.xpay.pay.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.models.Bill;
import com.xpay.pay.proxy.PaymentRequest.GoodBean;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;

@RestController
@RequestMapping("/v1/pay")
public class OrderRestService {
	@RequestMapping(value = "/unifiedorder ", method = RequestMethod.PUT)
	public BaseResponse<Bill> unifiedOrder(
			@RequestParam String storeId, 
			@RequestParam PayChannel payChannel,
			@RequestParam String ip,
			@RequestParam String totalFee,
			@RequestParam String orderTime, //yyyyMMddHHmmss
			@RequestParam(required = false) String storeName,
			@RequestParam(required = false) String operator,
			@RequestParam(required = false) String deviceInfo,
			@RequestParam(required = false) String sellerOrderNo,
			@RequestBody(required = false) String orderSubject,
			@RequestBody(required = false) String orderDesc,
			@RequestBody(required = false) GoodBean[] orderItems,
			@RequestParam(required = false) String notifyUrl,
			@RequestParam(required = false) String attach
	)  {
		return BaseResponse.OK;
		
	}
}
