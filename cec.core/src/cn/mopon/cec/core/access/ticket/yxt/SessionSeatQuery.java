package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.enums.SellStatus;
import coo.base.constants.Chars;
import coo.base.util.CollectionUtils;

/**
 * 排期座位请求对象。
 */
public class SessionSeatQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param status
	 *            可售状态
	 */
	public SessionSeatQuery(ChannelShow channelShow, SellStatus... status) {
		setAction("queryShowSeats.xml");
		addParam("channelShowCode", channelShow.getShowCode());
		addParam("cinemaCode", channelShow.getCinema().getCode());
		addParam("hallCode", channelShow.getHall().getCode());
		if (CollectionUtils.isNotEmpty(status)) {
			addParam("status", getSellStatus(status));
		}
	}

	/**
	 * 获取座位可售状态。
	 * 
	 * @param status
	 *            可售状态
	 * @return 返回座位可售状态。
	 */
	public String getSellStatus(SellStatus... status) {
		StringBuilder sb = new StringBuilder();
		for (SellStatus s : status) {
			if (sb.length() > 0) {
				sb.append(Chars.COMMA);
			}
			sb.append(s.getValue());
		}
		return sb.toString();
	}
}
