package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.assist.settle.FixedSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialRule;
import coo.base.util.StringUtils;

/**
 * 特殊定价渠道列表模型。
 */
public class SpecialChannelListModel {
	/** 关联特殊定价规则 */
	private SpecialRule specialRule;
	/** 源特殊定价渠道 */
	private SpecialChannel origChannel;
	/** 结算规则 */
	private SettleRule settleRule = new FixedSettleRule();
	/** 渠道列表 */
	private List<Channel> channels = new ArrayList<Channel>();
	/** 选择的渠道 */
	private List<Channel> switchChannels = new ArrayList<Channel>();

	/**
	 * 获取已设置的渠道列表。
	 * 
	 * @return 返回已设置的渠道列表。
	 */
	public List<Channel> getSettedChannels() {
		List<Channel> settedChannels = new ArrayList<Channel>();
		for (Channel channel : channels) {
			if (StringUtils.isNotEmpty(channel.getId())) {
				channels.add(channel);
			}
		}
		return settedChannels;
	}

	/**
	 * 增加特殊定价渠道。
	 * 
	 * @param specialChannels
	 *            特殊定价渠道列表
	 */
	public void addSpecialChannels(List<SpecialChannel> specialChannels) {
		for (SpecialChannel specialChannel : specialChannels) {
			if (!contains(specialChannel.getChannel())) {
				this.channels.add(specialChannel.getChannel());
			}
		}
	}

	/**
	 * 增加渠道。
	 * 
	 * @param newChannels
	 *            渠道列表
	 */
	public void addChannels(List<Channel> newChannels) {
		List<String> channelIds = new ArrayList<>();
		for (Channel channel : channels) {
			channelIds.add(channel.getId());
		}
		for (Channel channel : newChannels) {
			if (!channelIds.contains(channel.getId())) {
				switchChannels.add(channel);
			}
		}
	}

	/**
	 * 判断是否已包含指定的渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回是否已包含指定的渠道。
	 */
	private Boolean contains(Channel channel) {
		for (Channel oldChannel : channels) {
			if (oldChannel.equals(channel)) {
				return true;
			}
		}
		return false;
	}

	public SpecialRule getSpecialRule() {
		return specialRule;
	}

	public void setSpecialRule(SpecialRule specialRule) {
		this.specialRule = specialRule;
	}

	public SpecialChannel getOrigChannel() {
		return origChannel;
	}

	public void setOrigChannel(SpecialChannel origChannel) {
		this.origChannel = origChannel;
	}

	public SettleRule getSettleRule() {
		return settleRule;
	}

	public void setSettleRule(SettleRule settleRule) {
		this.settleRule = settleRule;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<Channel> getSwitchChannels() {
		return switchChannels;
	}

	public void setSwitchChannels(List<Channel> switchChannels) {
		this.switchChannels = switchChannels;
	}
}