package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import coo.base.util.NumberUtils;

/**
 * 固定减价。
 */
@SuppressWarnings("serial")
public class FixedMinusSettleRule extends SettleRule {
	/** 减价 */
	private Double minus = 0D;

	/**
	 * 构造方法。
	 */
	public FixedMinusSettleRule() {
		setType(SettleRuleType.FIXED_MINUS);
	}

	@Override
	public Double cal(Double price) {
		return NumberUtils.sub(price, minus);
	}

	public Double getMinus() {
		return minus;
	}

	public void setMinus(Double minus) {
		this.minus = minus;
	}

	@Override
	public String toString() {
		return getBasisType().getText() + "-"
				+ new DecimalFormat("0.00").format(minus) + "元"
				+ super.toString();
	}
}