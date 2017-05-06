package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackChannel;
import coo.base.util.StringUtils;

/**
 * 卖品渠道列表模型。
 */
public class SnackChannelListModel {
	/** 关联卖品 */
	private Snack snack;
	/** 源卖品渠道 */
	private SnackChannel origChannel;
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
	 * 增加卖品渠道。
	 * 
	 * @param snackChannels
	 *            卖品渠道列表
	 */
	public void addSnackChannels(List<SnackChannel> snackChannels) {
		for (SnackChannel snackChannel : snackChannels) {
			if (!contains(snackChannel.getChannel())) {
				this.channels.add(snackChannel.getChannel());
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

	public Snack getSnack() {
		return snack;
	}

	public void setSnack(Snack snack) {
		this.snack = snack;
	}

	public SnackChannel getOrigChannel() {
		return origChannel;
	}

	public void setOrigChannel(SnackChannel origChannel) {
		this.origChannel = origChannel;
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