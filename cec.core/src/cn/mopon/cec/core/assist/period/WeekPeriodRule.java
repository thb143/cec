package cn.mopon.cec.core.assist.period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.assist.enums.PeriodRuleType;
import cn.mopon.cec.core.assist.enums.Week;
import coo.base.util.CollectionUtils;
import coo.core.util.IEnumUtils;

/**
 * 星期特殊时段。
 */
@SuppressWarnings("serial")
public class WeekPeriodRule extends PeriodRule {
	/** 时段条目 */
	private List<WeekPeriodRuleItem> items = new ArrayList<WeekPeriodRuleItem>();

	/**
	 * 构造方法。
	 */
	public WeekPeriodRule() {
		setType(PeriodRuleType.WEEK);
	}

	@Override
	public Boolean contains(Date dateTime) {
		for (WeekPeriodRuleItem item : items) {
			if (item.contains(dateTime)) {
				return true;
			}
		}
		return false;
	}

	public List<WeekPeriodRuleItem> getItems() {
		return items;
	}

	public void setItems(List<WeekPeriodRuleItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		if (CollectionUtils.isEmpty(items)) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			if ((i + 1) % 4 == 0) {
				str.append("\r\n");
			}
			str.append("星期：");
			StringBuilder temp = new StringBuilder();
			if (CollectionUtils.isNotEmpty(items.get(i).getDays())) {
				for (Integer day : items.get(i).getDays()) {
					if (temp.length() > 0) {
						temp.append(",");
					}
					temp.append(IEnumUtils.getIEnumByValue(Week.class,
							String.valueOf(day)));
				}
				str.append(temp.toString()).append(" ");
			}
			str.append(" 时段：")
					.append(items.get(i).getStartTime().toString("HH:mm"))
					.append("~")
					.append(items.get(i).getEndTime().toString("HH:mm"))
					.append(" | ");
		}
		return str.toString().substring(0, str.toString().length() - 2);
	}
}