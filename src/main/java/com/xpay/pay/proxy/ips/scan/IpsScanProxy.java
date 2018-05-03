package com.xpay.pay.proxy.ips.scan;

import cn.com.ips.payat.webservice.scan.ScanService;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.StoreChannel.IpsProps;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.AbstractIpsProxy;
import com.xpay.pay.proxy.ips.common.ReqHead;
import com.xpay.pay.proxy.ips.scan.gatewayreq.Body;
import com.xpay.pay.proxy.ips.scan.gatewayreq.GateWayReq;
import com.xpay.pay.proxy.ips.scan.gatewayreq.Ips;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

/**
 * Created by sunjian on Date: 2017/12/12 下午9:51
 * Description:
 */
@Component
public class IpsScanProxy extends AbstractIpsProxy {

  @Qualifier("scanUnmarshaller")
  @Autowired
  protected Unmarshaller unmarshaller;
  @Autowired
  private ScanService scanService;

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
    String[] accountParam = request.getExtStoreId().split(",");
    try {
      //merCode, account, signature md5
      String merCode = accountParam[0];
      String account = accountParam[1];
      String md5Signature = accountParam[2];
      Ips ips = toIps(request, merCode, account, md5Signature);
      ByteArrayOutputStream os;
      os = new ByteArrayOutputStream();
      marshaller.marshal(ips, new StreamResult(os));
      String scanPayRequest = new String(os.toByteArray(),"UTF-8");
      scanPayRequest = scanPayRequest.substring(scanPayRequest.indexOf("<Ips>"));
      logger.info("ips order request: " + scanPayRequest);
      String result = scanService.scanPay(scanPayRequest);
      //支付宝支付通道商户被和谐后，返回是空
      if(request.getPayChannel() != null && request.getPayChannel() == PayChannel.ALIPAY){
    	  if(result == null || "".equals(result.trim())){
    		  logger.info("请求返回结果为【" + result+"】,疑似商户被关闭了！");
    		  throw new GatewayException("-2222222","疑似商户被关闭了！");
    	  }
      }
      logger.info("ips order response: " + result);
      StreamSource streamSource = new StreamSource(new ByteArrayInputStream(result.getBytes()));
      com.xpay.pay.proxy.ips.scan.gatewayrsp.Ips respIps = (com.xpay.pay.proxy.ips.scan.gatewayrsp.Ips) unmarshaller
          .unmarshal(streamSource);
      logger.info("ips response code:" + respIps.getGateWayRsp().getHead().getRspCode());
      if (!SUCCESS.equals(respIps.getGateWayRsp().getHead().getRspCode())) {
    	  if(respIps.getGateWayRsp().getHead().getRspMsg().indexOf("返回失败") >=0 ){
    		  logger.info("请求返回结果为【" +respIps.getGateWayRsp().getHead().getRspCode()+">>" +respIps.getGateWayRsp().getHead().getRspMsg()+"】,疑似商户被关闭了！");
    		  throw new GatewayException("-2222222","疑似商户被关闭了！");
    	  }
        throw new GatewayException(respIps.getGateWayRsp().getHead().getRspCode(),
            respIps.getGateWayRsp().getHead().getRspMsg());
      }
      signatureValidWithThrow(merCode, md5Signature, result,
          respIps.getGateWayRsp().getHead().getSignature());
      PaymentResponse response = new PaymentResponse();
      response.setCode(PaymentResponse.SUCCESS);
      Bill bill = new Bill();
      bill.setCodeUrl(respIps.getGateWayRsp().getBody().getQrCode());
      bill.setOrderNo(request.getOrderNo());
      bill.setOrderStatus(OrderStatus.NOTPAY);
      response.setBill(bill);
      return response;
    } catch (GatewayException e) {
      throw e;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new GatewayException("999999", e.getMessage());
    }

  }

  private Ips toIps(PaymentRequest request, String merCode, String account, String md5Signature)
      throws IOException {
    String date = request.getOrderTime();
    Ips ips = new Ips();
    GateWayReq gateWayReq = new GateWayReq();
    Body body = new Body();
    body.setMerBillNo(request.getOrderNo());
    switch (request.getPayChannel()) {
      case ALIPAY:
        body.setGatewayType("11");
        break;
      case WECHAT:
        body.setGatewayType("10");
        break;
      default:
        throw new RuntimeException("Not Support the pay channel");
    }
    body.setGoodsName(request.getSubject());
   
    if (request.getChannelProps() != null) {
      IpsProps props = (IpsProps) request.getChannelProps();
      if(props.getMerType() != null) {
        body.setMerType(props.getMerType());
        body.setSubMerCode(props.getSubMerCode());
        if(StringUtils.isNotBlank(props.getGoodNames())){
        	String [] goodNameList = props.getGoodNames().split(",");
        	String goodName = null;
        	if(goodNameList != null && goodNameList.length >0){
        	  int index =	(int)(Math.random()*goodNameList.length);
        	  goodName = goodNameList[index];
        	}
        	if(StringUtils.isNotBlank(goodName)){
        		body.setGoodsName(goodName);//使用预定义的商品名称
        	}
        }
      } else {
        body.setMerType("0");
      }
    } else{
      body.setMerType("0");
    }
    body.setDate(date.substring(0, 8));
    body.setCurrencyType("156");
    DecimalFormat numberFormat = new DecimalFormat("#.##");
    numberFormat.setGroupingUsed(false);
    body.setAmount(numberFormat.format(request.getTotalFee()));
    body.setLang("GB");
    body.setAttach(request.getAttach());
    body.setRetEncodeType("17");
    body.setServerUrl(request.getNotifyUrl());
    body.setBillEXP("2");
    
    gateWayReq.setBody(body);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    marshaller.marshal(body, new StreamResult(os));
    String bodyStr = new String(os.toByteArray(),"UTF-8");
    bodyStr = bodyStr.substring(bodyStr.indexOf("<body>"));
    logger.info("signature body: " + bodyStr + merCode + md5Signature);
    String signature = CryptoUtils.md5(bodyStr + merCode + md5Signature);
    ReqHead head = new ReqHead();
    head.setVersion(getVersion());
    head.setMsgId(IDGenerator.buildTimeSeriesId());
    head.setMerCode(merCode);
    head.setAccount(account);
    head.setReqDate(date);
    head.setSignature(signature);
    gateWayReq.setHead(head);
    ips.setGateWayReq(gateWayReq);
    return ips;
  }


  @Override
  protected String getReqDate(PaymentRequest request) {
    return request.getOrderTime();
  }

  @Override
  protected String getVersion() {
    return "v1.0.0";
  }
}