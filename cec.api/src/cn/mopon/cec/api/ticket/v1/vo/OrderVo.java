package cn.mopon.cec.api.ticket.v1.vo;

import java.util.Date;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.PrintMode;
import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.TicketOrderStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.BeanUtils;
import coo.core.xstream.DateConverter;
import coo.core.xstream.IEnumConverter;

/**
 * 订单。
 */
@XStreamAlias("order")
@JsonInclude(Include.NON_NULL)
public class OrderVo {
	/** 订单号 */
	private String code;
	/** 渠道订单号 */
	private String channelOrderCode;
	/** 确认时间 */
	@XStreamConverter(value = DateConverter.class)
	private Date confirmTime;
	/** 选座票数量 */
	private Integer ticketCount;
	/** 卖品数量 */
	private Integer snackCount = 0;
	/** 订单金额 */
	private Double amount;
	/** 取票方式 */
	@XStreamConverter(value = IEnumConverter.class, types = PrintMode.class)
	private PrintMode printMode;
	/** 接入商 */
	@XStreamConverter(value = IEnumConverter.class, types = Provider.class)
	private Provider provider;
	/** 凭证号 */
	private String voucherCode;
	/** 取票号 */
	private String printCode;
	/** 验证码 */
	private String verifyCode;
	/** 订单状态 */
	@XStreamConverter(value = IEnumConverter.class, types = TicketOrderStatus.class)
	private TicketOrderStatus status;
	/** 打印状态 */
	private String printStatus;

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            订单
	 */
	public OrderVo(TicketOrder order) {
		BeanUtils.copyFields(order, this);
		if (order.getSnackOrder() != null) {
			snackCount = order.getSnackOrder().getSnackCount();
			amount += order.getSnackOrder().getAmount();
		}
		if (order.getVoucher() != null) {
			TicketVoucher voucher = order.getVoucher();
			printMode = order.getCinema().getTicketSettings().getPrintMode();
			provider = order.getProvider();
			verifyCode = order.getVoucher().getVerifyCode();
			printCode = order.getVoucher().getPrintCode();
			voucherCode = voucher.getCode();
			printStatus = voucher.getStatus().getValue();
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

	public Integer getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
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

	public PrintMode getPrintMode() {
		return printMode;
	}

	public void setPrintMode(PrintMode printMode) {
		this.printMode = printMode;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
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

	public TicketOrderStatus getStatus() {
		return status;
	}

	public void setStatus(TicketOrderStatus status) {
		this.status = status;
	}

	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}
}