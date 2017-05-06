package cn.mopon.cec.core.access.ticket.dx;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 
 * 查询确认打票响应对象
 * 
 */
public class ConfirmPrintReply extends DxReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}
