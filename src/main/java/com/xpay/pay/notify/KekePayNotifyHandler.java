package com.xpay.pay.notify;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xpay.pay.proxy.kekepay.KekePayProxy;

/**
 * Created by sunjian on Date: 2017/9/21 下午3:12
 * Description:
 */
@Service
public class KekePayNotifyHandler extends AbstractNotifyHandler {

  @Override
  protected NotifyBody extractNotifyBody(String url, String content) {
	  String billNo = "";
	  String extOrderNo = "";
	  String totalFee = "";
	  String status = "";
	  try {
		  String paramStr = url.substring(url.indexOf("?")+1);
		  String decoded = URLDecoder.decode(paramStr, "utf-8");
		  String[] params = decoded.split("&");
			
		  for (String param : params) {
		 	  String[] pair = param.split("=");
			  String key = pair[0];
			  if ("outTradeNo".equals(key)) {
				  billNo = pair[1];
			  } if ("tradeStatus".equals(key)) {
			  	  status = pair[1];
			  } else if ("orderPrice".equals(key)) {
				  totalFee = String.valueOf((int) (Float.valueOf(pair[1]) * 100));
			  } else if ("trxNo".equals(key)) {
			 	  extOrderNo = pair[1];
			  }
		  }
		} catch (Exception e) {
			
		}
		return StringUtils.isBlank(billNo)?null:new NotifyBody(billNo, extOrderNo, KekePayProxy.toOrderStatus(status), totalFee, null);
   }

  @Override
  protected String getSuccessResponse() {
    return "SUCCESS";
  }

  @Override
  protected String getFailedResponse() {
    return null;
  }

  public static class KekePayNotification {

    private String payKey;
    private String orderPrice;
    private String outTradeNo;
    private String productType;
    private String orderTime;
    private String productName;
    private String tradeStatus;
    private String successTime;//yyyyMMddHHmmss
    private String trxNo;
    private String remark;
    private String sign;

    public String getPayKey() {
      return payKey;
    }

    public void setPayKey(String payKey) {
      this.payKey = payKey;
    }

    public String getOrderPrice() {
      return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
      this.orderPrice = orderPrice;
    }

    public String getOutTradeNo() {
      return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
      this.outTradeNo = outTradeNo;
    }

    public String getProductType() {
      return productType;
    }

    public void setProductType(String productType) {
      this.productType = productType;
    }

    public String getOrderTime() {
      return orderTime;
    }

    public void setOrderTime(String orderTime) {
      this.orderTime = orderTime;
    }

    public String getProductName() {
      return productName;
    }

    public void setProductName(String productName) {
      this.productName = productName;
    }

    public String getTradeStatus() {
      return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
      this.tradeStatus = tradeStatus;
    }

    public String getSuccessTime() {
      return successTime;
    }

    public void setSuccessTime(String successTime) {
      this.successTime = successTime;
    }

    public String getTrxNo() {
      return trxNo;
    }

    public void setTrxNo(String trxNo) {
      this.trxNo = trxNo;
    }

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }

    public String getSign() {
      return sign;
    }

    public void setSign(String sign) {
      this.sign = sign;
    }
  }

}