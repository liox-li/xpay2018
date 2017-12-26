package com.xpay.pay.proxy.ips.transfer.req;

/**
 * Created by sunjian on Date: 2017/12/26 下午8:33
 * Description:
 */
public class Body {

  private String merBillNo;
  private String transferType;
  private String merAcctNo;
  private String customerCode;
  private String transferAmount;
  private String collectionItemName;
  private String remark;

  public String getMerBillNo() {
    return merBillNo;
  }

  public void setMerBillNo(String merBillNo) {
    this.merBillNo = merBillNo;
  }

  public String getTransferType() {
    return transferType;
  }

  public void setTransferType(String transferType) {
    this.transferType = transferType;
  }

  public String getMerAcctNo() {
    return merAcctNo;
  }

  public void setMerAcctNo(String merAcctNo) {
    this.merAcctNo = merAcctNo;
  }

  public String getCustomerCode() {
    return customerCode;
  }

  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }

  public String getTransferAmount() {
    return transferAmount;
  }

  public void setTransferAmount(String transferAmount) {
    this.transferAmount = transferAmount;
  }

  public String getCollectionItemName() {
    return collectionItemName;
  }

  public void setCollectionItemName(String collectionItemName) {
    this.collectionItemName = collectionItemName;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}