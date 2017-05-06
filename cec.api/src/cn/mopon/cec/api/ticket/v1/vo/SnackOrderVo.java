package cn.mopon.cec.api.ticket.v1.vo;

import java.util.Date;

import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.enums.TicketOrderStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.DateConverter;
import coo.core.xstream.IEnumConverter;

/**
 * 订单卖品。
 */
@XStreamAlias("order")
public class SnackOrderVo {
	/** 平台订单号 */
	private String code = "";
	/** 渠道订单号 */
	private String channelOrderCode = "";
	/** 确认时间 */
	@XStreamConverter(value = DateConverter.class)
	private Date confirmTime;
	/** 卖品数量 */
	private Integer snackCount = 0;
	/** 订单金额 */
	private Double amount = 0D;
	/** 凭证号 */
	private String voucherCode = "";
	/** 订单状态 */
	@XStreamConverter(value = IEnumConverter.class, types = TicketOrderStatus.class)
	private TicketOrderStatus status;
	/** 退订时间 */
	@XStreamConverter(value = DateConverter.class)
	private Date revokeTime;

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            卖品订单
	 */
	public SnackOrderVo(SnackOrder order) {
		code = order.getCode();
		channelOrderCode = order.getChannelOrderCode();
		confirmTime = order.getCreateTime();
		snackCount = order.getSnackCount();
		amount = order.getAmount();
		if (order.getVoucher() != null) {
			voucherCode = order.getVoucher().getCode();
		}
		status = order.getStatus();
		if (order.getRevokeTime() != null) {
			revokeTime = order.getRevokeTime();
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Integer getSnackCount() {
		return snackCount;
	}

	public void setSnackCount(Integer snackCount) {
		this.snackCount = snackCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public TicketOrderStatus getStatus() {
		return status;
	}

	public void setStatus(TicketOrderStatus status) {
		this.status = status;
	}

	public Date getRevokeTime() {
		return revokeTime;
	}

	public void setRevokeTime(Date revokeTime) {
		this.revokeTime = revokeTime;
	}
}