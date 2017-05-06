package cn.mopon.cec.core.access.ticket.hfh;

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
@XStreamAlias("data")
public class SessionReply extends HfhReply {
	private static XStream xstream;
	@XStreamAlias("shows")
	private List<Show> shows = new ArrayList<>();

	static {
		xstream = new GenericXStream();
		xstream.alias("data", CinemaReply.class);
		xstream.alias("data", HfhReply.class, CinemaReply.class);

		xstream.alias("Show", Show.class);
		xstream.registerLocalConverter(Show.class, "showType",
				new IEnumConverter(ShowType.class));
		xstream.registerLocalConverter(Show.class, "showTime",
				new DateConverter("yyyy-MM-dd-HHmm"));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取排期。
	 * 
	 * @return 返回排期列表。
	 */
	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}
}