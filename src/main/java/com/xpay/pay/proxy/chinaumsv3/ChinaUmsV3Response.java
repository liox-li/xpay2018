package com.xpay.pay.proxy.chinaumsv3;

public class ChinaUmsV3Response {
	private String errCode;
	private String errMsg;
	private String msgType;
	private String msgSrc;
	private String mid;
	private String tid;
	private String instMid;
	private String h5PayUrl;
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgSrc() {
		return msgSrc;
	}
	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getInstMid() {
		return instMid;
	}
	public void setInstMid(String instMid) {
		this.instMid = instMid;
	}
	public String getH5PayUrl() {
		return h5PayUrl;
	}
	public void setH5PayUrl(String h5PayUrl) {
		this.h5PayUrl = h5PayUrl;
	}
}
