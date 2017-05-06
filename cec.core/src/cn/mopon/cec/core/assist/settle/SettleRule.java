package cn.mopon.cec.core.assist.settle;

import java.io.Serializable;

import cn.mopon.cec.core.assist.enums.SettleBasisType;
import cn.mopon.cec.core.assist.enums.SettleRuleType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 结算规则。
 */
@SuppressWarnings("serial")
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public abstract class SettleRule implements Serializable {
	/** 结算规则类型 */
	private SettleRuleType type = SettleRuleType.FIXED;
	/** 结算规则基准类型 */
	private SettleBasisType basisType = SettleBasisType.STD_PRICE;
	/** 影院结算价是否可低于最低价 */
	private Boolean cinemaPriceBelowMinPrice = false;
	/** 渠道结算价是否可低于最低价 */
	private Boolean channelPriceBelowMinPrice = false;
	/** 票房价是否可低于最低价 */
	private Boolean sumbitPriceBelowMinPrice = false;

	/**
	 * 计算价格。
	 * 
	 * @param price
	 *            原始价格
	 * @return 返回计算得到的价格。
	 */
	public abstract Double cal(Double price);

	/**
	 * 根据结算规则类型新建结算规则。
	 * 
	 * @param settleRuleType
	 *            结算规则类型
	 * @return 返回新建的结算规则。
	 */
	public static SettleRule createSettleRule(SettleRuleType settleRuleType) {
		switch (settleRuleType) {
		case FIXED:
			return new FixedSettleRule();
		case FIXED_PLUS:
			return new FixedPlusSettleRule();
		case FIXED_MINUS:
			return new FixedMinusSettleRule();
		case REBATE:
			return new RebateSettleRule();
		case REBATE_PLUS:
			return new RebatePlusSettleRule();
		case REBATE_MINUS:
			return new RebateMinusSettleRule();
		case ROUND:
			return new RoundSettleRule();
		default:
			return null;
		}
	}

	public SettleRuleType getType() {
		return type;
	}

	public void setType(SettleRuleType type) {
		this.type = type;
	}

	public SettleBasisType getBasisType() {
		return basisType;
	}

	public void setBasisType(SettleBasisType basisType) {
		this.basisType = basisType;
	}

	public Boolean getCinemaPriceBelowMinPrice() {
		return cinemaPriceBelowMinPrice;
	}

	public void setCinemaPriceBelowMinPrice(Boolean minCinemaPrice) {
		this.cinemaPriceBelowMinPrice = minCinemaPrice;
	}

	public Boolean getChannelPriceBelowMinPrice() {
		return channelPriceBelowMinPrice;
	}

	public void setChannelPriceBelowMinPrice(Boolean minChannelPrice) {
		this.channelPriceBelowMinPrice = minChannelPrice;
	}

	public Boolean getSumbitPriceBelowMinPrice() {
		return sumbitPriceBelowMinPrice;
	}

	public void setSumbitPriceBelowMinPrice(Boolean minSumbitPrice) {
		this.sumbitPriceBelowMinPrice = minSumbitPrice;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if (cinemaPriceBelowMinPrice) {
			str.append("；").append("影院结算价可低于最低价");
		}
		if (channelPriceBelowMinPrice) {
			str.append("；").append("渠道结算价可低于最低价");
		}
		if (sumbitPriceBelowMinPrice) {
			str.append("；").append("票房价可低于最低价");
		}
		return str.toString();
	}
}