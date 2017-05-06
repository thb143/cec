package cn.mopon.cec.core.assist.fee;

import java.text.DecimalFormat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cn.mopon.cec.core.assist.enums.FeeRuleType;

/**
 * 固定费用。
 */
@SuppressWarnings("serial")
public class FixedFeeRule extends FeeRule {
	/** 金额 */
	private Double amount = 0D;

	/**
	 * 构造方法。
	 */
	public FixedFeeRule() {
		setType(FeeRuleType.FIXED);
	}

	@Override
	public Double cal(Double price) {
		return amount;
	}

	@Override
	public Double getFee() {
		return amount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		FixedFeeRule other = (FixedFeeRule) obj;
		return new EqualsBuilder().append(getAmount(), other.getAmount())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(super.hashCode())
				.append(getAmount()).hashCode();
	}

	@Override
	public String toString() {
		return new DecimalFormat("0.00").format(amount) + "元";
	}
}
