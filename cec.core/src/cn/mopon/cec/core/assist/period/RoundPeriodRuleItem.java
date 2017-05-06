package cn.mopon.cec.core.assist.period;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * 日期区间时段条目。
 */
@SuppressWarnings("serial")
public class RoundPeriodRuleItem extends PeriodRuleItem {
	/** 开始日期 */
	private LocalDate startDate;
	/** 结束日期 */
	private LocalDate endDate;

	@Override
	public Boolean contains(Date date) {
		LocalDate localDate = new DateTime(date).toLocalDate();
		LocalTime localTime = new DateTime(date).toLocalTime();
		return (localDate.isAfter(startDate) || localDate.isEqual(startDate))
				&& (localDate.isBefore(endDate) || localDate.isEqual(endDate))
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
}