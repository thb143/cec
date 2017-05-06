package cn.mopon.cec.core.model;

import cn.mopon.cec.core.assist.district.City;

/**
 * 影院所在城市模型。
 */
public class ChannelPolicyCityModel implements
		Comparable<ChannelPolicyCityModel> {
	/** 城市 */
	private City city;
	/** 影院个数 */
	private int cinemaCount = 0;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public int getCinemaCount() {
		return cinemaCount;
	}

	public void setCinemaCount(int cinemaCount) {
		this.cinemaCount = cinemaCount;
	}

	@Override
	public int compareTo(ChannelPolicyCityModel o) {
		return city.getCode().compareTo(o.getCity().getCode());
	}
}