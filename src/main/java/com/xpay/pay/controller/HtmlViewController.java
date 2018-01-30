package com.xpay.pay.controller;

import com.xpay.pay.model.Order;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.ips.IpsProxy;
import com.xpay.pay.proxy.ips.quick.IpsQuickProxy;
import com.xpay.pay.proxy.ips.wxpay.IpsWxProxy;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sunjian on Date: 2017/12/15 下午2:12
 * Description:
 */
@Controller
@RequestMapping("/pay")
public class HtmlViewController {

  private static final Logger logger = LogManager.getLogger(HtmlViewController.class);

  @Autowired
  private OrderService orderService;

  @Autowired
  private IpsQuickProxy ipsQuickProxy;

  @Autowired
  private IpsWxProxy ipsWxpayProxy;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private IpsProxy ipsProxy;

  @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET)
  public ModelAndView pay(@PathVariable("orderNo") String orderNo) {
    logger.info("h5pay :" + orderNo);
    Order order = orderService.findActiveByOrderNo(orderNo);
    if (order == null) {
      return new ModelAndView("h5_error");
    }
    if (PaymentGateway.IPSQUICK.equals(order.getStoreChannel().getPaymentGateway())) {
      PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
      String reqParam = ipsQuickProxy.getReqParam(paymentRequest);
      Map<String, String> model = new HashMap<>();
      model.put("pGateWayReq", reqParam);
      return new ModelAndView("ips_quick", model);
    } else if(PaymentGateway.IPSWX.equals(order.getStoreChannel().getPaymentGateway())) {
      PaymentRequest paymentRequest = paymentService.toPaymentRequest(order);
      String reqParam = ipsWxpayProxy.getReqParam(paymentRequest);
      Map<String, String> model = new HashMap<>();
      model.put("wxPayReq", reqParam);
      return new ModelAndView("ips_wx", model);
    }

    return new ModelAndView("h5_error");
  }

  @RequestMapping(value = "/ips/open", method = RequestMethod.GET)
  public ModelAndView pay(@RequestParam("customerCode") String customerCode,
      @RequestParam("identityNo") String identityNo, @RequestParam("userName") String userName,
      @RequestParam("mobileNo") String mobileNo, HttpServletRequest request)
      throws IOException {

    String requestXml = ipsProxy
        .buildOpenRequest(request.getRemoteAddr(), "204693", "2046930018", "2", customerCode, "1",
            identityNo, userName, "", "",
            mobileNo, "", "", "", "", "http://www.wfpay.xyz", "http://www.wfpay.xyz", "", "",
            "");

    Map<String, String> model = new HashMap<>();
    model.put("ipsRequest", requestXml);
    return new ModelAndView("ips_open", model);
  }

  @RequestMapping(value = "/ips/withdraw", method = RequestMethod.GET)
  public ModelAndView withdrawal(@RequestParam("customerCode") String customerCode,
      @RequestParam("bankCard") String bankCard, @RequestParam("bankCode") String bankCode,
      HttpServletRequest request)
      throws IOException {

    String requestXml = ipsProxy
        .buildWithdrawalRequest(request.getRemoteAddr(), "", "204693", customerCode,
            "http://www.wfpay.xyz", "http://www.wfpay.xyz", bankCard, bankCode);

    Map<String, String> model = new HashMap<>();
    model.put("ipsRequest", requestXml);
    return new ModelAndView("ips_withdraw", model);
  }
}