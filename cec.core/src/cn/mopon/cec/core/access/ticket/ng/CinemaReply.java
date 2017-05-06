package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.enums.HallType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询影院基础信息响应对象。
 */
@XStreamAlias("QueryCinemaReply")
public class CinemaReply extends NgReply {
	private static XStream xstream;
	@XStreamAlias("Cinema")
	private Cinema cinema;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("QueryCinemaReply", CinemaReply.class);
		xstream.alias("QueryCinemaReply", NgReply.class, CinemaReply.class);
		xstream.aliasField("QueryCinemaReply", NgServiceReply.class, "reply");

		xstream.alias("Cinema", Cinema.class);
		xstream.alias("Hall", Hall.class);
		xstream.registerLocalConverter(Hall.class, "type", new IEnumConverter(
				HallType.class));
	}

	/**
	 * 构造方法。
	 */
	public CinemaReply() {
		setId("ID_QueryCinemaReply");
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
