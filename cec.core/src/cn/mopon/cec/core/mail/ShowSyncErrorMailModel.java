package cn.mopon.cec.core.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ShowErrorLog;
import cn.mopon.cec.core.entity.ShowSyncLog;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.ShowErrorType;
import cn.mopon.cec.core.mail.vo.ShowErrorLogVo;

/**
 * 同步排期数据异常通知。
 */
public class ShowSyncErrorMailModel extends MailModel {
	private Date syncTime;
	private String cinemaName;
	private ShowErrorType errorType;
	private List<ShowErrorLogVo> errorLogs = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param showSyncLog
	 *            排期同步日志
	 * @param showErrorType
	 *            排期异常类型
	 * @param showErrorLogs
	 *            排期异常列表
	 * @param users
	 *            邮件接收用户
	 */
	public ShowSyncErrorMailModel(ShowSyncLog showSyncLog,
			ShowErrorType showErrorType, List<ShowErrorLog> showErrorLogs,
			List<User> users) {
		super(users);
		templateName = "show-sync-error-mail.ftl";
		subject = "同步排期-" + showErrorType.toString() + "通知";
		errorType = showErrorType;
		this.syncTime = showSyncLog.getSyncTime();
		this.cinemaName = showSyncLog.getCinema().getName();
		for (ShowErrorLog showErrorLog : showErrorLogs) {
			this.errorLogs.add(new ShowErrorLogVo(showErrorLog));
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

	public ShowErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ShowErrorType errorType) {
		this.errorType = errorType;
	}

	public List<ShowErrorLogVo> getErrorLogs() {
		return errorLogs;
	}

	public void setErrorLogs(List<ShowErrorLogVo> errorLogs) {
		this.errorLogs = errorLogs;
	}
}