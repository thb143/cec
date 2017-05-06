package cn.mopon.cec.core.access.ticket.mtx.vo;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 锁座请求参数。
 * 
 */
@XStreamAlias("RealCheckSeatStateParameter")
public class RealCheckSeatStateParameter {
	/** 应用编码 */
	@XStreamAlias("AppCode")
	protected String appCode;
	/** 排期应用号 */
	@XStreamAlias("FeatureAppNo")
	protected String showCode;
	/** 流水号 */
	@XStreamAlias("SerialNum")
	protected String code;
	/** 座位信息 */
	@XStreamAlias("SeatInfos")
	protected List<SeatInfo> seats = new ArrayList<SeatInfo>();
	/** 渠道编号 */
	@XStreamAlias("PayType")
	protected String channelNo;
	/** 手机号码 */
	@XStreamAlias("RecvMobilePhone")
	protected String recvMobilePhone;
	/** 令牌ID */
	@XStreamAlias("TokenID")
	protected String tokenIds;
	/** 检验信息 */
	@XStreamAlias("VerifyInfo")
	protected String verifyInfo;

	/**
	 * 获取座位信息。
	 * 
	 * @param order
	 *            订单
	 * @return 返回座位信息。
	 */
	public List<SeatInfo> getSeats(TicketOrder order) {
		List<SeatInfo> seatInfos = new ArrayList<SeatInfo>();
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			SeatInfo seatInfo = new SeatInfo();
			seatInfo.setSeatNo(orderItem.getSeatCode());
			seatInfo.setTicketPrice(orderItem.getSubmitPrice());
			seatInfo.setHandlingfee(orderItem.getServiceFee());
			seatInfos.add(seatInfo);
		}
		return seatInfos;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<SeatInfo> getSeats() {
		return seats;
	}

	public void setSeats(List<SeatInfo> seats) {
		this.seats = seats;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}

	public String getRecvMobilePhone() {
		return recvMobilePhone;
	}

	public void setRecvMobilePhone(String recvMobilePhone) {
		this.recvMobilePhone = recvMobilePhone;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getTokenIds() {
		return tokenIds;
	}

	public void setTokenIds(String tokenIds) {
		this.tokenIds = tokenIds;
	}

	public String getVerifyInfo() {
		return verifyInfo;
	}

}
