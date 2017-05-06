package cn.mopon.cec.api.ticket;

import cn.mopon.cec.api.IReplyCode;

/**
 * 选座票API接口异常定义。
 */
public enum TicketReplyCode implements IReplyCode {
	CINEMA_NOT_EXIST("100", "影院未找到。"), CINEMA_NOT_OPENED("101", "影院未开放。"), HALL_NOT_EXIST(
			"120", "影厅未找到。"), FILM_NOT_EXIST("140", "影片未找到。"), CHANNELSHOW_NOT_EXIST(
			"200", "渠道场次未找到。"), CHANNELSHOW_INVALID("202", "渠道场次已失效。"), CHANNELSHOW_NO_REPLACE(
			"204", "替代渠道场次未找到。"), ORDER_NOT_EXIST("220", "订单未找到。"), ORDER_STATUS_INVALID(
			"221", "订单状态不符。"), ORDER_SEATS_UNMATCH("222", "订单座位不匹配。"), SUBMITPRICE_NOT_MATCH(
			"223", "电影票结算价不符。"), SNACK_NOT_EXIST("224", "卖品未找到。"), SNACK_STATUS_INVALID(
			"225", "卖品状态不符。"), SEATS_FORMAT_ERROR("226", "座位信息格式错误。"), SNACK_FORMAT_ERROR(
			"227", "卖品信息格式错误。"), SNACK_SETTLEPRICE_NOT_MATCH("228", "卖品结算价不符。"), SNACK_TICKET_EXIST(
			"229", "订单为票务订单。"), BENEFIT_FORMAT_ERROR("230", "权益卡信息格式错误。"), ORDER_LOCK_SEAT_FAILED(
			"240", "影院地面系统锁座失败。"), ORDER_SUBMIT_FAILED("241", "影院地面系统确认订单失败。"), ORDER_RELEASE_SEAT_FAILED(
			"242", "影院地面系统释放座位失败。"), ORDER_REVOKE_FAILED("243", "影院地面系统退票失败。"), SNACK_SUBMIT_FAILED(
			"244", "卖品订单确认失败。"), SNACK_REVOKE_FAILED("245", "卖品订单退订失败。"), SNACK_ORDER_PRINT(
			"246", "卖品订单已打票。"), ORDER_OVER_REVOKETIME("250", "已超过可退票时间。"), VOUCHER_NOT_EXIST(
			"260", "凭证未找到。"), VOUCHER_STATUS_INVALID("261", "凭证状态不符。"), VOUCHER_PRINT(
			"262", "凭证已打票。"), CONFIRM_PRINT_REFUSE("263", "影院地面不支持通知打票。"), BENEFITCARDTYPE_NOT_EXIST(
			"280", "权益卡类未找到。"), BENEFITCARD_NOT_EXIST("281", "权益卡未找到。"), BENEFITCARD_STATUS_INVALID(
			"282", "权益卡状态不符。"), OPEN_BENEFITCARD_AMOUNT_INVALID("283",
			"开卡金额不符。"), RECHARGE_BENEFITCARD_AMOUNT_INVALID("284", "续费金额不符。"), BENEFITCARD_REFUSE(
			"285", "权益卡不支持该场次。"), BENEFITCARD_TOTAL_COUNT_INSUFFICIENT("286",
			"权益卡总剩余次数不足。"), BENEFITCARD_DAILY_COUNT_INSUFFICIENT("287",
			"权益卡当天剩余次数不足。"), BENEFITCARD_EXPIRED("288", "权益卡已过期。"), BENEFITCARDTYPE_DISABLED(
			"289", "卡类已停用。"), BENEFITCARD_DISCOUNTPRICE_INVALID("290",
			"权益卡优惠价格不符。"), BENEFITCARD_RECHARGE_ORDER_NOT_EXIST("291",
			"续费订单未找到。"), RECHARGE_BENEFITCARD_FAIL("292", "续费失败。"), BENEFITCARD_EXIST(
			"293", "权益卡已存在。"), CHANNEL_ORDERCODE_EXISTS("294", "渠道订单号已存在。"), OPEN_BENEFITCARD_ORDER_NOT_EXIST(
			"295", "开卡订单未找到。"), BENEFITCARD_USER_EXIST("296", "权益卡用户已存在。"), BENEFITCARD_BIRTHDAY_FORMAT_INVALID(
			"297", "会员生日格式不符。"), BENEFITCARD_SNACK_REFUSE("298", "权益卡不支持该卖品。");

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
	private TicketReplyCode(String code, String msg) {
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