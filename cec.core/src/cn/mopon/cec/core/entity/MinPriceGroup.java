package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Indexed;
import org.joda.time.Interval;

import cn.mopon.cec.core.assist.district.DistrictHelper;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.model.FilmMinPriceItem;
import cn.mopon.cec.core.model.FilmMinPrices;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 最低价分组。
 */
@Entity
@Table(name = "CEC_MinPriceGroup")
@Indexed(index = "MinPriceGroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MinPriceGroup extends ResourceEntity<User> {
	/** 关联影片 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filmId")
	private Film film;
	/** 分组名称 */
	@LogField(text = "名称")
	private String name;
	/** 城市编码 */
	@LogField(text = "城市")
	private String cityCode;
	/** 最低价规则 */
	@Type(type = "Json")
	private FilmMinPrices minPrices = new FilmMinPrices();

	/**
	 * 获取最低价。
	 * 
	 * @param showType
	 *            放映类型
	 * @param showTime
	 *            放映时间
	 * @return 返回最低价。
	 */
	public Double getMinPrice(ShowType showType, Date showTime) {
		for (FilmMinPriceItem minPrice : getMinPrices().getItems()) {
			Interval policyInterval = DateUtils.getInterval(minPrice
					.getStartDate().toDate(), DateUtils.getNextDay(minPrice
					.getEndDate().toDate()));
			if (policyInterval.contains(showTime.getTime())) {
				return minPrice.getMinPriceByShowType(showType);
			}
		}
		return 0D;
	}

	/**
	 * 获取摘要信息。
	 * 
	 * @return 返回摘要信息。
	 */
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.isNotEmpty(getCitiesTexts())) {
			builder.append("城市：");
			builder.append(getCitiesTexts());
			builder.append("\n");
		}
		builder.append("规则：");
		for (FilmMinPriceItem model : getMinPrices().getItems()) {
			builder.append(model.getSummary()).append("\n");
		}
		return builder.toString();
	}

	/**
	 * 获取分组城市名称。
	 * 
	 * @return 返回城市名称，以","分隔。
	 */
	public String getCitiesTexts() {
		List<String> strs = new ArrayList<>();
		if (StringUtils.isNotEmpty(cityCode)) {
			for (String code : cityCode.split(",")) {
				strs.add(DistrictHelper.getDistrict(code).getName());
			}
			return StringUtils.join(strs, "，").concat("。");
		}
		return null;
	}

	/**
	 * 判断最低价分组关键信息是否相同。
	 * 
	 * @param other
	 *            其它最低价分组
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean equalsTo(MinPriceGroup other) {
		EqualsBuilder builder = new EqualsBuilder().append(getName(),
				other.getName()).append(getCityCode(), other.getCityCode());
		return builder.isEquals();
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public FilmMinPrices getMinPrices() {
		return minPrices;
	}

	public void setMinPrices(FilmMinPrices minPrices) {
		this.minPrices = minPrices;
	}

}