package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.User;

/**
 * 渠道排期渠道结算价预警通知。
 */
public class ChannelShowChannelPriceMailModel extends MailModel {
	/** 渠道名称 */
	private String channelName;
	/** 影院名称 */
	private String cinemaName;
	/** 影片名称 */
	private String filmName;
	/** 影院排期编码 */
	private String showCode;
	/** 放映时间 */
	private Date showTime;
	/** 最低价 */
	private Double minPrice = 0D;
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 影院结算价 */
	private Double cinemaPrice = 0D;
	/** 渠道结算价 */
	private Double channelPrice = 0D;
	/** 票房价 */
	private Double submitPrice = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 手续费 */
	private Double circuitFee = 0D;
	/** 补贴费 */
	private Double subsidyFee = 0D;

	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param users
	 *            邮件接收用户
	 */
	public ChannelShowChannelPriceMailModel(ChannelShow channelShow,
			List<User> users) {
		super(users);
		templateName = "channelShow-channelPrice-mail.ftl";
		subject = "渠道排期渠道结算价预警通知";
		channelName = channelShow.getChannel().getName();
		cinemaName = channelShow.getCinema().getName();
		filmName = channelShow.getFilm().getName();
		showCode = channelShow.getShowCode();
		showTime = channelShow.getShowTime();
		minPrice = channelShow.getMinPrice();
		stdPrice = channelShow.getStdPrice();
		cinemaPrice = channelShow.getCinemaPrice();
		channelPrice = channelShow.getChannelPrice();
		submitPrice = channelShow.getSubmitPrice();
		connectFee = channelShow.getConnectFee();
		circuitFee = channelShow.getCircuitFee();
		subsidyFee = channelShow.getSubsidyFee();
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Double getCinemaPrice() {
		return cinemaPrice;
	}

	public void setCinemaPrice(Double cinemaPrice) {
		this.cinemaPrice = cinemaPrice;
	}

	public Double getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(Double channelPrice) {
		this.channelPrice = channelPrice;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public Double getCircuitFee() {
		return circuitFee;
	}

	public void setCircuitFee(Double circuitFee) {
		this.circuitFee = circuitFee;
	}

	public Double getSubsidyFee() {
		return subsidyFee;
	}

	public void setSubsidyFee(Double subsidyFee) {
		this.subsidyFee = subsidyFee;
	}
}
