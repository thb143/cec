package cn.mopon.cec.core.access.ticket.dx;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 释放座位响应对象。
 */
public class ReleaseSeatReply extends DxReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", ReleaseSeatReply.class);
		xstream.alias("data", DxReply.class, ReleaseSeatReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}