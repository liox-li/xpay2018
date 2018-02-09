package com.xpay.pay.proxy;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.hm.HmProxy;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sunjian on Date: 2018/2/8 下午3:16
 * Description:
 */
public class HmProxyTest extends BaseSpringJunitTest {

  @Autowired
  private HmProxy hmProxy;


  @Test
  public void open() throws Exception {
    hmProxy
        .open("盛军鑫", "上海市", "上海市", "闵行区宝城路155弄25号1003室", "01",
            "33012319741023241X", "18930287787", "6225882110887244", "盛军鑫",
            "招商银行", "308584000013", "1", "0.004");
  }
  //hm resp{"merCode":"32083001116131","statusCode":"00","statusMsg":"成功"}

  @Test
  public void payWithCREDIT() throws Exception {
    Date now = new Date();
    hmProxy.payWithCREDIT("32083001116131", "201802091762",
        "10000", "6221558812340000",
        "13552535506", "123", "1711");
  }

  @Test
  public void withdrawl() throws Exception {
    hmProxy.withdrawl("32083001116131", "20180209", "1");
  }

  @Test
  public void queryWithdrawl() throws Exception {
    hmProxy.queryWithdraw("32083001116131", "20180209", "20180209", null);
  }
}