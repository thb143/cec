package cn.mopon.cec.core.mail;

import java.util.List;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.User;
import coo.base.util.ThrowableUtils;

/**
 * 异常订单处理失败通知。
 */
public class AbnormalOrderProcessFailedMailModel extends MailModel {
	private String orderCode;
	private String channelName;
	private String cinemaName;
	private String filmName;
	private String cinemaShowCode;
	private String channelShowCode;
	private String cause;

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @param ex
	 *            异常
	 * @param users
	 *            邮件接收用户
	 */
	public AbnormalOrderProcessFailedMailModel(TicketOrder ticketOrder,
			Exception ex, List<User> users) {
		super(users);
		templateName = "abnormal-order-process-failed-mail.ftl";
		subject = "异常订单处理失败通知";
		this.orderCode = ticketOrder.getCode();
		this.channelName = ticketOrder.getChannel().getName();
		this.cinemaName = ticketOrder.getCinema().getName();
		this.filmName = ticketOrder.getFilm().getName();
		this.cinemaShowCode = ticketOrder.getShowCode();
		this.channelShowCode = ticketOrder.getChannelShowCode();
		this.cause = ThrowableUtils.getStackTrace(ex);
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

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}