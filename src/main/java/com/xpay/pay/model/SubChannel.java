package com.xpay.pay.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xpay.pay.RiskEngine;
import com.xpay.pay.model.StoreChannel.ChannelProps;
import com.xpay.pay.model.StoreChannel.ChinaUmsProps;
import com.xpay.pay.model.StoreChannel.IpsProps;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.model.StoreChannel.SUPayProps;
import com.xpay.pay.util.JsonUtils;

public class SubChannel {
	
	private Long id = null;
	private String props;
	private String name;//子商户名称
	private Status status;
	private String poolType;
	private PaymentGateway paymentGateway;
	private ChannelProps channelProps;
	private Date createDate;
	private Date updateDate;
	
	private long timestamp;
	
	public enum Status{
		NORMAL,
		LOCKED,
		DISABLE,
		;
		public static Status getStatusByName(String name){
			if(name == null){
				return null;
			}
			for(Status item :Status.values()){
				if(item.name().equals(name)){
					return item;
				}
			}
			return null;
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
		if(StringUtils.isNotBlank(props)) {
			if(this.paymentGateway == PaymentGateway.CHINAUMSH5) {
				this.channelProps = JsonUtils.fromJson(props, ChinaUmsProps.class);
			} else if(this.paymentGateway == PaymentGateway.IPSSCAN
					|| this.paymentGateway == PaymentGateway.IPSWX) {
				if(this.props != null){
					this.channelProps = JsonUtils.fromJson(props, IpsProps.class);
				}
			} else if(this.paymentGateway == PaymentGateway.SUPay) {
				this.channelProps = JsonUtils.fromJson(props, SUPayProps.class);
			}
		}
	}
	public Status getStatus() {
		return status ;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getPoolType() {
		return poolType;
	}
	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}
	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public ChannelProps getChannelProps() {
		return channelProps;
	}
	public void setChannelProps(ChannelProps channelProps) {
		this.channelProps = channelProps;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTimestamp() {
		if(this.timestamp >0 ){
			return this.timestamp;
		}
		if(this.updateDate != null){
			this.timestamp = this.updateDate.getTime();
		}else{
			this.timestamp =10000;//默认
		}
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		SubChannel innerObj = (SubChannel) obj;
		if(innerObj.getId() == null){
			return false;
		}
		
        return (this.getId().longValue() == innerObj.getId().longValue());
    } 
	
	public  static void main(String args[]){
		String []liox ={"a","b","c"};
		String a = "fasdfas";
		//System.out.println(a.indexOf("fas"));
		float fee = 11.0f;
		System.out.println(fee%10 == 0);
		/*for(int i=0;i<100;i++){
			float x=(float)(Math.random()*1);
			x = (float)(Math.round(x*10))/10;
		   System.out.println(x-4); 
		}*/
		
	}
	
}
