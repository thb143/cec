package cn.mopon.cec.core.access.ticket.yxt;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.SeatType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 影厅座位响应对象。
 */
@XStreamAlias("reply")
public class SeatReply extends YxtReply {
	private static XStream xstream;
	private List<Seat> seats = new ArrayList<Seat>();

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", YxtReply.class, SeatReply.class);
		xstream.alias("seat", Seat.class);
		xstream.registerLocalConverter(Seat.class, "status",
				new IEnumConverter(EnabledStatus.class));
		xstream.registerLocalConverter(Seat.class, "type", new IEnumConverter(
				SeatType.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取座位图。
	 * 
	 * @return 返回座位图。
	 */
	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

}
