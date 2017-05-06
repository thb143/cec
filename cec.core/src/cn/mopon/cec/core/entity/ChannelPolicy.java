package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
import javax.persistence.Transient;

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
import cn.mopon.cec.core.enums.OpenStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.ChannelPolicyCinemaModel;
import cn.mopon.cec.core.model.ChannelRuleGroupModel;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 渠道结算策略。
 */
@Entity
@Table(name = "CEC_ChannelPolicy")
@Indexed(index = "ChannelPolicy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChannelPolicy extends ResourceEntity<User> implements
		Comparable<ChannelPolicy> {
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "code", "name" })
	private Channel channel;
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
	/** 渠道结算规则分组 */
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ChannelRuleGroup> groups = new ArrayList<ChannelRuleGroup>();
	/** 关联审批记录列表 */
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("submitTime desc")
	private List<ChannelPolicyLog> logs = new ArrayList<ChannelPolicyLog>();
	@Transient
	private RuleStatus ruleStatus;

	/**
	 * 获取未生效规则分组。
	 * 
	 * @return 返回未生效的规则分组列表。
	 */
	public List<ChannelRuleGroup> getUnvalidGroups() {
		List<ChannelRuleGroup> unvalidGroups = new ArrayList<>();
		for (ChannelRuleGroup group : getGroups()) {
			if (group.hasUnvalidRules()) {
				unvalidGroups.add(group);
			}
		}
		return unvalidGroups;
	}

	/**
	 * 获取排期匹配的渠道结算规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回排期匹配的渠道结算规则。
	 */
	public ChannelRule getMatchedRule(Show show) {
		// 判断渠道结算策略是有效且启用的
		if (valid != ValidStatus.VALID || enabled != EnabledStatus.ENABLED) {
			return null;
		}
		// 判断排期在有效时间范围内
		Interval policyInterval = DateUtils.getInterval(startDate,
				DateUtils.getNextDay(endDate));
		if (!policyInterval.contains(show.getShowTime().getTime())) {
			return null;
		}
		for (ChannelRuleGroup group : groups) {
			if (group.getCinema().equals(show.getCinema())) {
				return group.getMatchedRule(show);
			}
		}
		return null;
	}

	/**
	 * 获取开放的渠道结算规则分组。
	 * 
	 * @return 返回开放的渠道结算规则分组。
	 */
	public List<ChannelRuleGroup> getOpenedGroups() {
		List<ChannelRuleGroup> groups = new ArrayList<>();
		for (ChannelRuleGroup group : getGroups()) {
			if (group.getStatus() == OpenStatus.OPENED
					&& group.getCinema().getStatus() == EnabledStatus.ENABLED) {
				groups.add(group);
			}
		}
		return groups;
	}

	/**
	 * 获取规则ID列表。
	 * 
	 * @return 返回规则ID列表。
	 */
	public List<String> getRuleIds() {
		List<String> ruleIds = new ArrayList<>();
		for (ChannelRuleGroup group : getGroups()) {
			ruleIds.addAll(group.getRuleIds());
		}
		return ruleIds;
	}

	/**
	 * 获取规则分组模型。
	 * 
	 * @return 返回分组后的模型列表。
	 */
	public List<ChannelRuleGroupModel> getChannelRuleGroupModels() {
		List<ChannelRuleGroupModel> models = new ArrayList<>();
		for (ChannelRuleGroup group : getGroups()) {
			ChannelRuleGroupModel listModel = new ChannelRuleGroupModel(group,
					ruleStatus);
			models.add(listModel);
		}
		// 规则状态是不为空，则移除掉不包含规则的影院，用于切换规则状态用。
		if (ruleStatus != null) {
			Iterator<ChannelRuleGroupModel> iterator = models.iterator();
			while (iterator.hasNext()) {
				if (!iterator.next().hasRules()) {
					iterator.remove();
				}
			}
		}
		return models;
	}

	/**
	 * 获取影院按渠道分组。
	 * 
	 * @return 分组后的影院模型。
	 */
	public List<ChannelPolicyCinemaModel> getCinemaModels() {
		List<ChannelPolicyCinemaModel> cinemaModels = new ArrayList<>();
		ChannelPolicyCinemaModel cinemaModel = null;
		if (CollectionUtils.isNotEmpty(getGroups())) {
			for (ChannelRuleGroup group : getGroups()) {
				filterChannelRule(group);
				cinemaModel = new ChannelPolicyCinemaModel();
				cinemaModel.setCity(group.getCinema().getCounty().getCity());
				cinemaModel.setCinema(group.getCinema());
				cinemaModel.setGroup(group);
				cinemaModels.add(cinemaModel);
			}
		}
		Collections.sort(cinemaModels);
		return cinemaModels;
	}

	/**
	 * 过滤分组中不应该显示的规则。
	 * 
	 * @param group
	 *            规则分组
	 */
	public void filterChannelRule(ChannelRuleGroup group) {
		Iterator<ChannelRule> ruleIterable = group.getRules().iterator();
		while (ruleIterable.hasNext()) {
			ChannelRule rule = ruleIterable.next();
			// 过滤捆绑且生效的规则
			if (rule.isBounded() && rule.getValid() == ValidStatus.VALID) {
				ruleIterable.remove();
			}
		}
	}

	/**
	 * 复制策略。
	 * 
	 * @param origChannelPolicy
	 *            原策略
	 * @param toChannel
	 *            复制到目标渠道
	 * @return 返回复制的策略。
	 */
	public static ChannelPolicy copy(ChannelPolicy origChannelPolicy,
			Channel toChannel) {
		ChannelPolicy channelPolicy = new ChannelPolicy();
		BeanUtils.copyFields(origChannelPolicy, channelPolicy, "groups,logs");
		channelPolicy.setId(null);
		if (origChannelPolicy.getChannel().equals(toChannel)) {
			channelPolicy.setName(channelPolicy.getName() + "-副本");
		}
		channelPolicy.setOrdinal(toChannel.genNewPolicyOrdinal());
		channelPolicy.setStatus(PolicyStatus.SUBMIT);
		channelPolicy.setValid(ValidStatus.UNVALID);
		channelPolicy.setEnabled(EnabledStatus.DISABLED);
		channelPolicy.autoFillIn();
		channelPolicy.setChannel(toChannel);
		for (ChannelRuleGroup origGroup : origChannelPolicy.getGroups()) {
			ChannelRuleGroup group = ChannelRuleGroup.copy(origGroup,
					channelPolicy);
			channelPolicy.getGroups().add(group);
		}
		return channelPolicy;
	}

	/**
	 * 生成新的分组排序号。
	 * 
	 * @return 返回新的分组排序号。
	 */
	public Integer genNewGroupOrdinal() {
		if (!getGroups().isEmpty()) {
			return getGroups().get(getGroups().size() - 1).getOrdinal() + 1;
		} else {
			return 1;
		}
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
	public ChannelPolicyLog getLastRefusedLog() {
		if (!logs.isEmpty()) {
			ChannelPolicyLog lastLog = logs.get(0);
			if (lastLog.getStatus() == AuditStatus.REFUSED) {
				return lastLog;
			}
		}
		return null;
	}

	@Override
	public int compareTo(ChannelPolicy other) {
		return other.getOrdinal() - getOrdinal();
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
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
	 * 获取规则分组。
	 * 
	 * @return 返回排序后的规则分组。
	 */
	public List<ChannelRuleGroup> getGroups() {
		Collections.sort(groups);
		return groups;
	}

	public void setGroups(List<ChannelRuleGroup> groups) {
		this.groups = groups;
	}

	public List<ChannelPolicyLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ChannelPolicyLog> logs) {
		this.logs = logs;
	}

	public RuleStatus getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(RuleStatus ruleStatus) {
		this.ruleStatus = ruleStatus;
	}
}
