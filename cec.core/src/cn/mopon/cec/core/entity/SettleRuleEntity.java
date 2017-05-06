package cn.mopon.cec.core.entity;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;

import cn.mopon.cec.core.assist.period.DayPeriodRule;
import cn.mopon.cec.core.assist.period.PeriodRule;
import cn.mopon.cec.core.assist.settle.FixedSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.enums.ShowType;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 结算规则基类。
 */
@MappedSuperclass
public abstract class SettleRuleEntity extends ResourceEntity<User> implements
		Comparable<SettleRuleEntity> {
	/** 名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "结算策略规则名称")
	private String name;
	/** 放映类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ShowType showType = ShowType.NORMAL2D;
	/** 时段规则 */
	@Type(type = "Json")
	private PeriodRule periodRule = new DayPeriodRule();
	/** 结算规则 */
	@Type(type = "Json")
	private SettleRule settleRule = new FixedSettleRule();

	@Override
	public int compareTo(SettleRuleEntity other) {
		Integer ordinal = getPeriodRule().getType().ordinal()
				- other.getPeriodRule().getType().ordinal();
		if (ordinal == 0) {
			return other.getCreateDate().compareTo(getCreateDate());
		}
		return ordinal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public PeriodRule getPeriodRule() {
		return periodRule;
	}

	public void setPeriodRule(PeriodRule periodRule) {
		this.periodRule = periodRule;
	}

	public SettleRule getSettleRule() {
		return settleRule;
	}

	public void setSettleRule(SettleRule settleRule) {
		this.settleRule = settleRule;
	}
}