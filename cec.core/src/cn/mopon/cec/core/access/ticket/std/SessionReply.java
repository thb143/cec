package cn.mopon.cec.core.access.ticket.std;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.DateConverter;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询电影院放映计划信息响应对象。
 */
@XStreamAlias("QuerySessionReply")
public class SessionReply extends StdReply {
	private static XStream xstream;
	@XStreamAlias("shows")
	private List<Show> shows = new ArrayList<>();

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", StdServiceReply.class);
		xstream.alias("QuerySessionReply", SessionReply.class);
		xstream.alias("QuerySessionReply", StdReply.class, SessionReply.class);
		xstream.aliasField("QuerySessionReply", StdServiceReply.class, "reply");

		xstream.alias("Show", Show.class);
		xstream.registerLocalConverter(Show.class, "showType",
				new IEnumConverter(ShowType.class));
		xstream.registerLocalConverter(Show.class, "showTime",
				new DateConverter("yyyy-MM-dd'T'HH:mm:ss"));
	}

	/**
	 * 构造方法。
	 */
	public SessionReply() {
		setId("ID_QuerySessionReply");
	}

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}