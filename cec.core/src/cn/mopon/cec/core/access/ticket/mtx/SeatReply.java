package cn.mopon.cec.core.access.ticket.mtx;

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
@XStreamAlias("GetHallSiteResult")
public class SeatReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("Hall")
	private Hall hall;

	static {
		xstream = new GenericXStream();
		xstream.alias("GetHallSiteResult", SeatReply.class);
		xstream.alias("GetHallSiteResult", MtxReply.class, SeatReply.class);
		xstream.alias("Seat", Seat.class);
		xstream.registerLocalConverter(Seat.class, "status",
				new IEnumConverter(EnabledStatus.class));
		xstream.registerLocalConverter(Seat.class, "type", new IEnumConverter(
				SeatType.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}
}