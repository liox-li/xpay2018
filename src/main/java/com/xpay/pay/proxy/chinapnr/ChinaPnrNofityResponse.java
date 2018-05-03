package com.xpay.pay.proxy.chinapnr;

import org.apache.commons.lang3.StringUtils;

public class ChinaPnrNofityResponse {
	private String cmd_id;//消息类型
	private String resp_code;//218002--调用成功，其他见返回码附件表
	private String resp_desc;//应答返回描述
	private String bg_bank_code;//银行返回码
	private String bg_bank_message;//银行返回描述
	private String platform_seq_id;//本平台交易唯一标识号  组成规则：8位本平台日期+10位系统流水号
	private String pay_url;//支付地址  支付宝统一下单返回参数支付宝浏览器下直接用此链接请求支付宝支付(注：目前部分通道不支持返回支付地址)
	private String token_id;//动态口令 微信公众号参数根据token_id组装如下url地址，在微信下可直接唤起支付https://pay.swiftpass.cn/pay/jspay?token_id=9a0610bc519e782e6275e8c7dd94a445&showwxtitle=1
	private String pay_info;// 支付信息
	private String order_date;//订单日期
	private String order_id;//订单号
	private String mer_cust_id;//商户客户号
	private String user_cust_id;//用户客户号

	
}
