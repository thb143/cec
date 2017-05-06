package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 
 * 查询订单打印状态请求对象。
 */
@XStreamAlias("OnlineTicketingServiceQuery")
public class PrintStatusQuery extends NgQuery {
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
		xstream.alias("DQueryTakeTicketInfo", NgServiceQuery.class);
		xstream.alias("DQueryTakeTicketInfo", NgQuery.class,
				PrintStatusQuery.class);
		xstream.aliasField("DQueryTakeTicketInfo", NgServiceQuery.class,
				"query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单对象。
	 */
	public PrintStatusQuery(TicketOrder ticketOrder) {
		setId("ID_DQueryTakeTicketInfo");
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
