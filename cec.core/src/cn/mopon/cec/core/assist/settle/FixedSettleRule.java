package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

import cn.mopon.cec.core.assist.enums.SettleRuleType;

/**
 * 固定价。
 */
@SuppressWarnings("serial")
public class FixedSettleRule extends SettleRule {
	/** 金额 */
	private Double amount = 0D;

	/**
	 * 构造方法。
	 */
	public FixedSettleRule() {
		setType(SettleRuleType.FIXED);
	}

	@Override
	public Double cal(Double price) {
		return amount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return getType().getText() + "："
				+ new DecimalFormat("0.00").format(amount) + "元"
				+ super.toString();
	}
}