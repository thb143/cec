package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.ChannelShow;

/**
 * 查询不可售座位信息请求对象。
 */
public class SessionSeatQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	public SessionSeatQuery(ChannelShow channelShow) {
		setMethod("qryTicket");
		setNeedCinemaParams(true);
		addParam("hallId", channelShow.getHall().getCode());
		addParam("sectionId", channelShow.getHall().getSeats().get(0)
				.getGroupCode());
		addParam("filmId", channelShow.getFilmCode());
		genShowParams(channelShow.getShowCode(), channelShow.getShowTime());
	}
}