package cn.mopon.cec.api.ticket.v1.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardSettle;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.enums.ShelveStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.NumberUtils;
import coo.core.xstream.DateConverter;
import coo.core.xstream.IEnumConverter;

/**
 * 场次。
 */
@XStreamAlias("show")
public class ShowVo {
	/** 渠道场次编码 */
	private String channelShowCode = "";
	/** 影院编码 */
	private String cinemaCode = "";
	/** 影院场次编码 */
	private String cinemaShowCode = "";
	/** 影厅编码 */
	private String hallCode = "";
	/** 影厅名称 */
	private String hallName = "";
	/** 影片编码 */
	private String filmCode = "";
	/** 影片名称 */
	private String filmName = "";
	/** 放映类型 */
	// @XStreamConverter(value = IEnumConverter.class, types = ShowType.class)
	// private ShowType showType;
	private String showType;
	/** 影片语言 */
	private String language = "";
	/** 放映时间 */
	@XStreamConverter(value = DateConverter.class)
	private Date showTime;
	/** 时长 */
	private Integer duration;
	/** 停售时间 */
	@XStreamConverter(value = DateConverter.class)
	private Date stopSellTime;
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 最低价 */
	private Double minPrice = 0D;
	/** 结算价 */
	private Double settlePrice = 0D;
	/** 票房价 */
	private Double submitPrice = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 状态 */
	@XStreamConverter(value = IEnumConverter.class, types = ShelveStatus.class)
	private ShelveStatus status;
	/** 权益卡价格列表 */
	private List<BenefitCardPriceVo> benefitCardPrices = new ArrayList<BenefitCardPriceVo>();

	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道场次
	 */
	public ShowVo(ChannelShow channelShow) {
		channelShowCode = channelShow.getCode();
		cinemaCode = channelShow.getCinema().getCode();
		cinemaShowCode = channelShow.getShowCode();
		hallCode = channelShow.getHall().getCode();
		hallName = channelShow.getHall().getName();
		// 影片编码取前12位国标编码
		filmCode = channelShow.getFilmCode().substring(0, 12);
		filmName = channelShow.getFilm().getName();
		showType = filmCode.substring(3, 4);
		// showType = channelShow.getShowType();
		language = channelShow.getLanguage();
		showTime = channelShow.getShowTime();
		duration = channelShow.getDuration();
		stopSellTime = channelShow.getStopSellTime();
		minPrice = channelShow.getMinPrice();
		stdPrice = channelShow.getStdPrice();
		settlePrice = NumberUtils.add(channelShow.getChannelPrice(),
				channelShow.getConnectFee());
		submitPrice = channelShow.getSubmitPrice();
		connectFee = channelShow.getConnectFee();
		status = channelShow.getStatus();
		for (BenefitCardSettle benefitCardSettle : channelShow
				.getBenefitCardSettles()) {
			benefitCardPrices.add(new BenefitCardPriceVo(benefitCardSettle));
		}
	}

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String channelShowCode) {
		this.channelShowCode = channelShowCode;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCinemaShowCode() {
		return cinemaShowCode;
	}

	public void setCinemaShowCode(String cinemaShowCode) {
		this.cinemaShowCode = cinemaShowCode;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(Date stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
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

	public ShelveStatus getStatus() {
		return status;
	}

	public void setStatus(ShelveStatus status) {
		this.status = status;
	}

	public List<BenefitCardPriceVo> getBenefitCardPrices() {
		return benefitCardPrices;
	}

	public void setBenefitCardPrices(List<BenefitCardPriceVo> benefitCardPrices) {
		this.benefitCardPrices = benefitCardPrices;
	}
}