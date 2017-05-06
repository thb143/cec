package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 更改手机号码请求对象。
 */
public class ChangeMobileQuery extends ApiQuery {
	/** 旧手机号码 */
	@NotBlank(message = "旧手机号码不能为空。")
	private String oldMobile;
	/** 新手机号码 */
	@NotBlank(message = "新手机号码不能为空。")
	private String newMobile;
	/** 卡类编号 */
	@NotBlank(message = "卡类编号不能为空。")
	private String typeCode;

	public String getOldMobile() {
		return oldMobile;
	}

	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
}