package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.joda.time.DateTime;

import cn.mopon.cec.core.assist.enums.SettleBasisType;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.enums.ChannelShowType;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.SubmitType;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.exception.UncheckedException;
import coo.base.util.CollectionUtils;
import coo.base.util.CryptoUtils;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumTextBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.entity.ResourceEntity;

/**
 * 渠道排期。
 */
@Entity
@Table(name = "CEC_ChannelShow")
@Indexed(index = "ChannelShow")
@Cache(region = "ChannelShow", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChannelShow extends ResourceEntity<User> implements
		Comparable<ChannelShow> {
	/** 关联影院排期 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "showId")
	@IndexedEmbedded(includePaths = { "id", "code" })
	private Show show;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Channel channel;
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Cinema cinema;
	/** 关联影厅 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallId")
	@IndexedEmbedded(includePaths = "name")
	private Hall hall;
	/** 关联影片 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filmId")
	@IndexedEmbedded(includePaths = { "code", "name" })
	private Film film;
	/** 接入商 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumTextBridge.class))
	private Provider provider;
	/** 编码 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ChannelShowType type = ChannelShowType.NORMAL;
	/** 影院排期编码 */
	@Field(analyze = Analyze.NO)
	private String showCode;
	/** 影片编码 */
	@Field(analyze = Analyze.NO)
	private String filmCode;
	/** 影片语言 */
	private String language;
	/** 放映类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ShowType showType;
	/** 放映时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date showTime;
	/** 过期时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date expireTime;
	/** 最低价 */
	private Double minPrice = 0D;
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 关联影院结算规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaRuleId")
	@IndexedEmbedded(includePaths = "id")
	private CinemaRule cinemaRule;
	/** 关联渠道结算规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelRuleId")
	@IndexedEmbedded(includePaths = "id")
	private ChannelRule channelRule;
	/** 关联特殊定价规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialRuleId")
	@IndexedEmbedded(includePaths = "id")
	private SpecialRule specialRule;
	/** 关联特殊定价渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialChannelId")
	@IndexedEmbedded(includePaths = "id")
	private SpecialChannel specialChannel;
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
	/** 停售时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date stopSellTime;
	/** 停退时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date stopRevokeTime;
	/** 上架状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ShelveStatus status = ShelveStatus.OFF;
	/** 失效的排期编码(用来做唯一索引，保证一个影院排期只能生成一条上架的渠道排期) */
	private String invalidCode;
	/** 时长（分钟） */
	private Integer duration;
	/** 关联权益卡结算价格类别 */
	@OneToMany(mappedBy = "channelShow", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<BenefitCardSettle> benefitCardSettles = new ArrayList<>();

	/**
	 * 获取本渠道排期对应的当前有效的渠道排期。
	 * 
	 * @return 返回本渠道排期对应的当前有效的渠道排期。
	 */
	public ChannelShow getValidChannelShow() {
		for (ChannelShow channelShow : getShow().getChannelShows()) {
			if (channelShow.getChannel().equals(channel)
					&& channelShow.getStatus() != ShelveStatus.INVALID) {
				return channelShow;
			}
		}
		return null;
	}

	/**
	 * 获取本渠道排期对应的最后无效渠道排期。
	 * 
	 * @return 返回本渠道排期对应的最后无效渠道排期。
	 */
	public ChannelShow getLastInvalidChannelShow() {
		List<ChannelShow> channelShows = new ArrayList<>();
		for (ChannelShow channelShow : getShow().getChannelShows()) {
			if (channelShow.getChannel().equals(channel)
					&& channelShow.getStatus() == ShelveStatus.INVALID) {
				channelShows.add(channelShow);
			}
		}
		if (channelShows.isEmpty()) {
			return null;
		} else {
			Collections.sort(channelShows);
			return channelShows.get(0);
		}
	}

	/**
	 * 生成初始的失效排期编码（渠道ID+影院ID+影院排期编码的MD5码）。
	 * 
	 * @return 返回初始的失效排期编码。
	 */
	public String genInvalidCode() {
		return CryptoUtils.md5(getChannel().getId() + getCinema().getId()
				+ getShowCode());
	}

	/**
	 * 获取影院结算规则名称。
	 * 
	 * @return 返回影院结算规则名称。
	 */
	public String getCinemaRuleName() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getName();
		case SPECIAL:
			return getSpecialRule().getName();
		default:
			return null;
		}
	}

	/**
	 * 获取影院结算规则颜色。
	 * 
	 * @return 返回影院结算规则颜色。
	 */
	public String getCinemaRuleColor() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getEnabled().getColor();
		case SPECIAL:
			return getSpecialRule().getEnabled().getColor();
		default:
			return null;
		}
	}

	/**
	 * 获取影院结算规则摘要。
	 * 
	 * @return 返回影院结算规则摘要。
	 */
	public String getCinemaRuleSummary() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getSummary();
		case SPECIAL:
			return getSpecialRule().getSummary();
		default:
			return null;
		}
	}

	/**
	 * 获取渠道结算规则名称。
	 * 
	 * @return 返回渠道结算规则名称。
	 */
	public String getChannelRuleName() {
		switch (type) {
		case NORMAL:
			return getChannelRule().getName();
		case SPECIAL:
			return getSpecialRule().getName();
		default:
			return null;
		}
	}

	/**
	 * 获取渠道结算规则颜色。
	 * 
	 * @return 返回渠道结算规则颜色。
	 */
	public String getChannelRuleColor() {
		switch (type) {
		case NORMAL:
			return getChannelRule().getEnabled().getColor();
		case SPECIAL:
			return getSpecialChannel().getEnabled().getColor();
		default:
			return null;
		}
	}

	/**
	 * 获取渠道结算规则摘要。
	 * 
	 * @return 返回渠道结算规则摘要。
	 */
	public String getChannelRuleSummary() {
		switch (type) {
		case NORMAL:
			return getChannelRule().getSummary();
		case SPECIAL:
			return getSpecialChannel().getSummary();
		default:
			return null;
		}
	}

	/**
	 * 判断渠道排期是否可售。
	 * 
	 * @return 返回渠道排期是否可售。
	 */
	public Boolean isSalable() {
		return getCinema().getSalable() && getChannel().getSalable()
				&& isValid() && status == ShelveStatus.ON
				&& getStopSellTime().after(new Date());
	}

	/**
	 * 判断渠道排期是否可以进行上架操作。
	 * 
	 * @return 返回渠道排期是否可以进行上架操作。
	 */
	public Boolean isOnable() {
		return getCinema().getSalable() && getChannel().getSalable()
				&& isValid() && status == ShelveStatus.OFF;
	}

	/**
	 * 判断渠道排期是否可以进行下架操作。
	 * 
	 * @return 返回渠道排期是否可以进行下架操作。
	 */
	public Boolean isOffable() {
		return status == ShelveStatus.ON;
	}

	/**
	 * 判断渠道排期是否有效。
	 * 
	 * @return 返回渠道排期是否有效。
	 */
	public Boolean isValid() {
		return getCinema().getStatus() == EnabledStatus.ENABLED
				&& getCinema().getTicketSetted()
				&& getCinema().provideShowType(showType)
				&& getHall().getStatus() == HallStatus.ENABLED
				&& getChannel().getOpened() && status != ShelveStatus.INVALID
				&& expireTime.after(new Date());
	}

	/**
	 * 创建渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @param cinemaRule
	 *            影院结算规则
	 * @param channelRule
	 *            渠道结算规则
	 * @param benefitCardTypeRules
	 *            权益卡规则列表
	 * @return 返回渠道排期。
	 */
	public static ChannelShow create(Show show, CinemaRule cinemaRule,
			ChannelRule channelRule,
			List<BenefitCardTypeRule> benefitCardTypeRules) {
		ChannelShow channelShow = create(show, channelRule.getChannel());
		channelShow.setType(ChannelShowType.NORMAL);
		channelShow.setCinemaRule(cinemaRule);
		channelShow.setChannelRule(channelRule);
		// 如果普通类型的渠道排期停售时间大于影院结算策略截止时间或渠道结算策略截止时间，则把停售时间设置为影院结算策略截止时间或渠道结算策略截止时间。
		Date cinemaPolicyEndTime = DateUtils.getNextDay(cinemaRule.getPolicy()
				.getEndDate());
		Date channelPolicyEndTime = DateUtils.getNextDay(channelRule.getGroup()
				.getPolicy().getEndDate());
		if (cinemaPolicyEndTime.compareTo(channelPolicyEndTime) < 0) {
			if (channelShow.getStopSellTime().after(cinemaPolicyEndTime)) {
				channelShow.setStopSellTime(cinemaPolicyEndTime);
			}
		} else {
			if (channelShow.getStopSellTime().after(channelPolicyEndTime)) {
				channelShow.setStopSellTime(channelPolicyEndTime);
			}
		}
		channelShow.calPrice();
		List<BenefitCardSettle> settles = new ArrayList<BenefitCardSettle>();
		for (BenefitCardTypeRule rule : benefitCardTypeRules) {
			if (rule.getValid() == ValidStatus.VALID
					&& rule.getEnabled() == EnabledStatus.ENABLED
					&& rule.getType().getChannels()
							.contains(channelRule.getChannel())) {
				settles.add(BenefitCardSettle.create(channelShow, rule));
			}
		}
		channelShow.setBenefitCardSettles(settles);
		return channelShow;
	}

	/**
	 * 创建渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @param specialChannel
	 *            特殊定价渠道
	 * @param benefitCardTypeRules
	 *            权益卡规则列表
	 * @return 返回渠道排期。
	 */
	public static ChannelShow create(Show show, SpecialChannel specialChannel,
			List<BenefitCardTypeRule> benefitCardTypeRules) {
		ChannelShow channelShow = create(show, specialChannel.getChannel());
		channelShow.setType(ChannelShowType.SPECIAL);
		channelShow.setSpecialRule(specialChannel.getRule());
		channelShow.setSpecialChannel(specialChannel);
		// 如果特价渠道排期停售时间大于特殊价格策略截止时间，则把停售时间设置为特殊价格策略截止时间。
		Date specialPolicyEndTime = DateUtils.getNextDay(specialChannel
				.getRule().getPolicy().getEndDate());
		if (channelShow.getStopSellTime().after(specialPolicyEndTime)) {
			channelShow.setStopSellTime(specialPolicyEndTime);
		}
		channelShow.calPrice();
		List<BenefitCardSettle> settles = new ArrayList<BenefitCardSettle>();
		for (BenefitCardTypeRule rule : benefitCardTypeRules) {
			if (rule.getValid() == ValidStatus.VALID
					&& rule.getEnabled() == EnabledStatus.ENABLED
					&& rule.getType().getChannels()
							.contains(specialChannel.getChannel())) {
				settles.add(BenefitCardSettle.create(channelShow, rule));
			}
		}
		channelShow.setBenefitCardSettles(settles);
		return channelShow;
	}

	/**
	 * 创建渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @param channel
	 *            渠道
	 * @return 返回渠道排期。
	 */
	private static ChannelShow create(Show show, Channel channel) {
		ChannelShow channelShow = new ChannelShow();
		channelShow.setShow(show);
		channelShow.setChannel(channel);
		channelShow.setCinema(show.getCinema());
		channelShow.setHall(show.getHall());
		channelShow.setFilm(show.getFilm());
		channelShow.setProvider(show.getProvider());
		channelShow.setShowCode(show.getCode());
		channelShow.setFilmCode(show.getFilmCode());
		channelShow.setShowType(show.getShowType());
		channelShow.setLanguage(show.getLanguage());
		channelShow.setShowTime(show.getShowTime());
		channelShow.setExpireTime(show.getExpireTime());
		channelShow.setDuration(show.getDuration());
		Integer stopSellTime = channel.getSettings().getStopSellTime();
		channelShow.setStopSellTime(new DateTime(show.getShowTime())
				.minusMinutes(stopSellTime).toDate());
		Integer stopRevokeTime = channel.getSettings().getStopRevokeTime();
		channelShow.setStopRevokeTime(new DateTime(show.getShowTime())
				.minusMinutes(stopRevokeTime).toDate());
		channelShow.setMinPrice(show.getMinPrice());
		channelShow.setStdPrice(show.getStdPrice());
		// 当影院和渠道都是可售状态时，设置渠道排期为上架状态，否则是下架状态
		if (show.getCinema().getSalable() && channel.getSalable()) {
			channelShow.setStatus(ShelveStatus.ON);
		}
		channelShow.autoFillIn();
		return channelShow;
	}

	/**
	 * 计算价格。
	 */
	private void calPrice() {
		// 根据排期类型设置影院结算价、渠道结算价、票房价、接入费
		switch (type) {
		case NORMAL:
			calCinemaPrice(cinemaRule.getSettleRule());
			calChannelPrice(channelRule.getSettleRule());
			calSubmitPrice(cinemaRule.getPolicy().getAmount());
			connectFee = channelRule.getGroup().getConnectFee();
			// 计算价格是否可以低于最低价
			if (!cinemaRule.getSettleRule().getCinemaPriceBelowMinPrice()
					&& cinemaPrice < minPrice) {
				cinemaPrice = minPrice;
			}
			if (!channelRule.getSettleRule().getChannelPriceBelowMinPrice()
					&& channelPrice < minPrice) {
				channelPrice = minPrice;
			}
			if (!channelRule.getSettleRule().getSumbitPriceBelowMinPrice()
					&& submitPrice < minPrice) {
				submitPrice = minPrice;
			}
			break;
		case SPECIAL:
			calCinemaPrice(specialChannel.getRule().getSettleRule());
			calChannelPrice(specialChannel.getSettleRule());
			calSubmitPrice(specialChannel.getRule().getAmount());
			connectFee = specialChannel.getConnectFee();
			// 计算价格是否可以低于最低价
			if (!specialChannel.getRule().getSettleRule()
					.getCinemaPriceBelowMinPrice()
					&& cinemaPrice < minPrice) {
				cinemaPrice = minPrice;
			}
			if (!specialChannel.getRule().getSettleRule()
					.getChannelPriceBelowMinPrice()
					&& channelPrice < minPrice) {
				channelPrice = minPrice;
			}
			if (!specialChannel.getRule().getSettleRule()
					.getSumbitPriceBelowMinPrice()
					&& submitPrice < minPrice) {
				submitPrice = minPrice;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 计算票房价。
	 */
	private void calSubmitPrice(Double amount) {
		// 根据上报类型设置票房价
		switch (getSubmitType()) {
		case CINEMA_PRICE:
			calSubmitPriceForSubmitCinemaPrice(amount);
			break;
		case CHANNEL_PRICE:
			calSubmitPriceForSubmitChannelPrice(amount);
			break;
		case MIN_PRICE:
			calSubmitPriceForSubmitMinPrice(amount);
			break;
		default:
			break;
		}

	}

	/**
	 * 当上报类型为影院结算价时计算票房价。
	 */
	private void calSubmitPriceForSubmitCinemaPrice(Double amount) {
		// 如果渠道结算价大于影院结算价，则计为手续费，否则计为补贴费。
		if (channelPrice > cinemaPrice) {
			circuitFee = NumberUtils.sub(channelPrice, cinemaPrice);
		} else {
			subsidyFee = NumberUtils.sub(cinemaPrice, channelPrice);
		}
		// 如果影院结算价小于最低限价，则设置为最低限价，并保持影院结算价和渠道结算价之间的价差不变。
		// if (cinemaPrice < minPrice) {
		// cinemaPrice = minPrice;
		// channelPrice = cinemaPrice + circuitFee - subsidyFee;
		// }
		submitPrice = NumberUtils.add(cinemaPrice, amount);
	}

	/**
	 * 当上报类型为渠道结算价时计算票房价。
	 */
	private void calSubmitPriceForSubmitChannelPrice(Double amount) {
		// 如果渠道结算价小于最低限价，则设置为最低限价。
		// if (channelPrice < minPrice) {
		// channelPrice = minPrice;
		// }
		submitPrice = NumberUtils.add(channelPrice, amount);
	}

	/**
	 * 当上报类型为最低价时计算票房价。
	 */
	private void calSubmitPriceForSubmitMinPrice(Double amount) {
		submitPrice = NumberUtils.add(minPrice, amount);
	}

	/**
	 * 计算影院结算价。
	 * 
	 * @param settleRule
	 *            影院结算价规则
	 */
	private void calCinemaPrice(SettleRule settleRule) {
		switch (settleRule.getBasisType()) {
		case STD_PRICE:
			cinemaPrice = settleRule.cal(stdPrice);
			break;
		case MIN_PRICE:
			cinemaPrice = settleRule.cal(minPrice);
			break;
		case CINEMA_SETTLE:
			throw new UncheckedException("设置了错误的结算类型["
					+ SettleBasisType.CINEMA_SETTLE.getText() + "]");
		default:
			break;
		}

	}

	/**
	 * 计算渠道结算价格。
	 * 
	 * @param settleRule
	 *            渠道结算价规则
	 */
	private void calChannelPrice(SettleRule settleRule) {
		switch (settleRule.getBasisType()) {
		case CINEMA_SETTLE:
			channelPrice = settleRule.cal(cinemaPrice);
			break;
		case STD_PRICE:
			channelPrice = settleRule.cal(stdPrice);
			break;
		case MIN_PRICE:
			channelPrice = settleRule.cal(minPrice);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取上报类型。
	 * 
	 * @return 返回上报类型。
	 */
	public SubmitType getSubmitType() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getPolicy().getSubmitType();
		case SPECIAL:
			return getSpecialRule().getSubmitType();
		default:
			return null;
		}
	}

	/**
	 * 判断渠道排期关键信息是否相同。
	 * 
	 * @param other
	 *            其它渠道排期
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean equalsTo(ChannelShow other) {
		EqualsBuilder builder = new EqualsBuilder()
				.append(getChannel(), other.getChannel())
				.append(getFilmCode(), other.getFilmCode())
				.append(getHall(), other.getHall())
				.append(getType(), other.getType())
				.append(getShowCode(), other.getShowCode())
				.append(getLanguage(), other.getLanguage())
				.append(getShowType(), other.getShowType())
				.append(getShowTime(), other.getShowTime())
				.append(getExpireTime(), other.getExpireTime())
				.append(getMinPrice(), other.getMinPrice())
				.append(getStdPrice(), other.getStdPrice())
				.append(getCinemaPrice(), other.getCinemaPrice())
				.append(getChannelPrice(), other.getChannelPrice())
				.append(getSubmitPrice(), other.getSubmitPrice())
				.append(getConnectFee(), other.getConnectFee())
				.append(getCircuitFee(), other.getCircuitFee())
				.append(getSubsidyFee(), other.getSubsidyFee());
		if (getType() == ChannelShowType.NORMAL) {
			builder.append(getCinemaRule(), other.getCinemaRule()).append(
					getChannelRule(), other.getChannelRule());
		}
		if (getType() == ChannelShowType.SPECIAL) {
			builder.append(getSpecialRule(), other.getSpecialRule()).append(
					getSpecialChannel(), other.getSpecialChannel());
		}
		if (builder.isEquals()) {
			return benefitCardSettlesEqualsTo(other);
		} else {
			return false;
		}
	}

	/**
	 * 判断渠道排期权益卡信息是否相同。
	 * 
	 * @param other
	 *            其它渠道排期
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean benefitCardSettlesEqualsTo(ChannelShow other) {
		List<BenefitCardSettle> settles = other.getBenefitCardSettles();
		if (CollectionUtils.isEmpty(getBenefitCardSettles())
				&& CollectionUtils.isNotEmpty(settles)) {
			return false;
		}
		if (CollectionUtils.isNotEmpty(getBenefitCardSettles())
				&& CollectionUtils.isEmpty(settles)) {
			return false;
		}
		for (BenefitCardSettle otherSettle : settles) {
			boolean isEquals = false;
			for (BenefitCardSettle settle : getBenefitCardSettles()) {
				if (otherSettle.equalsTo(settle)) {
					isEquals = true;
					break;
				}
			}
			if (!isEquals) {
				return false;
			}
		}
		for (BenefitCardSettle settle : getBenefitCardSettles()) {
			boolean isEquals = false;
			for (BenefitCardSettle otherSettle : settles) {
				if (otherSettle.equalsTo(settle)) {
					isEquals = true;
					break;
				}
			}
			if (!isEquals) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compareTo(ChannelShow other) {
		return other.getCode().compareTo(getCode());
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ChannelShowType getType() {
		return type;
	}

	public void setType(ChannelShowType type) {
		this.type = type;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
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

	public CinemaRule getCinemaRule() {
		return cinemaRule;
	}

	public void setCinemaRule(CinemaRule cinemaRule) {
		this.cinemaRule = cinemaRule;
	}

	public ChannelRule getChannelRule() {
		return channelRule;
	}

	public void setChannelRule(ChannelRule channelRule) {
		this.channelRule = channelRule;
	}

	public SpecialRule getSpecialRule() {
		return specialRule;
	}

	public void setSpecialRule(SpecialRule specialRule) {
		this.specialRule = specialRule;
	}

	public SpecialChannel getSpecialChannel() {
		return specialChannel;
	}

	public void setSpecialChannel(SpecialChannel specialChannel) {
		this.specialChannel = specialChannel;
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

	public Date getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(Date stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public Date getStopRevokeTime() {
		return stopRevokeTime;
	}

	public void setStopRevokeTime(Date stopRevokeTime) {
		this.stopRevokeTime = stopRevokeTime;
	}

	public ShelveStatus getStatus() {
		return status;
	}

	public void setStatus(ShelveStatus status) {
		this.status = status;
	}

	public String getInvalidCode() {
		return invalidCode;
	}

	public void setInvalidCode(String expireCode) {
		this.invalidCode = expireCode;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<BenefitCardSettle> getBenefitCardSettles() {
		return benefitCardSettles;
	}

	public void setBenefitCardSettles(List<BenefitCardSettle> benefitCardSettles) {
		this.benefitCardSettles = benefitCardSettles;
	}

}
