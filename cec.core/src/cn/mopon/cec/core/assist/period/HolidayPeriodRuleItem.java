package cn.mopon.cec.core.assist.period;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * 特殊日期时段条目。
 */
@SuppressWarnings("serial")
public class HolidayPeriodRuleItem extends PeriodRuleItem {
	/** 日期 */
	private LocalDate date;

	@Override
	public Boolean contains(Date date) {
		LocalDate localDate = new DateTime(date).toLocalDate();
		LocalTime localTime = new DateTime(date).toLocalTime();
		return this.date.isEqual(localDate) && containsTime(localTime);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}