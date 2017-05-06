package cn.mopon.cec.core.access.ticket.ng;

import java.util.Date;

import cn.mopon.cec.core.entity.Cinema;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.DateConverter;
import coo.core.xstream.GenericXStream;

/**
 * 查询电影院放映计划信息请求对象。
 */
@XStreamAlias("DQuerySession")
public class SessionQuery extends NgQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	@XStreamAsAttribute
	@XStreamAlias("StartDate")
	@XStreamConverter(value = DateConverter.class, strings = { "yyyy-MM-dd" })
	private Date startDate;
	@XStreamAsAttribute
	@XStreamAlias("EndDate")
	@XStreamConverter(value = DateConverter.class, strings = { "yyyy-MM-dd" })
	private Date endDate;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", NgServiceQuery.class);
		xstream.alias("DQuerySession", SessionQuery.class);
		xstream.alias("DQuerySession", NgQuery.class, SessionQuery.class);
		xstream.aliasField("DQuerySession", NgServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            截止日期
	 */
	public SessionQuery(Cinema cinema, Date startDate, Date endDate) {
		setId("ID_DQuerySession");
		this.cinemaCode = cinema.getCode();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
