package com.xpay.pay.service;

import static com.xpay.pay.util.AppConfig.XPayConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.model.Store;
import com.xpay.pay.util.RoundRobinList;

@Service
public class RiskCheckService {
	private static final Logger LOG = LogManager.getLogger(RiskCheckService.class);
	private static boolean feeCheck = XPayConfig.getProperty("risk.check.fee", false);
	private static long DEFAULT_TTL = 6*60*1000L;
	private static ICache<String, RoundRobinList<Float>> feeCache = CacheManager.create(RoundRobinList.class, 1000);


	public boolean checkFee(Store store, float fee) {
		if(!feeCheck) {
			return true;
		}
		
		RoundRobinList<Float> list = feeCache.get(store.getCode());
		if(list == null) {
			list = new RoundRobinList<Float>(3);
			feeCache.put(store.getCode(), list, DEFAULT_TTL);
		}
		list.add(fee);
		if(list.size()<3) {
			return true;
		}
		
		float f1 = list.get(0);
		float f2 = list.get(1);
		float f3 = list.get(2);
//		if(f1<30f && equals(f1, f2) && equals(f2,f3)) {
//			LOG.warn(String.format("checkFee failed : %s,%s,%s", f1,f2,f3) );
//			return false;
//		}
		
		boolean result = false;
		for(float totalFee: list) {
			if(totalFee >5.0f) {
				return true;
			}
		}
		if(!result) {
			LOG.warn(String.format("checkFee failed : %s,%s,%s", f1,f2,f3) );
		}
		return result;
 	}
	
	public void refreshCache() {
		feeCache.destroy();
	}
	
	private boolean equals(float f1, float f2) {
		return Math.abs(f1 - f2)<=0.02f;
	}
}
