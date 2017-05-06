package cn.mopon.cec.core.access.member.hln.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 火烈鸟请求对象VO。
 */
@XStreamAlias("items")
public class PayOrderItemVo {
	/** 条目类型，1商品，2影票，必须 */
	private String itemType;
	/** 原价(标准价) */
	private Double originalPrice;
	/** 数量（电影票为1） */
	private Integer number;
	/** 放映计划Code，购买影票时必填，商品时不填 */
	private String planId;
	/** 座位Code，商品时不填 */
	private String seatId;
	/** 折扣价 */
	private Double discountPrice;
	/** 条目ID，商品时为商品ID，影票时，代售填座位Code */
	private String itemId;
	private Double serviceFee;

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

}
