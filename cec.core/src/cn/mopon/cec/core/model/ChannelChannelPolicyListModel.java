package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelPolicyLog;

/**
 * 渠道结算策略审核记录按渠道分组的列表模型。
 */
public class ChannelChannelPolicyListModel {
	private List<ChannelChannelPolicyModel> items = new ArrayList<ChannelChannelPolicyModel>();

	/**
	 * 构造方法。
	 */
	public ChannelChannelPolicyListModel() {
	}

	/**
	 * 构造方法。
	 * 
	 * @param channels
	 *            渠道列表
	 */
	public ChannelChannelPolicyListModel(List<Channel> channels) {
		for (Channel channel : channels) {
			ChannelChannelPolicyModel channelPolicyLogModel = new ChannelChannelPolicyModel();
			channelPolicyLogModel.setChannel(channel);
			items.add(channelPolicyLogModel);
		}
	}

	/**
	 * 增加策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @param count
	 *            影院分组数量
	 */
	public void addPolicy(ChannelPolicy channelPolicy, Integer count) {
		for (ChannelChannelPolicyModel channelPolicyModel : items) {
			if (channelPolicyModel.getChannel().equals(
					channelPolicy.getChannel())) {
				channelPolicyModel.addPolicy(channelPolicy, count);
			}
		}
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
		for (ChannelChannelPolicyModel channelPolicyModel : items) {
			if (channelPolicyModel.getChannel().equals(
					policyLog.getPolicy().getChannel())) {
				channelPolicyModel.addPolicyLog(policyLog, count);
			}
		}
	}

	public List<ChannelChannelPolicyModel> getItems() {
		return items;
	}

	public void setItems(List<ChannelChannelPolicyModel> items) {
		this.items = items;
	}
}