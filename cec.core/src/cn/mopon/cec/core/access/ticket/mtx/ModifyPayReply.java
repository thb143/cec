package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 修改订单价格响应对象。
 */
public class ModifyPayReply extends MtxReply {
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
		xstream.alias("ModifyOrderPayPriceResult", ModifyPayReply.class);
		xstream.alias("ModifyOrderPayPriceResult", MtxReply.class,
				ModifyPayReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}
}
