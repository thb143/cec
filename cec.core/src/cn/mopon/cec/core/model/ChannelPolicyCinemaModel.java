package cn.mopon.cec.core.model;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 影院所在渠道模型。
 */
public class ChannelPolicyCinemaModel implements
		Comparable<ChannelPolicyCinemaModel> {
	/** 影院 */
	private Cinema cinema;
	/** 城市 */
	private City city;
	/** 规则分组 */
	private ChannelRuleGroup group;

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public ChannelRuleGroup getGroup() {
		return group;
	}

	public void setGroup(ChannelRuleGroup group) {
		this.group = group;
	}

	@Override
	public int compareTo(ChannelPolicyCinemaModel o) {
		return city.getCode().compareTo(o.getCity().getCode());
	}
}