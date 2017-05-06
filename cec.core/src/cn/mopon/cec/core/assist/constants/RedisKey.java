package cn.mopon.cec.core.assist.constants;

/**
 * Redis键名定义。
 */
public interface RedisKey {
	/* ========================编号============================= */
	/** 渠道排期编号 */
	String CHANNEL_SHOW_CODE = "CHANNEL_SHOW_CODE";
	/** 选座票订单编号 */
	String TICKET_ORDER_CODE = "TICKET_ORDER_CODE";
	/** 选座票凭证编号 */
	String TICKET_VOUCHER_CODE = "TICKET_VOUCHER_CODE";
	/** 卖品编码 */
	String SNACK_CODE = "SNACK_CODE";
	/** 卖品凭证编号 */
	String SNACK_VOUCHER_CODE = "SNACK_VOUCHER_CODE";
	/** 权益卡编号 */
	String BENEFITCARD_CODE = "BENEFITCARD_CODE";
	/** 权益卡充值订单号 */
	String BENEFITCARD_RECHARGE_CODE = "BENEFITCARD_RECHARGE_CODE";

	/* ========================座位图============================= */
	/** 排期座位图前缀 */
	String SHOW_SEAT_MAP = "SHOW_SEAT_MAP";
	/** 座位图最后更新时间 */
	String SHOW_SEAT_UPDATE_TIME_MAP = "SHOW_SEAT_UPDATE_TIME_MAP";

	/* ========================权益卡============================= */
	/** 权益卡每日消费次数 */
	String BENEFITCARD_DAILY_COUNT = "BENEFITCARD_DAILY_COUNT";
}
