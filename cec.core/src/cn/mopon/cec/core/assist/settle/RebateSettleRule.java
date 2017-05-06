package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import coo.base.util.NumberUtils;

/**
 * 折扣价。
 */
@SuppressWarnings("serial")
public class RebateSettleRule extends SettleRule {
	/** 折扣 */
	private Double rebate = 6D;

	/**
	 * 构造方法。
	 */
	public RebateSettleRule() {
		setType(SettleRuleType.REBATE);
	}

	@Override
	public Double cal(Double price) {
		return NumberUtils.mul(price, rebate / 10);
	}

	public Double getRebate() {
		return rebate;
	}

	public void setRebate(Double rebate) {
		this.rebate = rebate;
	}

	@Override
	public String toString() {
		return getBasisType().getText() + "*"
				+ new DecimalFormat("0.00").format(rebate) + "折"
				+ super.toString();
	}
}