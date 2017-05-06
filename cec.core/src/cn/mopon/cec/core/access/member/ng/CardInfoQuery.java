package cn.mopon.cec.core.access.member.ng;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡信息请求对象。
 */
public class CardInfoQuery extends NgQuery {

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡
	 * @param secKey
	 *            渠道密码
	 */
	public CardInfoQuery(MemberCard memberCard, String secKey) {
		super(memberCard, secKey);
		setAction("queryCardInfo.json");
	}
}
