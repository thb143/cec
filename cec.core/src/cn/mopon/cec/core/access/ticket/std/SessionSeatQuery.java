package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.access.ticket.std.enums.SessionSeatStatus;
import cn.mopon.cec.core.entity.ChannelShow;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询放映计划座位售出状态请求对象。
 */
@XStreamAlias("QuerySessionSeat")
public class SessionSeatQuery extends StdQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	@XStreamAsAttribute
	@XStreamAlias("SessionCode")
	private String sessionCode;
	@XStreamAsAttribute
	@XStreamAlias("Status")
	@XStreamConverter(IEnumConverter.class)
	private SessionSeatStatus status;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", StdServiceQuery.class);

		xstream.alias("QuerySessionSeat", SessionSeatQuery.class);
		xstream.alias("QuerySessionSeat", StdQuery.class,
				SessionSeatQuery.class);
		xstream.aliasField("QuerySessionSeat", StdServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	public SessionSeatQuery(ChannelShow channelShow) {
		setId("ID_QuerySessionSeat");
		cinemaCode = channelShow.getCinema().getCode();
		sessionCode = channelShow.getShowCode();
		status = SessionSeatStatus.ALL;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public SessionSeatStatus getStatus() {
		return status;
	}

	public void setStatus(SessionSeatStatus status) {
		this.status = status;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}