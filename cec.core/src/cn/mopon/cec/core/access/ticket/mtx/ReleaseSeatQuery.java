package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 释放座位请求对象。
 */
@XStreamAlias("UnLockOrder")
public class ReleaseSeatQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 订单号 */
	@XStreamAlias("pOrderNO")
	protected String orderCode;
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
	 * @param order
	 *            选座票订单
	 */
	public ReleaseSeatQuery(TicketOrder order) {
		this.orderCode = order.getCinemaOrderCode();
		this.param = orderCode + tokenId + token;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
