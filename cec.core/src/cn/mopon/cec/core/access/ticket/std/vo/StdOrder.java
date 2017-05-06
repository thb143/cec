package cn.mopon.cec.core.access.ticket.std.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 订单VO。
 */
public class StdOrder {
	/** 排期编码 */
	@XStreamAsAttribute
	@XStreamAlias("SessionCode")
	private String sessionCode;
	/** 选座票数量 */
	@XStreamAsAttribute
	@XStreamAlias("Count")
	private Integer count;
	/** 影院订单号 */
	@XStreamAsAttribute
	@XStreamAlias("OrderCode")
	private String cinemaOrderCode;
	/** 取票号 */
	@XStreamAlias("PrintNo")
	private String printNo;
	/** 取票验证码 */
	@XStreamAlias("VerifyCode")
	private String verifyCode;
	/** 状态（YES;NO） */
	private Boolean status;
	/** 打印时间 */
	private String printTime;
	/** 退票时间 */
	private String refundTime;
	/** 关联座位 */
	@XStreamImplicit
	private List<StdSeat> seats = new ArrayList<StdSeat>();
	/** 渠道编号 */
	@XStreamAsAttribute
	@XStreamAlias("ChannelId")
	private String channelId;

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<StdSeat> getSeats() {
		return seats;
	}

	public void setSeats(List<StdSeat> seats) {
		this.seats = seats;
	}

	public String getCinemaOrderCode() {
		return cinemaOrderCode;
	}

	public void setCinemaOrderCode(String orderCode) {
		this.cinemaOrderCode = orderCode;
	}

	public String getPrintNo() {
		return printNo;
	}

	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getChannelId() {
		return channelId;
	}
}
