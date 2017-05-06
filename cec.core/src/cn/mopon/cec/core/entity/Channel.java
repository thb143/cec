package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import cn.mopon.cec.core.enums.ChannelType;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.core.hibernate.search.IEnumTextBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 渠道。
 */
@Entity
@Table(name = "CEC_Channel")
@Indexed(index = "Channel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Channel extends ResourceEntity<User> {
	/** 渠道名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "渠道名称")
	private String name;
	/** 渠道编号 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "渠道编号")
	private String code;
	/** 通讯秘钥 */
	private String secKey;
	/** 渠道类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumTextBridge.class))
	private ChannelType type = ChannelType.OTHER;
	/** 可售状态 */
	@Field(analyze = Analyze.NO)
	private Boolean salable = true;
	/** 开放状态 */
	@Field(analyze = Analyze.NO)
	private Boolean opened = true;
	/** 备注 */
	private String remark;
	/** 关联渠道设置 */
	@OneToOne(mappedBy = "channel", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ChannelSettings settings;
	/** 关联渠道结算策略 */
	@OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ChannelPolicy> policys = new ArrayList<>();

	/**
	 * 获取对渠道开放的影院。
	 * 
	 * @return 返回对渠道开放的影院。
	 */
	public List<Cinema> getOpenedCinemas() {
		List<Cinema> cinemas = new ArrayList<>();
		for (ChannelPolicy policy : getValidPolicys()) {
			for (ChannelRuleGroup group : policy.getOpenedGroups()) {
				Cinema cinema = group.getCinema();
				if (!cinemas.contains(cinema)) {
					cinemas.add(cinema);
				}
			}
		}
		return cinemas;
	}

	/**
	 * 获取匹配排期的渠道结算规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回匹配排期的渠道结算规则。
	 */
	public ChannelRule getMatchedRule(Show show) {
		if (show.isValid() && opened) {
			for (ChannelPolicy policy : getValidPolicys()) {
				ChannelRule rule = policy.getMatchedRule(show);
				if (rule != null) {
					return rule;
				}
			}
		}
		return null;
	}

	/**
	 * 获取有效启用的渠道策略列表。
	 * 
	 * @return 返回有效启用的渠道策略列表。
	 */
	public List<ChannelPolicy> getValidPolicys() {
		List<ChannelPolicy> validPolicys = new ArrayList<>();
		for (ChannelPolicy policy : getPolicys()) {
			if (policy.getEnabled() == EnabledStatus.ENABLED
					&& policy.getValid() == ValidStatus.VALID
					&& !policy.getExpired()) {
				validPolicys.add(policy);
			}
		}
		return validPolicys;
	}

	/**
	 * 获取未失效的渠道策略列表。
	 * 
	 * @return 返回未失效的渠道策略列表。
	 */
	public List<ChannelPolicy> getNotInvalidPolicys() {
		List<ChannelPolicy> notInvalidPolicys = new ArrayList<>();
		for (ChannelPolicy policy : getPolicys()) {
			if (policy.getValid() != ValidStatus.INVALID) {
				notInvalidPolicys.add(policy);
			}
		}
		return notInvalidPolicys;
	}

	/**
	 * 生成新的策略排序号。
	 * 
	 * @return 返回新的策略排序号。
	 */
	public Integer genNewPolicyOrdinal() {
		if (!policys.isEmpty()) {
			return getPolicys().get(0).getOrdinal() + 1;
		} else {
			return 1;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ChannelType getType() {
		return type;
	}

	public void setType(ChannelType type) {
		this.type = type;
	}

	public Boolean getSalable() {
		return salable;
	}

	public void setSalable(Boolean salable) {
		this.salable = salable;
	}

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public ChannelSettings getSettings() {
		return settings;
	}

	public void setSettings(ChannelSettings settings) {
		this.settings = settings;
	}

	/**
	 * 获取策略列表。
	 * 
	 * @return 返回排序后的策略列表。
	 */
	public List<ChannelPolicy> getPolicys() {
		Collections.sort(policys);
		return policys;
	}

	public void setPolicys(List<ChannelPolicy> policys) {
		this.policys = policys;
	}
}