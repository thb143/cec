package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 
 * 确认打票请求对象。
 */
@XStreamAlias("DTakeTicketConfirm")
public class ConfirmPrintQuery extends NgQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 影院编码 */
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	/** 取票号 */
	@XStreamAlias("PrintNo")
	private String printNo;
	/** 取票验证码 */
	@XStreamAlias("VerifyCode")
	private String verifyCode;

	static {
		xstream = new GenericXStream();
		xstream.alias("DTakeTicketConfirm", NgServiceQuery.class);
		xstream.alias("DTakeTicketConfirm", NgQuery.class,
				ConfirmPrintQuery.class);
		xstream.aliasField("DTakeTicketConfirm", NgServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单对象。
	 */
	public ConfirmPrintQuery(TicketOrder ticketOrder) {
		setId("ID_DTakeTicketConfirm");
		this.cinemaCode = ticketOrder.getCinema().getCode();
		setPrintNo(ticketOrder.getVoucher().getPrintCode());
		setVerifyCode(ticketOrder.getVoucher().getVerifyCode());
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getPrintNo() {
		return printNo;
	}

	public void setPrintNo(String takeNo) {
		this.printNo = takeNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String takeVerifyCode) {
		this.verifyCode = takeVerifyCode;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
