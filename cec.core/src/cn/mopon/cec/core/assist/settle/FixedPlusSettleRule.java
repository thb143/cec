package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import coo.base.util.NumberUtils;

/**
 * 固定加价。
 */
@SuppressWarnings("serial")
public class FixedPlusSettleRule extends SettleRule {
	/** 加价 */
	private Double plus = 0D;

	/**
	 * 构造方法。
	 */
	public FixedPlusSettleRule() {
		setType(SettleRuleType.FIXED_PLUS);
	}

	@Override
	public Double cal(Double price) {
		return NumberUtils.add(price, plus);
	}

	public Double getPlus() {
		return plus;
	}

	public void setPlus(Double plus) {
		this.plus = plus;
	}

	@Override
	public String toString() {
		return getBasisType().getText() + "+"
				+ new DecimalFormat("0.00").format(plus) + "元"
				+ super.toString();
	}
}