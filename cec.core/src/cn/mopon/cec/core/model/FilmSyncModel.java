package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import cn.mopon.cec.core.entity.Film;
import coo.core.model.DateRangeSearchModel;

/**
 * 影片同步模型。
 */
public class FilmSyncModel extends DateRangeSearchModel {
	/** 总记录数 */
	private Integer totalCount = 0;
	/** 影片列表 */
	private List<Film> films = new ArrayList<Film>();

	/**
	 * 构造方法。
	 */
	public FilmSyncModel() {
		this.setStartDate(DateTime.now().minusMonths(2).toLocalDate().toDate());
		this.setEndDate(DateTime.now().plusMonths(1).toLocalDate().toDate());
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Film> getFilms() {
		return films;
	}

	public void setFilms(List<Film> films) {
		this.films = films;
	}
}
