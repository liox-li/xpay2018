package com.xpay.pay.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.controller.GoodsQrCodeServlet;
import com.xpay.pay.model.Order;
import com.xpay.pay.proxy.notify.NotifyProxy;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.NotificationResponse;
import com.xpay.pay.util.CryptoUtils;

@Service
public class NotifyService {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	@Autowired
	private NotifyProxy notifyProxy;
	
	private static final Executor executor = Executors.newFixedThreadPool(80);
	@SuppressWarnings("rawtypes")
	public void notify(final Order order) {
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
				notification.setCodeUrl(GoodsQrCodeServlet.QR_CODE_PREFIX+order.getGoods().getCode()+"?uid="+order.getSellerOrderNo());
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
			notification.setUid(order.getSellerOrderNo());
			return notification;
		} catch(Exception e) {
			logger.error("convert to NotResponse failed", e);
			return null;
		}
	}
	
}
