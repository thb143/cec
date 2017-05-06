package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.entity.TicketVoucher;

/**
 * 更换凭证响应对象。
 */
public class ChangeVoucherReply extends ApiReply {
	/** 凭证编号 */
	private String voucherCode = "";

	/**
	 * 构造方法。
	 * 
	 * @param voucher
	 *            凭证
	 */
	public ChangeVoucherReply(TicketVoucher voucher) {
		this.voucherCode = voucher.getCode();
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
}