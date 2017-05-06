package cn.mopon.cec.core.assist.fee;

import cn.mopon.cec.core.assist.enums.FeeRuleType;

/**
 * 无费用。
 */
@SuppressWarnings("serial")
public class NoneFeeRule extends FeeRule {
	/**
	 * 构造方法。
	 */
	public NoneFeeRule() {
		setType(FeeRuleType.NONE);
	}

	@Override
	public Double cal(Double price) {
		return 0D;
	}

	@Override
	public Double getFee() {
		return 0D;
	}

	@Override
	public String toString() {
		return getType().getText();
	}
}