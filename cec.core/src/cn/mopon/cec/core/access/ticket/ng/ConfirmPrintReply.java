package cn.mopon.cec.core.access.ticket.ng;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 确认打票响应对象。
 */
@XStreamAlias("DTakeTicketConfirmReply")
public class ConfirmPrintReply extends NgReply {
	@XStreamOmitField
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("DTakeTicketConfirmReply", ConfirmPrintReply.class);
		xstream.alias("DTakeTicketConfirmReply", NgReply.class,
				ConfirmPrintReply.class);
		xstream.aliasField("DTakeTicketConfirmReply", NgServiceReply.class,
				"reply");
	}

	/**
	 * 构造方法。
	 */
	public ConfirmPrintReply() {
		setId("ID_DTakeTicketConfirmReply");
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
