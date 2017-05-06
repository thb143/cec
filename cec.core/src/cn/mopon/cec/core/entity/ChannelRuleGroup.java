package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.OpenStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;
import coo.core.security.annotations.LogField;
import coo.mvc.constants.StatusColor;

/**
 * 渠道结算规则分组。
 */
@Entity
@Table(name = "CEC_ChannelRuleGroup")
@Indexed(index = "ChannelRuleGroup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChannelRuleGroup extends UuidEntity implements
		Comparable<ChannelRuleGroup> {
	/** 关联策略 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyId")
	@IndexedEmbedded(includePaths = "id")
	private ChannelPolicy policy;
	/** 关联影院 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "code", "name", "county.city.code",
			"county.city.name" })
	private Cinema cinema;
	/** 排序 */
	private Integer ordinal = 0;
	/** 状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private OpenStatus status = OpenStatus.OPENED;
	/** 接入费 */
	@LogField(text = "接入费")
	private Double connectFee = 0D;
	/** 是否生效，当分组下没有未生效的规则时，为true，否则为false */
	@Field(analyze = Analyze.NO)
	private Boolean valid = true;
	/** 关联规则列表 */
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ChannelRule> rules = new ArrayList<ChannelRule>();

	/**
	 * 判断规则分组是否存在未生效规则。
	 * 
	 * @return 如果存在未生效规则，返回true，否则返回false。
	 */
	public Boolean hasUnvalidRules() {
		for (ChannelRule rule : getRules()) {
			if (rule.getValid() == ValidStatus.UNVALID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断规则分组是否存在生效且启用的规则。
	 * 
	 * @return 如果存在生效且启用的规则，返回true，否则返回false。
	 */
	public Boolean hasValidAndEnabledRules() {
		for (ChannelRule rule : getRules()) {
			if (rule.getValid() == ValidStatus.VALID
					&& rule.getEnabled() == EnabledStatus.ENABLED) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断规则分组是否存在生效且启用的规则。
	 * 
	 * @return 如果存在生效且启用的规则，返回true，否则返回false。
	 */
	public Boolean getValidAndEnabledRules() {
		return hasValidAndEnabledRules();
	}

	/**
	 * 判断影院名称、策略名称、渠道名称中是否包含关键字。
	 * 
	 * @param keyWord
	 *            关键字
	 * @param group
	 *            规则分组
	 * @return 如果包含，返回true，否则返回false。
	 */
	public Boolean isContain(String keyWord, ChannelRuleGroup group) {
		if (StringUtils.isEmpty(keyWord)) {
			return true;
		}
		keyWord = keyWord.trim();
		return group.getCinema().getName().contains(keyWord)
				|| group.getCinema().getCounty().getCity().getName()
						.contains(keyWord)
				|| group.getPolicy().getName().contains(keyWord);
	}

	/**
	 * 获取排期匹配的渠道结算规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回排期匹配的渠道结算规则。
	 */
	public ChannelRule getMatchedRule(Show show) {
		// 判断渠道规则分组是开放的
		if (status != OpenStatus.OPENED) {
			return null;
		}
		for (ChannelRule rule : getRulesExcludeCopy()) {
			if (rule.matchShow(show)) {
				return rule;
			}
		}
		return null;
	}

	/**
	 * 获取规则ID列表。
	 * 
	 * @return 返回规则ID列表。
	 */
	public List<String> getRuleIds() {
		List<String> ruleIds = new ArrayList<>();
		for (ChannelRule rule : getRules()) {
			ruleIds.add(rule.getId());
		}
		return ruleIds;
	}

	/**
	 * 获取渠道结算规则列表。（不包含原本）
	 * 
	 * @return 返回渠道结算规则列表。
	 */
	public List<ChannelRule> getRulesExcludeOrig() {
		List<ChannelRule> rules = new ArrayList<>();
		for (ChannelRule channelRule : getRules()) {
			if (!channelRule.isOrig()) {
				rules.add(channelRule);
			}
		}
		return rules;
	}

	/**
	 * 获取渠道结算规则列表。（不包含副本）
	 * 
	 * @return 返回渠道结算规则列表。
	 */
	public List<ChannelRule> getRulesExcludeCopy() {
		List<ChannelRule> rules = new ArrayList<>();
		for (ChannelRule channelRule : getRules()) {
			if (!channelRule.isCopy()) {
				rules.add(channelRule);
			}
		}
		return rules;
	}

	/**
	 * 复制策略规则分组。
	 * 
	 * @param origGroup
	 *            原策略规则
	 * @param toPolicy
	 *            复制到目标分组
	 * @return 返回复制的策略规则。
	 */
	public static ChannelRuleGroup copy(ChannelRuleGroup origGroup,
			ChannelPolicy toPolicy) {
		ChannelRuleGroup group = new ChannelRuleGroup();
		BeanUtils.copyFields(origGroup, group, "rules,valid");
		group.setId(null);
		group.setStatus(OpenStatus.OPENED);
		group.setValid(false);
		group.setPolicy(toPolicy);
		for (ChannelRule origRule : origGroup.getRules()) {
			// 复制已生效或者未生效且未绑定的策略规则。
			if (isCopy(group, origRule)) {
				ChannelRule rule = ChannelRule.copy(origRule, group);
				group.getRules().add(rule);
			}
		}
		// 如果是复制到同一个策略，则设置序号为新的序号。
		if (origGroup.getPolicy().equals(toPolicy)) {
			group.setOrdinal(toPolicy.genNewGroupOrdinal());
		}
		return group;
	}

	/**
	 * 判断是否可进行复制。
	 * 
	 * @param group
	 *            结算规则分组
	 * @param origRule
	 *            原策略规则
	 * @return 如果原策略规则可复制到结算规则分组中，返回true，否则，返回false。
	 */
	private static Boolean isCopy(ChannelRuleGroup group, ChannelRule origRule) {
		return (origRule.getValid() == ValidStatus.VALID || (origRule
				.getValid() == ValidStatus.UNVALID && !origRule.isBounded()))
				&& group.getCinema().getTicketSettings().getShowTypes()
						.contains(origRule.getShowType());
	}

	/**
	 * 获取规则分组页面展示的颜色。
	 * 
	 * @return 返回规则分组颜色。
	 */
	public String getRuleGroupColor() {
		// 关闭状态为灰色
		if (getStatus() == OpenStatus.CLOSED || getPolicy().getExpired()) {
			return StatusColor.GRAY;
		}
		// 如果策略未生效，则分组为红色
		if (getPolicy().getValid() == ValidStatus.UNVALID) {
			return StatusColor.RED;
		}
		if (getPolicy().getValid() == ValidStatus.VALID
				&& getPolicy().getStatus() == PolicyStatus.APPROVED
				&& getPolicy().getEnabled() == EnabledStatus.ENABLED) {
			return StatusColor.GREEN;
		}
		if (CollectionUtils.isNotEmpty(getRules())) {
			if (isModify()) {
				return StatusColor.ORANGE;
			}
			if (isNew()) {
				return StatusColor.RED;
			}
			return StatusColor.GREEN;
		}
		return StatusColor.BLACK;
	}

	/**
	 * 判断是否为新加的分组。
	 * 
	 * @return 如果是新加，返回true，否则返回false。
	 */
	private Boolean isNew() {
		for (ChannelRule rule : getRules()) {
			if (rule.getValid() != ValidStatus.UNVALID) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断分组是否修改（分组下规则是否有变动）。
	 * 
	 * @return 如果是，返回true，否则返回false。
	 */
	private Boolean isModify() {
		int newCount = 0;
		int allCount = 0;
		for (ChannelRule rule : getRules()) {
			allCount++;
			if (rule.isBounded()) {
				return true;
			}
			if (rule.getValid() == ValidStatus.UNVALID) {
				newCount++;
			}
		}
		if (newCount != 0 && newCount != allCount) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(ChannelRuleGroup other) {
		return getCinema().getCounty().getCity().getCode()
				.compareTo(other.getCinema().getCounty().getCity().getCode());
	}

	public ChannelPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(ChannelPolicy policy) {
		this.policy = policy;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public OpenStatus getStatus() {
		return status;
	}

	public void setStatus(OpenStatus status) {
		this.status = status;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	/**
	 * 获取规则。
	 * 
	 * @return 返回排序后的规则列表。
	 */
	public List<ChannelRule> getRules() {
		Collections.sort(rules);
		return rules;
	}

	public void setRules(List<ChannelRule> rules) {
		this.rules = rules;
	}
}
