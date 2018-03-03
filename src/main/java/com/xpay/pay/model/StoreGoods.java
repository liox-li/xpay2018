package com.xpay.pay.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.xpay.pay.util.JsonUtils;

public class StoreGoods {
	private Long id;
	private Long storeId;
	private String code;
	private String extStoreId;
	private String name;
	private String desc;
	private Float amount;
	private String extQrCode;
	private String extGoods;
	private List<ExtGoods> extGoodsList;
	private List<StoreExtGoods> storeExtGoodsList;
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
		if(CollectionUtils.isNotEmpty(this.getExtGoodsList())) {
			return this.getExtGoodsList().stream().map(x -> x.getExtQrCode()).toArray(String[]::new);
		}
		return new String[0];
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public List<ExtGoods> getExtGoodsList() {
		if(CollectionUtils.isEmpty(this.extGoodsList)) {
			if(StringUtils.isNotBlank(extGoods)) {
				extGoodsList = JsonUtils.fromJsonArray(extGoods, ExtGoods.class);
			} else if(StringUtils.isNotBlank(extQrCode)) {
				extGoodsList = new ArrayList<ExtGoods>();
				String[] goodsArr = StringUtils.split(this.extQrCode, ',');
				for(int i=0;i<goodsArr.length;i++) {
					ExtGoods goods = new ExtGoods();
					goods.setExtQrCode(goodsArr[i]);
					goods.setNote(String.valueOf(i+1));
					extGoodsList.add(goods);
				}
			}
		}
		
		return extGoodsList;
	}
	public String getExtGoods() {
		if(StringUtils.isBlank(extGoods)) {
			List<ExtGoods> extGoodsList = this.getExtGoodsList();
			this.extGoods = JsonUtils.toJson(extGoodsList);
		}
		return extGoods;
	}
	public void setExtGoods(String extGoods) {
		this.extGoods = extGoods;
	}
	public void setExtGoodsList(List<ExtGoods> extGoodsList) {
		this.extGoodsList = extGoodsList;
	}

	
	public List<StoreExtGoods> getStoreExtGoodsList() {
		return storeExtGoodsList;
	}
	public void setStoreExtGoodsList(List<StoreExtGoods> storeExtGoodsList) {
		this.storeExtGoodsList = storeExtGoodsList;
	}


	public static class ExtGoods {
		private String extStoreId;
		private String extQrCode;
		private String note;
		
		public String getExtStoreId() {
			return extStoreId;
		}
		public void setExtStoreId(String extStoreId) {
			this.extStoreId = extStoreId;
		}
		public String getExtQrCode() {
			return extQrCode;
		}
		public void setExtQrCode(String extQrCode) {
			this.extQrCode = extQrCode;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
	}
}
