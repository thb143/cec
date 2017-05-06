package cn.mopon.cec.core.access.ticket.dx;

import java.util.Iterator;
import java.util.List;

import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

import com.thoughtworks.xstream.XStream;

import coo.base.util.CollectionUtils;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 排期座位响应对象。
 */
public class SessionSeatReply extends DxReply {
	private static XStream xstream;
	private List<ShowSeat> showSeats;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", SessionSeatReply.class);
		xstream.alias("data", DxReply.class, SessionSeatReply.class);
		xstream.alias("showSeat", ShowSeat.class);
		xstream.registerLocalConverter(ShowSeat.class, "status",
				new IEnumConverter(SellStatus.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取指定状态的排期座位。
	 * 
	 * @param status
	 *            座位状态
	 * @return 返回指定状态的排期座位。
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
}
