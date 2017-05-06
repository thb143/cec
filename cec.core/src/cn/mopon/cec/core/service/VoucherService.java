package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import cn.mopon.cec.core.assist.constants.RedisKey;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.enums.ProductType;
import coo.base.constants.Chars;
import coo.base.util.CryptoUtils;

/**
 * 凭证服务类。
 */
@Service
public class VoucherService {
	@Resource(name = "redisTemplate")
	private SetOperations<String, String> voucherCodeCache;
	/** 卖品凭证前缀 */
	@Value(value = "${snackVoucher.prefix:88}")
	private String snackVoucherPrefix;
	/** 卖品凭证长度 */
	@Value(value = "${snackVoucher.length:12}")
	private Integer snackVoucherLength;

	/**
	 * 生成选座票凭证号。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回生成的选座票凭证号。
	 */
	public String genTicketVoucherCode(TicketOrder order) {
		String ticketVoucherCode = null;
		while (ticketVoucherCode == null) {
			ticketVoucherCode = getTicketVoucherCode(order.getCinema());
		}
		return ticketVoucherCode;
	}

	/**
	 * 生成卖品凭证号。
	 * 
	 * @param order
	 *            卖品订单
	 * @return 返回生成的卖品凭证号。
	 */
	public String genSnackVoucherCode() {
		String snackVoucherCode = null;
		while (snackVoucherCode == null) {
			String code = snackVoucherPrefix
					+ CryptoUtils.genRandomCode("0123456789",
							snackVoucherLength - (snackVoucherPrefix.length()));
			if (saveVoucher(null, ProductType.SNACK, code)) {
				snackVoucherCode = code;
			}
		}
		return snackVoucherCode;
	}

	/**
	 * 注销选座票凭证号。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void desTicketVoucherCode(TicketOrder order) {
		deleteVoucher(order.getCinema(), ProductType.TICKET, order.getVoucher()
				.getCode());
	}

	/**
	 * 注销卖品凭证号。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void desSnackVoucherCode(SnackOrder order) {
		deleteVoucher(null, ProductType.SNACK, order.getVoucher().getCode());
	}

	/**
	 * 获取指定影院的选座票凭证号。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回指定影院的选座票凭证号，如果生成的选座票凭证号重复，返回null。
	 */
	private String getTicketVoucherCode(Cinema cinema) {
		String code = CryptoUtils.genRandomCode("0123456789", cinema
				.getTicketSettings().getVoucherCodeLength());
		if (saveVoucher(cinema, ProductType.TICKET, code)) {
			return code;
		} else {
			return null;
		}
	}

	/**
	 * 保存凭证。
	 * 
	 * @param cinema
	 *            影院
	 * @param type
	 *            产品类型
	 * @param voucherCode
	 *            凭证号
	 * @return 是否保存成功。
	 */
	public Boolean saveVoucher(Cinema cinema, ProductType type,
			String voucherCode) {
		return voucherCodeCache.add(getKey(cinema, type), voucherCode) > 0;
	}

	/**
	 * 删除凭证。
	 * 
	 * @param cinema
	 *            影院
	 * @param type
	 *            产品类型
	 * @param voucherCode
	 *            凭证号
	 */
	public void deleteVoucher(Cinema cinema, ProductType type,
			String voucherCode) {
		voucherCodeCache.remove(getKey(cinema, type), voucherCode);
	}

	/**
	 * 获取影院凭证key。
	 * 
	 * @param cinema
	 *            影院
	 * @param type
	 *            产品类型
	 * @return 影院凭证key。
	 */
	private String getKey(Cinema cinema, ProductType type) {
		if (cinema != null) {
			return RedisKey.TICKET_VOUCHER_CODE + Chars.UNDERLINE
					+ cinema.getCode() + Chars.UNDERLINE + type.getValue();
		} else {
			return RedisKey.SNACK_VOUCHER_CODE + Chars.UNDERLINE
					+ type.getValue();
		}
	}
}