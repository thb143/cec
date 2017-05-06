package cn.mopon.cec.core.access.member;

import java.util.List;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 会员卡接入接口
 */
public interface MemberAccessService {
	/**
	 * 会员卡登入。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @return 返回会员卡信息。
	 */
	MemberCard getVerifyCard(MemberCard memberCard);

	/**
	 * 会员卡信息(卡号)。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @return 返回会员卡信息。
	 */
	MemberCard getMemberCardInfo(MemberCard memberCard);

	/**
	 * 会员卡信息(芯片号)。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @return 返回会员卡信息。
	 */
	MemberCard getMemberCardInfoByChip(MemberCard memberCard);

	/**
	 * 会员卡充值。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param money
	 *            充值金额
	 * @param orderNo
	 *            外部充值流水号
	 * @return 返回充值信息。
	 */
	Double getMemberCardRecharge(MemberCard memberCard, String money,
			String orderNo);

	/**
	 * 会员卡充值（芯片号）。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param money
	 *            充值金额
	 * @param orderNo
	 *            外部充值流水号
	 * @return 返回充值信息。
	 */
	Double getMemberCardRechargeByChip(MemberCard memberCard, String money,
			String orderNo);

	/**
	 * 会员卡折扣。
	 * 
	 * @param ticketOrder
	 *            SCEC选座票订单
	 * @param memberCard
	 *            会员卡信息
	 * @return 返回会员卡座位信息列表。
	 */
	List<SeatInfo> getDiscountPrice(TicketOrder ticketOrder,
			MemberCard memberCard);

	/**
	 * 会员卡扣款。
	 * 
	 * @param ticketOrder
	 *            cec订单号
	 * @param memberCard
	 *            会员卡信息
	 * @return 返回会员卡支付信息。
	 */
	TicketOrder memberCardPay(TicketOrder ticketOrder, MemberCard memberCard);

	/**
	 * 会员退票
	 * 
	 * @param ticketOrder
	 *            cec订单
	 * @param memberCard
	 *            会员卡
	 * @return 会员退票结果
	 */
	Boolean refundMemberTicket(TicketOrder ticketOrder, MemberCard memberCard);
}