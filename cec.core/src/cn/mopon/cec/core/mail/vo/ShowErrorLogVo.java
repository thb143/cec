package cn.mopon.cec.core.mail.vo;

import cn.mopon.cec.core.entity.ShowErrorLog;
import coo.base.util.DateUtils;

/**
 * 排期异常日志VO。
 */
public class ShowErrorLogVo {
	private String showCode;
	private String filmCode;
	private String showTime;
	private String msg;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            异常日志
	 */
	public ShowErrorLogVo(ShowErrorLog log) {
		this.showCode = log.getShowCode();
		this.filmCode = log.getFilmCode();
		this.showTime = DateUtils.format(log.getShowTime(), DateUtils.MINUTE);
		this.msg = log.getMsg();
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}