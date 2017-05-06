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
 * 影院结算策略规则。
 */
@Entity
@Table(name = "CEC_CinemaRule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CinemaRule extends SettleRuleEntity {
	/** 关联结算策略 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyId")
	@LogBean({ @LogField(text = "结算策略名称", property = "name") })
	private CinemaPolicy policy;
	/** 关联影厅 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_CinemaRule_Hall", joinColumns = @JoinColumn(name = "ruleId"), inverseJoinColumns = @JoinColumn(name = "hallId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Hall> halls = new ArrayList<Hall>();
	/** 生效状态 */
	@Type(type = "IEnum")
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 绑定结算策略规则 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundRuleId")
	private CinemaRule boundRule;
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
	 * 复制策略规则。
	 * 
	 * @param origPolicyRule
	 *            原策略规则
	 * @param toPolicy
	 *            复制到目标策略
	 * @return 返回复制的策略规则。
	 */
	public static CinemaRule copy(CinemaRule origPolicyRule,
			CinemaPolicy toPolicy) {
		CinemaRule cinemaRule = new CinemaRule();
		BeanUtils.copyFields(origPolicyRule, cinemaRule, "boundRule,status");
		cinemaRule.setId(null);
		cinemaRule.setPeriodRule(origPolicyRule.getPeriodRule());
		cinemaRule.setSettleRule(origPolicyRule.getSettleRule());
		cinemaRule.setShowType(origPolicyRule.getShowType());
		cinemaRule.setValid(ValidStatus.UNVALID);
		cinemaRule.setEnabled(EnabledStatus.DISABLED);
		cinemaRule.autoFillIn();
		cinemaRule.setPolicy(toPolicy);
		toPolicy.getRules().add(cinemaRule);
		// 如果不是复制同一个影院下的策略，则应清空策略规则中的影厅设置。
		if (!origPolicyRule.getPolicy().getCinema().getId()
				.equals(toPolicy.getCinema().getId())) {
			cinemaRule.getHalls().clear();
			cinemaRule.setName(origPolicyRule.getName());
		} else {
			cinemaRule.setName(origPolicyRule.getName() + "-副本");
		}
		return cinemaRule;
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
	 * 是否已被绑定（在生效状态下被修改生成了副本）。
	 * 
	 * @return 如果被绑定返回true，否则返回false。
	 */
	public Boolean isBounded() {
		return boundRule != null;
	}

	/**
	 * 是否原本。
	 * 
	 * @return 如果是原本返回true，否则返回false。
	 */
	public Boolean isOrig() {
		return boundRule != null && valid == ValidStatus.VALID;
	}

	/**
	 * 是否副本。
	 * 
	 * @return 如果是副本返回true，否则返回false。
	 */
	public Boolean isCopy() {
		return boundRule != null && valid == ValidStatus.UNVALID;
	}

	/**
	 * 是否可以操作。
	 * 
	 * @return 如果可以操作返回true,否则返回false。
	 */
	public Boolean isOperate() {
		return status != RuleStatus.AUDITPASS
				&& status != RuleStatus.APPROVEPASS;
	}

	/**
	 * 是否未审核。
	 * 
	 * @return 如果未审核返回true,否则返回false。
	 */
	public Boolean isUnAudit() {
		return status == RuleStatus.UNAUDIT && valid == ValidStatus.UNVALID;
	}

	public Boolean isApprovePass() {
		return status == RuleStatus.APPROVEPASS
				&& (valid != ValidStatus.INVALID || (isBounded() && valid == ValidStatus.UNVALID));
	}

	public CinemaPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(CinemaPolicy policy) {
		this.policy = policy;
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

	public CinemaRule getBoundRule() {
		return boundRule;
	}

	public void setBoundRule(CinemaRule boundRule) {
		this.boundRule = boundRule;
	}

	public RuleStatus getStatus() {
		return status;
	}

	public void setStatus(RuleStatus status) {
		this.status = status;
	}
}
