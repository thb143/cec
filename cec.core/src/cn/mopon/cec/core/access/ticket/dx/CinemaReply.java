package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.enums.HallType;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 影院响应对象。
 */
public class CinemaReply extends DxReply {
	private static XStream xstream;
	private Cinema cinema;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("cinema", Cinema.class);
		xstream.alias("hall", Hall.class);
		xstream.registerLocalConverter(Hall.class, "type", new IEnumConverter(
				HallType.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}
}