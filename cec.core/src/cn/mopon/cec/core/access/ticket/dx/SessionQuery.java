package cn.mopon.cec.core.access.ticket.dx;

import java.util.Date;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.Cinema;
import coo.base.util.DateUtils;

/**
 * 排期查询对象。
 */
public class SessionQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 */
	public SessionQuery(Cinema cinema, Date start, Date end) {
		end = DateUtils.getNextDay(end);
		setAction("cinema/plays");
		params.add(new BasicNameValuePair("cid", cinema.getCode()));
		params.add(new BasicNameValuePair("start", DateUtils.format(start)));
		params.add(new BasicNameValuePair("end", DateUtils.format(end)));
	}
}
