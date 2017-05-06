package cn.mopon.cec.core.access.ticket.ngc;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 释放座位响应对象。
 */
@XStreamAlias("reply")
public class ReleaseSeatReply extends NgcReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", NgcReply.class, ReleaseSeatReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}
