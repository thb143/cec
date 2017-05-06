package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ValidMonth;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.constants.Chars;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 权益卡类。
 */
@Entity
@Table(name = "CEC_BenefitCardType")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "BenefitCardType")
public class BenefitCardType extends ResourceEntity<User> {
	/** 编号 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "编号")
	private String code;
	/** 名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "名称")
	private String name;
	/** 卡号前缀 */
	@LogField(text = "卡号前缀")
	@Field(analyze = Analyze.NO)
	private String prefix;
	/** 开卡金额 */
	@LogField(text = "开卡金额")
	private Double initAmount;
	/** 续费金额 */
	@LogField(text = "续费金额")
	private Double rechargeAmount;
	/** 总优惠次数 */
	@LogField(text = "总优惠次数")
	private Integer totalDiscountCount;
	@LogField(text = "单日优惠次数")
	private Integer dailyDiscountCount;
	@Type(type = "IEnum")
	@LogField(text = "开卡有效期")
	private ValidMonth validMonth;
	/** 绑定卡类 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundTypeId")
	@IndexedEmbedded(includePaths = { "id" })
	private BenefitCardType boundType;
	/** 生效状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 审批状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private PolicyStatus status = PolicyStatus.SUBMIT;
	/** 权益卡可开卡渠道 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_BenefitCardType_Channel", joinColumns = @JoinColumn(name = "typeId"), inverseJoinColumns = @JoinColumn(name = "channelId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Channel> channels = new ArrayList<>();
	/** 关联票务规则列表 */
	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<BenefitCardTypeRule> rules = new ArrayList<>();
	/** 关联卖品规则列表 */
	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<BenefitCardTypeSnackRule> snackRules = new ArrayList<>();
	/** 关联审批记录列表,不级联删除，因为审批通过需要把绑定的卡类删除，把审批记录copy到原有卡类上 */
	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("submitTime desc")
	private List<BenefitCardTypeLog> logs = new ArrayList<>();

	/**
	 * 获取卡类开放的渠道。
	 * 
	 * @return 卡类开放的渠道。
	 */
	public String getOpenCardChannelText() {
		List<String> openCardChannels = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(channels)) {
			for (Channel channel : channels) {
				openCardChannels.add(channel.getName());
			}
		}
		return StringUtils.join(openCardChannels, Chars.SEMICOLON);
	}

	/**
	 * 获取最后一次退回的审批记录。
	 * 
	 * @return 返回最后一次退回的审批记录，如果没有审批记录或最后一条审批记录不是已退回状态则返回null。
	 */
	public BenefitCardTypeLog getLastRefusedLog() {
		if (!logs.isEmpty()) {
			BenefitCardTypeLog lastLog = logs.get(0);
			if (lastLog.getStatus() == AuditStatus.REFUSED) {
				return lastLog;
			}
		}
		return null;
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
	 * 获取规则列表。（不包含副本）
	 * 
	 * @return 返回规则列表。
	 */
	public List<BenefitCardTypeRule> getRulesExcludeCopy() {
		List<BenefitCardTypeRule> rules = new ArrayList<BenefitCardTypeRule>();
		for (BenefitCardTypeRule rule : getRules()) {
			if (!rule.isCopy() && rule.getValid() != ValidStatus.INVALID) {
				rules.add(rule);
			}
		}
		Collections.sort(rules);
		return rules;
	}

	/**
	 * 获取卖品规则列表。（不包含副本）
	 * 
	 * @return 返回规则列表。
	 */
	public List<BenefitCardTypeSnackRule> getSnackRulesExcludeCopy() {
		List<BenefitCardTypeSnackRule> rules = new ArrayList<BenefitCardTypeSnackRule>();
		for (BenefitCardTypeSnackRule rule : getSnackRules()) {
			if (!rule.isCopy() && rule.getValid() != ValidStatus.INVALID) {
				rules.add(rule);
			}
		}
		Collections.sort(rules);
		return rules;
	}

	/**
	 * 获取有效的规则
	 * 
	 * @return 返回有效的规则列表。
	 */
	public List<BenefitCardTypeRule> getValidRules() {
		List<BenefitCardTypeRule> validRules = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(rules)) {
			for (BenefitCardTypeRule rule : rules) {
				if (rule.getValid() == ValidStatus.VALID) {
					validRules.add(rule);
				}
			}
		}
		return validRules;
	}

	/**
	 * 获取匹配排期的规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回匹配排期的规则。
	 */
	public BenefitCardTypeRule getMatchedRule(Show show) {
		// 判断策略是有效且启用的
		if (valid != ValidStatus.VALID) {
			return null;
		}
		for (BenefitCardTypeRule rule : getRules()) {
			if (rule.matchShow(show)) {
				return rule;
			}
		}
		return null;
	}

	/**
	 * 获取匹配卖品的卖品规则。
	 * 
	 * @param snack
	 *            卖品
	 * @return 返回匹配卖品的卖品规则。
	 */
	public BenefitCardTypeSnackRule getMatchedSnackRule(Snack snack) {
		// 判断策略是有效且启用的
		if (valid != ValidStatus.VALID) {
			return null;
		}
		for (BenefitCardTypeSnackRule rule : getSnackRules()) {
			if (rule.matchSnack(snack)) {
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
		for (BenefitCardTypeRule rule : getRules()) {
			ruleIds.add(rule.getId());
		}
		return ruleIds;
	}

	/**
	 * 判断是否已被绑定（在生效状态下被修改生成了副本）。
	 * 
	 * @return 如果被绑定返回true，否则返回false。
	 */
	public boolean isBounded() {
		return boundType != null;
	}

	/**
	 * 判断卡类是否包含渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 包含返回true,不包含返回false。
	 */
	public boolean containsChannel(Channel channel) {
		return getChannels().contains(channel);
	}

	/**
	 * 是否有绑定的规则。
	 * 
	 * @return 有返回true,没有返回false。
	 */
	public boolean hasBoundRule() {
		for (BenefitCardTypeRule rule : getRules()) {
			if (rule.isBounded()) {
				return true;
			}
		}
		return false;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Double getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(Double initAmount) {
		this.initAmount = initAmount;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Integer getTotalDiscountCount() {
		return totalDiscountCount;
	}

	public void setTotalDiscountCount(Integer totalDiscountCount) {
		this.totalDiscountCount = totalDiscountCount;
	}

	public Integer getDailyDiscountCount() {
		return dailyDiscountCount;
	}

	public void setDailyDiscountCount(Integer dailyDiscountCount) {
		this.dailyDiscountCount = dailyDiscountCount;
	}

	public ValidMonth getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(ValidMonth validMonth) {
		this.validMonth = validMonth;
	}

	public BenefitCardType getBoundType() {
		return boundType;
	}

	public void setBoundType(BenefitCardType boundType) {
		this.boundType = boundType;
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

	public PolicyStatus getStatus() {
		return status;
	}

	public void setStatus(PolicyStatus status) {
		this.status = status;
	}

	public List<BenefitCardTypeRule> getRules() {
		Collections.sort(rules);
		return rules;
	}

	public void setRules(List<BenefitCardTypeRule> rules) {
		this.rules = rules;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<BenefitCardTypeLog> getLogs() {
		return logs;
	}

	public void setLogs(List<BenefitCardTypeLog> logs) {
		this.logs = logs;
	}

	public List<BenefitCardTypeSnackRule> getSnackRules() {
		Collections.sort(snackRules);
		return snackRules;
	}

	public void setSnackRules(List<BenefitCardTypeSnackRule> snackRules) {
		this.snackRules = snackRules;
	}
}