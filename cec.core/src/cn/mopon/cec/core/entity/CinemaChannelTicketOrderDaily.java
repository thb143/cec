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
 * 影院渠道选座票订单日统计。
 */
@Entity
@Table(name = "CEC_CinemaChannelTicketOrderDaily")
@Indexed(index = "CinemaChannelTicketOrderDaily")
public class CinemaChannelTicketOrderDaily extends DailyStatEntity {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = "id")
	private Cinema cinema;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = "id")
	private Channel channel;

	/**
	 * 构造方法。
	 */
	public CinemaChannelTicketOrderDaily(){

	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param channel
	 *            渠道
	 */
	public CinemaChannelTicketOrderDaily(Cinema cinema, Channel channel) {
		this.cinema = cinema;
		this.channel = channel;
	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param channel
	 *            渠道
	 * @param statDate
	 *            统计日期
	 */
	public CinemaChannelTicketOrderDaily(Cinema cinema, Channel channel,
			Date statDate) {
		this.cinema = cinema;
		this.channel = channel;
		this.statDate = statDate;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
