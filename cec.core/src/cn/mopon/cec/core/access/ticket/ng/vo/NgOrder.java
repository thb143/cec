package cn.mopon.cec.core.access.ticket.ng.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 订单VO。
 */
public class NgOrder {
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
	/** 会员支付流水号 */
	@XStreamAsAttribute
	@XStreamAlias("MemberPayOrderCode")
	private String memberPayOrderCode;
	/** 状态（YES;NO） */
	private Boolean status;
	/** 打印时间 */
	private String printTime;
	/** 退票时间 */
	private String refundTime;
	/** 渠道编号 */
	@XStreamAsAttribute
	@XStreamAlias("ChannelId")
	private String channelId;
	/** 渠道名称 */
	@XStreamAsAttribute
	@XStreamAlias("ChannelName")
	private String channelName;
	/** 关联座位 */
	@XStreamImplicit
	private List<NgSeat> seats = new ArrayList<NgSeat>();

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

	public List<NgSeat> getSeats() {
		return seats;
	}

	public void setSeats(List<NgSeat> seats) {
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

	public String getMemberPayOrderCode() {
		return memberPayOrderCode;
	}

	public void setMemberPayOrderCode(String memberPayOrderCode) {
		this.memberPayOrderCode = memberPayOrderCode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

}
