package com.xpay.pay.proxy.haoda;

/**
 * Created with IntelliJ IDEA.
 * User: simons.xiao
 * Date: 2018/2/24
 * Time: 上午1:29
 */
public class CommonResponseBody {
    private String code;
    private String message;
    private String sign;

    private String data;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
    
}