package com.xpay.pay.proxy.ips.queryorders.req;

public class Body {

	  private String merAcctNo;

	  private String customerCode;

	  private String ordersType;

	  private String merBillNo;

	  private String ipsBillNo;

	  private String startTime;

	  private String endTime;

	  private String currentPage;

	  private String pageSize;


	  public String getMerAcctNo() {
	    return merAcctNo;
	  }

	  public void setMerAcctNo(String merAcctNo) {
	    this.merAcctNo = merAcctNo;
	  }

	  public String getCustomerCode() {
	    return customerCode;
	  }

	  public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	  }

	  public String getOrdersType() {
	    return ordersType;
	  }

	  public void setOrdersType(String ordersType) {
	    this.ordersType = ordersType;
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

	  public String getStartTime() {
	    return startTime;
	  }

	  public void setStartTime(String startTime) {
	    this.startTime = startTime;
	  }

	  public String getEndTime() {
	    return endTime;
	  }

	  public void setEndTime(String endTime) {
	    this.endTime = endTime;
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
	  
}