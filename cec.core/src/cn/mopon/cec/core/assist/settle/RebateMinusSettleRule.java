package cn.mopon.cec.core.assist.settle;

import java.text.DecimalFormat;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import coo.base.util.NumberUtils;

/**
 * 折扣减价。
 */
@SuppressWarnings("serial")
public class RebateMinusSettleRule extends RebateSettleRule {
	/** 减价 */
	private Double minus = 0D;

	/**
	 * 构造方法。
	 */
	public RebateMinusSettleRule() {
		setType(SettleRuleType.REBATE_MINUS);
	}

	@Override
	public Double cal(Double price) {
		return NumberUtils.sub(super.cal(price), minus);
	}

	public Double getMinus() {
		return minus;
	}

	public void setMinus(Double minus) {
		this.minus = minus;
	}

	/**
	 * @return 返回折扣减价。
	 */
	public String toString() {
		String str = getBasisType().getText() + "*"
				+ new DecimalFormat("0.00").format(getRebate()) + "折-"
				+ new DecimalFormat("0.00").format(minus) + "元";
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