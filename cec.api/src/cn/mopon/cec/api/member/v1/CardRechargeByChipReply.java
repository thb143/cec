package cn.mopon.cec.api.member.v1;

import cn.mopon.cec.api.ApiReply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 会员卡充值响应对象（芯片号）。
 */
@JsonInclude(Include.NON_NULL)
public class CardRechargeByChipReply extends ApiReply {
	/** 账户余额 */
	private Double balance;

	/**
	 * 构造方法。
	 * 
	 * @param balance
	 *            账户余额
	 */
	public CardRechargeByChipReply(Double balance) {
		this.balance = balance;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
