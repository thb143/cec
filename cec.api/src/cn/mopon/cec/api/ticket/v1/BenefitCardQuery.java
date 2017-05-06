package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询卡信息请求对象。
 */
public class BenefitCardQuery extends ApiQuery {
	/** 手机号码 */
	@NotBlank(message = "手机号码不能为空。")
	private String mobile;
	/** 卡类编号 */
	@NotBlank(message = "卡类编号不能为空。")
	private String typeCode;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
}