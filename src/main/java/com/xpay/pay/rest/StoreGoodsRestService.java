package com.xpay.pay.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.StoreExtGoods;
import com.xpay.pay.model.StoreGoods;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.ExtStore;
import com.xpay.pay.service.StoreExtGoodsService;
import com.xpay.pay.service.StoreGoodsService;

@CrossOrigin(maxAge = 3600)
@RestController
public class StoreGoodsRestService extends AdminRestService {

	@Autowired
	private StoreGoodsService storeGoodsService;
	@Autowired
	private StoreExtGoodsService extGoodsService;
	
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
		Assert.isTrue(result, "Update goods failed");
		BaseResponse<StoreGoods> response = new BaseResponse<StoreGoods>();
		response.setData(goods);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/goods/{goodsId}", method = RequestMethod.DELETE)
	public BaseResponse<StoreGoods> deleteStoreGoods(@PathVariable long id, @PathVariable long storeId, 
			 @PathVariable long goodsId) {
		validateAgent(id);
		
		boolean result = storeGoodsService.delete(goodsId);
		Assert.isTrue(result, "Delete goods failed");
		BaseResponse<StoreGoods> response = new BaseResponse<StoreGoods>();
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/store_pool", method = RequestMethod.GET)
	public BaseResponse<List<ExtStore>> getStorePool(@PathVariable long id, @PathVariable long storeId) {
		validateAgent(id);
		
		List<ExtStore> result = extGoodsService.getExtStorePool(storeId);
		Assert.notNull(result, "No ext store pool found");
		BaseResponse<List<ExtStore>> response = new BaseResponse<List<ExtStore>>();
		response.setData(result);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/store_pool", method = RequestMethod.PUT)
	public BaseResponse addExtStore(@PathVariable long id, @PathVariable long storeId,  @RequestBody ExtStore extStore) {
		validateAgent(id);
		
		List<ExtStore> extStores = extGoodsService.getExtStorePool(storeId);
		Assert.isTrue(!extStores.stream().filter(x -> x.equals(extStore)).findAny().isPresent(), "Ext Store already exist");
		
		boolean result = extGoodsService.createExtStore(storeId, extStore);
		Assert.isTrue(result, "Create ext store faile");
		return BaseResponse.OK;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/store_pool/{extStoreId}", method = RequestMethod.DELETE)
	public BaseResponse deleteExtStore(@PathVariable long id, @PathVariable long storeId,  @PathVariable String extStoreId) {
		validateAgent(id);
		
		boolean result = extGoodsService.deleteExtStore(extStoreId);
		Assert.isTrue(result, "Delete ext store failed");
		return BaseResponse.OK;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/store_pool/{extStoreId}/goods", method = RequestMethod.POST)
	public BaseResponse addExtStoreGoods(@PathVariable long id, @PathVariable long storeId, @PathVariable String extStoreId,  @RequestBody StoreGoods storeGoods) {
		validateAgent(id);
		List<ExtStore> extStores = extGoodsService.getExtStorePool(storeId);
		ExtStore extStore = extStores.stream().filter(x -> x.getExtStoreId().equals(extStoreId) ).findAny().orElse(null);
		Assert.notNull(extStore, "Ext store not found");
		
		StoreExtGoods extGoods = extGoodsService.findByStoreId(storeId).stream().filter(x -> x.getExtStoreId().equals(extStoreId) && CollectionUtils.isEmpty(x.getExtGoodsList())).findAny().orElse(null);
		if(extGoods == null) {
			extGoods = new StoreExtGoods();
			extGoods.setStoreId(storeId);
			extGoods.setExtStoreId(extStoreId);
			extGoods.setExtStoreName(extStore.getExtStoreName());
		}
		extGoods.setName(storeGoods.getName());
		extGoods.setAmount(storeGoods.getAmount());
		extGoods.setExtGoodsList(storeGoods.getExtGoodsList());
				
		boolean result = extGoodsService.createExtStoreGoods(extGoods);
		Assert.isTrue(result, "Create ext store faile");
		return BaseResponse.OK;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/store_pool/{extStoreId}/goods", method = RequestMethod.GET)
	public BaseResponse<List<StoreExtGoods>> getExtStoreGoods(@PathVariable long id, @PathVariable long storeId, @PathVariable String extStoreId) {
		validateAgent(id);

//		List<StoreExtGoods> extGoods = extGoodsService.findByStoreId(storeId).stream().filter(x -> x.getExtStoreId().equals(extStoreId) && !CollectionUtils.isEmpty(x.getExtGoodsList())).collect(Collectors.toList());
		List<StoreExtGoods> extGoods = extGoodsService.findByExtStoreId(extStoreId);
		BaseResponse<List<StoreExtGoods>> response = new BaseResponse<List<StoreExtGoods>>();
		response.setData(extGoods);
		return response;
	}
	
}
