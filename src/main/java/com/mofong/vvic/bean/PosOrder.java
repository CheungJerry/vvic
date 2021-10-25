package com.mofong.vvic.bean;

import com.mofong.vvic.bean.PosOrderDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PosOrder {
	String orderId;

	String orderNo;

	String customerName;

	String operatorName;

	Double originPrice;

	Double changePrice;

	Integer totalNum;

	Integer getNum;

	Integer returnNum;

	Double getPrice;

	Double returnPrice;

	String ctime;

	String ymd;

	List<PosOrderDetail> list = new ArrayList<>();

	public PosOrder() {
	}

	public PosOrder(Map<String, Object> map) {
		this.orderId = (String) map.get("orderId");
		this.orderNo = (String) map.get("orderNo");
		this.customerName = (String) map.get("customerName");
		this.operatorName = (String) map.get("operatorName");
		this.originPrice = (Double) map.get("originPrice");
		this.changePrice = (Double) map.get("changePrice");
		this.totalNum = (Integer) map.get("totalNum");
		this.getNum = (Integer) map.get("getNum");
		this.returnNum = (Integer) map.get("returnNum");
		this.getPrice = (Double) map.get("getPrice");
		this.returnPrice = (Double) map.get("returnPrice");
		this.ctime = (String) map.get("ctime");
		this.ymd = (String) map.get("ymd");
		List<Map<String, Object>> orderDetailList = (List<Map<String, Object>>) map.get("orderDetailList");
		for (Map<String, Object> orderDetail : orderDetailList)
			this.list.add(new PosOrderDetail(orderDetail));
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + ((this.changePrice == null) ? 0 : this.changePrice.hashCode());
		result = 31 * result + ((this.ctime == null) ? 0 : this.ctime.hashCode());
		result = 31 * result + ((this.customerName == null) ? 0 : this.customerName.hashCode());
		result = 31 * result + ((this.getNum == null) ? 0 : this.getNum.hashCode());
		result = 31 * result + ((this.getPrice == null) ? 0 : this.getPrice.hashCode());
		result = 31 * result + ((this.operatorName == null) ? 0 : this.operatorName.hashCode());
		result = 31 * result + ((this.orderId == null) ? 0 : this.orderId.hashCode());
		result = 31 * result + ((this.orderNo == null) ? 0 : this.orderNo.hashCode());
		result = 31 * result + ((this.originPrice == null) ? 0 : this.originPrice.hashCode());
		result = 31 * result + ((this.returnNum == null) ? 0 : this.returnNum.hashCode());
		result = 31 * result + ((this.returnPrice == null) ? 0 : this.returnPrice.hashCode());
		result = 31 * result + ((this.totalNum == null) ? 0 : this.totalNum.hashCode());
		result = 31 * result + ((this.ymd == null) ? 0 : this.ymd.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		com.mofong.vvic.bean.PosOrder other = (com.mofong.vvic.bean.PosOrder) obj;
		if (this.changePrice == null) {
			if (other.changePrice != null)
				return false;
		} else if (!this.changePrice.equals(other.changePrice)) {
			return false;
		}
		if (this.ctime == null) {
			if (other.ctime != null)
				return false;
		} else if (!this.ctime.equals(other.ctime)) {
			return false;
		}
		if (this.customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!this.customerName.equals(other.customerName)) {
			return false;
		}
		if (this.getNum == null) {
			if (other.getNum != null)
				return false;
		} else if (!this.getNum.equals(other.getNum)) {
			return false;
		}
		if (this.getPrice == null) {
			if (other.getPrice != null)
				return false;
		} else if (!this.getPrice.equals(other.getPrice)) {
			return false;
		}
		if (this.orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!this.orderId.equals(other.orderId)) {
			return false;
		}
		if (this.orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!this.orderNo.equals(other.orderNo)) {
			return false;
		}
		if (this.originPrice == null) {
			if (other.originPrice != null)
				return false;
		} else if (!this.originPrice.equals(other.originPrice)) {
			return false;
		}
		if (this.returnNum == null) {
			if (other.returnNum != null)
				return false;
		} else if (!this.returnNum.equals(other.returnNum)) {
			return false;
		}
		if (this.returnPrice == null) {
			if (other.returnPrice != null)
				return false;
		} else if (!this.returnPrice.equals(other.returnPrice)) {
			return false;
		}
		if (this.totalNum == null) {
			if (other.totalNum != null)
				return false;
		} else if (!this.totalNum.equals(other.totalNum)) {
			return false;
		}
		if (this.ymd == null) {
			if (other.ymd != null)
				return false;
		} else if (!this.ymd.equals(other.ymd)) {
			return false;
		}
		return true;
	}

	public String compare(com.mofong.vvic.bean.PosOrder po) {
		return "PosOrder [operatorName=" + this.operatorName + "/" + po.operatorName + ", originPrice="
				+ this.originPrice + "/" + po.originPrice + ", changePrice=" + this.changePrice + "/" + po.changePrice
				+ ", totalNum=" + this.totalNum + "/" + po.totalNum + ", getNum=" + this.getNum + "/" + po.getNum
				+ ", returnNum=" + this.returnNum + "/" + po.returnNum + ", getPrice=" + this.getPrice + "/"
				+ po.getPrice + ", returnPrice=" + this.returnPrice + "/" + po.returnPrice + "]";
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Double getOriginPrice() {
		return this.originPrice;
	}

	public void setOriginPrice(Double originPrice) {
		this.originPrice = originPrice;
	}

	public Double getChangePrice() {
		return this.changePrice;
	}

	public void setChangePrice(Double changePrice) {
		this.changePrice = changePrice;
	}

	public Integer getTotalNum() {
		return this.totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getGetNum() {
		return this.getNum;
	}

	public void setGetNum(Integer getNum) {
		this.getNum = getNum;
	}

	public Integer getReturnNum() {
		return this.returnNum;
	}

	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}

	public Double getGetPrice() {
		return this.getPrice;
	}

	public void setGetPrice(Double getPrice) {
		this.getPrice = getPrice;
	}

	public Double getReturnPrice() {
		return this.returnPrice;
	}

	public void setReturnPrice(Double returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getCtime() {
		return this.ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getYmd() {
		return this.ymd;
	}

	public void setYmd(String ymd) {
		this.ymd = ymd;
	}

	public List<PosOrderDetail> getList() {
		return this.list;
	}

	public void setList(List<PosOrderDetail> list) {
		this.list = list;
	}
}
