package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * API接口。
 */
public enum TicketApiMethod implements IEnum {
	QUERY_CINEMAS("查询影院列表", "100"), QUERY_CINEMA("查询影院", "101"), QUERY_HALLS(
			"查询影厅列表", "120"), QUERY_SEATS("查询影厅座位列表", "121"), QUERY_FILMS(
			"查询影片列表", "140"), QUERY_FILM("查询影片", "141"), QUERY_SHOWS("查询场次列表",
			"200"), QUERY_REPLACE_SHOW("查询替代场次", "201"), QUERY_SHOW_SEATS(
			"查询场次座位列表", "202"), LOCK_SEATS("锁座", "220"), RELEASE_SEATS("释放座位",
			"221"), SUBMIT_ORDER("确认订单", "222"), QUERY_ORDER("查询订单", "223"), REVOKE_TICKET(
			"退票", "224"), MARK_TICKET_REVOKED("标记退票", "225"), CHANGE_VOUCHER(
			"更换凭证", "241"), RESET_VOUCHER("重置凭证", "242"), QUERY_PRINT_BY_VERIFYCODE(
			"取票号查询打印状态", "243"), QUERY_PRINT_BY_VOUCHER("凭证编码查询打印状态", "244"), CONFIRM_PRINT(
			"确认打票", "245"), QUERY_CHANNELS("查询渠道列表", "261"), QUERY_SNACKS(
			"查询卖品列表", "271"), QUERY_BENEFITCARDTYPE("获取卡类", "281"), OPEN_BENEFITCARD(
			"开卡", "282"), OPEN_BENEFITCARDORDER("查询开卡订单", "283"), RECHARGE_BENEFITCARD(
			"续费", "284"), QUERY_BENEFITCARD("查询卡信息", "285"), QUERY_RECHARGEBENEFITCATDORDER(
			"查询续费订单", "286"), CHANGE_MOBILE("更改手机号码", "287"), SUBMIT_SNACK(
			"确认卖品", "288"), QUERY_SNACK_ORDER("查询卖品订单", "289"), REVOKE_SNACK(
			"退订卖品", "290"), MARK_SNACK_REVOKED("标记退订卖品", "291");

	private String text;
	private String value;

	/**
	 * 构造方法
	 * 
	 * @param text
	 *            文本
	 * @param value
	 *            值
	 */
	private TicketApiMethod(String text, String value) {
		this.text = text;
		this.value = value;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return text;
	}
}
