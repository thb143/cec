package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialRule;

/**
 * 排期结算规则列表模型。
 */
public class ShowSettleRulesModel {
	/** 影院结算规则 */
	private CinemaRule cinemaRule;
	/** 渠道结算规则列表 */
	private List<ChannelRule> channelRules = new ArrayList<>();
	/** 特殊定价渠道列表 */
	private List<SpecialChannel> specialChannels = new ArrayList<>();
	/** 权益卡价格规则 */
	private List<BenefitCardTypeRule> benefitRules = new ArrayList<>();
	/** 是否独占排期 */
	private Boolean exclusive = false;

	/**
	 * 是否匹配了影院结算规则。
	 * 
	 * @return 返回是否匹配了影院结算规则。
	 */
	public Boolean hasMatchedCinemaRule() {
		return cinemaRule != null;
	}

	/**
	 * 增加渠道结算规则。
	 * 
	 * @param channelRules
	 *            渠道结算规则列表
	 */
	public void addChannelRules(List<ChannelRule> channelRules) {
		for (ChannelRule channelRule : channelRules) {
			if (!contains(channelRule.getChannel())) {
				this.channelRules.add(channelRule);
			}
		}
	}

	/**
	 * 增加特殊定价渠道。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 */
	public void addSpecialChannels(SpecialRule specialRule) {
		for (SpecialChannel specialChannel : specialRule.getValidChannels()) {
			if (!contains(specialChannel.getChannel())) {
				specialChannels.add(specialChannel);
			}
		}
	}

	/**
	 * 增加权益卡价格规则。
	 * 
	 * @param cardTypeRule
	 *            权益卡价格规则
	 */
	public void addBenefitRules(BenefitCardTypeRule cardTypeRule) {
		this.benefitRules.add(cardTypeRule);
	}

	/**
	 * 判断是否包含指定的特殊定价渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 如果已包含指定的特殊定价渠道返回true，否则返回false。
	 */
	private Boolean contains(Channel channel) {
		for (SpecialChannel specialChannel : specialChannels) {
			if (specialChannel.getChannel().equals(channel)) {
				return true;
			}
		}
		return false;
	}

	public List<SpecialChannel> getSpecialChannels() {
		return specialChannels;
	}

	public void setSpecialChannels(List<SpecialChannel> specialChannels) {
		this.specialChannels = specialChannels;
	}

	public Boolean getExclusive() {
		return exclusive;
	}

	public void setExclusive(Boolean exclusive) {
		this.exclusive = exclusive;
	}

	public CinemaRule getCinemaRule() {
		return cinemaRule;
	}

	public void setCinemaRule(CinemaRule cinemaRule) {
		this.cinemaRule = cinemaRule;
	}

	public List<ChannelRule> getChannelRules() {
		return channelRules;
	}

	public void setChannelRules(List<ChannelRule> channelRules) {
		this.channelRules = channelRules;
	}

	public List<BenefitCardTypeRule> getBenefitRules() {
		return benefitRules;
	}

	public void setBenefitRules(List<BenefitCardTypeRule> benefitRules) {
		this.benefitRules = benefitRules;
	}
}