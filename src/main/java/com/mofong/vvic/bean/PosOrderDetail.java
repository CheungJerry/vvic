package com.mofong.vvic.bean;

import com.mofong.util.JsonUtil;
import java.util.Map;

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
		return this.orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public String getArtNo() {
		return this.artNo;
	}

	public void setArtNo(String artNo) {
		this.artNo = artNo;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIndexImgUrl() {
		return this.indexImgUrl;
	}

	public void setIndexImgUrl(String indexImgUrl) {
		this.indexImgUrl = indexImgUrl;
	}

	public Integer getSkuNum() {
		return this.skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public Double getChangePrice() {
		return this.changePrice;
	}

	public void setChangePrice(Double changePrice) {
		this.changePrice = changePrice;
	}

	public Double getActualPrice() {
		return this.actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Double getSkuLevelPrice() {
		return this.skuLevelPrice;
	}

	public void setSkuLevelPrice(Double skuLevelPrice) {
		this.skuLevelPrice = skuLevelPrice;
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

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOrderType() {
		return this.orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getSkuCancelNum() {
		return this.skuCancelNum;
	}

	public void setSkuCancelNum(Integer skuCancelNum) {
		this.skuCancelNum = skuCancelNum;
	}

	public Integer getShowNum() {
		return this.showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public String compare(com.mofong.vvic.bean.PosOrderDetail pos) {
		return "PosOrderDetail [artNo=" + this.artNo + "/" + pos.artNo + ", size=" + this.size + "/" + pos.size
				+ ", title=" + this.title + "/" + pos.title + ", indexImgUrl=" + this.indexImgUrl + "/"
				+ pos.indexImgUrl + ", skuNum=" + this.skuNum + "/" + pos.skuNum + ", changePrice=" + this.changePrice
				+ "/" + pos.changePrice + ", skuLevelPrice=" + this.skuLevelPrice + "/" + pos.skuLevelPrice
				+ ", actualPrice=" + this.actualPrice + "/" + pos.actualPrice + ", unitPrice=" + this.unitPrice + "/"
				+ pos.unitPrice + ", type=" + this.type + "/" + pos.type + ", orderType=" + this.orderType + "/"
				+ pos.orderType + ", skuCancelNum=" + this.skuCancelNum + "/" + pos.skuCancelNum + ", showNum="
				+ this.showNum + "/" + pos.showNum + "]";
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + ((this.actualPrice == null) ? 0 : this.actualPrice.hashCode());
		result = 31 * result + ((this.artNo == null) ? 0 : this.artNo.hashCode());
		result = 31 * result + ((this.changePrice == null) ? 0 : this.changePrice.hashCode());
		result = 31 * result + ((this.ctime == null) ? 0 : this.ctime.hashCode());
		result = 31 * result + ((this.customerName == null) ? 0 : this.customerName.hashCode());
		result = 31 * result + ((this.indexImgUrl == null) ? 0 : this.indexImgUrl.hashCode());
		result = 31 * result + ((this.orderDetailId == null) ? 0 : this.orderDetailId.hashCode());
		result = 31 * result + ((this.orderNo == null) ? 0 : this.orderNo.hashCode());
		result = 31 * result + ((this.orderType == null) ? 0 : this.orderType.hashCode());
		result = 31 * result + ((this.showNum == null) ? 0 : this.showNum.hashCode());
		result = 31 * result + ((this.size == null) ? 0 : this.size.hashCode());
		result = 31 * result + ((this.skuCancelNum == null) ? 0 : this.skuCancelNum.hashCode());
		result = 31 * result + ((this.skuLevelPrice == null) ? 0 : this.skuLevelPrice.hashCode());
		result = 31 * result + ((this.skuNum == null) ? 0 : this.skuNum.hashCode());
		result = 31 * result + ((this.title == null) ? 0 : this.title.hashCode());
		result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
		result = 31 * result + ((this.unitPrice == null) ? 0 : this.unitPrice.hashCode());
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
		com.mofong.vvic.bean.PosOrderDetail other = (com.mofong.vvic.bean.PosOrderDetail) obj;
		if (this.actualPrice == null) {
			if (other.actualPrice != null)
				return false;
		} else if (!this.actualPrice.equals(other.actualPrice)) {
			return false;
		}
		if (this.artNo == null) {
			if (other.artNo != null)
				return false;
		} else if (!this.artNo.equals(other.artNo)) {
			return false;
		}
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
		if (this.indexImgUrl == null) {
			if (other.indexImgUrl != null)
				return false;
		} else if (!this.indexImgUrl.equals(other.indexImgUrl)) {
			return false;
		}
		if (this.orderDetailId == null) {
			if (other.orderDetailId != null)
				return false;
		} else if (!this.orderDetailId.equals(other.orderDetailId)) {
			return false;
		}
		if (this.orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!this.orderNo.equals(other.orderNo)) {
			return false;
		}
		if (this.orderType == null) {
			if (other.orderType != null)
				return false;
		} else if (!this.orderType.equals(other.orderType)) {
			return false;
		}
		if (this.showNum == null) {
			if (other.showNum != null)
				return false;
		} else if (!this.showNum.equals(other.showNum)) {
			return false;
		}
		if (this.size == null) {
			if (other.size != null)
				return false;
		} else if (!this.size.equals(other.size)) {
			return false;
		}
		if (this.skuCancelNum == null) {
			if (other.skuCancelNum != null)
				return false;
		} else if (!this.skuCancelNum.equals(other.skuCancelNum)) {
			return false;
		}
		if (this.skuLevelPrice == null) {
			if (other.skuLevelPrice != null)
				return false;
		} else if (!this.skuLevelPrice.equals(other.skuLevelPrice)) {
			return false;
		}
		if (this.skuNum == null) {
			if (other.skuNum != null)
				return false;
		} else if (!this.skuNum.equals(other.skuNum)) {
			return false;
		}
		if (this.title == null) {
			if (other.title != null)
				return false;
		} else if (!this.title.equals(other.title)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		if (this.unitPrice == null) {
			if (other.unitPrice != null)
				return false;
		} else if (!this.unitPrice.equals(other.unitPrice)) {
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
}
