package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.Cinema;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 查询影院基础信息响应对象。
 */
@XStreamAlias("reply")
public class CinemaReply extends YxtReply {
	private static XStream xstream;

	@XStreamAlias("cinema")
	private Cinema cinema;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", YxtReply.class, CinemaReply.class);
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
