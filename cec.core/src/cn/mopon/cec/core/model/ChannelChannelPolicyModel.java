package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelPolicyLog;

/**
 * 渠道结算策略审核记录按渠道分组模型。
 */
public class ChannelChannelPolicyModel {
	private Channel channel;
	private List<ChannelPolicyModel> items = new ArrayList<>();

	/**
	 * 增加策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @param count
	 *            影院分组数量
	 */
	public void addPolicy(ChannelPolicy channelPolicy, Integer count) {
		ChannelPolicyModel channelPolicyModel = new ChannelPolicyModel(
				channelPolicy, count);
		items.add(channelPolicyModel);
	}

	/**
	 * 增加策略审批记录。
	 * 
	 * @param policyLog
	 *            策略审批记录
	 * @param count
	 *            影院分组数量
	 */
	public void addPolicyLog(ChannelPolicyLog policyLog, Integer count) {
		ChannelPolicyModel channelPolicyModel = new ChannelPolicyModel(
				policyLog, count);
		items.add(channelPolicyModel);
	}

	public List<ChannelPolicyModel> getItems() {
		return items;
	}

	public void setItems(List<ChannelPolicyModel> items) {
		this.items = items;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}