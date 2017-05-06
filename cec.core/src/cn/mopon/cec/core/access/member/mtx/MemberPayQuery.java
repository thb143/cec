package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.NumberUtils;

/**
 * 会员卡消费扣款请求对象。
 */
@XStreamAlias("cardPay")
public class MemberPayQuery extends MTXQuery {

	/** 卡号 */
	@XStreamAlias("cardId")
	private String cardCode;
	/** 手机号 */
	private String mobilePhone = "";
	/** 会员卡密码 */
	private String passWord;
	/** 交易类型 */
	@XStreamAlias("traceTypeNo")
	private String traceType = "01";
	/** 支付金额 */
	private String price;
	/** 交易费用 */
	private Double tracePrice;
	/** 折扣 */
	private String discount = "10";
	/** 排期编码 */
	@XStreamAlias("featureNo")
	private String showCode;
	/** 影片编码 */
	@XStreamAlias("filmNo")
	private String filmCode;
	/** 票数 */
	private String ticketNum = "1";
	/** 备注 */
	private String ticketMemo = "";
	/** 签名 */
	@XStreamAlias("validateKey")
	private String sign;

	/***
	 * 构造方法
	 * 
	 * @param order
	 *            订单号
	 * @param memberCard
	 *            会员卡信息
	 * @param settings
	 *            会员卡接入设置
	 */
	public MemberPayQuery(TicketOrder order, MemberCard memberCard,
			MemberSettings settings) {
		this.cinameCode = memberCard.getCinemaCode();
		this.cardCode = memberCard.getCardCode();
		this.passWord = memberCard.getPassword();
		this.price = order.getSubmitAmount().toString();
		this.showCode = order.getShowCode();
		this.filmCode = order.getFilmCode();
		this.pid = settings.getUsername();
		this.tracePrice = getFees(order);
		this.param = this.cardCode + this.passWord + this.traceType
				+ this.price + this.tracePrice + this.discount + this.showCode
				+ this.filmCode + this.ticketNum + settings.getPassword();
		this.sign = getVerifyInfo(this.param);
	}

	/**
	 * 获取服务费。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回服务费。
	 */
	private Double getFees(TicketOrder order) {
		Double fees = 0.0;
		for (TicketOrderItem item : order.getOrderItems()) {
			fees += (NumberUtils
					.sub(item.getSalePrice(), item.getSubmitPrice()));
		}
		return fees;
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

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getTraceType() {
		return traceType;
	}

	public void setTraceType(String traceType) {
		this.traceType = traceType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Double getTracePrice() {
		return tracePrice;
	}

	public void setTracePrice(Double tracePrice) {
		this.tracePrice = tracePrice;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(String ticketNum) {
		this.ticketNum = ticketNum;
	}

	public String getTicketMemo() {
		return ticketMemo;
	}

	public void setTicketMemo(String ticketMemo) {
		this.ticketMemo = ticketMemo;
	}

}
