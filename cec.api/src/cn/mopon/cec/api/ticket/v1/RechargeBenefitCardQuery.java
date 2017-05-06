package cn.mopon.cec.api.ticket.v1;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 续费请求对象。
 */
public class RechargeBenefitCardQuery extends ApiQuery {
	/** 用户手机号 */
	@NotBlank(message = "用户手机号不能为空。")
	private String mobile;
	/** 卡类编号 */
	@NotBlank(message = "卡类编号不能为空。")
	private String typeCode;
	/** 渠道续费订单号 */
	@NotBlank(message = "渠道续费订单号不能为空。")
	private String channelOrderCode;
	/** 续费金额（价格以元为单位，保留2位小数） */
	@NotNull(message = "续费金额不能为空。")
	private Double rechargeAmount;

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

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
}