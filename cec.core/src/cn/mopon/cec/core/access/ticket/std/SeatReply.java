package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.SeatType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询影厅座位基础信息响应对象。
 */
@XStreamAlias("QuerySeatReply")
public class SeatReply extends StdReply {
	private static XStream xstream;
	@XStreamAlias("Hall")
	private Hall hall;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", StdServiceReply.class);
		xstream.alias("QuerySeatReply", SeatReply.class);
		xstream.alias("QuerySeatReply", StdReply.class, SeatReply.class);
		xstream.aliasField("QuerySeatReply", StdServiceReply.class, "reply");
		xstream.alias("Seat", Seat.class);
		xstream.registerLocalConverter(Seat.class, "status",
				new IEnumConverter(EnabledStatus.class));
		xstream.registerLocalConverter(Seat.class, "type", new IEnumConverter(
				SeatType.class));
	}

	/**
	 * 构造方法。
	 */
	public SeatReply() {
		setId("ID_QuerySeat");
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
