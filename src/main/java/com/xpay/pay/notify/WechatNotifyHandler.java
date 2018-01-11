package com.xpay.pay.notify;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.model.Order;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.TimeUtils;

@Service
public class WechatNotifyHandler extends AbstractNotifyHandler {
	@Autowired
	protected OrderService orderService;
	
	@Override
	protected NotifyBody extractNotifyBody(String url, String content) {
		String orderNo = "";
		String extOrderNo = "";
		Float totalFee = 0f;
		try {
			WechatNotification notification = JsonUtils.fromJson(content, WechatNotification.class);
			Date orderTime = parseOrderTimeFromOrderNo(notification.getOrderNo());
			Date payTime = TimeUtils.parseTime(notification.getPayTime(), "yyyy-MM-dd HH:mm:ss");
			if(orderTime.compareTo(payTime)>0 || !isWithinMinutes(orderTime, payTime, 1)) {
				return null;
			}
			extOrderNo = notification.getOrderNo();
			
			Order order = orderService.findActiveByOrderTime(notification.getExtStoreId(), notification.getOrderNo(), notification.getAmount(), notification.getSubject(), orderTime);
			
			orderNo = order == null?null:order.getOrderNo();
			totalFee = notification.getAmount();
		
		} catch (Exception e) {
			logger.error("WechatNotifyHandler extractNotifyBody "+content, e);
		}
		return StringUtils.isBlank(orderNo)?null:new NotifyBody(orderNo, extOrderNo, OrderStatus.SUCCESS, (int)(totalFee*100), null);
	}

	private static final String SUCCESS_STR = "{\"status\":200}";
	@Override
	protected String getSuccessResponse() {
		return SUCCESS_STR;
	}

	private static final String FAIL_STR = "{\"status\":500}";
	@Override
	protected String getFailedResponse() {
		return FAIL_STR;
	}
	
	private static final Date parseOrderTimeFromOrderNo(String orderNo) {
		if(StringUtils.isBlank(orderNo) || orderNo.length()<19) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		String sYear = "20"+orderNo.substring(4, 6);
		int year = Integer.valueOf(sYear);
		String sMonth = orderNo.substring(6,8);
		int month = Integer.valueOf(sMonth);
		String sDay = orderNo.substring(8,10);
		int day = Integer.valueOf(sDay);
		String sMinute = orderNo.substring(13,15);
		int minute = Integer.valueOf(sMinute);
		String sHour = orderNo.substring(17,19);
		int hourOfDay = Integer.valueOf(sHour);
		String sSecond = orderNo.substring(15,17);
		int second = Integer.valueOf(sSecond);
		c.set(year, month-1, day, hourOfDay, minute, second);
		c.add(Calendar.SECOND, 2);
		return c.getTime();
	}
	
	private static final boolean isWithinMinutes(Date date1, Date date2, int minutes) {
		if(date1==null || date2 == null) {
			return false;
		}
		return Math.abs(date1.getTime()-date2.getTime())/1000<minutes*60;
	}

	public static class WechatNotification {
		private String openId;
		private String orderNo;
		private String payTime;
		private Float amount;
		private String subject;
		private String payChannel;
		private String extStoreId;
		private String tradeNo;
		private String note;
		public String getOpenId() {
			return openId;
		}
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		public String getPayTime() {
			return payTime;
		}
		public void setPayTime(String payTime) {
			this.payTime = payTime;
		}
		public Float getAmount() {
			return amount;
		}
		public void setAmount(Float amount) {
			this.amount = amount;
		}
		
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getPayChannel() {
			return payChannel;
		}
		public void setPayChannel(String payChannel) {
			this.payChannel = payChannel;
		}
		public String getExtStoreId() {
			return extStoreId;
		}
		public void setExtStoreId(String extStoreId) {
			this.extStoreId = extStoreId;
		}
		public String getTradeNo() {
			return tradeNo;
		}
		public void setTradeNo(String tradeNo) {
			this.tradeNo = tradeNo;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
	}
	
	public static void main(String args[]) {
		String orderNo = "100017121320658481277792970";
		Date date1 = parseOrderTimeFromOrderNo(orderNo);
		System.out.println(date1);
		
		Date date2 = TimeUtils.parseTime("2017-12-13 12:58:52", "yyyy-MM-dd HH:mm:ss");
		System.out.println(date2);
		
		System.out.println(isWithinMinutes(date1, date2, 1));
		System.out.println(date1.compareTo(date2));
	}
}
