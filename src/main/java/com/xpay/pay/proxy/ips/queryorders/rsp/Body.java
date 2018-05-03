package com.xpay.pay.proxy.ips.queryorders.rsp;

public class Body {

	private String totalCount;

	private String rowCount;

	private String currentPage;

	private String pageSize;

	private String totalPage;

	private String merBillNo;

	private String ipsBillNo;

	private String createTime;

	private String ccyType;

	private String ordersType;

	private String collectAcc;

	private String payMerAcc;

	private String bankCard;

	private String Ipsfeepayer;

	private String ipsFee;

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getRowCount() {
		return rowCount;
	}

	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTotalPage() {
	    return totalPage;
	}

	public void setTotalPage(String totalPage) {
	    this.totalPage = totalPage;
	}

	public String getMerBillNo() {
		return merBillNo;
	}

	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}

	public String getIpsBillNo() {
		return ipsBillNo;
	}

	public void setIpsBillNo(String ipsBillNo) {
		this.ipsBillNo = ipsBillNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCcyType() {
		return ccyType;
	}

	public void setCcyType(String ccyType) {
		this.ccyType = ccyType;
	}

	public String getOrdersType() {
		return ordersType;
	}

	public void setOrdersType(String ordersType) {
		this.ordersType = ordersType;
	}

	public String getCollectAcc() {
		return collectAcc;
	}

	public void setCollectAcc(String collectAcc) {
		this.collectAcc = collectAcc;
	}

	public String getPayMerAcc() {
		return payMerAcc;
	}

	public void setPayMerAcc(String payMerAcc) {
		this.payMerAcc = payMerAcc;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getIpsfeepayer() {
		return Ipsfeepayer;
	}

	public void setIpsfeepayer(String Ipsfeepayer) {
		this.Ipsfeepayer = Ipsfeepayer;
	}

	public String getIpsFee() {
		return ipsFee;
	}

	public void setIpsFee(String ipsFee) {
		this.ipsFee = ipsFee;
	}

}
