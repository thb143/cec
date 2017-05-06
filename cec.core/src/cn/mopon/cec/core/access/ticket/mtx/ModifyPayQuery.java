package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 修改订单价格请求对象。
 */
@XStreamAlias("ModifyOrderPayPrice")
public class ModifyPayQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 订单号 */
	private String pOrderNO;
	/** 票面价 */
	private double pAppPric;
	/** 确认票面价 */
	private double pBalancePric;
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
	 *            订单
	 */
	public ModifyPayQuery(TicketOrder order) {
		pOrderNO = order.getCinemaOrderCode();
		pAppPric = order.getOrderItems().get(0).getSubmitPrice();
		pBalancePric = order.getOrderItems().get(0).getSubmitPrice();
		this.param = pOrderNO + pAppPric + pBalancePric + tokenId + token;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getPOrderNO() {
		return pOrderNO;
	}

	public void setPOrderNO(String pOrderNO) {
		this.pOrderNO = pOrderNO;
	}

	public double getPAppPric() {
		return pAppPric;
	}

	public void setPAppPric(double pAppPric) {
		this.pAppPric = pAppPric;
	}

	public double getPBalancePric() {
		return pBalancePric;
	}

	public void setPBalancePric(double pBalancePric) {
		this.pBalancePric = pBalancePric;
	}

	public void setTokenId(String tokenIds) {
		this.tokenIds = tokenIds;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}