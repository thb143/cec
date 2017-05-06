package cn.mopon.cec.core.assist.period;

import java.io.Serializable;
import java.util.Date;

import cn.mopon.cec.core.assist.enums.PeriodRuleType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 时段规则。
 */
@SuppressWarnings("serial")
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public abstract class PeriodRule implements Serializable {
	/** 时段规则类型 */
	private PeriodRuleType type = PeriodRuleType.DAY;

	/**
	 * 判断时段规则中是否包含指定的日期时间。
	 * 
	 * @param dateTime
	 *            日期时间
	 * @return 如果包含返回true，否则返回false。
	 */
	public abstract Boolean contains(Date dateTime);

	/**
	 * 根据时段规则类型新建时段规则。
	 * 
	 * @param periodRuleType
	 *            时段规则类型
	 * @return 返回新建的时段规则。
	 */
	public static PeriodRule createPeriodRule(PeriodRuleType periodRuleType) {
		switch (periodRuleType) {
		case DAY:
			return new DayPeriodRule();
		case WEEK:
			return new WeekPeriodRule();
		case HOLIDAY:
			return new HolidayPeriodRule();
		case ROUND:
			return new RoundPeriodRule();
		case ROUNDWEEK:
			return new RoundWeekPriodRule();
		default:
			return null;
		}
	}

	public PeriodRuleType getType() {
		return type;
	}

	public void setType(PeriodRuleType type) {
		this.type = type;
	}
}