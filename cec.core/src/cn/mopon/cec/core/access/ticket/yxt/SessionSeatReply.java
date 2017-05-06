package cn.mopon.cec.core.access.ticket.yxt;

import java.util.List;

import cn.mopon.cec.core.enums.SeatType;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 排期座位响应对象。
 */
@XStreamAlias("reply")
public class SessionSeatReply extends YxtReply {
	private static XStream xstream;
	@XStreamAlias("seats")
	private List<ShowSeat> showSeats;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", SessionSeatReply.class);
		xstream.alias("reply", YxtReply.class, SessionSeatReply.class);
		xstream.alias("seat", ShowSeat.class);
		xstream.registerLocalConverter(ShowSeat.class, "status",
				new IEnumConverter(SellStatus.class));
		xstream.registerLocalConverter(ShowSeat.class, "type",
				new IEnumConverter(SeatType.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public List<ShowSeat> getShowSeats() {
		return showSeats;
	}

	public void setShowSeats(List<ShowSeat> showSeats) {
		this.showSeats = showSeats;
	}

}