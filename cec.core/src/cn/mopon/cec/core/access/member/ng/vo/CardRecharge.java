package cn.mopon.cec.core.access.member.ng.vo;

/**
 * 会员卡充值。
 */
public class CardRecharge {
	/** 账户余额 */
	private Double cardBalance;
	/** 充值订单号 */
	private String sctsOrderNo;
	/** 会员卡号 */
	private String cardCode;

	public Double getCardBalance() {
		return cardBalance;
	}

	public void setCardBalance(Double cardBalance) {
		this.cardBalance = cardBalance;
	}

	public String getSctsOrderNo() {
		return sctsOrderNo;
	}

	public void setSctsOrderNo(String sctsOrderNo) {
		this.sctsOrderNo = sctsOrderNo;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
}