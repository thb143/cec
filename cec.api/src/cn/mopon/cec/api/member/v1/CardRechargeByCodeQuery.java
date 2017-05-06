package cn.mopon.cec.api.member.v1;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 会员卡充值请求对象（会员卡号）。
 */
public class CardRechargeByCodeQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;
	/** 会员卡号 */
	@NotBlank(message = "会员卡号不能为空。")
	private String cardCode;
	/** 会员卡密码 */
	@NotBlank(message = "会员卡密码不能为空。")
	private String password;
	/** 充值金额 */
	@NotNull(message = "充值金额不能为空。")
	private String money;
	/** 外部充值流水号 */
	@NotBlank(message = "外部充值流水号不能为空。")
	private String partnerDepositCode;

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPartnerDepositCode() {
		return partnerDepositCode;
	}

	public void setPartnerDepositCode(String partnerDepositCode) {
		this.partnerDepositCode = partnerDepositCode;
	}

}
