package com.xpay.pay.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.chinaums.ChinaUmsProxy;
import com.xpay.pay.proxy.chinaumsh5.ChinaUmsH5Proxy;
import com.xpay.pay.proxy.chinaumsv2.ChinaUmsV2Proxy;
import com.xpay.pay.proxy.chinaumsv3.ChinaUmsAliPayProxy;
import com.xpay.pay.proxy.chinaumswap.ChinaUmsWapProxy;
import com.xpay.pay.proxy.juzhen.JuZhenProxy;
import com.xpay.pay.proxy.kefu.KeFuProxy;
import com.xpay.pay.proxy.kekepay.KekePayProxy;
import com.xpay.pay.proxy.miaofu.MiaoFuProxy;
import com.xpay.pay.proxy.upay.UPayProxy;

@Service
public class PaymentProxyFactory {
	@Autowired
	private MiaoFuProxy miaoFuProxy;
//	@Autowired
//	private SwiftpassProxy swiftpassProxy;
	@Autowired
	private ChinaUmsProxy chinaUmsProxy;
	@Autowired
	private ChinaUmsV2Proxy chinaUmsV2Proxy;
	@Autowired
	private ChinaUmsAliPayProxy chinaUmsAliPayProxy;
	@Autowired
	private ChinaUmsH5Proxy chinaUmsH5Proxy;
	@Autowired
	private ChinaUmsWapProxy chinaUmsWapProxy;
	@Autowired
	private JuZhenProxy juZhenProxy;
	@Autowired
	private KeFuProxy keFuProxy;
	@Autowired
	private KekePayProxy kekePayProxy;
	@Autowired
	private UPayProxy upayProxy;
//	@Autowired
//	private RubiPayProxy rubiPayProxy;

	public IPaymentProxy getPaymentProxy(PaymentGateway channel) {
		switch (channel) {
			case CHINAUMSV2:
				return chinaUmsV2Proxy;
			case CHINAUMSALIPAY:
				return chinaUmsAliPayProxy;
			case CHINAUMSH5:
				return chinaUmsH5Proxy;
			case CHINAUMSWAP:
				return chinaUmsWapProxy;
			case CHINAUMS:
				return chinaUmsProxy;
			case UPAY:
				return upayProxy;
			case  JUZHEN:
				return juZhenProxy;
			case  KEFU:
				return keFuProxy;
			case MIAOFU:
				return miaoFuProxy;
			case KEKEPAY:
				return kekePayProxy;
 
      default:
        return chinaUmsV2Proxy;
		}
	}
}
