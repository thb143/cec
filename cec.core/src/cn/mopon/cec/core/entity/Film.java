package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.joda.time.Interval;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.model.FilmMinPriceItem;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 影片。
 */
@Entity
@Table(name = "CEC_Film")
@Indexed(index = "Film")
@Cache(region = "Film", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Film extends ResourceEntity<User> {
	/** 编码 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "影片编号")
	private String code;
	/** 名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "影片名称")
	private String name;
	/** 时长（分钟） */
	private Integer duration;
	/** 影片公映日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date publishDate;
	/** 发行商 */
	private String publisher;
	/** 导演 */
	private String director;
	/** 演员 */
	private String cast;
	/** 简介 */
	private String intro;
	/** 放映类型 */
	private String showTypes;
	/** 发行国家 */
	private String country;
	/** 影片类型 */
	private String type;
	/** 影片语言 */
	private String language;
	/** 海报 */
	private String poster;
	/** 剧照 */
	private String stills;
	/** 预告片 */
	private String trailers;
	/** 精彩看点 */
	private String highlight;
	/** 关联分组分类 */
	@OneToMany(mappedBy = "film", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("createDate desc")
	private List<MinPriceGroup> groups = new ArrayList<>();

	/**
	 * 获取影片最低价。
	 * 
	 * @param city
	 *            城市
	 * @param showType
	 *            放映类型
	 * @param showTime
	 *            放映时间
	 * @return 返回影片最低价。
	 */
	public Double getMinPrice(City city, ShowType showType, Date showTime) {
		MinPriceGroup group = getGroupByCity(city, showTime);
		if (group != null) {
			return group.getMinPrice(showType, showTime);
		}
		return 0D;
	}

	/**
	 * 根据城市获取对应的最低价分组。
	 * 
	 * @param city
	 *            城市
	 * @return 返回对应的最低价分组。
	 */
	private MinPriceGroup getGroupByCity(City city, Date showTime) {
		for (MinPriceGroup group : getGroups()) {
			if (StringUtils.isNotEmpty(group.getCityCode())
					&& group.getCityCode().contains(city.getCode())) {
				for (FilmMinPriceItem minPrice : group.getMinPrices()
						.getItems()) {
					Interval policyInterval = DateUtils.getInterval(minPrice
							.getStartDate().toDate(), DateUtils
							.getNextDay(minPrice.getEndDate().toDate()));
					if (policyInterval.contains(showTime.getTime())) {
						return group;
					}
				}
			}
		}
		for (MinPriceGroup group : groups) {
			if (StringUtils.isEmpty(group.getCityCode())) {
				return group;
			}
		}
		return null;
	}

	/**
	 * 判断影片关键信息是否相同。
	 * 
	 * @param other
	 *            其它影片
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean equalsTo(Film other) {
		return new EqualsBuilder().append(getCode(), other.getCode())
				.append(getName(), other.getName())
				.append(getDuration(), other.getDuration())
				.append(getPublishDate(), other.getPublishDate())
				.append(getPublisher(), other.getPublisher())
				.append(getDirector(), other.getDirector())
				.append(getCast(), other.getCast())
				.append(getIntro(), other.getIntro())
				.append(getShowTypes(), other.getShowTypes())
				.append(getCountry(), other.getCountry())
				.append(getLanguage(), other.getLanguage())
				.append(getType(), other.getType())
				.append(getPoster(), other.getPoster())
				.append(getStills(), other.getStills())
				.append(getTrailers(), other.getTrailers())
				.append(getHighlight(), other.getHighlight()).isEquals();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getShowTypes() {
		return showTypes;
	}

	public void setShowTypes(String showTypes) {
		this.showTypes = showTypes;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getStills() {
		return stills;
	}

	public void setStills(String stills) {
		this.stills = stills;
	}

	public String getTrailers() {
		return trailers;
	}

	public void setTrailers(String trailers) {
		this.trailers = trailers;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	public List<MinPriceGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<MinPriceGroup> groups) {
		this.groups = groups;
	}
}