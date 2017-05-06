package cn.mopon.cec.core.access.member.ng.vo;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;

/**
 * 订单vo。
 */
public class OrderVo {
	/** 订单号 */
	private String sctsOrderNo;
	/** 取票号 */
	private String printCode;
	/** 验证码 */
	private String verifyCode;

	public TicketOrder getOrder() {
		TicketOrder order = new TicketOrder();
		TicketVoucher voucher = new TicketVoucher();
		voucher.setPrintCode(printCode);
		voucher.setVerifyCode(verifyCode);
		order.setVoucher(voucher);
		return order;
	}

	public String getSctsOrderNo() {
		return sctsOrderNo;
	}

	public void setSctsOrderNo(String sctsOrderNo) {
		this.sctsOrderNo = sctsOrderNo;
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
}
