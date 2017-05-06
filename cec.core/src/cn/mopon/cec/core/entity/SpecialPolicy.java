package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.SpecialPolicyStatus;
import cn.mopon.cec.core.enums.SpecialPolicyType;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.HallModel;
import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumListValueBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.hibernate.search.UuidEntityListBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 特殊定价策略。
 */
@Entity
@Table(name = "CEC_SpecialPolicy")
@Indexed(index = "SpecialPolicy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecialPolicy extends ResourceEntity<User> {
	/** 名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "名称")
	private String name;
	/** 类型 */
	@Type(type = "IEnum")
	private SpecialPolicyType type;
	/** 放映类型 */
	@Type(type = "IEnumList")
	@Field(analyzer = @Analyzer(impl = WhitespaceAnalyzer.class), bridge = @FieldBridge(impl = IEnumListValueBridge.class))
	@LogField(text = "放映类型")
	private List<ShowType> showTypes = new ArrayList<>();
	/** 关联影厅 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_SpecialPolicy_Hall", joinColumns = @JoinColumn(name = "policyId"), inverseJoinColumns = @JoinColumn(name = "hallId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Field(analyzer = @Analyzer(impl = WhitespaceAnalyzer.class), bridge = @FieldBridge(impl = UuidEntityListBridge.class))
	private List<Hall> halls = new ArrayList<>();
	/** 关联影片 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_SpecialPolicy_Film", joinColumns = @JoinColumn(name = "policyId"), inverseJoinColumns = @JoinColumn(name = "filmId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Field(analyzer = @Analyzer(impl = WhitespaceAnalyzer.class), bridge = @FieldBridge(impl = UuidEntityListBridge.class))
	private List<Film> films = new ArrayList<>();
	/** 关联渠道 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_SpecialPolicy_Channel", joinColumns = @JoinColumn(name = "policyId"), inverseJoinColumns = @JoinColumn(name = "channelId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Channel> channels = new ArrayList<>();
	/** 起始日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "起始日期", format = DateUtils.DAY)
	private Date startDate;
	/** 截止日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "截止日期", format = DateUtils.DAY)
	private Date endDate;
	/** 排期起始日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "排期起始日期", format = DateUtils.DAY)
	private Date showStartDate;
	/** 排期截止日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "排期截止日期", format = DateUtils.DAY)
	private Date showEndDate;
	/** 是否独占排期 */
	@LogField(text = "是否独占排期")
	private Boolean exclusive = false;
	/** 审批状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private SpecialPolicyStatus status = SpecialPolicyStatus.SUBMIT;
	/** 生效状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 排序 */
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IntegerBridge.class))
	private Integer ordinal = 0;
	/** 关联规则列表 */
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<SpecialRule> rules = new ArrayList<>();
	/** 关联审批记录列表 */
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("submitTime desc")
	private List<SpecialPolicyLog> logs = new ArrayList<>();

	/**
	 * 获取匹配排期的规则列表。
	 * 
	 * @param show
	 *            排期
	 * @return 返回匹配排期的规则列表。
	 */
	public List<SpecialRule> getMatchedRules(Show show) {
		List<SpecialRule> rules = new ArrayList<>();
		// 判断策略是有效且启用的
		if (valid != ValidStatus.VALID || enabled != EnabledStatus.ENABLED) {
			return rules;
		}
		// 判断策略在有效时间范围内
		Interval policyInterval = DateUtils.getInterval(startDate,
				DateUtils.getNextDay(endDate));
		if (!policyInterval.containsNow()) {
			return rules;
		}
		// 判断排期在有效时间范围内
		Interval showInterval = DateUtils.getInterval(showStartDate,
				DateUtils.getNextDay(showEndDate));
		if (!showInterval.contains(show.getShowTime().getTime())) {
			return rules;
		}
		for (SpecialRule rule : getRules()) {
			if (rule.matchShow(show)) {
				rules.add(rule);
			}
		}
		return rules;
	}

	/**
	 * 获取策略是否已过期。
	 * 
	 * @return 如果已过期返回true，否则返回false。
	 */
	public Boolean getExpired() {
		DateTime expiredDate = new DateTime(endDate).plusDays(1);
		return expiredDate.isBefore(DateTime.now());
	}

	/**
	 * 生成新的规则排序号。
	 * 
	 * @return 返回新的规则排序号。
	 */
	public Integer genNewRuleOrdinal() {
		if (!rules.isEmpty()) {
			return rules.get(0).getOrdinal() + 1;
		} else {
			return 1;
		}
	}

	/**
	 * 获取规则ID列表。
	 * 
	 * @return 返回规则ID列表。
	 */
	public List<String> getRuleIds() {
		List<String> ruleIds = new ArrayList<>();
		for (SpecialRule rule : getRules()) {
			ruleIds.add(rule.getId());
		}
		return ruleIds;
	}

	/**
	 * 获取规则列表。（不包含原本）
	 * 
	 * @return 返回规则列表。
	 */
	public List<SpecialRule> getRulesExcludeOrig() {
		List<SpecialRule> rules = new ArrayList<SpecialRule>();
		for (SpecialRule rule : getRules()) {
			if (!rule.isOrig()) {
				rules.add(rule);
			}
		}
		return rules;
	}

	/**
	 * 获取规则列表。（不包含副本）
	 * 
	 * @return 返回规则列表。
	 */
	public List<SpecialRule> getRulesExcludeCopy() {
		List<SpecialRule> rules = new ArrayList<SpecialRule>();
		for (SpecialRule rule : getRules()) {
			if (!rule.isCopy() && rule.getValid() != ValidStatus.INVALID) {
				rules.add(rule);
			}
		}
		Collections.sort(rules);
		return rules;
	}

	/**
	 * 获取最后一次退回的审批记录。
	 * 
	 * @return 返回最后一次退回的审批记录，如果没有审批记录或最后一条审批记录不是已退回状态则返回null。
	 */
	public SpecialPolicyLog getLastRefusedLog() {
		if (!logs.isEmpty()) {
			SpecialPolicyLog lastLog = logs.get(0);
			if (lastLog.getStatus() == AuditStatus.REFUSED) {
				return lastLog;
			}
		}
		return null;
	}

	/**
	 * 判断策略是否可以被手动启用(如果策略生效开始时间小于当前时间且已生效，则不允许被手动启用)。
	 * 
	 * @return 如果小于当前时间且已生效，返回true，否则返回false。
	 */
	public Boolean isCanEnabled() {
		return getStartDate().before(new Date()) && valid == ValidStatus.VALID;
	}

	/**
	 * 获取影厅模型。
	 * 
	 * @return 返回影厅模型。
	 */
	public List<HallModel> getHallModels() {
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
		List<HallModel> hallModelList = new ArrayList<HallModel>();
		for (Entry<Cinema, List<Hall>> entry : map.entrySet()) {
			HallModel hallModel = new HallModel();
			hallModel.setCinemaName(entry.getKey().getName());
			hallModel.setCinemaId(entry.getKey().getId());
			StringBuilder hallNames = new StringBuilder();
			StringBuilder hallIds = new StringBuilder();
			for (Hall hall : entry.getValue()) {
				hallNames.append(hall.getFullName()).append(",");
				hallIds.append(hall.getId()).append(",");
			}
			hallModel.setHallId(hallIds.toString());
			hallModel.setHallName(hallNames.toString());
			hallModelList.add(hallModel);
		}
		return hallModelList;
	}

	/**
	 * 获取放映类型文本。
	 * 
	 * @return 放映类型字符串。
	 */
	public String getShowTypesText() {
		List<String> texts = new ArrayList<String>();
		for (ShowType showType : getShowTypes()) {
			texts.add(showType.getText());
		}
		return StringUtils.join(texts, " ");
	}

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
	 * 复制策略。
	 * 
	 * @param origPolicy
	 *            原策略
	 * @return 返回复制的策略。
	 */
	public static SpecialPolicy copy(SpecialPolicy origPolicy) {
		SpecialPolicy policy = new SpecialPolicy();
		BeanUtils.copyFields(origPolicy, policy, "rules,logs");
		policy.setId(null);
		policy.setName(origPolicy.getName() + "-副本");
		policy.setStatus(SpecialPolicyStatus.SUBMIT);
		policy.setValid(ValidStatus.UNVALID);
		policy.setEnabled(EnabledStatus.DISABLED);
		policy.autoFillIn();
		for (SpecialRule origRule : origPolicy.getRules()) {
			// 复制已生效或者未生效且未绑定的规则。
			if (origRule.getValid() == ValidStatus.VALID
					|| (origRule.getValid() == ValidStatus.UNVALID && !origRule
							.isBounded())) {
				SpecialRule rule = SpecialRule.copy(origRule, policy);
				policy.getRules().add(rule);
			}
		}
		return policy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpecialPolicyType getType() {
		return type;
	}

	public void setType(SpecialPolicyType type) {
		this.type = type;
	}

	public List<ShowType> getShowTypes() {
		return showTypes;
	}

	public void setShowTypes(List<ShowType> showTypes) {
		this.showTypes = showTypes;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	public List<Film> getFilms() {
		return films;
	}

	public void setFilms(List<Film> films) {
		this.films = films;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getShowStartDate() {
		return showStartDate;
	}

	public void setShowStartDate(Date showStartDate) {
		this.showStartDate = showStartDate;
	}

	public Date getShowEndDate() {
		return showEndDate;
	}

	public void setShowEndDate(Date showEndDate) {
		this.showEndDate = showEndDate;
	}

	public Boolean getExclusive() {
		return exclusive;
	}

	public void setExclusive(Boolean exclusive) {
		this.exclusive = exclusive;
	}

	public SpecialPolicyStatus getStatus() {
		return status;
	}

	public void setStatus(SpecialPolicyStatus status) {
		this.status = status;
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

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * 获取规则。
	 * 
	 * @return 返回排序后的规则列表。
	 */
	public List<SpecialRule> getRules() {
		Collections.sort(rules);
		return rules;
	}

	public void setRules(List<SpecialRule> rules) {
		this.rules = rules;
	}

	public List<SpecialPolicyLog> getLogs() {
		return logs;
	}

	public void setLogs(List<SpecialPolicyLog> logs) {
		this.logs = logs;
	}
}
