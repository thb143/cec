package cn.mopon.cec.core.access.ticket.yxt;

import com.thoughtworks.xstream.XStream;
import coo.core.xstream.GenericXStream;

/**
 * 查询订单响应对象。
 */
public class ConfirmPrintReply extends YxtReply {

	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", ConfirmPrintReply.class);
		xstream.alias("reply", YxtReply.class, ConfirmPrintReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}
