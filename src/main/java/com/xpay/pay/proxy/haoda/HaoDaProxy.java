package com.xpay.pay.proxy.haoda;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huifu.saturn.cfca.CFCASignature;
import com.huifu.saturn.cfca.SignResult;
import com.huifu.saturn.cfca.VerifyResult;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.chinapnr.ChinaPnrResponse;
import com.xpay.pay.proxy.chinaums.ChinaUmsRequest;
import com.xpay.pay.proxy.chinaums.ChinaUmsResponse;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.HttpClient;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.JsonUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static com.xpay.pay.model.StoreChannel.PaymentGateway.CHINAUMS;

@Component
public class HaoDaProxy implements IPaymentProxy {
	protected final Logger logger = LogManager.getLogger("AccessLog");
	private static final String SCAN_URI = "/qrc/c2b/order/v0";//获取二维码-主扫
	private static final String H5_WECHAT_URI = "/qrc/wxh5/order/v0";//微信H5下单
	private static final String QUERY_H5_WECHAT_URI = "/qrc/wxh5/order/{platformId}";//微信H5交易查询
	
	
	
	private static final AppConfig config =  null;//AppConfig.ChinaUmsConfig;
	private static final String baseEndpoint = null;//config.getProperty("provider.endpoint");
	private static final String appId = null;//config.getProperty("provider.app.id");
	private static final String appSecret = null;//config.getProperty("provider.app.secret");
	private static final String appName = null;//config.getProperty("provider.app.name");
	private static final String tId = null;//config.getProperty("provider.tid");
	private static final String instMid = null;//config.getProperty("provider.inst.mid");
	
	private static final String URL = "http://mertest.chinapnr.com/npay/merchantRequest";
	private static final DecimalFormat decimalFormat=new DecimalFormat(".00");
	private static final String KEY_PATH = "/Users/lizeman/Documents/workspace1/demo-huifu/";//"/usr/local/app/key/chinapnr/";
	/**
	 * pfx、cert文件的名字是商户号
	 */
	private  String merCustId= null;
	private  String pfxFilePwd= null;//pfx 文件密码；
	private String divAcctId = null;//分账账号id
	private static final String version = "10";
	

