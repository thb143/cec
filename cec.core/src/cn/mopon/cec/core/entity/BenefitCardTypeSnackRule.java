package cn.mopon.cec.core.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.entity.ResourceEntity;

/**
 * 权益卡类卖品规则。
 */
@Entity
@Table(name = "CEC_BenefitCardTypeSnackRule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BenefitCardTypeSnackRule extends ResourceEntity<User> implements
		Comparable<BenefitCardTypeSnackRule> {
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
	/** 关联卖品 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_BenefitCardTypeSnackRule_Snack", joinColumns = @JoinColumn(name = "snackRuleId"), inverseJoinColumns = @JoinColumn(name = "snackId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Snack> snacks = new ArrayList<>();
	/** 排序 */
	private Integer ordinal = 0;
	/** 绑定规则 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundRuleId")
	private BenefitCardTypeSnackRule boundRule;

	/**
	 * 获取卖品的影院。
	 * 
	 * @return 返回影院。
	 */
	public Map<Cinema, List<Snack>> getAllCinemas() {
		Map<Cinema, List<Snack>> map = new HashMap<Cinema, List<Snack>>();
		List<Snack> snacks = null;
		for (Snack snack : getSnacks()) {
			if (!map.containsKey(snack.getCinema())) {
				snacks = new ArrayList<Snack>();
				snacks.add(snack);
				map.put(snack.getCinema(), snacks);
			} else {
				map.get(snack.getCinema()).add(snack);
			}
		}
		return map;
	}

	/**
	 * 获取卖品的影院。
	 * 
	 * @return 返回影院。
	 */
	public Map<String, List<Snack>> getCinemas() {
		Map<String, List<Snack>> map = new HashMap<String, List<Snack>>();
		List<Snack> snacks = null;
		for (Snack snack : getSnacks()) {
			if (!map.containsKey(snack.getCinema().getName())) {
				snacks = new ArrayList<Snack>();
				snacks.add(snack);
				map.put(snack.getCinema().getName(), snacks);
			} else {
				map.get(snack.getCinema().getName()).add(snack);
			}
		}
		return map;
	}

	/**
	 * 是否匹配指定的卖品。
	 * 
	 * @param snack
	 *            卖品
	 * @return 如果匹配卖品返回true，否则返回false。
	 */
	public Boolean matchSnack(Snack snack) {
		return (valid == ValidStatus.VALID || valid == ValidStatus.INVALID)
				&& enabled == EnabledStatus.ENABLED
				&& getSnacks().contains(snack);
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
		builder.append(getSnacksText());
		return builder.toString();
	}

	/**
	 * 获取规则关联卖品字符串。
	 * 
	 * @return 规则关联卖品字符串。
	 */
	public String getSnacksText() {
		List<String> texts = new ArrayList<String>();
		for (Snack snack : getSnacks()) {
			texts.add(snack.getType().getName());
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
	 * 计算结算价。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @return 返回结算价。
	 */
	public Double calSettlePrice(SnackChannel snackChannel) {
		Double settlePrice = getSettleRule().cal(snackChannel.getSettlePrice());
		return settlePrice > 0 ? settlePrice : 0D;
	}

	/**
	 * 计算优惠金额。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @return 返回优惠金额。
	 */
	public Double calDiscountPrice(SnackChannel snackChannel) {
		Double settlePrice = calSettlePrice(snackChannel);
		return NumberUtils.sub(snackChannel.getSettlePrice(), settlePrice);
	}

	@Override
	public int compareTo(BenefitCardTypeSnackRule other) {
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

	public BenefitCardTypeSnackRule getBoundRule() {
		return boundRule;
	}

	public void setBoundRule(BenefitCardTypeSnackRule boundRule) {
		this.boundRule = boundRule;
	}

	public List<Snack> getSnacks() {
		return snacks;
	}

	public void setSnacks(List<Snack> snacks) {
		this.snacks = snacks;
	}
}