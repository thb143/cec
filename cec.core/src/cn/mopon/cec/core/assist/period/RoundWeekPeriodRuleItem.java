package cn.mopon.cec.core.assist.period;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import coo.base.util.CollectionUtils;

/**
 * 区间星期时段条目。
 */
@SuppressWarnings("serial")
public class RoundWeekPeriodRuleItem extends PeriodRuleItem {
	/** 开始日期 */
	private LocalDate startDate;
	/** 结束日期 */
	private LocalDate endDate;
	/** 星期限制 */
	private Integer[] days;

	@Override
	public Boolean contains(Date dateTime) {
		LocalDate localDate = new DateTime(dateTime).toLocalDate();
		Integer day = new DateTime(dateTime).getDayOfWeek();
		LocalTime localTime = new DateTime(dateTime).toLocalTime();
		return (localDate.isAfter(startDate) || localDate.isEqual(startDate))
				&& (localDate.isBefore(endDate) || localDate.isEqual(endDate))
				&& CollectionUtils.contains(days, day)
				&& containsTime(localTime);
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer[] getDays() {
		return days;
	}

	public void setDays(Integer[] days) {
		this.days = days;
	}
}
