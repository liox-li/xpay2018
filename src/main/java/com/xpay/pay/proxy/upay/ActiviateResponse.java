package com.xpay.pay.proxy.upay;

public class ActiviateResponse {
	private String result_code;
	private BizResponse biz_response;

	public String getResult_code() {
		return result_code;
	}


	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}


	public BizResponse getBiz_response() {
		return biz_response;
	}


	public void setBiz_response(BizResponse biz_response) {
		this.biz_response = biz_response;
	}


	public class BizResponse {
		private String terminal_sn;
		private String terminal_key;
		private String merchant_sn;
		private String merchant_name;
		private String store_sn;
		private String store_name;
		public String getTerminal_sn() {
			return terminal_sn;
		}
		public void setTerminal_sn(String terminal_sn) {
			this.terminal_sn = terminal_sn;
		}
		public String getTerminal_key() {
			return terminal_key;
		}
		public void setTerminal_key(String terminal_key) {
			this.terminal_key = terminal_key;
		}
		public String getMerchant_sn() {
			return merchant_sn;
		}
		public void setMerchant_sn(String merchant_sn) {
			this.merchant_sn = merchant_sn;
		}
		public String getMerchant_name() {
			return merchant_name;
		}
		public void setMerchant_name(String merchant_name) {
			this.merchant_name = merchant_name;
		}
		public String getStore_sn() {
			return store_sn;
		}
		public void setStore_sn(String store_sn) {
			this.store_sn = store_sn;
		}
		public String getStore_name() {
			return store_name;
		}
		public void setStore_name(String store_name) {
			this.store_name = store_name;
		}
	}

}

