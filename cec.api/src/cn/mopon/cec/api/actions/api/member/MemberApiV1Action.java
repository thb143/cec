package cn.mopon.cec.api.actions.api.member;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.api.actions.api.ApiAction;
import cn.mopon.cec.api.member.v1.CardInfoByChipQuery;
import cn.mopon.cec.api.member.v1.CardInfoByChipReply;
import cn.mopon.cec.api.member.v1.CardInfoByCodeQuery;
import cn.mopon.cec.api.member.v1.CardInfoByCodeReply;
import cn.mopon.cec.api.member.v1.CardPayQuery;
import cn.mopon.cec.api.member.v1.CardPayReply;
import cn.mopon.cec.api.member.v1.CardRechargeByChipQuery;
import cn.mopon.cec.api.member.v1.CardRechargeByChipReply;
import cn.mopon.cec.api.member.v1.CardRechargeByCodeQuery;
import cn.mopon.cec.api.member.v1.CardRechargeByCodeReply;
import cn.mopon.cec.api.member.v1.CardVerifyQuery;
import cn.mopon.cec.api.member.v1.CardVerifyReply;
import cn.mopon.cec.api.member.v1.DiscountPriceQuery;
import cn.mopon.cec.api.member.v1.DiscountPriceReply;
import cn.mopon.cec.api.member.v1.MemberCardApiV1Service;

/**
 * 接出API。
 */

@Controller
@RequestMapping("/api/member/v1")
public class MemberApiV1Action extends ApiAction {
	@Resource
	private MemberCardApiV1Service memberCardApiV1Service;

	/**
	 * 验证会员卡。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */

	@RequestMapping("cardVerify")
	public CardVerifyReply verifyCard(@Valid CardVerifyQuery query) {
		return memberCardApiV1Service.verifyCard(query);
	}

	/**
	 * 获取会员卡信息。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */

	@RequestMapping("queryCardInfoByCode")
	public CardInfoByCodeReply queryCardByCode(@Valid CardInfoByCodeQuery query) {
		return memberCardApiV1Service.queryCardByCode(query);
	}

	/**
	 * 获取会员卡信息接口(芯片号)。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryCardInfoByChip")
	public CardInfoByChipReply queryCardByChip(@Valid CardInfoByChipQuery query) {
		return memberCardApiV1Service.getMemberCardInfoByChip(query);
	}

	/**
	 * 会员卡充值。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */

	@RequestMapping("cardRechargeByCode")
	public CardRechargeByCodeReply cardRechargeByCode(
			@Valid CardRechargeByCodeQuery query) {
		return memberCardApiV1Service.cardRechargeByCode(query);
	}

	/**
	 * 会员卡充值接口(芯片号)。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */

	@RequestMapping("cardRechargeByChip")
	public CardRechargeByChipReply cardRechargeByChip(
			@Valid CardRechargeByChipQuery query) {
		return memberCardApiV1Service.cardRechargeByChip(query);
	}

	/**
	 * 会员卡折扣查询。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */

	@RequestMapping("cardDiscount")
	public DiscountPriceReply cardDiscount(@Valid DiscountPriceQuery query) {
		return memberCardApiV1Service.getDiscountPrice(query);
	}

	/**
	 * 会员卡消费扣款。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */

	@RequestMapping("cardPay")
	public CardPayReply cardPay(@Valid CardPayQuery query) {
		return memberCardApiV1Service.memberCardPay(query);
	}
}