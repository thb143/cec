package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡折扣信息响应对象。
 */
public class DiscountPriceReply extends MTXReply {
	/** 会员卡 */
	private MemberCard card;
	/** 折扣类型 */
	private String discountType = "";

	/**
	 * 构造方法。
	 */
	public DiscountPriceReply() {
		xstream.alias("CardDiscount", DiscountPriceReply.class, MTXReply.class);
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
}
