package com.xpay.pay.proxy.haoda;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.xpay.pay.proxy.haoda.ZSRequest.HaoDaMediaType;
import com.xpay.pay.util.JsonUtils;

import java.io.IOException;

/**
 * @author Administrator
 */
public class HttpRestTest {
    public static void postZSOrder() {
        //测试公司的API接口，将json当做一个字符串传入httppost的请求体
        String result = null;
        HttpPost httpPost = new HttpPost("http://39.108.13.25:9000/qrc/wxh5/order/v0");
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            //设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            CommonRequestBody commonRequestBody = new CommonRequestBody();
            commonRequestBody.setPlatformId("10010005");

            ZSRequest zsRequest = new ZSRequest();
            zsRequest.setMerchantId("2018040910000003");
            zsRequest.setOrderDate("20180312");
            zsRequest.setMediaType(HaoDaMediaType.WAP.name());
            zsRequest.setMediaIdentify("www.zmpay.top");
            zsRequest.setMediaName("纳优支付");
            zsRequest.setOrderId("201803120001007");
            zsRequest.setGoodsDesc("测试订单");
            zsRequest.setOrderAmount("100"); //1元
            zsRequest.setCallBackUrl("http://www.zmpay.top/");
            zsRequest.setTerminalIp("101.95.130.178");
    
            commonRequestBody.setBusinessBody(JsonUtils.toJson(zsRequest));
            commonRequestBody.setBusinessBodySign(MD5Util.getMD5Str(commonRequestBody.getBusinessBody() + "4E80F0523935447B"));
            StringEntity entity = new StringEntity(JsonUtils.toJson(commonRequestBody),"UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            result = response.toString();
            HttpEntity respEntity = response.getEntity();
            ;
            String content = IOUtils.toString(respEntity.getContent(), "UTF-8");
            System.out.println(content);
            CommonResponseBody body = JsonUtils.fromJson(content, CommonResponseBody.class);
            ZSResponse res = JsonUtils.fromJson(body.getData(), ZSResponse.class);
           
            System.out.println(res.getWxPayUrl());
        } catch (Exception e) {
            System.out.println("接口请求失败" + e.toString());
        }
     
    }

    public static void getOrderInfo() {
        CloseableHttpClient httpCilent2 = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet2 = new HttpGet("localhost:9000/qrc/c2b/order/10001001?merchantId=20180312100000000000&orderType=JD&orderDate=20180312&orderId=201803125");
        httpGet2.setConfig(requestConfig);
        String srtResult = "";
        try {
            HttpResponse httpResponse = httpCilent2.execute(httpGet2);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
                System.out.println(srtResult);
            } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
            } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpCilent2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String []args){
    	postZSOrder();
    	/*String liox = "{\"code\":\"200\",\"message\":\"success\",\"data\":{\"merchantId\":\"2018040910000003\",\"orderAmount\":\"100\",\"orderDate\":\"20180312\",\"orderId\":\"201803120001001\",\"payStatus\":\"PROCESSING\",\"respCode\":\"0000\",\"respMessage\":\"获取微信H5支付链接成功\",\"systemOrderId\":\"20180417100001061476\",\"wxPayUrl\":\"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx17141734000869e41cf6f9591615714936&package=2516307639\",\"wxPrepayId\":\"wx17141734000869e41cf6f9591615714936\"},\"sign\":\"lsqvKxQfFU5NvX4icjqwJg==\"}"; 
    	
    	String liox ="{\"code\":\"200\",\"message\":\"success\",\"data\":{\"merchantId\":\"2018040910000003\",\"orderAmount\":\"100\",\"orderDate\":\"20180312\",\"orderId\":\"201803120001003\",\"payStatus\":\"PROCESSING\",\"respCode\":\"0000\",\"respMessage\":\"获取微信H5支付链接成功\",\"systemOrderId\":\"20180417100001061701\",\"wxPayUrl\":\"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx17143511869488b4c3b404841749607656&package=3716363405\",\"wxPrepayId\":\"wx17143511869488b4c3b404841749607656\"},\"sign\":\"mq2ovNzYFJ1ZbLxKTSuakA==\"}";
    	CommonResponseBody body = JsonUtils.fromJson(liox, CommonResponseBody.class);
    	System.out.println(body.getData().getWxPayUrl());*/
    }
}
