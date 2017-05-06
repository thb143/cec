package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

/**
 * 区间定价条目。
 */
public class RoundSettleRuleItem {
	/** 起始金额 */
	private Double startAmount = 0D;
	/** 截止金额 */
	private Double endAmount = 0D;

	private SettleRule settleRule = new FixedSettleRule();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("【" + new DecimalFormat("0.00").format(startAmount)
				+ "~");
		builder.append(new DecimalFormat("0.00").format(endAmount) + "="
				+ settleRule.toString() + "】");
		return builder.toString();
	}

	/**
	 * 判断区间定价条目是否包含指定的价格。
	 * 
	 * @param price
	 *            价格
	 * @return 如果包含返回true，否则返回false。
	 */
	public Boolean contains(Double price) {
		return price >= startAmount && price < endAmount;
	}

	public Double getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(Double startAmount) {
		this.startAmount = startAmount;
	}

	public Double getEndAmount() {
		return endAmount;
	}

	public void setEndAmount(Double endAmount) {
		this.endAmount = endAmount;
	}

	public SettleRule getSettleRule() {
		return settleRule;
	}

	public void setSettleRule(SettleRule settleRule) {
		this.settleRule = settleRule;
	}
}