package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.MemberSettings;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员卡充值请求对象（芯片号）。
 */
@XStreamAlias("cardRecharge")
public class MemberRechargeByChipQuery extends MTXQuery {

	/** 芯片号 */
	@XStreamAlias("cardId")
	private String cardCode;
	/** 手机号 */
	private String mobilePhone = "";
	/** 会员密码 */
	private String passWord = "";
	/** 充值金额 */
	private String price;
	/** 外部交易流水号 */
	private String partnerDepositCode;
	/** 交易备注 */
	private String traceMemo = "";
	/** 签名 */
	@XStreamAlias("validateKey")
	private String sign;

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param money
	 *            充值金额
	 * @param settings
	 *            会员卡接入设置
	 */
	public MemberRechargeByChipQuery(MemberCard memberCard, String money,
			MemberSettings settings) {
		this.cinameCode = memberCard.getCinemaCode();
		this.cardCode = "#" + memberCard.getChipCode();
		this.price = money;
		this.pid = settings.getUsername();
		this.param = this.cardCode + this.passWord + this.price
				+ settings.getPassword();
		this.sign = getVerifyInfo(this.param);
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPartnerDepositCode() {
		return partnerDepositCode;
	}

	public void setPartnerDepositCode(String partnerDepositCode) {
		this.partnerDepositCode = partnerDepositCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTraceMemo() {
		return traceMemo;
	}

	public void setTraceMemo(String traceMemo) {
		this.traceMemo = traceMemo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
