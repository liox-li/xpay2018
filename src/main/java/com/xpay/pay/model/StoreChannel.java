package com.xpay.pay.model;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.TimeUtils;

public class StoreChannel {
	private long id;
	private String extStoreId;
	private PaymentGateway paymentGateway;
	private AtomicLong lastUseTime = new AtomicLong(0);
	private String extStoreName;
	private Long agentId;
	private String props;
	private ChannelProps channelProps;
	private Date updateDate;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExtStoreId() {
		return extStoreId;
	}

	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}

	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}

	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	
	public long getLastUseTime() {
		return this.lastUseTime.get();
	}

	public void setLastUseTime(long lastUseTime) {
		this.lastUseTime.set(lastUseTime);;
	}
	
	public String getExtStoreName() {
		return extStoreName;
	}

	public void setExtStoreName(String extStoreName) {
		this.extStoreName = extStoreName;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
		if(StringUtils.isNotBlank(props)) {
			if(this.paymentGateway == PaymentGateway.CHINAUMSH5) {
				this.channelProps = JsonUtils.fromJson(props, ChinaUmsProps.class);
			} else if(this.paymentGateway == PaymentGateway.IPSSCAN) {
				this.channelProps = JsonUtils.fromJson(props, IpsProps.class);
			} else if(this.paymentGateway == PaymentGateway.SUPay) {
				this.channelProps = JsonUtils.fromJson(props, SUPayProps.class);
			}
		}
 	}
	
	public ChannelProps getChannelProps() {
		return this.channelProps;
	}
	
	public void setChannelProps(ChannelProps channelProps) {
		this.channelProps = channelProps;
		if(channelProps!=null) {
			this.props = JsonUtils.toJson(channelProps);
		}
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}



	private static final long BLOCK_TIME_DAY= 2*1000;
	private static final long BLOCK_TIME_NIGHT= 5*1000;
	public boolean available() {
		long blockTime = TimeUtils.isNowDayTime()?BLOCK_TIME_DAY:BLOCK_TIME_NIGHT;
		boolean avail = System.currentTimeMillis()-this.lastUseTime.get()>blockTime;
		return avail;
	}

	public enum PaymentGateway {
		MIAOFU("jspay", "query", "refund"), 
		SWIFTPASS("unified.trade.pay", "unified.trade.query", "unified.trade.refund"), 
		CHINAUMS("yuedan.getQRCode", "yuedan.query", "yuedan.refund"), 
		CHINAUMSV2("bills.getQRCode", "bills.query", "bills.refund"), 
		CHINAUMSV3("trade.precreate", "query", "refund"), 
//		CHINAUMSH5("WXPay.jsPay", "query", "refund"),
		CHINAUMSH5("WXPay.jsPay", "query", "refund"),
		CHINAUMSWAP("WXPay.jsPay", "query", "refund"),
		UPAY("", "query", "refund"),
		RUBIPAY("unified.trade.pay", "unified.trade.query", "unified.trade.refund"),
		BAIFU("trade.weixin.gzpay", "query", "unified.trade.refund"), 
		JUZHEN("070201", "070101", ""), 
		KEFU("msBank_WeChatPay", "msBank_ScanPayQuery", ""), 
		KEKEPAY("pay","query",""),
		QFTXMP("pay", "query", ""),
		SUPay("pay", "", ""),
		IPSSCAN("pay","query", "refund"),
		IPSQUICK("pay","query", "refund"),
		TXF("", "query", "refund");
		
		String unifiedOrder;
		String query;
		String refund;
		PaymentGateway(String unifiedOrder, String query, String refund) {
			this.unifiedOrder = unifiedOrder;
			this.query = query;
			this.refund = refund;
		}
		
		public String UnifiedOrder() {
			return this.unifiedOrder;
		}
		
		public String Query() {
			return this.query;
		}
		
		public String Refund() {
			return this.refund;
		}
		
	}
	
	public static interface ChannelProps {
		
	}
	
	public static class ChinaUmsProps implements ChannelProps {
		private String tid;
		private String msgSrcId;
		private String msgSrc;
		private String signKey;
		private String instMid;
		public String getTid() {
			return tid;
		}
		public void setTid(String tid) {
			this.tid = tid;
		}
		public String getMsgSrcId() {
			return msgSrcId;
		}
		public void setMsgSrcId(String msgSrcId) {
			this.msgSrcId = msgSrcId;
		}
		public String getMsgSrc() {
			return msgSrc;
		}
		public void setMsgSrc(String msgSrc) {
			this.msgSrc = msgSrc;
		}
		public String getSignKey() {
			return signKey;
		}
		public void setSignKey(String signKey) {
			this.signKey = signKey;
		}
		public String getInstMid() {
			return instMid;
		}
		public void setInstMid(String instMid) {
			this.instMid = instMid;
		}
	}

	public static class IpsProps implements ChannelProps {
		private String merType;
		private String subMerCode;

		public String getMerType() {
			return merType;
		}

		public void setMerType(String merType) {
			this.merType = merType;
		}

		public String getSubMerCode() {
			return subMerCode;
		}

		public void setSubMerCode(String subMerCode) {
			this.subMerCode = subMerCode;
		}
	}

  public static class SUPayProps implements ChannelProps {

		private String itemId;
		private String payType;
		private String serverCode;

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public String getServerCode() {
			return serverCode;
		}

		public void setServerCode(String serverCode) {
			this.serverCode = serverCode;
		}
	}

	public enum ChannelType {
		WECHAT, ALIPAY, BANK, H5
	}
}
