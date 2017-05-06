package cn.mopon.cec.core.mail.vo;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 异常订单VO。
 */
public class AbnormalOrderVo {
	private String orderCode;
	private String channelName;
	private String cinemaName;
	private String filmName;
	private String cinemaShowCode;
	private String channelShowCode;

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	public AbnormalOrderVo(TicketOrder ticketOrder) {
		this.orderCode = ticketOrder.getCode();
		this.channelName = ticketOrder.getChannel().getName();
		this.cinemaName = ticketOrder.getCinema().getName();
		this.filmName = ticketOrder.getFilm().getName();
		this.cinemaShowCode = ticketOrder.getShowCode();
		this.channelShowCode = ticketOrder.getChannelShowCode();
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getCinemaShowCode() {
		return cinemaShowCode;
	}

	public void setCinemaShowCode(String cinemaShowCode) {
		this.cinemaShowCode = cinemaShowCode;
	}

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String channelShowCode) {
		this.channelShowCode = channelShowCode;
	}
}