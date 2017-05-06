package cn.mopon.cec.core.access.ticket.hfh;

import java.util.List;

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
@XStreamAlias("data")
public class SeatReply extends HfhReply {
	private static XStream xstream;
	@XStreamAlias("Hall")
	private Hall hall;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", SeatReply.class);
		xstream.alias("data", HfhReply.class, SeatReply.class);
		xstream.alias("Hall", Seat.class);
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

	/**
	 * 获取座位。
	 * 
	 * @return 返回转换后的座位列表。
	 */
	public List<Seat> getSeats() {
		List<Seat> seats = hall.getSeats();
		for (Seat seat : seats) {
			switch (seat.getLoveCode()) {
			case "L":
				seat.setLoveCode(seat.getXCoord() + "_" + seat.getYCoord()
						+ "_" + (seat.getYCoord() + 1));
				break;
			case "R":
				seat.setLoveCode(seat.getXCoord() + "_"
						+ (seat.getYCoord() - 1) + "_" + seat.getYCoord());
				break;
			default:
				seat.setLoveCode(null);
				break;
			}
		}
		return seats;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}
}
