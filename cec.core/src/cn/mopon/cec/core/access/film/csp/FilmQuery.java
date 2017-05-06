package cn.mopon.cec.core.access.film.csp;

import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import coo.base.util.DateUtils;

/**
 * 查询影片请求对象。
 */
public class FilmQuery extends CspQuery {
	/** 起始日期 */
	private String start;
	/** 截止日期 */
	private String end;

	/**
	 * 构造方法。
	 * 
	 * @param account
	 *            账号
	 * @param key
	 *            密钥
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 */
	public FilmQuery(String account, String key, Date start, Date end) {
		List<NameValuePair> nvps = getNvps();
		nvps.add(new BasicNameValuePair("account", account));
		nvps.add(new BasicNameValuePair("key", key));
		nvps.add(new BasicNameValuePair("start", DateUtils.format(start,
				DateUtils.DAY)));
		nvps.add(new BasicNameValuePair("end", DateUtils.format(end,
				DateUtils.DAY)));
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}