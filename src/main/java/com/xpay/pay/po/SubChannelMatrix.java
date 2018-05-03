package com.xpay.pay.po;

import com.xpay.pay.model.SubChannel;

public class SubChannelMatrix {
	private Long id; //子商户id;
	private String name; //子商户名字
	private String dateTime;//日期
	private int notpayCnt; //notpay状态的订单笔数
	private int payerrorCnt;
	private int succssCnt;
	private int totalCnt; 
	private float minAmt;//子商户允许最低交易额
	private float maxAmt;//子商户允许最高交易额
	private long timestamp; //
	
	@Override
	public boolean equals(Object obj) {
		SubChannel innerObj = (SubChannel) obj;
		if(innerObj == null || innerObj.getId() == null){
			return false;
		}
        return (this.getId().longValue() == innerObj.getId().longValue());
    } 
	
	/*private static int count = 1;
	public SubChannelMatrix(){
		id =  Long.parseLong(count+"");
		name = "名称"+1;
		dateTime = "2018-04-22";
		notpayCnt = 10+count;
		succssCnt = 5+count;
		payerrorCnt = 2+count;
		totalCnt = notpayCnt + succssCnt + payerrorCnt;
		minAmt = 10*count;
		maxAmt = 10* count +9;
		
		count ++;
	}*/
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getNotpayCnt() {
		return notpayCnt;
	}
	public void setNotpayCnt(int notpayCnt) {
		this.notpayCnt = notpayCnt;
	}
	public int getPayerrorCnt() {
		return payerrorCnt;
	}
	public void setPayerrorCnt(int payerrorCnt) {
		this.payerrorCnt = payerrorCnt;
	}
	public int getSuccssCnt() {
		return succssCnt;
	}
	public void setSuccssCnt(int succssCnt) {
		this.succssCnt = succssCnt;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public float getMinAmt() {
		return minAmt;
	}
	public void setMinAmt(float minAmt) {
		this.minAmt = minAmt;
	}
	public float getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(float maxAmt) {
		this.maxAmt = maxAmt;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public float getSuccessRate(){
		if(totalCnt == 0){ //没有单子的时候
			return 0.5f;
		}else{
			return (float)this.succssCnt/ (float)this.totalCnt;
		}
	}
	

}
