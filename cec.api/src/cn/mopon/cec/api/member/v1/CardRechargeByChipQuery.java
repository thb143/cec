package cn.mopon.cec.api.member.v1;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 会员卡充值请求对象（芯片号）。
 */
public class CardRechargeByChipQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;
	/** 芯片号 */
	@NotBlank(message = "芯片号不能为空。")
	private String chipCode;
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

	public String getChipCode() {
		return chipCode;
	}

	public void setChipCode(String chipCode) {
		this.chipCode = chipCode;
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
