package cn.mopon.cec.core.access.ticket.std.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 会员订单。
 * 
 */
@XStreamAlias("Order")
public class MemberOrder {
	@XStreamAsAttribute
	@XStreamAlias("OrderCode")
	private String orderCode;
	@XStreamAsAttribute
	@XStreamAlias("SessionCode")
	private String sessionCode;
	@XStreamAsAttribute
	@XStreamAlias("Count")
	private Integer count;
	@XStreamAsAttribute
	@XStreamAlias("MemberPayOrderCode")
	private String memberPayOrderCode;
	/** 关联座位 */
	@XStreamImplicit
	private List<StdSeat> seats = new ArrayList<StdSeat>();

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public List<StdSeat> getSeats() {
		return seats;
	}

	public void setSeats(List<StdSeat> seats) {
		this.seats = seats;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMemberPayOrderCode() {
		return memberPayOrderCode;
	}

	public void setMemberPayOrderCode(String memberPayOrderCode) {
		this.memberPayOrderCode = memberPayOrderCode;
	}

}
