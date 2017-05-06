package cn.mopon.cec.api.ticket.v1;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 开卡请求对象。
 */
public class OpenBenefitCardQuery extends ApiQuery {
	/** 用户手机号 */
	@NotBlank(message = "用户手机号不能为空。")
	private String mobile;
	/** 卡类编号 */
	@NotBlank(message = "卡类编号不能为空。")
	private String typeCode;
	/** 渠道开卡订单号 */
	@NotBlank(message = "渠道开卡订单号不能为空。")
	private String channelOrderCode;
	/** 开卡金额（价格以元为单位，保留2位小数） */
	@NotNull(message = "开卡金额不能为空。")
	private Double initAmount;
	/** 会员生日 */
	private String birthday;

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

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}

	public Double getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(Double initAmount) {
		this.initAmount = initAmount;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}