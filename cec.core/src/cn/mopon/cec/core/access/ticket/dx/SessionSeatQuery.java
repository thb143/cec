package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.ChannelShow;

/**
 * 排期座位请求对象。
 */
public class SessionSeatQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	public SessionSeatQuery(ChannelShow channelShow) {
		setAction("play/seat-status");
		String showCode = channelShow.getShowCode();
		params.add(new BasicNameValuePair("cid", channelShow.getCinema()
				.getCode()));
		params.add(new BasicNameValuePair("play_id", getPlayId(showCode)));
		params.add(new BasicNameValuePair("play_update_time",
				getUpdateTime(showCode)));
	}
}
