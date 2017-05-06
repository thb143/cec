package cn.mopon.cec.core.access.ticket.yxt;

import com.thoughtworks.xstream.XStream;
import coo.core.xstream.GenericXStream;

/**
 * 退票响应对象。
 */
public class RevokeReply extends YxtReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", RevokeReply.class);
		xstream.alias("reply", YxtReply.class, RevokeReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

}
