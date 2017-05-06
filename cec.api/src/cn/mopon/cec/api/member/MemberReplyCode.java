package cn.mopon.cec.api.member;

import cn.mopon.cec.api.IReplyCode;

/**
 * 会员API接口异常定义。
 */
public enum MemberReplyCode implements IReplyCode {
	MEMBER_NOT_SETTED("300", "影院没有会员卡接入设置。"), CINEMA_NOT_EXIST("301", "影院未找到。"), CARD_OR_PASSWORD_ERROR(
			"302", "会员卡号或密码错误。"), MEMBERCHIP_NOT_EXIST("303", "会员卡芯片号不存在。"), BALANCE_NOT_ENOUGH(
			"311", "会员卡余额不足或扣款金额错误。"), AMOUNT_FORMAT_ERROR("312", "充值金额格式错误。"), AMOUNT_NOT_MINUS(
			"313", "充值金额必须大于零。"), ORDER_NOT_EXIST("320", "订单未找到。"), ORDER_STATUS_INVALID(
			"321", "订单状态不符。"), ORDER_SEATS_UNMATCH("322", "订单座位不匹配。"), CHANNELSHOW_INVALID(
			"323", "渠道场次已失效。"), PAY_SUBMIT_SUMBIT_ERROR("324",
			"会员卡扣款成功，确认订单失败。"), MEMBERCHIP_SEAT_PAY("325", "同一座位，不允许多人同时购买。"), ORDER_SUBMIT_FAILED(
			"330", "影院地面系统确认订单失败。");

	private String code;
	private String msg;

	/**
	 * 构造方法。
	 * 
	 * @param code
	 *            编码
	 * @param msg
	 *            信息
	 */
	private MemberReplyCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}