package cn.mopon.cec.core.access.ticket.hfh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.CollectionUtils;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询放映计划座位售出状态响应对象。
 */
@XStreamAlias("data")
public class SessionSeatReply extends HfhReply {
	private static XStream xstream;
	@XStreamAlias("showSeats")
	private List<ShowSeat> showSeats;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", CinemaReply.class);
		xstream.alias("data", HfhReply.class, CinemaReply.class);

		xstream.alias("ShowSeat", ShowSeat.class);
		xstream.registerLocalConverter(ShowSeat.class, "status",
				new IEnumConverter(SellStatus.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取排期座位。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param status
	 *            状态
	 * @return 返回指定状态的排期座位列表。
	 */
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		List<ShowSeat> showSeatList = new ArrayList<ShowSeat>();
		for (Seat seat : channelShow.getHall().getSeats()) {
			ShowSeat sessionSeat = ShowSeat.createBySeat(seat);
			for (ShowSeat showSeat : showSeats) {
				if (seat.getCode().equals(showSeat.getCode())) {
					sessionSeat.setStatus(SellStatus.DISABLED);
				}
			}
			showSeatList.add(sessionSeat);
		}
		Iterator<ShowSeat> showSeatIterator = showSeatList.iterator();
		if (CollectionUtils.isNotEmpty(status)) {
			while (showSeatIterator.hasNext()) {
				if (!CollectionUtils.contains(status, showSeatIterator.next()
						.getStatus())) {
					showSeatIterator.remove();
				}
			}
		}
		return showSeatList;
	}

	public void setShowSeats(List<ShowSeat> showSeats) {
		this.showSeats = showSeats;
	}
}