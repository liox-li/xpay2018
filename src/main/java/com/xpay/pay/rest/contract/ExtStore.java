package com.xpay.pay.rest.contract;

public class ExtStore {
	private String extStoreId;
	private String extStoreName;
	private String type = "CHINAUMS_XIAOWEI";
	public ExtStore() {
		
	}
	
	public ExtStore(String extStoreId, String extStoreName) {
		this.extStoreId = extStoreId;
		this.extStoreName = extStoreName;
	}
	
	public String getExtStoreId() {
		return extStoreId;
	}
	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}
	public String getExtStoreName() {
		return extStoreName;
	}
	public void setExtStoreName(String extStoreName) {
		this.extStoreName = extStoreName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extStoreId == null) ? 0 : extStoreId.hashCode());
		result = prime * result + ((extStoreName == null) ? 0 : extStoreName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtStore other = (ExtStore) obj;
		if (extStoreId == null) {
			if (other.extStoreId != null)
				return false;
		} else if (!extStoreId.equals(other.extStoreId))
			return false;
		if (extStoreName == null) {
			if (other.extStoreName != null)
				return false;
		} else if (!extStoreName.equals(other.extStoreName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
