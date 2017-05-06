package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 退票响应对象。
 */
@XStreamAlias("BackTicketResult")
public class RefundTicketReply extends MtxReply {
	private static XStream xstream;
	private Boolean refunded;

	static {
		xstream = new GenericXStream();
		xstream.alias("BackTicketResult", RefundTicketReply.class);
		xstream.alias("BackTicketResult", MtxReply.class,
				RefundTicketReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public Boolean getRefunded() {
		return refunded;
	}

	public void setRefunded(Boolean refunded) {
		this.refunded = refunded;
	}
}
