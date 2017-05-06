package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.ChannelVo;
import cn.mopon.cec.core.entity.Channel;

/**
 * 查询渠道列表响应对象。
 */
public class ChannelsReply extends ApiReply {
	/** 渠道列表 */
	private List<ChannelVo> channels = new ArrayList<ChannelVo>();

	/**
	 * 构造方法。
	 * 
	 * @param channels
	 *            渠道列表
	 */
	public ChannelsReply(List<Channel> channels) {
		for (Channel channel : channels) {
			this.channels.add(new ChannelVo(channel));
		}
	}

	public List<ChannelVo> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelVo> channels) {
		this.channels = channels;
	}
}