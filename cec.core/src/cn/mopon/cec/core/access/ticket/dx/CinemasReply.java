package cn.mopon.cec.core.access.ticket.dx;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Cinema;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 影院列表响应对象。
 */
public class CinemasReply extends DxReply {
	private static XStream xstream;
	private List<Cinema> cinemas = new ArrayList<Cinema>();

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("cinema", Cinema.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public List<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}
}