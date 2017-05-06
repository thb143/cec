package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.DateUtils;

/**
 * 会员卡折扣信息请求对象。
 */
@XStreamAlias("getDiscount")
public class DiscountPriceQuery extends MTXQuery {

	/** 卡号 */
	@XStreamAlias("cardId")
	private String cardCode;
	/** 手机号 */
	private String mobilePhone = "";
	/** 排期号 */
	@XStreamAlias("featureNo")
	private String showCode;
	/** 放映时间 */
	@XStreamAlias("featureDate")
	private String showTime;
	/** 放映日期 */
	@XStreamAlias("featureTime")
	private String showDate;
	/** 签名 */
	@XStreamAlias("validateKey")
	private String sign;

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param cardCode
	 *            会员卡号
	 * @param settings
	 *            会员卡接入设置
	 */
	public DiscountPriceQuery(TicketOrder order, String cardCode,
			MemberSettings settings) {
		this.cinameCode = order.getCinema().getCode();
		this.cardCode = cardCode;
		this.showCode = order.getShowCode();
		this.showTime = DateUtils.format(order.getShowTime(), DateUtils.DAY);
		this.showDate = DateUtils.format(order.getShowTime(), DateUtils.SECOND);
		this.pid = settings.getUsername();
		this.param = this.cardCode + this.showCode + this.showTime
				+ this.showDate + settings.getPassword();
		this.sign = getVerifyInfo(this.param);
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}
}
