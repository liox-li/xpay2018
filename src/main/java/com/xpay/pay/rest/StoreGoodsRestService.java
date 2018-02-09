package com.xpay.pay.rest;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.StoreGoods;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.service.StoreGoodsService;

@CrossOrigin(maxAge = 3600)
@RestController
public class StoreGoodsRestService extends AdminRestService {

	@Autowired
	private StoreGoodsService storeGoodsService;
	
	@RequestMapping(value = "/{id}/stores/{storeId}/goods", method = RequestMethod.GET)
	public BaseResponse<List<StoreGoods>> getStoreGoods(@PathVariable long id, @PathVariable long storeId) {
		validateAgent(id);
		
		List<StoreGoods> goods = storeGoodsService.findByStoreId(storeId);
		BaseResponse<List<StoreGoods>> response = new BaseResponse<List<StoreGoods>>();
		response.setData(goods);
		if(CollectionUtils.isNotEmpty(goods)) {
			response.setCount(goods.size());
		}
		return response;
	}
	

	@RequestMapping(value = "/{id}/stores/{storeId}/goods", method = RequestMethod.PUT)
	public BaseResponse<StoreGoods> addStoreGoods(@PathVariable long id, @PathVariable long storeId, 
			@RequestBody StoreGoods goods) {
		validateAgent(id);
		
		if(goods.getStoreId() == null) {
			goods.setStoreId(storeId);
		}
		boolean result = storeGoodsService.create(goods);
		Assert.isTrue(result, "Create goods failed");
		BaseResponse<StoreGoods> response = new BaseResponse<StoreGoods>();
		response.setData(goods);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/goods/{goodsId}", method = RequestMethod.PATCH)
	public BaseResponse<StoreGoods> updateStoreGoods(@PathVariable long id, @PathVariable long storeId, 
			 @PathVariable long goodsId, @RequestBody StoreGoods goods) {
		validateAgent(id);
		
		boolean result = storeGoodsService.update(goodsId, goods);
		Assert.isTrue(result, "Create goods failed");
		BaseResponse<StoreGoods> response = new BaseResponse<StoreGoods>();
		response.setData(goods);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/goods/{goodsId}", method = RequestMethod.DELETE)
	public BaseResponse<StoreGoods> deleteStoreGoods(@PathVariable long id, @PathVariable long storeId, 
			 @PathVariable long goodsId) {
		validateAgent(id);
		
		boolean result = storeGoodsService.delete(goodsId);
		Assert.isTrue(result, "Create goods failed");
		BaseResponse<StoreGoods> response = new BaseResponse<StoreGoods>();
		return response;
	}
	
}
