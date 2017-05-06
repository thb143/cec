package cn.mopon.cec.core.access.member.hln;

import cn.mopon.cec.core.access.member.hln.vo.PayOrderVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员卡详细信息请求对象。
 */
@XStreamAlias("queryPayDetails")
public class MemberDetailQuery extends HLNQuery {
	private PayOrderVo payOrder;

	/**
	 * 构造方法。
	 * 
	 * @param cardCode
	 *            会员卡卡号
	 */
	public MemberDetailQuery(String cardCode) {
		payOrder = new PayOrderVo();
		payOrder.setAccount(cardCode);
		payOrder.setPayType(2);
	}

	public PayOrderVo getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(PayOrderVo payOrder) {
		this.payOrder = payOrder;
	}

}
