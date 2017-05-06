package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.access.ticket.mtx.constants.MTXConstants;
import cn.mopon.cec.core.access.ticket.mtx.vo.SellTicketParameter;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketSettings;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单请求对象。
 */
@XStreamAlias("SellTicket")
public class SubmitOrderQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAlias("pXmlString")
	private String pXmlString;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param settings
	 *            选座票设置
	 */
	public SubmitOrderQuery(TicketOrder order, TicketSettings settings) {
		SellTicketParameter sellTicket = new SellTicketParameter();
		sellTicket.setAppCode(settings.getUsername());
		sellTicket.setFeatureAppNo(order.getShowCode());
		sellTicket.setSerialNum(order.getCode());
		sellTicket.setPrintpassword("");
		sellTicket.setBalance(0);
		sellTicket.setPayType(settings.getAccessType().getParams()
				.get(MTXConstants.PAYTYPE));
		sellTicket.setRecvMobilePhone(order.getMobile());
		sellTicket.setSendType("100");
		sellTicket.setPayResult("0");
		sellTicket.setPayMobile("");
		sellTicket.setBookSign("0");
		sellTicket.setPayed(0);
		sellTicket.setSendModeID("001");
		sellTicket.setPaySeqNo("");
		sellTicket.setTokenId(tokenId);
		param = sellTicket.getFeatureAppNo() + sellTicket.getSerialNum()
				+ sellTicket.getPrintpassword() + sellTicket.getBalance()
				+ sellTicket.getPayType() + sellTicket.getRecvMobilePhone()
				+ sellTicket.getSendType() + sellTicket.getPayResult()
				+ sellTicket.isCmtsPay() + sellTicket.isCmtsSendCode()
				+ sellTicket.getPayMobile() + sellTicket.getBookSign()
				+ sellTicket.getPayed() + sellTicket.getSendModeID()
				+ sellTicket.getPaySeqNo() + tokenId + token;
		sellTicket.setVerifyInfo(genVerifyInfo(settings.getUsername(),
				settings.getPassword(), param));
		pXmlString = getXstream().toXML(sellTicket);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getPXmlString() {
		return pXmlString;
	}

	public void setPXmlString(String pXmlString) {
		this.pXmlString = pXmlString;
	}
}
