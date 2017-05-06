package cn.mopon.cec.core.access.ticket.mtx;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Cinema;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 查询影院基础信息响应对象。
 */

@XStreamAlias("GetCinemaResult")
public class CinemaReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("cinemas")
	private List<Cinema> cinemas = new ArrayList<Cinema>();
	@XStreamAlias("ResultCode")
	private String resultCode;

	static {
		xstream = new GenericXStream();
		xstream.alias("GetCinemaResult", CinemaReply.class);
		xstream.alias("GetCinemaResult", MtxReply.class, CinemaReply.class);

		xstream.alias("cinema", Cinema.class);
		xstream.alias("ResultCode", String.class);
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
		return null;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public List<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}
}
