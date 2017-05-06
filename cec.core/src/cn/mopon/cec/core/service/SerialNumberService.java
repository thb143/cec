package cn.mopon.cec.core.service;

import java.text.DecimalFormat;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import cn.mopon.cec.core.assist.constants.RedisKey;
import cn.mopon.cec.core.enums.ProductType;
import coo.base.constants.Chars;
import coo.base.util.DateUtils;

/**
 * 编号管理。
 */
@Service
public class SerialNumberService {
	@Resource(name = "redisTemplate")
	private ValueOperations<String, Long> redisStat;

	/**
	 * 获取渠道排期编号。
	 * 
	 * @return 返回渠道排期编号。
	 */
	public String getChannelShowCode() {
		String year = DateUtils.format(DateUtils.getToday(), "yy");
		return getCode(RedisKey.CHANNEL_SHOW_CODE, year,
				ProductType.TICKET.getValue(), 8);
	}

	/**
	 * 获取选座票订单号。
	 * 
	 * @return 返回选座票订单号。
	 */
	public String getTicketOrderCode() {
		String date = DateUtils.format(DateUtils.getToday(), "yyMMdd");
		return getCode(RedisKey.TICKET_ORDER_CODE, date,
				ProductType.TICKET.getValue(), 8);
	}

	/**
	 * 获取权益卡充值订单号。
	 * 
	 * @return　返回权益卡充值订单号。
	 */
	public String getBenefitCardRechargeOrderCode() {
		String date = DateUtils.format(DateUtils.getToday(), "yyMMdd");
		return getCode(RedisKey.BENEFITCARD_RECHARGE_CODE, date,
				ProductType.BENEFITCARD.getValue(), 8);
	}

	/**
	 * 获取权益卡卡号。
	 * 
	 * @param prefix
	 *            　卡号前缀
	 * @return　返回权益卡卡号。
	 */
	public String getBenefitCardCode(String prefix) {
		return getCode(RedisKey.BENEFITCARD_CODE, prefix, "", 8);
	}

	/**
	 * 获取卖品编码。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @return 返回卖品编码。
	 */
	public String getSnackCode(String cinemaCode) {
		return getCode(RedisKey.SNACK_CODE, cinemaCode,
				ProductType.SNACK.getValue(), 3);
	}

	/**
	 * 获取值。
	 * 
	 * @param keyPrefix
	 *            键前缀
	 * @param date
	 *            日期
	 * @param productType
	 *            产品类型
	 * @param width
	 *            位数
	 * @return 编号。
	 */
	private String getCode(String keyPrefix, String date, String productType,
			int width) {
		String key = getKey(keyPrefix, productType, date);
		return date + productType + format(width, (int) incr(key));
	}

	/**
	 * 格式化指定数字。
	 * 
	 * @param width
	 *            位数
	 * @param num
	 *            数字
	 * @return 格式化之后的值。
	 */
	private String format(int width, int num) {
		char[] chs = new char[width];
		for (int i = 0; i < width; i++) {
			chs[i] = '0';
		}
		DecimalFormat df = new DecimalFormat(new String(chs));
		return df.format(num);
	}

	/**
	 * 获取缓存key。
	 * 
	 * @param keyPrefix
	 *            key 前缀
	 * @param productType
	 *            产品类型
	 * @param date
	 *            日期
	 * @return 缓存key。
	 */
	private String getKey(String keyPrefix, String productType, String date) {
		StringBuilder key = new StringBuilder();
		key.append(keyPrefix).append(Chars.UNDERLINE).append(date);
		key.append(Chars.UNDERLINE).append(productType);
		return key.toString();
	}

	/**
	 * 累增序列号。
	 * 
	 * @param key
	 *            键
	 * @return 累增后的值。
	 */
	private long incr(String key) {
		return redisStat.increment(key, 1);
	}
}
