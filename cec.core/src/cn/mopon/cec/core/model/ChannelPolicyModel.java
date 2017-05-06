package cn.mopon.cec.core.model;

import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelPolicyLog;

/**
 * 渠道结算策略规则分组列表模型。
 */
public class ChannelPolicyModel {
	private ChannelPolicyLog channelPolicyLog;
	private ChannelPolicy channelPolicy;
	private Integer count;

	/**
	 * 构造方法。
	 * 
	 * @param channelPolicy
	 *            渠道策略
	 * @param count
	 *            影院分组数量
	 */
	public ChannelPolicyModel(ChannelPolicy channelPolicy, Integer count) {
		this.channelPolicy = channelPolicy;
		this.count = count;
	}

	/**
	 * 构造方法。
	 * 
	 * @param channelPolicy
	 *            渠道策略审批记录
	 * @param count
	 *            影院分组数量
	 */
	public ChannelPolicyModel(ChannelPolicyLog channelPolicyLog, Integer count) {
		this.channelPolicyLog = channelPolicyLog;
		this.count = count;
	}

	public ChannelPolicyLog getChannelPolicyLog() {
		return channelPolicyLog;
	}

	public void setChannelPolicyLog(ChannelPolicyLog channelPolicyLog) {
		this.channelPolicyLog = channelPolicyLog;
	}

	public ChannelPolicy getChannelPolicy() {
		return channelPolicy;
	}

	public void setChannelPolicy(ChannelPolicy channelPolicy) {
		this.channelPolicy = channelPolicy;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}