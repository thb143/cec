package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.Hall;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询影厅座位信息请求对象。
 */
@XStreamAlias("QuerySeat")
public class SeatQuery extends NgQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;

	@XStreamAsAttribute
	@XStreamAlias("ScreenCode")
	private String screenCode;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", NgServiceQuery.class);
		xstream.alias("QuerySeat", NgQuery.class, SeatQuery.class);
		xstream.aliasField("QuerySeat", NgServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param hall
	 *            影厅
	 */
	public SeatQuery(Hall hall) {
		setId("ID_QuerySeat");
		cinemaCode = hall.getCinema().getCode();
		screenCode = hall.getCode();
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getScreenCode() {
		return screenCode;
	}

	public void setScreenCode(String screenCode) {
		this.screenCode = screenCode;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
