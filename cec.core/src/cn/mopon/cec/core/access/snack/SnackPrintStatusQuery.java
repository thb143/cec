package cn.mopon.cec.core.access.snack;

import cn.mopon.cec.core.entity.SnackOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查看卖品打印状态请求对象。
 */
@XStreamAliasType("mopon")
public class SnackPrintStatusQuery {
	@XStreamOmitField
	private XStream xstream = new GenericXStream();
	/** 请求报文头 */
	private SnackPrintStatusQueryHead head;
	/** 请求报文体 */
	private SnackPrintStatusQueryBody body;

	/**
	 * 构造方法。
	 * 
	 * @param appkey
	 *            客户端编号
	 * @param password
	 *            客户端密码
	 * @param tradeId
	 *            交易ID
	 * @param order
	 *            卖品订单
	 * @throws Exception
	 *             异常信息
	 */
	public SnackPrintStatusQuery(String appkey, String password,
			String tradeId, SnackOrder order) throws Exception {
		this.head = new SnackPrintStatusQueryHead(appkey, password, tradeId);
		this.body = new SnackPrintStatusQueryBody(order);
	}

	/**
	 * 请求消息转XML。
	 * 
	 * @param query
	 *            请求消息
	 * @return 消息转换后的XML字符串。
	 */
	public String convertXml(SnackPrintStatusQuery query) {
		return xstream.toXML(query);
	}

	public SnackPrintStatusQueryHead getHead() {
		return head;
	}

	public void setHead(SnackPrintStatusQueryHead head) {
		this.head = head;
	}

	public SnackPrintStatusQueryBody getBody() {
		return body;
	}

	public void setBody(SnackPrintStatusQueryBody body) {
		this.body = body;
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}
}