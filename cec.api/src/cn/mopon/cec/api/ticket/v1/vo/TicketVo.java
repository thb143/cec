package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.PrintStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 影票。
 */
@XStreamAlias("ticket")
public class TicketVo {
	/** 影票编码 */
	private String ticketCode = "";
	/** 座位编码 */
	private String seatCode = "";
	/** 座位分组编码 */
	private String seatGroupCode = "";
	/** 行号 */
	private String rowNum = "";
	/** 列号 */
	private String colNum = "";
	/** 二维码 */
	private String barCode = "";
	/** 票价 */
	private Double price = 0D;
	/** 服务费 */
	private Double serviceFee = 0D;
	/** 打印状态 */
	@XStreamConverter(value = IEnumConverter.class, types = PrintStatus.class)
	private PrintStatus printStatus;

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrderitem
	 *            订单明细
	 */
	public TicketVo(TicketOrderItem ticketOrderitem) {
		ticketCode = ticketOrderitem.getTicketCode();
		seatCode = ticketOrderitem.getSeatCode();
		seatGroupCode = ticketOrderitem.getSeatGroupCode();
		rowNum = ticketOrderitem.getSeatRow();
		colNum = ticketOrderitem.getSeatCol();
		price = ticketOrderitem.getSubmitPrice();
		serviceFee = ticketOrderitem.getServiceFee();
		printStatus = ticketOrderitem.getPrintStatus();
		barCode = ticketOrderitem.getBarCode();
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getSeatCode() {
		return seatCode;
	}

	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}

	public String getSeatGroupCode() {
		return seatGroupCode;
	}

	public void setSeatGroupCode(String seatGroupCode) {
		this.seatGroupCode = seatGroupCode;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getColNum() {
		return colNum;
	}

	public void setColNum(String colNum) {
		this.colNum = colNum;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public PrintStatus getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(PrintStatus printStatus) {
		this.printStatus = printStatus;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
}