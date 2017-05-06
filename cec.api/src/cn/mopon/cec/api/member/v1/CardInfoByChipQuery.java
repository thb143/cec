package cn.mopon.cec.api.member.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 会员卡信息请求对象（芯片号）。
 */
public class CardInfoByChipQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;
	/** 芯片号 */
	@NotBlank(message = "芯片号不能为空。")
	private String chipCode;

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

}
