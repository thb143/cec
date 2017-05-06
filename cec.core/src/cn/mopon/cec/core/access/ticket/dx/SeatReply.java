package cn.mopon.cec.core.access.ticket.dx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.SeatType;

import com.thoughtworks.xstream.XStream;

import coo.base.constants.Chars;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 影厅座位响应对象。
 */
public class SeatReply extends DxReply {
	private static XStream xstream;
	private List<Seat> seats = new ArrayList<Seat>();

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", SeatReply.class);
		xstream.alias("data", DxReply.class, SeatReply.class);
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
		Iterator<Seat> iterator = seats.iterator();
		while (iterator.hasNext()) {
			Seat seat = iterator.next();
			if ("0".equals(seat.getRowNum()) && "0".equals(seat.getColNum())) {
				iterator.remove();
			}
			seat.setGroupCode("01");
		}
		Map<String, Seat> map = new HashMap<>();
		for (Seat seat : seats) {
			if (seat.getType() == SeatType.LOVE) {
				if (map.containsKey(seat.getLoveCode())) {
					Seat loveSeat = map.get(seat.getLoveCode());
					StringBuilder sb = new StringBuilder();
					sb.append(loveSeat.getXCoord()).append(Chars.UNDERLINE)
							.append(loveSeat.getYCoord()).append(Chars.UNDERLINE)
							.append(seat.getYCoord());
					seat.setLoveCode(sb.toString());
					loveSeat.setLoveCode(sb.toString());
					map.remove(seat.getLoveCode());
				} else {
					map.put(seat.getLoveCode(), seat);
				}
			}
		}
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

}
