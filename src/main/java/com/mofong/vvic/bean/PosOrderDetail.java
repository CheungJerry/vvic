package com.mofong.vvic.bean;

import java.util.Map;

import org.apache.logging.log4j.core.util.TypeUtil;

import com.mofong.util.JsonUtil;

public class PosOrderDetail {
	Long orderDetailId;
	String orderNo;
	String customerName;
	String artNo;
	String size;
	String title;
	String indexImgUrl;
	Integer skuNum;
	Double changePrice;
	Double skuLevelPrice;
	Double actualPrice;
	String ctime;
	String ymd;
	Double unitPrice;
	Integer type;
	Integer orderType;
	Integer skuCancelNum;
	Integer showNum;

	public PosOrderDetail() {
		super();
	}

	public PosOrderDetail(Map<String, Object> map) {
		this.orderDetailId = JsonUtil.getLong(map.get("orderDetailId"));
		this.orderNo = (String) map.get("orderNo");
		this.artNo = (String) map.get("artNo");
		this.size = (String) map.get("size");
		this.title = (String) map.get("title");
		this.indexImgUrl = (String) map.get("indexImgUrl");
		this.skuNum = (Integer) map.get("skuNum");
		this.changePrice = (Double) map.get("changePrice");
		this.actualPrice = (Double) map.get("actualPrice");
		this.skuLevelPrice = (Double) map.get("skuLevelPrice");
		this.ctime = (String) map.get("ctime");
		this.ymd = (String) map.get("ymd");
		this.unitPrice = (Double) map.get("unitPrice");
		this.type = (Integer) map.get("type");
		this.orderType = (Integer) map.get("orderType");
		this.skuCancelNum = (Integer) map.get("skuCancelNum");
		this.showNum = (Integer) map.get("showNum");
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public String getArtNo() {
		return artNo;
	}

	public void setArtNo(String artNo) {
		this.artNo = artNo;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIndexImgUrl() {
		return indexImgUrl;
	}

	public void setIndexImgUrl(String indexImgUrl) {
		this.indexImgUrl = indexImgUrl;
	}

	public Integer getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public Double getChangePrice() {
		return changePrice;
	}

	public void setChangePrice(Double changePrice) {
		this.changePrice = changePrice;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Double getSkuLevelPrice() {
		return skuLevelPrice;
	}

	public void setSkuLevelPrice(Double skuLevelPrice) {
		this.skuLevelPrice = skuLevelPrice;
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

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getSkuCancelNum() {
		return skuCancelNum;
	}

	public void setSkuCancelNum(Integer skuCancelNum) {
		this.skuCancelNum = skuCancelNum;
	}

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

}
