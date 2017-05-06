package cn.mopon.cec.core.access.ticket.ngc;

import com.thoughtworks.xstream.XStream;
import coo.core.xstream.GenericXStream;

/**
 * 查询订单响应对象。
 */
public class ConfirmPrintReply extends NgcReply {

	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", ConfirmPrintReply.class);
		xstream.alias("reply", NgcReply.class, ConfirmPrintReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}
