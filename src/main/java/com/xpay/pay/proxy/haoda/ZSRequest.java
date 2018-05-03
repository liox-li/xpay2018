package com.xpay.pay.proxy.haoda;

/**
 * Created with IntelliJ IDEA.
 * User: simons.xiao
 * Date: 2018/3/10
 * Time: 下午11:08
 */
public class ZSRequest {
    private String merchantId;
    private String orderId;
    private String orderDate;
    private String orderType;
    private String orderAmount;
    private String orderSense;//订单场景
    private String goodsDesc;//商品描述
    private String callBackUrl;
    
    private String mediaType;//IOS（IOS），ANDROID（ANDROID）,WAP(H5)等
    private String mediaIdentify;//IOS填写bundleId  , ANDROID填写package name   ,WAP填写域名
    private String mediaName ;//例：王者荣耀
    private String terminalIp;//终端IP
    
    public static enum HaoDaMediaType{
    	IOS,ANDROID,WAP
    }
    
    public String getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderSense() {
        return orderSense;
    }

    public void setOrderSense(String orderSense) {
        this.orderSense = orderSense;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaIdentify() {
		return mediaIdentify;
	}

	public void setMediaIdentify(String mediaIdentify) {
		this.mediaIdentify = mediaIdentify;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getTerminalIp() {
		return terminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}
    
}