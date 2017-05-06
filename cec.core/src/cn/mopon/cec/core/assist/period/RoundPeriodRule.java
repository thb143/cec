package cn.mopon.cec.core.assist.period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.assist.enums.PeriodRuleType;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;

/**
 * 日期区间时段。
 */
@SuppressWarnings("serial")
public class RoundPeriodRule extends PeriodRule {
	/** 时段条目 */
	private List<RoundPeriodRuleItem> items = new ArrayList<RoundPeriodRuleItem>();

	/**
	 * 构造方法。
	 */
	public RoundPeriodRule() {
		setType(PeriodRuleType.ROUND);
	}

	@Override
	public Boolean contains(Date dateTime) {
		for (RoundPeriodRuleItem item : items) {
			if (item.contains(dateTime)) {
				return true;
			}
		}
		return false;
	}

	public List<RoundPeriodRuleItem> getItems() {
		return items;
	}

	public void setItems(List<RoundPeriodRuleItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		if (CollectionUtils.isEmpty(items)) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			if ((i + 1) % 4 == 0) {
				builder.append("\r\n");
			}
			builder.append("日期：");
			builder.append(items.get(i).getStartDate().toString(DateUtils.DAY))
					.append("~")
					.append(items.get(i).getEndDate().toString(DateUtils.DAY))
					.append("  ").append("时段：").append(" ")
					.append(items.get(i).getStartTime().toString("HH:mm"))
					.append("~")
					.append(items.get(i).getEndTime().toString("HH:mm"))
					.append(" | ");
		}
		return builder.toString().substring(0, builder.toString().length() - 2);
	}
}
