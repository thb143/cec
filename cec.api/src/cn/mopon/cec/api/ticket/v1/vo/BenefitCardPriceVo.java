package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.BenefitCardSettle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.NumberUtils;

/**
 * 权益卡价格。
 */
@XStreamAlias("benefitCardPrice")
public class BenefitCardPriceVo {
	/** 卡类编码 */
	private String typeCode = "";
	/** 优惠金额 */
	private Double discountPrice = 0D;
	/** 结算价 */
	private Double settlePrice = 0D;
	/** 票房价 */
	private Double submitPrice = 0D;

	/**
	 * 构造方法。
	 * 
	 * @param benefitCardSettle
	 *            权益卡结算价格
	 */
	public BenefitCardPriceVo(BenefitCardSettle benefitCardSettle) {
		this.typeCode = benefitCardSettle.getRule().getType().getCode();
		this.discountPrice = benefitCardSettle.getDiscountPrice();
		this.settlePrice = NumberUtils.add(benefitCardSettle.getChannelPrice(),
				benefitCardSettle.getChannelShow().getConnectFee());
		this.submitPrice = benefitCardSettle.getSubmitPrice();
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Double getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
	}
}