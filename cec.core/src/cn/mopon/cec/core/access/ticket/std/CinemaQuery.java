package cn.mopon.cec.core.access.ticket.std;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询影院基础信息请求对象。
 */
@XStreamAlias("QueryCinema")
public class CinemaQuery extends StdQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String code;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", StdServiceQuery.class);
		xstream.alias("QueryCinema", CinemaQuery.class);
		xstream.alias("QueryCinema", StdQuery.class, CinemaQuery.class);
		xstream.aliasField("QueryCinema", StdServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param code
	 *            影院编码
	 */
	public CinemaQuery(String code) {
		setId("ID_QueryCinema");
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}