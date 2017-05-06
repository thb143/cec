package cn.mopon.cec.core.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;

import cn.mopon.cec.core.assist.settle.FixedMinusSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.ValidStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import coo.base.util.StringUtils;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.entity.ResourceEntity;

/**
 * 权益卡类规则。
 */
@Entity
@Table(name = "CEC_BenefitCardTypeRule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BenefitCardTypeRule extends ResourceEntity<User> implements
		Comparable<BenefitCardTypeRule> {
	/** 关联类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	private BenefitCardType type;
	/** 规则名称 */
	private String name;
	/** 生效状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 结算规则 */
	@Type(type = "Json")
	private SettleRule settleRule = new FixedMinusSettleRule();
	/** 放映类型 */
	@Type(type = "IEnumList")
	private List<ShowType> showTypes = new ArrayList<ShowType>();
	/** 关联影厅 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_BenefitCardTypeRule_Hall", joinColumns = @JoinColumn(name = "ruleId"), inverseJoinColumns = @JoinColumn(name = "hallId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Hall> halls = new ArrayList<>();
	/** 排序 */
	private Integer ordinal = 0;
	/** 绑定规则 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundRuleId")
	private BenefitCardTypeRule boundRule;
	/** 规则关联渠道排期权益卡结算 */
	@OneToMany(mappedBy = "rule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<BenefitCardSettle> settles;

	/**
	 * 获取影厅的影院。
	 * 
	 * @return 返回影院。
	 */
	public Map<Cinema, List<Hall>> getAllCinemas() {
		Map<Cinema, List<Hall>> map = new HashMap<Cinema, List<Hall>>();
		List<Hall> halls = null;
		for (Hall hall : getHalls()) {
			if (!map.containsKey(hall.getCinema())) {
				halls = new ArrayList<Hall>();
				halls.add(hall);
				map.put(hall.getCinema(), halls);
			} else {
				map.get(hall.getCinema()).add(hall);
			}
		}
		return map;
	}

	/**
	 * 获取影厅的影院。
	 * 
	 * @return 返回影院。
	 */
	public Map<String, List<Hall>> getCinemas() {
		Map<String, List<Hall>> map = new HashMap<String, List<Hall>>();
		List<Hall> halls = null;
		for (Hall hall : getHalls()) {
			if (!map.containsKey(hall.getCinema().getName())) {
				halls = new ArrayList<Hall>();
				halls.add(hall);
				map.put(hall.getCinema().getName(), halls);
			} else {
				map.get(hall.getCinema().getName()).add(hall);
			}
		}
		return map;
	}

	/**
	 * 是否匹配指定的排期。
	 * 
	 * @param show
	 *            排期
	 * @return 如果匹配排期返回true，否则返回false。
	 */
	public Boolean matchShow(Show show) {
		return (valid == ValidStatus.VALID || valid == ValidStatus.INVALID)
				&& enabled == EnabledStatus.ENABLED
				&& getShowTypes().contains(show.getShowType())
				&& getHalls().contains(show.getHall());
	}

	/**
	 * 判断是否已被绑定（在生效状态下被修改生成了副本）。
	 * 
	 * @return 如果被绑定返回true，否则返回false。
	 */
	public boolean isBounded() {
		return boundRule != null;
	}

	/**
	 * 判断是否副本。
	 * 
	 * @return 如果是副本返回true，否则返回false。
	 */
	public Boolean isCopy() {
		return boundRule != null && valid == ValidStatus.UNVALID;
	}

	/**
	 * 获取摘要信息。
	 * 
	 * @return 返回摘要信息。
	 */
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		builder.append(" ");
		builder.append(getSettleSummary());
		builder.append("\n");
		builder.append(getShowTypesText());
		builder.append("\n");
		builder.append(getHallsText());
		return builder.toString();
	}

	/**
	 * 获取规则关联影厅字符串。
	 * 
	 * @return 规则关联影厅字符串。
	 */
	public String getHallsText() {
		List<String> texts = new ArrayList<String>();
		for (Hall hall : getHalls()) {
			texts.add(hall.getFullName());
		}
		return StringUtils.join(texts, " ");
	}

	/**
	 * 获取规则说明。
	 * 
	 * @return 规则说明。
	 */
	private String getSettleSummary() {
		return "渠道结算价-"
				+ new DecimalFormat("0.00")
						.format(((FixedMinusSettleRule) getSettleRule())
								.getMinus()) + "元";
	}

	/**
	 * 获取放映类型文本。
	 * 
	 * @return 返回放映类型文本。
	 */
	@JsonIgnore
	public String getShowTypesText() {
		List<String> showTypeNames = new ArrayList<String>();
		for (ShowType showType : showTypes) {
			showTypeNames.add(showType.getText());
		}
		return StringUtils.join(showTypeNames, ",");
	}

	/**
	 * 获取放映类型编码。
	 * 
	 * @return 返回放映类型编码。
	 */
	@JsonIgnore
	public String getShowTypesValue() {
		List<String> showTypeNames = new ArrayList<String>();
		for (ShowType showType : showTypes) {
			showTypeNames.add(showType.getValue());
		}
		return StringUtils.join(showTypeNames, "|");
	}

	@Override
	public int compareTo(BenefitCardTypeRule other) {
		if (getValid().compareTo(other.getValid()) == 0) {
			return other.getCreateDate().compareTo(getCreateDate());
		} else {
			return getValid().compareTo(other.getValid());
		}
	}

	public BenefitCardType getType() {
		return type;
	}

	public void setType(BenefitCardType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ValidStatus getValid() {
		return valid;
	}

	public void setValid(ValidStatus valid) {
		this.valid = valid;
	}

	public EnabledStatus getEnabled() {
		return enabled;
	}

	public void setEnabled(EnabledStatus enabled) {
		this.enabled = enabled;
	}

	public List<ShowType> getShowTypes() {
		return showTypes;
	}

	public void setShowTypes(List<ShowType> showTypes) {
		this.showTypes = showTypes;
	}

	public SettleRule getSettleRule() {
		return settleRule;
	}

	public void setSettleRule(SettleRule settleRule) {
		this.settleRule = settleRule;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public BenefitCardTypeRule getBoundRule() {
		return boundRule;
	}

	public void setBoundRule(BenefitCardTypeRule boundRule) {
		this.boundRule = boundRule;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	public List<BenefitCardSettle> getSettles() {
		return settles;
	}

	public void setSettles(List<BenefitCardSettle> settles) {
		this.settles = settles;
	}
}