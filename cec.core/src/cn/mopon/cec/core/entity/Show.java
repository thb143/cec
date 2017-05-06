package cn.mopon.cec.core.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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

import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ShowStatus;
import cn.mopon.cec.core.enums.ShowType;
import coo.base.util.DateUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumTextBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogBean;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 排期。
 */
@Entity
@Table(name = "CEC_Show")
@Indexed(index = "Show")
@Cache(region = "Show", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Show extends ResourceEntity<User> {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	@LogBean({ @LogField(property = "code", text = "影院编码"),
			@LogField(property = "name", text = "影院名称") })
	private Cinema cinema;
	/** 关联影厅 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	@LogBean({ @LogField(property = "code", text = "影厅编码"),
			@LogField(property = "name", text = "影厅名称") })
	private Hall hall;
	/** 关联影片 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filmId")
	@IndexedEmbedded(includePaths = { "code", "name" })
	@LogBean({ @LogField(property = "name", text = "影片名称") })
	private Film film;
	/** 接入商 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumTextBridge.class))
	private Provider provider;
	/** 编码 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "排期编码")
	private String code;
	/** 影片编码 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "影片编码")
	private String filmCode;
	/** 影片语言 */
	@LogField(text = "影片语言")
	private String language;
	/** 放映类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	@LogField(text = "放映类型")
	private ShowType showType;
	/** 放映时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "放映时间", format = DateUtils.SECOND)
	private Date showTime;
	/** 过期时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	@LogField(text = "过期时间", format = DateUtils.SECOND)
	private Date expireTime;
	/** 连场状态 */
	@LogField(text = "连场状态")
	private Boolean through = false;
	/** 最低价 */
	@LogField(text = "最低价")
	private Double minPrice = 0D;
	/** 标准价 */
	@LogField(text = "标准价")
	private Double stdPrice = 0D;
	/** 排期状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	@LogField(text = "状态")
	private ShowStatus status = ShowStatus.NEW;
	/** 时长（分钟） */
	@LogField(text = "时长")
	private Integer duration;
	/** 关联渠道排期列表 */
	@OneToMany(mappedBy = "show", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ChannelShow> channelShows = new ArrayList<>();
	/** 关联更新记录 */
	@OneToMany(mappedBy = "show", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OrderBy("createDate desc")
	private List<ShowUpdateLog> updateLogs = new ArrayList<>();

	/**
	 * 获取有效的渠道排期列表。
	 * 
	 * @return 返回有效的渠道排期列表。
	 */
	public List<ChannelShow> getValidChannelShows() {
		List<ChannelShow> channelShows = new ArrayList<>();
		for (ChannelShow channelShow : getChannelShows()) {
			if (channelShow.getStatus() != ShelveStatus.INVALID) {
				channelShows.add(channelShow);
			}
		}
		return channelShows;
	}

	/**
	 * 获取指定渠道当前有效的渠道排期。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回指定渠道当前有效的渠道排期，如果没有找到则返回null。
	 */
	public ChannelShow getValidChannelShow(Channel channel) {
		for (ChannelShow channelShow : getChannelShows()) {
			if (channelShow.getChannel().equals(channel)
					&& channelShow.getStatus() != ShelveStatus.INVALID) {
				return channelShow;
			}
		}
		return null;
	}

	/**
	 * 判断影院排期是否可售。
	 * 
	 * @return 返回影院排期是否可售。
	 */
	public Boolean isSalable() {
		return getCinema().getSalable() && isValid();
	}

	/**
	 * 判断影院排期是否有效。
	 * 
	 * @return 返回影院排期是否有效。
	 */
	public Boolean isValid() {
		return getCinema().getStatus() == EnabledStatus.ENABLED
				&& getCinema().getTicketSetted()
				&& getCinema().provideShowType(showType)
				&& getHall().getStatus() == HallStatus.ENABLED
				&& (status == ShowStatus.NEW || status == ShowStatus.UPDATE)
				&& expireTime.after(new Date());
	}

	/**
	 * 获取排期最低价。
	 * 
	 * @return 返回排期最低价。
	 */
	public Double getLargerMinPrice() {
		List<MinPriceGroup> groups = film.getGroups();
		if (groups.isEmpty() || groups == null) {
			return minPrice;
		}

		double filmMinPrice = film.getMinPrice(cinema.getCounty().getCity(),
				showType, showTime);
		BigDecimal local = new BigDecimal(filmMinPrice);
		BigDecimal remote = new BigDecimal(minPrice);
		return (local.compareTo(remote) > 0 ? local : remote).doubleValue();
	}

	/**
	 * 判断排期关键信息是否相同。
	 * 
	 * @param other
	 *            其它排期
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean equalsTo(Show other) {
		EqualsBuilder builder = new EqualsBuilder()
				.append(getCode(), other.getCode())
				.append(getFilmCode(), other.getFilmCode())
				.append(getHall().getCode(), other.getHall().getCode())
				.append(getShowType(), other.getShowType())
				.append(getLanguage(), other.getLanguage())
				.append(getShowTime(), other.getShowTime())
				.append(getMinPrice(), other.getMinPrice())
				.append(getStdPrice(), other.getStdPrice());
		return builder.isEquals();
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

	public Boolean getThrough() {
		return through;
	}

	public void setThrough(Boolean through) {
		this.through = through;
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

	public ShowStatus getStatus() {
		return status;
	}

	public void setStatus(ShowStatus status) {
		this.status = status;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<ChannelShow> getChannelShows() {
		return channelShows;
	}

	public void setChannelShows(List<ChannelShow> channelShows) {
		this.channelShows = channelShows;
	}

	public List<ShowUpdateLog> getUpdateLogs() {
		return updateLogs;
	}

	public void setUpdateLogs(List<ShowUpdateLog> updateLogs) {
		this.updateLogs = updateLogs;
	}
}