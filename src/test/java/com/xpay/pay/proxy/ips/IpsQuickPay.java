package com.xpay.pay.proxy.ips;

import com.xpay.pay.BaseSpringJunitTest;

import cn.com.ips.payat.webservice.quickpay.CollService;
import cn.com.ips.payat.webservice.scan.ScanService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class IpsQuickPay extends BaseSpringJunitTest {

  @Autowired
  private CollService collService;
  @Autowired
  ScanService scanService;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void acquireSms() throws Exception {
	 String req =" <Ips> <AcquireSmsReq>"+
			 " <head> <Version>[String]</Version> <MerCode>[String]</MerCode> <MerName>[String]</MerName> <Account>[String]</Account> <ReqDate>[String]</ReqDate> <SignType>[String]</SignType> <Signature>[String]</Signature>"+
			 " </head>"+
			 "  <body> <IpsSignNo>[string]</IpsSignNo>"+
			 "  </body> </AcquireSmsReq>"+
			 "  </Ips> ";
	  String liox = collService.acquireSms(req);
	  System.out.println(liox);
  }

 

}