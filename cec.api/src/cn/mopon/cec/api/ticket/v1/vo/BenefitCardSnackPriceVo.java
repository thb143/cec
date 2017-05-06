package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.SnackChannel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 权益卡价格。
 */
@XStreamAlias("benefitCardPrice")
public class BenefitCardSnackPriceVo {
	/** 卡类编码 */
	private String typeCode = "";
	/** 优惠金额 */
	private Double discountPrice = 0D;
	/** 结算价 */
	private Double settlePrice = 0D;

	/**
	 * 构造方法。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @param snackRule
	 *            权益卡类卖品规则
	 */
	public BenefitCardSnackPriceVo(SnackChannel snackChannel,
			BenefitCardTypeSnackRule snackRule) {
		this.typeCode = snackRule.getType().getCode();
		this.settlePrice = snackRule.calSettlePrice(snackChannel);
		this.discountPrice = snackRule.calDiscountPrice(snackChannel);
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
}