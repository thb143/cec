package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询订单请求对象。
 */
@XStreamAlias("GetOrderState")
public class OrderQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 合作商方定单号 */
	@XStreamAlias("pSerialNum")
	private String code;
	/** 检验信息 */
	@XStreamAlias("pVerifyInfo")
	protected String verifyInfo;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public OrderQuery(TicketOrder order) {
		this.code = order.getCode();
		this.param = code;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
