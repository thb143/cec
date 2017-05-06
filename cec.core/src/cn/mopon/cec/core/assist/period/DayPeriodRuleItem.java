package cn.mopon.cec.core.assist.period;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * 每天特殊时段条目。
 */
@SuppressWarnings("serial")
public class DayPeriodRuleItem extends PeriodRuleItem {
	@Override
	public Boolean contains(Date date) {
		LocalTime localTime = new DateTime(date).toLocalTime();
		return containsTime(localTime);
	}
}