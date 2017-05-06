package cn.mopon.cec.core.assist.district;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.annotations.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 城市。
 */
@SuppressWarnings("serial")
@XStreamAlias("city")
public class City extends District {
	/** 关联省份 */
	@JsonBackReference
	@XStreamOmitField
	@IndexedEmbedded
	private Province province;
	/** 关联辖区 */
	@XStreamImplicit
	private List<County> counties = new ArrayList<County>();

	/**
	 * 根据辖区编码获取辖区。
	 * 
	 * @param countyCode
	 *            辖区编码
	 * @return 返回对应的辖区，如果没有找到返回null。
	 */
	public County getCounty(String countyCode) {
		for (County county : counties) {
			if (county.getCode().equals(countyCode)) {
				return county;
			}
		}
		return null;
	}

	/**
	 * 获取包含省份的完整名称。
	 * 
	 * @return 返回包含省份的完整名称。
	 */
	@JsonIgnore
	public String getFullName() {
		return province.getName() + "-" + getName();
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public List<County> getCounties() {
		return counties;
	}

	public void setCounties(List<County> counties) {
		this.counties = counties;
	}
}
