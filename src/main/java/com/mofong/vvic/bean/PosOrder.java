package com.mofong.vvic.bean;

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
	List<PosOrderDetail> list = new ArrayList<PosOrderDetail>();

	public PosOrder() {
		super();
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
		for (Map<String, Object> orderDetail : orderDetailList) {
			this.list.add(new PosOrderDetail(orderDetail));
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Double getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(Double originPrice) {
		this.originPrice = originPrice;
	}

	public Double getChangePrice() {
		return changePrice;
	}

	public void setChangePrice(Double changePrice) {
		this.changePrice = changePrice;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getGetNum() {
		return getNum;
	}

	public void setGetNum(Integer getNum) {
		this.getNum = getNum;
	}

	public Integer getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}

	public Double getGetPrice() {
		return getPrice;
	}

	public void setGetPrice(Double getPrice) {
		this.getPrice = getPrice;
	}

	public Double getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(Double returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getYmd() {
		return ymd;
	}

	public void setYmd(String ymd) {
		this.ymd = ymd;
	}

	public List<PosOrderDetail> getList() {
		return list;
	}

	public void setList(List<PosOrderDetail> list) {
		this.list = list;
	}

}
