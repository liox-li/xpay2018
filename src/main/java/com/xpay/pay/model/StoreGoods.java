package com.xpay.pay.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class StoreGoods {
	private Long id;
	private Long storeId;
	private String code;
	private String extStoreId;
	private String name;
	private String desc;
	private Float amount;
	private String extQrCode;
	private Date updateDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExtStoreId() {
		return extStoreId;
	}
	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getExtQrCode() {
		return extQrCode;
	}
	public void setExtQrCode(String extQrCode) {
		this.extQrCode = extQrCode;
	}
	public String[] getExtQrCodes() {
		return StringUtils.split(extQrCode, ',');
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
