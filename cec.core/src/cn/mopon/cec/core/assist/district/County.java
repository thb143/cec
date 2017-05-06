package cn.mopon.cec.core.assist.district;

import org.hibernate.search.annotations.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 辖区。
 */
@SuppressWarnings("serial")
@XStreamAlias("county")
public class County extends District {
	/** 关联城市 */
	@JsonBackReference
	@XStreamOmitField
	@IndexedEmbedded
	private City city;

	/**
	 * 获取包含省份城市的完整名称。
	 * 
	 * @return 返回包含省份城市的完整名称。
	 */
	@JsonIgnore
	public String getFullName() {
		return city.getFullName() + "-" + getName();
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
