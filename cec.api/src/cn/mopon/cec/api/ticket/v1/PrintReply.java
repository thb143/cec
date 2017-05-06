package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.SnackOrderVo;
import cn.mopon.cec.api.ticket.v1.vo.TicketVo;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.DateConverter;

/**
 * 查询响应对象。
 */
public class PrintReply extends ApiReply {
	/** 影院编码 */
	private String cinemaCode = "";
	/** 影院名称 */
	private String cinemaName = "";
	/** 影厅编码 */
	private String hallCode = "";
	/** 影厅名称 */
	private String hallName = "";
	/** 影片编码 */
	private String filmCode = "";
	/** 影片名称 */
	private String filmName = "";
	/** 影院场次编码 */
	private String cinemaShowCode = "";
	/** 渠道场次编码 */
	private String channelShowCode = "";
	/** 放映时间 */
	@XStreamConverter(value = DateConverter.class)
	private Date showTime;
	/** 重打印次数 */
	private Integer reprintCount = 0;
	/** 地面取票号 */
	private String printCode = "";
	/** 取票验证码 */
	private String verifyCode = "";
	/** 影票列表 */
	private List<TicketVo> tickets = new ArrayList<TicketVo>();
	/** 卖品列表 */
	private List<SnackOrderVo> snacks = new ArrayList<SnackOrderVo>();

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	public PrintReply(TicketOrder ticketOrder) {
		cinemaCode = ticketOrder.getCinema().getCode();
		cinemaName = ticketOrder.getCinema().getName();
		hallCode = ticketOrder.getHall().getCode();
		hallName = ticketOrder.getHall().getName();
		filmCode = ticketOrder.getFilmCode();
		filmName = ticketOrder.getFilm().getName();
		cinemaShowCode = ticketOrder.getShowCode();
		channelShowCode = ticketOrder.getChannelShowCode();
		showTime = ticketOrder.getShowTime();
		reprintCount = ticketOrder.getVoucher().getReprintCount();
		printCode = ticketOrder.getVoucher().getPrintCode();
		verifyCode = ticketOrder.getVoucher().getVerifyCode();
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			tickets.add(new TicketVo(orderItem));
		}
		// if (CollectionUtils.isNotEmpty(ticketOrder.getOrderSnacks())) {
		// for (SnackOrderItem orderSnack : ticketOrder.getOrderSnacks()) {
		// snacks.add(new OrderSnackVo(orderSnack));
		// }
		// }
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getCinemaShowCode() {
		return cinemaShowCode;
	}

	public void setCinemaShowCode(String cinemaShowCode) {
		this.cinemaShowCode = cinemaShowCode;
	}

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String channelShowCode) {
		this.channelShowCode = channelShowCode;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Integer getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(Integer reprintCount) {
		this.reprintCount = reprintCount;
	}

	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public List<TicketVo> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketVo> tickets) {
		this.tickets = tickets;
	}

	public List<SnackOrderVo> getSnacks() {
		return snacks;
	}

	public void setSnacks(List<SnackOrderVo> snacks) {
		this.snacks = snacks;
	}
}