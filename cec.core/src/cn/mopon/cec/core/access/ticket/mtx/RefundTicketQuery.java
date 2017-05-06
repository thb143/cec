package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 退票请求对象。
 */
@XStreamAlias("BackTicket")
public class RefundTicketQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 订单号 */
	@XStreamAlias("pOrderNO")
	private String orderNo;
	/** 退票原因 */
	@XStreamAlias("pDesc")
	private String desc = "";
	/** 令牌ID */
	@XStreamAlias("pTokenID")
	protected String tokenIds;
	/** 检验信息 */
	@XStreamAlias("pVerifyInfo")
	protected String verifyInfo;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 */
	public RefundTicketQuery(TicketOrder ticketOrder) {
		this.orderNo = ticketOrder.getCinemaOrderCode();
		this.param = orderNo + tokenId + token;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
