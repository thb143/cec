package cn.mopon.cec.core.access.member.ng;

import cn.mopon.cec.core.access.member.ng.vo.CardRecharge;

/**
 * 充值响应对象。
 * */
public class CardRechargeReply extends NgReply {
	/** 充值vo */
	private CardRecharge data;

	public CardRecharge getData() {
		return data;
	}

	public void setData(CardRecharge data) {
		this.data = data;
	}

}
