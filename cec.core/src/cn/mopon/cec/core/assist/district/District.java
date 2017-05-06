package cn.mopon.cec.core.assist.district;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 行政区划抽象基类。
 */
@SuppressWarnings("serial")
public abstract class District implements Comparable<District>, Serializable {
	/** 编码 */
	@XStreamAsAttribute
	@Field(analyze = Analyze.NO)
	private String code;
	/** 名称 */
	@XStreamAsAttribute
	@Field(analyze = Analyze.NO)
	private String name;

	/**
	 * 判断行政区划是否省份。
	 * 
	 * @return 如果是省份返回true，否则返回false。
	 */
	@JsonIgnore
	public Boolean isProvince() {
		return code.endsWith("0000");
	}

	/**
	 * 判断行政区划是否城市。
	 * 
	 * @return 如果是城市返回true，否则返回false。
	 */
	@JsonIgnore
	public Boolean isCity() {
		return !isProvince() && code.endsWith("00");
	}

	/**
	 * 判断行政区划是否辖区。
	 * 
	 * @return 如果是辖区返回true，否则返回false。
	 */
	@JsonIgnore
	public Boolean isCounty() {
		return !isProvince() && !isCity();
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

	@Override
	public int compareTo(District o) {
		return code.compareTo(o.getCode());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getCode()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		District other = (District) obj;
		return new EqualsBuilder().append(getCode(), other.getCode())
				.isEquals();
	}
}