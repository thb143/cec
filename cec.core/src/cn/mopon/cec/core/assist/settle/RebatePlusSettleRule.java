package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import coo.base.util.NumberUtils;

/**
 * 折扣加价。
 */
@SuppressWarnings("serial")
public class RebatePlusSettleRule extends RebateSettleRule {
	/** 加价 */
	private Double plus = 0D;

	/**
	 * 构造方法。
	 */
	public RebatePlusSettleRule() {
		setType(SettleRuleType.REBATE_PLUS);
	}

	@Override
	public Double cal(Double price) {
		return NumberUtils.add(super.cal(price), plus);
	}

	public Double getPlus() {
		return plus;
	}

	public void setPlus(Double plus) {
		this.plus = plus;
	}

	/**
	 * @return 返回折扣加价。
	 */
	public String toString() {
		String str = getBasisType().getText() + "*"
				+ new DecimalFormat("0.00").format(getRebate()) + "折+"
				+ new DecimalFormat("0.00").format(plus) + "元";
		if (super.getCinemaPriceBelowMinPrice()) {
			str = str + "；" + "影院结算价可低于最低价";
		}
		if (super.getChannelPriceBelowMinPrice()) {
			str = str + "；" + "渠道结算价可低于最低价";
		}
		if (super.getSumbitPriceBelowMinPrice()) {
			str = str + "；" + "票房价可低于最低价";
		}
		return str;
	}
}