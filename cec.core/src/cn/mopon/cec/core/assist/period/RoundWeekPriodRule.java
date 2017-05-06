package cn.mopon.cec.core.assist.period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.assist.enums.PeriodRuleType;
import cn.mopon.cec.core.assist.enums.Week;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.util.IEnumUtils;

/**
 * 区间星期时段。
 */
@SuppressWarnings("serial")
public class RoundWeekPriodRule extends PeriodRule {
	/** 区间星期时段条目 */
	private List<RoundWeekPeriodRuleItem> items = new ArrayList<>();

	/**
	 * 构造方法。
	 */
	public RoundWeekPriodRule() {
		setType(PeriodRuleType.ROUNDWEEK);
	}

	@Override
	public Boolean contains(Date dateTime) {
		for (RoundWeekPeriodRuleItem item : items) {
			if (item.contains(dateTime)) {
				return true;
			}
		}
		return false;
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
			RoundWeekPeriodRuleItem item = items.get(i);
			str.append("日期：")
					.append(item.getStartDate().toString(DateUtils.DAY))
					.append("~")
					.append(item.getEndDate().toString(DateUtils.DAY))
					.append(" ").append("星期：");
			if (CollectionUtils.isNotEmpty(item.getDays())) {
				str.append(getWeekText(item)).append(" ");
			}
			str.append(" 时段：").append(item.getStartTime().toString("HH:mm"))
					.append("~").append(item.getEndTime().toString("HH:mm"))
					.append(" | ");
		}
		return str.toString().substring(0, str.toString().length() - 2);
	}

	/**
	 * 获取星期文本。
	 * 
	 * @param item
	 *            区间星期
	 * @return 返回区间星期字符串。
	 */
	private String getWeekText(RoundWeekPeriodRuleItem item) {
		StringBuilder temp = new StringBuilder();
		for (Integer day : item.getDays()) {
			if (temp.length() > 0) {
				temp.append(",");
			}
			temp.append(IEnumUtils.getIEnumByValue(Week.class,
					String.valueOf(day)));
		}
		return temp.toString();
	}

	public List<RoundWeekPeriodRuleItem> getItems() {
		return items;
	}

	public void setItems(List<RoundWeekPeriodRuleItem> items) {
		this.items = items;
	}
}
