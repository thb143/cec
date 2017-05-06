package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 查询打票状态响应对象。
 */
@XStreamAlias("AppPrintTicketResult")
public class ConfirmPrintReply extends MtxReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("AppPrintTicketResult", ConfirmPrintReply.class);
		xstream.alias("AppPrintTicketResult", MtxReply.class,
				ConfirmPrintReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}