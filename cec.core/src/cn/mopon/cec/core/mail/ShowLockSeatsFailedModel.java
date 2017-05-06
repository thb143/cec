package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.User;
import coo.base.util.ThrowableUtils;

/**
 * 锁座失败通知。
 */
public class ShowLockSeatsFailedModel extends MailModel {
	private String channelName;
	private String cinemaName;
	private String filmName;
	private String cinemaShowCode;
	private String channelShowCode;
	private Date showTime;
	private String cause;

	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param ex
	 *            异常
	 * @param users
	 *            邮件接收用户
	 */
	public ShowLockSeatsFailedModel(ChannelShow channelShow, Exception ex,
			List<User> users) {
		super(users);
		templateName = "show-lockSeats-failed-mail.ftl";
		subject = "排期锁座失败通知";
		this.channelName = channelShow.getChannel().getName();
		this.cinemaName = channelShow.getCinema().getName();
		this.filmName = channelShow.getFilm().getName();
		this.cinemaShowCode = channelShow.getShowCode();
		this.channelShowCode = channelShow.getCode();
		this.showTime = channelShow.getShowTime();
		this.cause = ThrowableUtils.getStackTrace(ex);
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

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}