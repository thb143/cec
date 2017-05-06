package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Indexed;

import cn.mopon.cec.core.assist.district.City;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 城市分组。
 */
@Entity
@Table(name = "CEC_CityGroup")
@Indexed(index = "CityGroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CityGroup extends ResourceEntity<User> {
	/** 名称 */
	@LogField(text = "名称")
	private String name;
	/** 城市列表 */
	@LogField(text = "城市列表")
	@Type(type = "DistrictList")
	private List<City> cities = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
}