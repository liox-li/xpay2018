package com.xpay.pay.proxy.ips;

import com.xpay.pay.BaseSpringJunitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sunjian on 2017/12/27.
 */
public class IpsProxyTest extends BaseSpringJunitTest {

  @Autowired
  private IpsProxy ipsProxy;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void open() throws Exception {
    ipsProxy
        .open("203.156.236.194", "204693", "2046930018", "2", "test", "1", "33012319741023241X", "盛军鑫", "", "",
            "18930287787", "", "", "", "", "http://www.wfpay.xyz", "http://www.wfpay.xyz", "", "",
            "");
  }

  @Test
  public void transfer() throws Exception {
   /* ipsProxy
        .transfer("203.156.236.194", "", "204693", "2046930018", "test", "3", "ShengTest", "test");*/
  }

  @Test
  public void withdrawal() throws Exception {
    ipsProxy.withdrawal("203.156.236.194", "", "204693","test", "http://www.wfpay.xyz", "http://www.wfpay.xyz", "6225882110887244", "1102");
  }

  @Test
  public void transferAndWithdrawal() throws Exception {

  }

}