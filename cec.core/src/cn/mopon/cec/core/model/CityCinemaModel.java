package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 影院按城市分组模型。
 */
public class CityCinemaModel implements Comparable<CityCinemaModel> {
	private City city;
	private List<Cinema> cinemas = new ArrayList<Cinema>();

	/**
	 * 增加影院。
	 * 
	 * @param cinema
	 *            影院
	 */
	public void addCinema(Cinema cinema) {
		cinemas.add(cinema);
	}

	@Override
	public int compareTo(CityCinemaModel o) {
		return city.compareTo(o.getCity());
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}
}