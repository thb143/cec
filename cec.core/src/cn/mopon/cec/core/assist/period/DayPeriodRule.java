package cn.mopon.cec.core.assist.period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.assist.enums.PeriodRuleType;
import coo.base.util.CollectionUtils;

/**
 * 常规时段。
 */
@SuppressWarnings("serial")
public class DayPeriodRule extends PeriodRule {
	/** 时段条目 */
	private List<DayPeriodRuleItem> items = new ArrayList<DayPeriodRuleItem>();

	/**
	 * 构造方法。
	 */
	public DayPeriodRule() {
		setType(PeriodRuleType.DAY);
	}

	@Override
	public Boolean contains(Date dateTime) {
		for (DayPeriodRuleItem item : items) {
			if (item.contains(dateTime)) {
				return true;
			}
		}
		return false;
	}

	public List<DayPeriodRuleItem> getItems() {
		return items;
	}

	public void setItems(List<DayPeriodRuleItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		if (CollectionUtils.isEmpty(items)) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("时段：");
		for (DayPeriodRuleItem item : items) {
			if (builder.length() > 4) {
				builder.append(" | ");
			}
			builder.append(item.getStartTime().toString("HH:mm")).append("~")
					.append(item.getEndTime().toString("HH:mm"));
		}
		return builder.toString();
	}
}