	private static ObjectMapper instance = new ObjectMapper();
	static{
		instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// instance.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
		instance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		instance.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		instance.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	@Override
	public void init(String props){
		
	}
	
	/**
	 * 支付接口
	 * @param request
	 * @return
	 */
	@Override
	public PaymentResponse unifiedOrder(PaymentRequest request) {
		
	    Map<String, String> params = new HashMap<String, String>();
	    request.setGatewayOrderNo(IDGenerator.buildQrCode(appId));
	    params.put("version", "10");
        params.put("cmd_id", request.getCmdId());
        params.put("mer_cust_id", this.merCustId);
        params.put("order_id", request.getOrderNo());
        params.put("order_date", request.getOrderTime());
        params.put("goods_desc", request.getSubject());
        /**
         *  04：微信正扫
			05：支付正扫
			10 : 微信APP支付
			12：支付宝统一下单
			13：微信公众号
			14：apple pay
         */
        if(request.getPayType() == PayType.WECHAT_GZH){
        	 params.put("pay_type", "13");//04 wecha,05 alipay
        }else if(request.getPayType() == PayType.WECHAT_SCAN){
        	 params.put("pay_type", "04");
        }else if(request.getPayType() == PayType.WECHAT_APP){
       	 params.put("pay_type", "10");
        }else if(request.getPayType() == PayType.ALIPAY){
      	 params.put("pay_type", "12");
        }else if(request.getPayType() == PayType.ALIPAY_SCAN){
     	 params.put("pay_type", "05");
        }else if(request.getPayType() == PayType.APPLE_PAY){
     	 params.put("pay_type", "14");
        } 
        // params.put("request_type", "01");
        String amt =  decimalFormat.format(request.getTotalFee()*100);
        params.put("trans_amt",amt);
        if(!StringUtils.isEmpty(this.divAcctId)){
        	 params.put("div_detail", "[{'divCustId':'"+this.merCustId+"','divAcctId':'"+this.divAcctId+"','divAmt':'"+amt+"','divFreezeFg':'01'}]");
        }
        //params.put("goods_type", "");
        //params.put("order_expire_time", "");
        if(!StringUtils.isEmpty(request.getUserOpenId())){
        	params.put("oper_user_Id", request.getUserOpenId());
        }
        if(!StringUtils.isEmpty(request.getDeviceId())){
        	params.put("device_info", request.getDeviceId());
        }
        if(!StringUtils.isEmpty(request.getReturnUrl())){
        	  params.put("ret_url", request.getReturnUrl());
        } 
        params.put("bg_ret_url", request.getNotifyUrl());
        
        String paramsStr = JsonUtils.toJson(params);
       // paramsStr = "{\"bg_ret_url\":\"http://192.168.0.74:8001/npayCallBack/asyncHandle.json\",\"goods_desc\":\"购买商品\",\"cmd_id\":\"218\",\"request_type\":\"01\",\"pay_type\":\"05\",\"div_detail\":\"[{'divCustId':'6666000000036492','divAcctId':'38888','divAmt':'1.00','divFreezeFg':'01'}]\",\"trans_amt\":\"1.00\",\"order_date\":\"20180327\",\"order_id\":\"20180101501004022039\",\"mer_cust_id\":\"6666000000036492\",\"version\":\"10\"}";
        System.out.println(">>>>>"+paramsStr);
        String sign = this.signature(paramsStr);
        System.out.println(">>>>"+sign);
        //组织post数据
        String postStr = "cmd_id=" + request.getCmdId()+ "&version=" + version + "&mer_cust_id=" + this.merCustId + "&check_value=" + sign;
        
        long l = System.currentTimeMillis();
      
		ChinaPnrResponse chinaPnrResponse = null;//chinaPnrProxy.exchange(URL, HttpMethod.POST, httpEntity, ChinaPnrResponse.class).getBody();
		//String result = HttpClient.doPost(URL, postStr, 5000);
		HttpRequest httpRequest = HttpRequest.post(URL).charset("UTF-8");
		HttpResponse httpResponse = httpRequest.contentType("application/x-www-form-urlencoded").body(postStr).send();
		// 取得同步返回数据
        String result = httpResponse.bodyText(); 
		
		this.parseResult(result);
		/*logger.info("unifiedOrder result: " + chinaPnrResponse.getResp_code() + " "+chinaPnrResponse.getResp_desc() + ", took "
				+ (System.currentTimeMillis() - l) + "ms");*/
		PaymentResponse response = null;
		//response = toPaymentResponse(chinaPnrResponse, chinaPnrResponse);
		//response.getBill().setGatewayOrderNo(request.getGatewayOrderNo());
		
		String url = baseEndpoint;
		
		/*try {
			request.setGatewayOrderNo(IDGenerator.buildQrCode(appId));
			ChinaUmsRequest chinaUmsRequest = this.toChinaUmsRequest(CHINAUMS.UnifiedOrder(),request);
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = null;//this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("unifiedOrder POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsResponse chinaUmsResponse = chinaPnrProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsResponse.class).getBody();
			logger.info("unifiedOrder result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);
			response.getBill().setGatewayOrderNo(request.getGatewayOrderNo());
		} catch (RestClientException e) {
			logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}*/
		
		return response;
	}

	@Override
	public PaymentResponse query(PaymentRequest request) {
		String url = baseEndpoint;
		logger.info("query POST: " + url);
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			ChinaUmsRequest chinaUmsRequest = this.toChinaUmsRequest(CHINAUMS.Query(),request);
			
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = null;//this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("query POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			/*HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsResponse chinaUmsResponse = chinaPnrProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsResponse.class).getBody();
			logger.info("query result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);*/
		} catch (RestClientException e) {
			logger.info("query failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}
	
	@Override
	public PaymentResponse refund(PaymentRequest request) {
		String url = baseEndpoint;
		long l = System.currentTimeMillis();
		PaymentResponse response = null;
		try {
			ChinaUmsRequest chinaUmsRequest = this.toChinaUmsRequest(CHINAUMS.Refund(),request);
			chinaUmsRequest.setRefundAmount(chinaUmsRequest.getTotalAmount());
			List<KeyValuePair> keyPairs = this.getKeyPairs(chinaUmsRequest);
			String sign = null;//this.signature(keyPairs, appSecret);
			chinaUmsRequest.setSign(sign);
			logger.info("refund POST: " + url+", body "+JsonUtils.toJson(chinaUmsRequest));
			
			/*HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> httpEntity = new HttpEntity<>(chinaUmsRequest, headers);
			ChinaUmsResponse chinaUmsResponse = chinaPnrProxy.exchange(url, HttpMethod.POST, httpEntity, ChinaUmsResponse.class).getBody();
			logger.info("refund result: " + chinaUmsResponse.getErrCode() + " "+chinaUmsResponse.getErrMsg() + ", took "
					+ (System.currentTimeMillis() - l) + "ms");
			response = toPaymentResponse(chinaUmsRequest, chinaUmsResponse);*/
		} catch (RestClientException e) {
			logger.info("refund failed, took " + (System.currentTimeMillis() - l) + "ms", e);
			throw e;
		}
		return response;
	}

	private ChinaUmsRequest toChinaUmsRequest(String method, PaymentRequest request) {
		ChinaUmsRequest chinaUmsRequest = new ChinaUmsRequest();
		chinaUmsRequest.setMsgSrc(appName);
		chinaUmsRequest.setMid(request.getExtStoreId());
		chinaUmsRequest.setInstMid(instMid);
		chinaUmsRequest.setTid(tId);
		chinaUmsRequest.setBillNo(request.getGatewayOrderNo());
		chinaUmsRequest.setRequestTimeStamp(IDGenerator.formatTime());
		if(CHINAUMS.UnifiedOrder().equals(method)) {
			chinaUmsRequest.setBillDate(IDGenerator.formatDate());
		} else {
			chinaUmsRequest.setBillDate(IDGenerator.formatDate(IDGenerator.TimePattern14, request.getOrderTime()));
		}
		chinaUmsRequest.setBillDesc(request.getSubject());
		if(request.getTotalFee()!=null) {
			chinaUmsRequest.setTotalAmount(String.valueOf((int)(request.getTotalFee()*100)));
		}
		chinaUmsRequest.setGoods(request.getGoods());
		chinaUmsRequest.setNotifyUrl(request.getNotifyUrl());
		chinaUmsRequest.setReturnUrl(request.getReturnUrl());
		chinaUmsRequest.setMsgType(method);
//		chinaUmsRequest.setSystemId(appId);
		return chinaUmsRequest;
	}
	
	private List<KeyValuePair> getKeyPairs(ChinaUmsRequest paymentRequest) {
		if (paymentRequest == null) {
			return null;
		}
		List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

		if (StringUtils.isNotBlank(paymentRequest.getMsgSrc())) {
			keyPairs.add(new KeyValuePair("msgSrc", paymentRequest.getMsgSrc()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getRequestTimeStamp())) {
			keyPairs.add(new KeyValuePair("requestTimeStamp", paymentRequest
					.getRequestTimeStamp()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMid())) {
			keyPairs.add(new KeyValuePair("mid", paymentRequest
					.getMid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTid())) {
			keyPairs.add(new KeyValuePair("tid", paymentRequest.getTid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBillNo())) {
			keyPairs.add(new KeyValuePair("billNo", paymentRequest.getBillNo()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBillDate())) {
			keyPairs.add(new KeyValuePair("billDate", paymentRequest.getBillDate()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getBillDesc())) {
			keyPairs.add(new KeyValuePair("billDesc", paymentRequest.getBillDesc()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getTotalAmount())) {
			keyPairs.add(new KeyValuePair("totalAmount", paymentRequest.getTotalAmount()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getSign())) {
			keyPairs.add(new KeyValuePair("sign", paymentRequest.getSign()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getNotifyUrl())) {
			keyPairs.add(new KeyValuePair("notifyUrl", paymentRequest.getNotifyUrl()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getInstMid())) {
			keyPairs.add(new KeyValuePair("instMid", paymentRequest.getInstMid()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getMsgType())) {
			keyPairs.add(new KeyValuePair("msgType", paymentRequest.getMsgType()));
		}
		if (StringUtils.isNotBlank(paymentRequest.getRefundAmount())) {
			keyPairs.add(new KeyValuePair("refundAmount", paymentRequest.getRefundAmount()));
		}
		if(paymentRequest.getGoods()!=null) {
			keyPairs.add(new KeyValuePair("goods", JsonUtils.toJson(paymentRequest.getGoods())));
		}
		if(StringUtils.isNotBlank(paymentRequest.getReturnUrl())) {
			keyPairs.add(new KeyValuePair("returnUrl", paymentRequest.getReturnUrl()));
		}
		keyPairs.sort((x1, x2) -> {
			return x1.getKey().compareTo(x2.getKey());
		});
		return keyPairs;
	}
	
	private String signature(String params) {
        // 进行base64转换
        String base64RequestParams = Base64.encodeBase64String(params.getBytes(Charset
            .forName("utf-8")));    
        
        // 加签
        String pfxFile = KEY_PATH+ this.merCustId+".pfx";
        
        SignResult signResult  = CFCASignature.signature( pfxFile, this.pfxFilePwd,
            base64RequestParams, "utf-8");

        if ("000".equals(signResult.getCode())) {       	
            return signResult.getSign();
        } else {
        	logger.error("加签失败>>"+params);
            return "加签失败";
        }
	}
	
	private String parseResult(String responseJson){
		 // 将json格式字符串转换为json对象
		 Map<String,String> jsonObject = JsonUtils.fromJson(responseJson, Map.class);

        // 取得返回数据密文
        String sign = jsonObject.get("check_value");
        // 验签cer文件名
        String cerName = KEY_PATH+this.merCustId+".cer";
        
        // 进行验签，参数1为汇付商户号，固定为100001
        VerifyResult verifyResult = CFCASignature.verifyMerSign("100001", sign, "utf-8",  cerName);
  
        logger.info("verifyResult" + verifyResult.toString());
        System.out.println("verifyResult" + verifyResult.toString());
        
        if ("000".equals(verifyResult.getCode())) {
            
            //取得base64格式内容
            String content = new String(verifyResult.getContent(), Charset.forName("utf-8"));
            //System.out.println("content = " + content);
            
            //base64格式解码
            String decrptyContent = new String(Base64.decodeBase64(content), Charset.forName("utf-8"));
            logger.info("decrptyContent = " + decrptyContent);
            System.out.println("decrptyContent = " + decrptyContent);
            return decrptyContent;
        } else {
        	logger.error("验签失败>>"+responseJson);
            return "验签失败";
        }
	}

	private PaymentResponse toPaymentResponse(ChinaUmsRequest chinaUmsRequest, ChinaUmsResponse chinaUmsResponse) {
		if (chinaUmsResponse == null || !ChinaUmsResponse.SUCCESS.equals(chinaUmsResponse.getErrCode())
				|| StringUtils.isBlank(chinaUmsResponse.getBillQRCode())) {
			String code = chinaUmsResponse == null ? NO_RESPONSE : chinaUmsResponse.getErrCode();
			String msg = chinaUmsResponse == null ? "No response" : chinaUmsResponse.getErrMsg();
			throw new GatewayException(code, msg);
		}
		PaymentResponse response = new PaymentResponse();
		response.setCode(PaymentResponse.SUCCESS);
		Bill bill = new Bill();
		bill.setCodeUrl(chinaUmsResponse.getBillQRCode());
		bill.setOrderNo(chinaUmsRequest.getBillNo());
		bill.setGatewayOrderNo(chinaUmsResponse.getQrCodeId());
		bill.setOrderStatus(toOrderStatus(chinaUmsResponse.getBillStatus()));
		response.setBill(bill);
		return response;
	}

	public static OrderStatus toOrderStatus(String billStatus) {
		if("PAID".equals(billStatus) || "TRADE_SUCCESS".equals(billStatus)) {
			return OrderStatus.SUCCESS;
		} else if("UNPAID".equals(billStatus)) {
			return OrderStatus.NOTPAY;
		}else if("REFUND".equals(billStatus) || "TRADE_REFUND".equals(billStatus)) {
			return OrderStatus.REFUND;
		}else if("CLOSED".equals(billStatus)) {
			return OrderStatus.CLOSED;
		} else {
			return OrderStatus.NOTPAY;
		}
		
	}
	
	public static void main(String args[]){
		//6666000000002619
		/*
		 * this.merCustId = config.get("merCustId");
			this.pfxFilePwd = config.get("pfxFilePwd");
			this.divAcctId = config.get("divAcctId");
		 */
		HaoDaProxy proxy = new HaoDaProxy();
		proxy.init("{\"merCustId\":\"6666000000036492\",\"pfxFilePwd\":\"123456\",\"divAcctId\":\"38888\"}");
		PaymentRequest request = new PaymentRequest();
		request.setOrderNo("20180101501004022041");
		request.setOrderTime("20180327");
		request.setTotalFee(0.01f);
		request.setCmdId("218");
		request.setSubject("购买商品");
		request.setPayType(PayType.ALIPAY_SCAN);
		request.setNotifyUrl("http://192.168.0.74:8001/npayCallBack/asyncHandle.json");
		proxy.unifiedOrder(request);
	}


}
