package cn.mopon.cec.core.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.mail.vo.ErrorHallVo;

/**
 * 同步排期数据异常通知。
 */
public class SeatEmptyMailModel extends MailModel {
	private Date syncTime;
	private String cinemaName;
	private List<ErrorHallVo> errorHalls = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param cinemaName
	 *            影院名称
	 * @param noSeatHalls
	 *            无座位影列表
	 * @param users
	 *            邮件接收用户
	 */
	public SeatEmptyMailModel(String cinemaName, List<Hall> noSeatHalls,
			List<User> users) {
		super(users);
		templateName = "seat-empty-mail.ftl";
		subject = "同步影厅数据异常通知";
		this.syncTime = new Date();
		this.cinemaName = cinemaName;
		for (Hall hall : noSeatHalls) {
			this.errorHalls.add(new ErrorHallVo(hall));
		}
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public List<ErrorHallVo> getErrorHalls() {
		return errorHalls;
	}

	public void setErrorHalls(List<ErrorHallVo> errorHalls) {
		this.errorHalls = errorHalls;
	}
}