package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Channel;

/**
 * 卖品渠道复制模型。
 */
public class SnackChannelCopyModel {
	/** 原卖品渠道ID */
	private String origSnackChannelId;
	/** 要复制到的渠道 */
	private List<Channel> channels = new ArrayList<>();

	public String getOrigSnackChannelId() {
		return origSnackChannelId;
	}

	public void setOrigSnackChannelId(String origSnackChannelId) {
		this.origSnackChannelId = origSnackChannelId;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}