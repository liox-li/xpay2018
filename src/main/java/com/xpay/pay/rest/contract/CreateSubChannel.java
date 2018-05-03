package com.xpay.pay.rest.contract;

import java.util.List;

public class CreateSubChannel {
	
	private String companyName;
	private String mertCode;
    private String poolType;
    private String [] goodNameList;
    
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getMertCode() {
		return mertCode;
	}
	public void setMertCode(String mertCode) {
		this.mertCode = mertCode;
	}
	public String getPoolType() {
		return poolType;
	}
	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}
	public String[] getGoodNameList() {
		return goodNameList;
	}
	public void setGoodNameList(String[] goodNameList) {
		this.goodNameList = goodNameList;
	}

	
	
    
    
	
}
