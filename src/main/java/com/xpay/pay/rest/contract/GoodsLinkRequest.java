package com.xpay.pay.rest.contract;

import java.util.List;

public class GoodsLinkRequest {
	private long goodsId;
	private List<Long> extGoodsIds;
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public List<Long> getExtGoodsIds() {
		return extGoodsIds;
	}
	public void setExtGoodsIds(List<Long> extGoodsIds) {
		this.extGoodsIds = extGoodsIds;
	}
}
