package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ShowSyncLog;
import cn.mopon.cec.core.entity.User;

/**
 * 同步排期失败通知。
 */
public class ShowSyncFailedMailModel extends MailModel {
	private Date syncTime;
	private String cinemaName;
	private String msg;

	/**
	 * 构造方法。
	 * 
	 * @param showSyncLog
	 *            排期同步日志
	 * @param users
	 *            邮件接收用户
	 */
	public ShowSyncFailedMailModel(ShowSyncLog showSyncLog, List<User> users) {
		super(users);
		templateName = "show-sync-failed-mail.ftl";
		subject = "同步排期失败通知";
		this.syncTime = showSyncLog.getSyncTime();
		this.cinemaName = showSyncLog.getCinema().getName();
		this.msg = showSyncLog.getMsg();
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}