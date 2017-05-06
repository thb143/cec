package cn.mopon.cec.core.access.ticket.yxt;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShowStatus;
import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.DateConverter;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 获取影片排期的返回对象。
 */
public class SessionReply extends YxtReply {
	private static XStream xstream;
	@XStreamAlias("shows")
	private List<Show> shows = new ArrayList<Show>();

	static {
		xstream = new GenericXStream();
		xstream.alias("data", SessionReply.class);
		xstream.alias("data", YxtReply.class, SessionReply.class);
		xstream.alias("show", Show.class);
		xstream.registerLocalConverter(Show.class, "showType",
				new IEnumConverter(ShowType.class));
		xstream.registerLocalConverter(Show.class, "status",
				new IEnumConverter(ShowStatus.class));
		xstream.registerLocalConverter(Show.class, "showTime",
				new DateConverter("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

}
