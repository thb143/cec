package cn.mopon.cec.core.access.ticket.yxt;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 释放座位响应对象。
 */
@XStreamAlias("reply")
public class ReleaseSeatReply extends YxtReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", YxtReply.class, ReleaseSeatReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}
