package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.FilmSyncLog;
import cn.mopon.cec.core.entity.User;

/**
 * 同步影片失败通知。
 */
public class FilmSyncFailedMailModel extends MailModel {
	private Date syncTime;
	private String msg;

	/**
	 * 构造方法。
	 * 
	 * @param filmSyncLog
	 *            排期同步日志
	 * @param users
	 *            邮件接收用户
	 */
	public FilmSyncFailedMailModel(FilmSyncLog filmSyncLog, List<User> users) {
		super(users);
		templateName = "film-sync-failed-mail.ftl";
		subject = "同步影片失败通知";
		this.syncTime = filmSyncLog.getSyncTime();
		this.msg = filmSyncLog.getMsg();
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}