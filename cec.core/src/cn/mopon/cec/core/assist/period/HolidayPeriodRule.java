package cn.mopon.cec.core.assist.period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.assist.enums.PeriodRuleType;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;

/**
 * 特殊日期时段。
 */
@SuppressWarnings("serial")
public class HolidayPeriodRule extends PeriodRule {
	/** 时段条目 */
	private List<HolidayPeriodRuleItem> items = new ArrayList<HolidayPeriodRuleItem>();

	/**
	 * 构造方法。
	 */
	public HolidayPeriodRule() {
		setType(PeriodRuleType.HOLIDAY);
	}

	@Override
	public Boolean contains(Date dateTime) {
		for (HolidayPeriodRuleItem item : items) {
			if (item.contains(dateTime)) {
				return true;
			}
		}
		return false;
	}

	public List<HolidayPeriodRuleItem> getItems() {
		return items;
	}

	public void setItems(List<HolidayPeriodRuleItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (CollectionUtils.isEmpty(items)) {
			return "";
		}
		for (HolidayPeriodRuleItem item : items) {
			builder.append("日期：");
			builder.append(item.getDate().toString(DateUtils.DAY)).append("  ")
					.append("时段：").append(" ")
					.append(item.getStartTime().toString("HH:mm")).append("~")
					.append(item.getEndTime().toString("HH:mm")).append(" | ");
		}
		return builder.toString().substring(0, builder.toString().length() - 2);
	}
}