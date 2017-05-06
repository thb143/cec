package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.List;

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

import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.base.util.StringUtils;
import coo.core.security.annotations.LogBean;
import coo.core.security.annotations.LogField;

/**
 * 渠道结算策略规则。
 */
@Entity
@Table(name = "CEC_ChannelRule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChannelRule extends SettleRuleEntity {
	/** 关联分组 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId")
	@LogBean({ @LogField(text = "分组影院名称", property = "cinema.name") })
	private ChannelRuleGroup group;
	/** 关联影厅 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_ChannelRule_Hall", joinColumns = @JoinColumn(name = "ruleId"), inverseJoinColumns = @JoinColumn(name = "hallId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Hall> halls = new ArrayList<Hall>();
	/** 生效状态 */
	@Type(type = "IEnum")
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 绑定策略规则 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundRuleId")
	private ChannelRule boundRule;
	/** 结算策略规则审核标记状态 */
	@Type(type = "IEnum")
	private RuleStatus status = RuleStatus.UNAUDIT;

	/**
	 * 获取摘要信息。
	 * 
	 * @return 返回摘要信息。
	 */
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		builder.append(getShowType().getText());
		builder.append(" ");
		builder.append(getSettleRule().toString());
		builder.append("\n");
		builder.append(getHallsText());
		builder.append("\n");
		builder.append(getPeriodRule().toString());
		return builder.toString();
	}

	/**
	 * 获取渠道。
	 * 
	 * @return 返回渠道。
	 */
	public Channel getChannel() {
		return getGroup().getPolicy().getChannel();
	}

	/**
	 * 复制策略规则。
	 * 
	 * @param origChannelRule
	 *            原策略规则
	 * @param toGroup
	 *            复制到目标分组
	 * @return 返回复制的策略规则。
	 */
	public static ChannelRule copy(ChannelRule origChannelRule,
			ChannelRuleGroup toGroup) {
		ChannelRule channelRule = new ChannelRule();
		BeanUtils.copyFields(origChannelRule, channelRule, "boundRule,status");
		channelRule.setId(null);
		if (origChannelRule.getGroup().equals(toGroup)) {
			channelRule.setName(origChannelRule.getName() + "-副本");
		} else {
			channelRule.setName(origChannelRule.getName());
		}
		channelRule.setPeriodRule(origChannelRule.getPeriodRule());
		channelRule.setSettleRule(origChannelRule.getSettleRule());
		channelRule.setShowType(origChannelRule.getShowType());
		channelRule.setValid(ValidStatus.UNVALID);
		channelRule.setEnabled(EnabledStatus.DISABLED);
		channelRule.autoFillIn();
		channelRule.setGroup(toGroup);
		return channelRule;
	}

	/**
	 * 是否匹配指定的排期。
	 * 
	 * @param show
	 *            排期
	 * @return 如果匹配排期返回true，否则返回false。
	 */
	public Boolean matchShow(Show show) {
		return valid == ValidStatus.VALID && enabled == EnabledStatus.ENABLED
				&& getShowType() == show.getShowType()
				&& getHalls().contains(show.getHall())
				&& getPeriodRule().contains(show.getShowTime());
	}

	/**
	 * 构造规则关联影厅字符串。
	 * 
	 * @return 放映类型。
	 */
	public String getHallsText() {
		List<String> texts = new ArrayList<String>();
		for (Hall hall : getHalls()) {
			texts.add(hall.getFullName());
		}
		return StringUtils.join(texts, " ");
	}

	/**
	 * 判断策略规则是否已被绑定（在生效状态下被修改生成了副本）。
	 * 
	 * @return 如果被绑定返回true，否则返回false。
	 */
	public Boolean isBounded() {
		return boundRule != null;
	}

	/**
	 * 判断策略规则是否原本。
	 * 
	 * @return 如果是原本返回true，否则返回false。
	 */
	public Boolean isOrig() {
		return boundRule != null && valid == ValidStatus.VALID;
	}

	/**
	 * 判断策略规则是否副本。
	 * 
	 * @return 如果是副本返回true，否则返回false。
	 */
	public Boolean isCopy() {
		return boundRule != null && valid != ValidStatus.VALID;
	}

	/**
	 * 是否未审核。
	 * 
	 * @return 如果未审核返回true,否则返回false。
	 */
	public Boolean isUnAudit() {
		return status == RuleStatus.UNAUDIT && valid == ValidStatus.UNVALID;
	}

	/**
	 * 是否未审批。
	 * 
	 * @return 如果未审批返回true,否则返回false。
	 */
	public Boolean isApprovePass() {
		return status == RuleStatus.APPROVEPASS
				&& (valid != ValidStatus.INVALID || (isBounded() && valid == ValidStatus.UNVALID));
	}

	public ChannelRuleGroup getGroup() {
		return group;
	}

	public void setGroup(ChannelRuleGroup group) {
		this.group = group;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
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

	public ChannelRule getBoundRule() {
		return boundRule;
	}

	public void setBoundRule(ChannelRule boundRule) {
		this.boundRule = boundRule;
	}

	public RuleStatus getStatus() {
		return status;
	}

	public void setStatus(RuleStatus status) {
		this.status = status;
	}
}
