package cn.mopon.cec.core.assist.fee;

import java.text.DecimalFormat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cn.mopon.cec.core.assist.enums.FeeRuleType;
import coo.base.util.NumberUtils;

/**
 * 百分比费用。
 */
@SuppressWarnings("serial")
public class PercentFeeRule extends FeeRule {
	/** 百分比 */
	private Double percent = 0D;

	/**
	 * 构造方法。
	 */
	public PercentFeeRule() {
		setType(FeeRuleType.PERCENT);
	}

	@Override
	public Double cal(Double price) {
		return NumberUtils.halfUp(NumberUtils.mul(price, percent / 100));
	}

	@Override
	public Double getFee() {
		return 0D;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		PercentFeeRule other = (PercentFeeRule) obj;
		return new EqualsBuilder().append(getPercent(), other.getPercent())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(super.hashCode())
				.append(getPercent()).hashCode();
	}

	@Override
	public String toString() {
		return new DecimalFormat("0.00").format(percent) + "%";
	}
}
