package cn.mopon.cec.core.access.ticket.hfh;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.exception.BusinessException;
import coo.core.xstream.GenericXStream;

/**
 * 查询影院基础信息响应对象。
 */
@XStreamAlias("data")
public class CinemaReply extends HfhReply {
	private static XStream xstream;
	@XStreamAlias("cinemas")
	private List<Cinema> cinemas = new ArrayList<Cinema>();
	@XStreamAlias("hall")
	private List<Hall> halls = new ArrayList<Hall>();

	static {
		xstream = new GenericXStream();
		xstream.alias("data", CinemaReply.class);
		xstream.alias("data", HfhReply.class, CinemaReply.class);

		xstream.alias("cinema", Cinema.class);
		xstream.alias("hall", Hall.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 根据影院编码获取影院信息。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @return 影院信息
	 */
	public Cinema getCinema(String cinemaCode) {
		for (Cinema cinema : cinemas) {
			if (cinemaCode.equals(cinema.getCode())) {
				return cinema;
			}
		}
		throw new BusinessException("影院编码不存在。");
	}

	public List<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}
}
