package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.SubmitType;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.CinemaRuleListModel;
import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 影院结算定价策略。
 */
@Entity
@Table(name = "CEC_CinemaPolicy")
@Indexed(index = "CinemaPolicy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CinemaPolicy extends ResourceEntity<User> implements
		Comparable<CinemaPolicy> {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "name", "county.city.code",
			"county.city.name" })
	private Cinema cinema;
	/** 名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "名称")
	private String name;
	/** 起始时间 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "有效起始时间", format = DateUtils.DAY)
	private Date startDate;
	/** 截止时间 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "有效截止时间", format = DateUtils.DAY)
	private Date endDate;
	/** 上报类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	@LogField(text = "上报类型 ")
	private SubmitType submitType;
	/** 加减值 */
	@LogField(text = "加减值 ")
	private Double amount = 0D;
	/** 审批状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private PolicyStatus status = PolicyStatus.SUBMIT;
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
	private List<CinemaRule> rules = new ArrayList<>();
	/** 关联审批记录列表 */
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("submitTime desc")
	private List<CinemaPolicyLog> logs = new ArrayList<>();

	/**
	 * 获取匹配排期的影院结算规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回匹配排期的影院结算规则。
	 */
	public CinemaRule getMatchedRule(Show show) {
		// 判断影院结算策略是有效且启用的
		if (valid != ValidStatus.VALID || enabled != EnabledStatus.ENABLED) {
			return null;
		}
		// 判断排期在有效时间范围内
		Interval policyInterval = DateUtils.getInterval(startDate,
				DateUtils.getNextDay(endDate));
		if (!policyInterval.contains(show.getShowTime().getTime())) {
			return null;
		}
		for (CinemaRule rule : getRulesExcludeCopy()) {
			if (rule.matchShow(show)) {
				return rule;
			}
		}
		return null;
	}

	/**
	 * 复制策略。
	 * 
	 * @param origPolicy
	 *            原策略
	 * @param toCinema
	 *            复制到目标影院
	 * @return 返回复制的策略。
	 */
	public static CinemaPolicy copy(CinemaPolicy origPolicy, Cinema toCinema) {
		CinemaPolicy policy = new CinemaPolicy();
		BeanUtils.copyFields(origPolicy, policy, "rules,logs");
		policy.setId(null);
		if (origPolicy.getCinema().equals(toCinema)) {
			policy.setName(policy.getName() + "-副本");
		}
		policy.setOrdinal(toCinema.genNewPolicyOrdinal());
		policy.setStatus(PolicyStatus.SUBMIT);
		policy.setValid(ValidStatus.UNVALID);
		policy.setEnabled(EnabledStatus.DISABLED);
		policy.autoFillIn();
		policy.setCinema(toCinema);
		toCinema.getPolicys().add(policy);
		for (CinemaRule origCinemaRule : origPolicy.getRules()) {
			if (isCopy(policy, origCinemaRule)) {
				CinemaRule cinemaRule = CinemaRule.copy(origCinemaRule, policy);
				policy.getRules().add(cinemaRule);
			}
		}
		return policy;
	}

	/**
	 * 判断是否可进行复制。
	 * 
	 * @param policy
	 *            结算策略
	 * @param origCinemaRule
	 *            原策略规则
	 * @return 如果原策略规则可复制到结算策略中，返回true，否则，返回false。
	 */
	private static Boolean isCopy(CinemaPolicy policy, CinemaRule origCinemaRule) {
		return (origCinemaRule.getValid() == ValidStatus.VALID || (origCinemaRule
				.getValid() == ValidStatus.UNVALID && !origCinemaRule
				.isBounded()))
				&& policy.getCinema().getTicketSettings() != null
				&& policy.getCinema().getTicketSettings().getShowTypes()
						.contains(origCinemaRule.getShowType());
	}

	/**
	 * 获取规则ID列表。
	 * 
	 * @return 返回规则ID列表。
	 */
	public List<String> getRuleIds() {
		List<String> ruleIds = new ArrayList<>();
		for (CinemaRule rule : getRules()) {
			ruleIds.add(rule.getId());
		}
		return ruleIds;
	}

	/**
	 * 获取策略规则列表。（不包含原本）
	 * 
	 * @return 返回策略规则列表。
	 */
	public List<CinemaRule> getRulesExcludeOrig() {
		List<CinemaRule> cinemaRules = new ArrayList<>();
		for (CinemaRule cinemaRule : getRules()) {
			if (!cinemaRule.isOrig()) {
				cinemaRules.add(cinemaRule);
			}
		}
		return cinemaRules;
	}

	/**
	 * 获取策略规则列表。（不包含副本）
	 * 
	 * @return 返回策略规则列表。
	 */
	public List<CinemaRule> getRulesExcludeCopy() {
		List<CinemaRule> cinemaRules = new ArrayList<>();
		for (CinemaRule cinemaRule : getRules()) {
			if (!cinemaRule.isCopy()) {
				cinemaRules.add(cinemaRule);
			}
		}
		return cinemaRules;
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
	 * 获取最后一次退回的审批记录。
	 * 
	 * @return 返回最后一次退回的审批记录，如果没有审批记录或最后一条审批记录不是已退回状态则返回null。
	 */
	public CinemaPolicyLog getLastRefusedLog() {
		if (!logs.isEmpty()) {
			CinemaPolicyLog lastLog = logs.get(0);
			if (lastLog.getStatus() == AuditStatus.REFUSED) {
				return lastLog;
			}
		}
		return null;
	}

	/**
	 * 按放映类型分组获取所有结算规则。
	 * 
	 * @return 返回按放映类型分组获取结算规则模型。
	 */
	public CinemaRuleListModel getAllCinemaRuleList() {
		CinemaRuleListModel cinemaRuleListModel = new CinemaRuleListModel(
				getCinema());
		for (CinemaRule rule : getRules()) {
			if ((rule.isBounded() && rule.getValid() == ValidStatus.UNVALID)
					|| (!rule.isBounded())) {
				cinemaRuleListModel.add(rule);
			}
		}
		return cinemaRuleListModel;
	}

	/**
	 * 按放映类型分组获取未审核结算规则。
	 * 
	 * @return 返回按放映类型分组获取结算规则模型。
	 */
	public CinemaRuleListModel getUnAuditCinemaRuleList() {
		CinemaRuleListModel cinemaRuleListModel = new CinemaRuleListModel();
		for (CinemaRule rule : getRules()) {
			if (rule.isUnAudit()) {
				cinemaRuleListModel.add(rule);
			}
		}
		Collections.sort(cinemaRuleListModel.getItems());
		return cinemaRuleListModel;
	}

	/**
	 * 按放映类型分组获取审核通过结算规则。
	 * 
	 * @return 返回按放映类型分组获取结算规则模型。
	 */
	public CinemaRuleListModel getAuditpassCinemaRuleList() {
		CinemaRuleListModel cinemaRuleListModel = new CinemaRuleListModel();
		for (CinemaRule rule : getRules()) {
			if (rule.getStatus() == RuleStatus.AUDITPASS) {
				cinemaRuleListModel.add(rule);
			}
		}
		Collections.sort(cinemaRuleListModel.getItems());
		return cinemaRuleListModel;
	}

	/**
	 * 按放映类型分组获取审批通过结算规则。
	 * 
	 * @return 返回按放映类型分组获取结算规则模型。
	 */
	public CinemaRuleListModel getApprovepassCinemaRuleList() {
		CinemaRuleListModel cinemaRuleListModel = new CinemaRuleListModel();
		for (CinemaRule rule : getRules()) {
			if (rule.isApprovePass()) {
				cinemaRuleListModel.add(rule);
			}
		}
		Collections.sort(cinemaRuleListModel.getItems());
		return cinemaRuleListModel;
	}

	/**
	 * 按放映类型分组获取待审核结算规则。
	 * 
	 * @return 返回按放映类型分组获取结算规则模型。
	 */
	public CinemaRuleListModel getRefuseCinemaRuleList() {
		CinemaRuleListModel cinemaRuleListModel = new CinemaRuleListModel();
		for (CinemaRule rule : getRules()) {
			if (rule.getStatus() == RuleStatus.REFUSE) {
				cinemaRuleListModel.add(rule);
			}
		}
		return cinemaRuleListModel;
	}

	@Override
	public int compareTo(CinemaPolicy other) {
		return other.getOrdinal() - getOrdinal();
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public SubmitType getSubmitType() {
		return submitType;
	}

	public void setSubmitType(SubmitType submitType) {
		this.submitType = submitType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public PolicyStatus getStatus() {
		return status;
	}

	public void setStatus(PolicyStatus status) {
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
	 * 排序结算策略规则。
	 * 
	 * @return 返回排序后的结算规则。
	 */
	public List<CinemaRule> getRules() {
		Collections.sort(rules);
		return rules;
	}

	public void setRules(List<CinemaRule> rules) {
		this.rules = rules;
	}

	public List<CinemaPolicyLog> getLogs() {
		return logs;
	}

	public void setLogs(List<CinemaPolicyLog> logs) {
		this.logs = logs;
	}
}
