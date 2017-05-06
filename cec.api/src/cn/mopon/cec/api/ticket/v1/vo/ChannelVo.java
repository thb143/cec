package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.enums.ChannelType;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 渠道。
 */
@XStreamAlias("channel")
public class ChannelVo {
	/** 编码 */
	private String code = "";
	/** 名称 */
	private String name = "";
	/** 通讯密钥 */
	private String secKey = "";
	/** 渠道类型 */
	@XStreamConverter(value = IEnumConverter.class, types = ChannelType.class)
	private ChannelType type;
	/** 开放状态 */
	private Boolean opened;
	/** 销售状态 */
	private Boolean salable;

	/**
	 * 构造方法。
	 * 
	 * @param channel
	 *            渠道
	 */
	public ChannelVo(Channel channel) {
		code = channel.getCode();
		name = channel.getName();
		secKey = channel.getSecKey();
		type = channel.getType();
		opened = channel.getOpened();
		salable = channel.getSalable();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}

	public ChannelType getType() {
		return type;
	}

	public void setType(ChannelType type) {
		this.type = type;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public boolean isSalable() {
		return salable;
	}

	public void setSalable(boolean salable) {
		this.salable = salable;
	}
}