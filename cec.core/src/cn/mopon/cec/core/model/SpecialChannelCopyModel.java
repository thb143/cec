package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Channel;

/**
 * 特殊定价渠道复制模型。
 */
public class SpecialChannelCopyModel {
	/** 原特殊定价渠道ID */
	private String origSpecialChannelId;
	/** 要复制到的渠道 */
	private List<Channel> channels = new ArrayList<>();

	public String getOrigSpecialChannelId() {
		return origSpecialChannelId;
	}

	public void setOrigSpecialChannelId(String origSpecialChannelId) {
		this.origSpecialChannelId = origSpecialChannelId;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}