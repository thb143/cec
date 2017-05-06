package cn.mopon.cec.core.assist.period;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 时段规则条目。
 */
@SuppressWarnings("serial")
public abstract class PeriodRuleItem implements Serializable {
	/** 开始时间 */
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	/** 结束时间 */
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;

	/**
	 * 判断时段规则条目是否包含指定的日期时间。
	 * 
	 * @param dateTime
	 *            日期时间
	 * @return 如果包含返回true，否则返回false。
	 */
	public abstract Boolean contains(Date dateTime);

	/**
	 * 判断时间区间是否包含指定的时间。
	 * 
	 * @param time
	 *            时间
	 * @return 如果包含返回true，否则返回false。
	 */
	protected Boolean containsTime(LocalTime time) {
		return (time.isAfter(startTime) || time.isEqual(startTime))
				&& time.isBefore(endTime);
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}