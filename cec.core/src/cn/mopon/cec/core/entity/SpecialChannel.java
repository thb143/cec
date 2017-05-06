package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.assist.settle.FixedSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.core.security.entity.ResourceEntity;

/**
 * 特殊定价渠道。
 */
@Entity
@Table(name = "CEC_SpecialChannel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecialChannel extends ResourceEntity<User> implements
		Comparable<SpecialChannel> {
	/** 关联规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ruleId")
	private SpecialRule rule;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	private Channel channel;
	/** 结算规则 */
	@Type(type = "Json")
	private SettleRule settleRule = new FixedSettleRule();
	/** 接入费 */
	private Double connectFee = 0D;
	/** 生效状态 */
	@Type(type = "IEnum")
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 绑定渠道 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundChannelId")
	private SpecialChannel boundChannel;

	/**
	 * 获取摘要信息。
	 * 
	 * @return 返回摘要信息。
	 */
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		builder.append(getSettleRule().toString());
		return builder.toString();
	}

	/**
	 * 复制渠道。
	 * 
	 * @param origChannel
	 *            原渠道
	 * @param toRule
	 *            复制到目标规则
	 * @return 返回复制的渠道。
	 */
	public static SpecialChannel copy(SpecialChannel origChannel,
			SpecialRule toRule) {
		SpecialChannel channel = new SpecialChannel();
		BeanUtils.copyFields(origChannel, channel, "boundChannel");
		channel.setId(null);
		channel.setSettleRule(origChannel.getSettleRule());
		channel.setChannel(origChannel.getChannel());
		channel.setEnabled(EnabledStatus.DISABLED);
		channel.setValid(ValidStatus.UNVALID);
		channel.setConnectFee(origChannel.getConnectFee());
		channel.autoFillIn();
		channel.setRule(toRule);
		toRule.getChannels().add(channel);
		return channel;
	}

	/**
	 * 判断特殊定价渠道是否有效。
	 * 
	 * @return 返回特殊定价渠道是否有效。
	 */
	public Boolean isValid() {
		return getChannel().getOpened() && enabled == EnabledStatus.ENABLED
				&& valid == ValidStatus.VALID;
	}

	/**
	 * 判断特殊定价渠道是否已被绑定（在生效状态下被修改生成了副本）。
	 * 
	 * @return 返回特殊定价渠道是否绑定。
	 */
	public Boolean isBounded() {
		return boundChannel != null;
	}

	/**
	 * 判断特殊定价渠道是否原本。
	 * 
	 * @return 返回特殊定价渠道是否原本。
	 */
	public Boolean isOrig() {
		return boundChannel != null && valid == ValidStatus.VALID;
	}

	/**
	 * 判断特殊定价渠道是否副本。
	 * 
	 * @return 返回特殊定价渠道是否副本。
	 */
	public Boolean isCopy() {
		return boundChannel != null && valid == ValidStatus.UNVALID;
	}

	/**
	 * 判断是否启用。
	 * 
	 * @return 如果是启用返回true，否则返回false。
	 */
	public boolean isEnabled() {
		return enabled == EnabledStatus.ENABLED;
	}

	@Override
	public int compareTo(SpecialChannel other) {
		return getChannel().getCode().compareTo(other.getChannel().getCode());
	}

	public SpecialRule getRule() {
		return rule;
	}

	public void setRule(SpecialRule rule) {
		this.rule = rule;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public SettleRule getSettleRule() {
		return settleRule;
	}

	public void setSettleRule(SettleRule settleRule) {
		this.settleRule = settleRule;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
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

	public SpecialChannel getBoundChannel() {
		return boundChannel;
	}

	public void setBoundChannel(SpecialChannel boundChannel) {
		this.boundChannel = boundChannel;
	}
}