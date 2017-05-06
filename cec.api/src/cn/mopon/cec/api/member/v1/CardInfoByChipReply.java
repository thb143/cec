package cn.mopon.cec.api.member.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.member.v1.vo.MemberVo;
import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡登入验证信息响应对象。
 */
public class CardInfoByChipReply extends ApiReply {
	private MemberVo card;

	/**
	 * 构造方法。
	 * 
	 * @param card
	 *            会员卡
	 */
	public CardInfoByChipReply(MemberCard card) {
		this.card = new MemberVo(card);
	}

	public MemberVo getCard() {
		return card;
	}

	public void setCard(MemberVo card) {
		this.card = card;
	}

}