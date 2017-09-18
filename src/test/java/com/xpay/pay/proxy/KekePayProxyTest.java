package com.xpay.pay.proxy;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.kekepay.KekePayProxy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class KekePayProxyTest extends BaseSpringJunitTest {

  @Autowired
  private KekePayProxy proxy;

  @Test
  public void testUnifiedOrder() {
    PaymentRequest request = new PaymentRequest();
    request.setExtStoreId("测试");
    request.setTotalFee("10.00");
    request.setServerIp("203.156.236.194");
    request.setNotifyUrl("http://127.0.0.1");
    request.setReturnUrl("http://www.baidu.com");
    request.setOrderNo("X11010120170918111838283160");
    request.setSubject("电子商品");
    PaymentResponse response = proxy.unifiedOrder(request);
//    System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
//    System.out.println(response.getBill().getCodeUrl());
  }
}