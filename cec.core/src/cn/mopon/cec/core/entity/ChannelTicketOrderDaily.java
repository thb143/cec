package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * 渠道选座票订单日统计。
 */
@Entity
@Table(name = "CEC_ChannelTicketOrderDaily")
@Indexed(index = "ChannelTicketOrderDaily")
public class ChannelTicketOrderDaily extends DailyStatEntity {
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = "id")
	private Channel channel;

	/**
	 * 构造方法。
	 */
	public ChannelTicketOrderDaily() {
	}

	/**
	 * 构造方法。
	 * 
	 * @param channel
	 *            渠道
	 */
	public ChannelTicketOrderDaily(Channel channel) {
		this.channel = channel;
	}

	/**
	 * 构造方法。
	 * 
	 * @param channel
	 *            渠道
	 * @param statDate
	 *            统计日期
	 */
	public ChannelTicketOrderDaily(Channel channel, Date statDate) {
		this.channel = channel;
		this.statDate = statDate;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
