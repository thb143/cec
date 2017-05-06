package cn.mopon.cec.core.assist.fee;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cn.mopon.cec.core.assist.enums.FeeRuleType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 费用规则。
 */
@SuppressWarnings("serial")
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public abstract class FeeRule implements Serializable {
	/** 费用规则类型 */
	private FeeRuleType type = FeeRuleType.NONE;

	/**
	 * 计算费用。
	 * 
	 * @param price
	 *            价格
	 * @return 返回计算得到的费用。
	 */
	public abstract Double cal(Double price);

	/**
	 * 获取费用。
	 * 
	 * @return 返回不用计算的费用。
	 */
	@JsonIgnore
	public abstract Double getFee();

	/**
	 * 根据费用规则类型新建费用规则。
	 * 
	 * @param feeRuleType
	 *            费用规则类型
	 * @return 返回新建的费用规则。
	 */
	public static FeeRule createFeeRule(FeeRuleType feeRuleType) {
		switch (feeRuleType) {
		case NONE:
			return new NoneFeeRule();
		case FIXED:
			return new FixedFeeRule();
		case PERCENT:
			return new PercentFeeRule();
		default:
			return null;
		}
	}

	public FeeRuleType getType() {
		return type;
	}

	public void setType(FeeRuleType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		FeeRule other = (FeeRule) obj;
		return new EqualsBuilder().append(getType(), other.getType())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getType()).hashCode();
	}
}
