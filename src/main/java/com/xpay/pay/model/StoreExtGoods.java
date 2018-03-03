package com.xpay.pay.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.xpay.pay.model.StoreGoods.ExtGoods;
import com.xpay.pay.util.JsonUtils;

public class StoreExtGoods {
	private Long id;
	private Long goodsId;
	private Long storeId;
	private String extStoreId;
	private String extStoreName;
	private String extGoods;
	private List<ExtGoods> extGoodsList;
	private Date updateDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getExtStoreId() {
		return extStoreId;
	}
	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}
	public String getExtStoreName() {
		return extStoreName;
	}
	public void setExtStoreName(String extStoreName) {
		this.extStoreName = extStoreName;
	}
	public String[] getExtQrCodes() {
		if(CollectionUtils.isNotEmpty(this.getExtGoodsList())) {
			return this.getExtGoodsList().stream().map(x -> x.getExtQrCode()).toArray(String[]::new);
		}
		return new String[0];
	}
	public List<ExtGoods> getExtGoodsList() {
		if(CollectionUtils.isEmpty(this.extGoodsList)) {
			if(StringUtils.isNotBlank(extGoods)) {
				extGoodsList = JsonUtils.fromJsonArray(extGoods, ExtGoods.class);
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
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	

}
