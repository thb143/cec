package cn.mopon.cec.core.assist.period;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import coo.base.util.CollectionUtils;

/**
 * 星期特殊时段条目。
 */
@SuppressWarnings("serial")
public class WeekPeriodRuleItem extends PeriodRuleItem {
	/** 星期限制 */
	private Integer[] days;

	@Override
	public Boolean contains(Date dateTime) {
		Integer day = new DateTime(dateTime).getDayOfWeek();
		LocalTime localTime = new DateTime(dateTime).toLocalTime();
		return CollectionUtils.contains(days, day) && containsTime(localTime);
	}

	public Integer[] getDays() {
		return days;
	}

	public void setDays(Integer[] days) {
		this.days = days;
	}
}