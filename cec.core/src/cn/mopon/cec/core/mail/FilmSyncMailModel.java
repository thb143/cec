package cn.mopon.cec.core.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.User;

/**
 * 同步影片数据新增通知。
 */
public class FilmSyncMailModel extends MailModel {
	private Date syncTime;
	private List<Film> films = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param films
	 *            新增影片列表
	 * @param syncTime
	 *            同步时间
	 * @param users
	 *            邮件接收用户
	 */
	public FilmSyncMailModel(List<Film> films, Date syncTime, List<User> users) {
		super(users);
		templateName = "film-sync-mail.ftl";
		subject = "同步影片数据新增通知";
		this.syncTime = syncTime;
		this.films = films;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public List<Film> getFilms() {
		return films;
	}

	public void setFilms(List<Film> films) {
		this.films = films;
	}
}