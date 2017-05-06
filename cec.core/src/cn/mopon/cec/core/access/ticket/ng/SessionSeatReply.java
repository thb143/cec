package cn.mopon.cec.core.access.ticket.ng;

import java.util.Iterator;
import java.util.List;

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
@XStreamAlias("QuerySessionSeatReply")
public class SessionSeatReply extends NgReply {
	private static XStream xstream;
	@XStreamAlias("showSeats")
	private List<ShowSeat> showSeats;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("QuerySessionSeatReply", NgReply.class,
				SessionSeatReply.class);
		xstream.aliasField("QuerySessionSeatReply", NgServiceReply.class,
				"reply");

		xstream.alias("ShowSeat", ShowSeat.class);
		xstream.registerLocalConverter(ShowSeat.class, "status",
				new IEnumConverter(SellStatus.class));
	}

	/**
	 * 构造方法。
	 */
	public SessionSeatReply() {
		setId("ID_QuerySessionSeatReply");
	}

	/**
	 * 获取指定状态的排期座位列表，如果未指定状态则返回全部排期座位列表。
	 * 
	 * @param status
	 *            排期座位状态
	 * @return 返回指定状态的排期座位列表。
	 */
	public List<ShowSeat> getShowSeats(SellStatus... status) {
		Iterator<ShowSeat> showSeatIterator = showSeats.iterator();
		if (CollectionUtils.isNotEmpty(status)) {
			while (showSeatIterator.hasNext()) {
				if (!CollectionUtils.contains(status, showSeatIterator.next()
						.getStatus())) {
					showSeatIterator.remove();
				}
			}
		}
		return showSeats;
	}

	public void setShowSeats(List<ShowSeat> showSeats) {
		this.showSeats = showSeats;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}