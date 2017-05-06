package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.Snack;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 卡类可使用卖品VO。
 */
@XStreamAlias("snack")
public class SnackBenefitCardVo {
	private String snackCode;
	private String snackName;
	private String snackType;
	private Double snackDiscountPrice;

	/**
	 * 构造方法。
	 * 
	 * @param snack
	 *            卖品
	 * @param discountPrice
	 *            优惠金额
	 */
	public SnackBenefitCardVo(Snack snack, Double discountPrice) {
		this.snackCode = snack.getCode();
		this.snackName = snack.getType().getName();
		this.snackType = snack.getType().getGroup().getName();
		this.snackDiscountPrice = discountPrice;
	}

	public String getSnackCode() {
		return snackCode;
	}

	public void setSnackCode(String snackCode) {
		this.snackCode = snackCode;
	}

	public String getSnackName() {
		return snackName;
	}

	public void setSnackName(String snackName) {
		this.snackName = snackName;
	}

	public String getSnackType() {
		return snackType;
	}

	public void setSnackType(String snackType) {
		this.snackType = snackType;
	}

	public Double getSnackDiscountPrice() {
		return snackDiscountPrice;
	}

	public void setSnackDiscountPrice(Double snackDiscountPrice) {
		this.snackDiscountPrice = snackDiscountPrice;
	}
}