package cn.mopon.cec.core.access.member.hfh.vo;

/**
 * 选座票折扣。
 */
public class TicketDiscountVo {
	/** 折扣价 */
	private Double discountPrice;
	/** 是否折扣 */
	private boolean discount;
	/** 票数 */
	private Integer count;
	/** 票类 */
	private String name;

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public boolean isDiscount() {
		return discount;
	}

	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
