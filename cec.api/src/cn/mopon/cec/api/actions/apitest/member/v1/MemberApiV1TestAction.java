package cn.mopon.cec.api.actions.apitest.member.v1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.api.actions.api.ApiAction;
import cn.mopon.cec.api.member.v1.CardInfoByChipQuery;
import cn.mopon.cec.api.member.v1.CardInfoByCodeQuery;
import cn.mopon.cec.api.member.v1.CardPayQuery;
import cn.mopon.cec.api.member.v1.CardRechargeByChipQuery;
import cn.mopon.cec.api.member.v1.CardRechargeByCodeQuery;
import cn.mopon.cec.api.member.v1.CardVerifyQuery;
import cn.mopon.cec.api.member.v1.DiscountPriceQuery;

/**
 * 接出API。
 */
@Controller
@RequestMapping("/apitest/member/v1")
public class MemberApiV1TestAction extends ApiAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do
	}

	/**
	 * 验证会员卡。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardVerify")
	public void verifyCard(Model model) {
		model.addAttribute("query", new CardVerifyQuery());
	}

	/**
	 * 会员卡充值。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardRechargeByCode")
	public void cardRechargeByCode(Model model) {
		model.addAttribute("query", new CardRechargeByCodeQuery());
	}

	/**
	 * 会员卡充值接口(芯片号)。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardRechargeByChip")
	public void cardRechargeByChip(Model model) {
		model.addAttribute("query", new CardRechargeByChipQuery());
	}

	/**
	 * 获取会员卡信息。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardInfoByCode")
	public void queryCardByCode(Model model) {
		model.addAttribute("query", new CardInfoByCodeQuery());
	}

	/**
	 * 获取会员卡信息接口(芯片号)。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardInfoByChip")
	public void queryCardByChip(Model model) {
		model.addAttribute("query", new CardInfoByChipQuery());
	}

	/**
	 * 会员卡折扣查询。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardDiscount")
	public void cardDiscount(Model model) {
		model.addAttribute("query", new DiscountPriceQuery());
	}

	/**
	 * 会员卡消费扣款。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("cardPay")
	public void cardPay(Model model) {
		model.addAttribute("query", new CardPayQuery());
	}

}