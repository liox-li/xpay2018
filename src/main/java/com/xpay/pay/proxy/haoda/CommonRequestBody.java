package com.xpay.pay.proxy.haoda;

/**
 * Created with IntelliJ IDEA.
 * User: simons.xiao
 * Date: 2018/2/26
 * Time: 上午11:39
 */
public class CommonRequestBody {
    private String platformId;
    private String token;

    private String businessBody;
    private String businessBodySign;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBusinessBody() {
        return businessBody;
    }

    public void setBusinessBody(String businessBody) {
        this.businessBody = businessBody;
    }

    public String getBusinessBodySign() {
        return businessBodySign;
    }

    public void setBusinessBodySign(String businessBodySign) {
        this.businessBodySign = businessBodySign;
    }
}