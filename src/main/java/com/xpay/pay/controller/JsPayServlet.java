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
		if(order!=null) {
			if(PaymentGateway.MIAOFU.equals(order.getStoreChannel().getPaymentGateway())) {
				PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
				String jsUrl = miaoFuProxy.getJsUrl(paymentRequest);
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				String redirectUrl = encodeSubject(jsUrl);
				response.sendRedirect(redirectUrl);
			} else {
				response.sendError(404, "Order not found");
			}
		} else {
			response.sendError(404, "Order not found");
		}
	}

	private static String encodeSubject(String codeUrl) {
		String str1 = codeUrl.substring(0, codeUrl.indexOf("&subject=")+9);
		String str2 =  codeUrl.substring(codeUrl.indexOf("&subject=")+9);
		String str3 = str2.substring(str2.indexOf("&"));
		String str4 = str2.substring(0, str2.indexOf("&"));
		try {
			return str1 +URLEncoder.encode(str4, "UTF-8") + str3;
		} catch (UnsupportedEncodingException e) {
			return codeUrl;
		}
	}
	
	public static void main(String args[]) {
		String str = "http://www.quick-pass.net/miaofuapi/v3/pay/jspay?busi_code=T2016093011252905060661&dev_id=127.0.0.1&amount=0.01&raw_data=a&down_trade_no=X003005120170914142313755789&subject=测试&redirect_url=http://106.14.47.193/xpay/notify/miaofu&app_id=149026948119189&timestamp=1505370193&version=v3&sign=AA16D9ACE0CD3E208FB6CA89991D2F8D";
		System.out.println(encodeSubject(str));
	}
}
