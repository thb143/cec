package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;

/**
 * 外部排期分组模型。
 */
public class ExternalShowsModel {
	private List<Show> newShows = new ArrayList<Show>();
	private List<Show> updateShows = new ArrayList<Show>();
	private List<Show> deleteShows = new ArrayList<Show>();

	/**
	 * 获取外部排期总数。
	 * 
	 * @return 返回外部排期总数。
	 */
	public Integer getCount() {
		return newShows.size() + updateShows.size();
	}

	/**
	 * 增加新增的外部排期。
	 * 
	 * @param show
	 *            外部排期
	 */
	public void addNewShow(Show show) {
		show.setChannelShows(new ArrayList<ChannelShow>());
		newShows.add(show);
	}

	/**
	 * 增加更新的外部排期。
	 * 
	 * @param show
	 *            外部排期
	 */
	public void addUpdateShow(Show show) {
		updateShows.add(show);
	}

	/**
	 * 增加删除的外部排期。
	 * 
	 * @param show
	 *            外部排期
	 */
	public void addDeleteShow(Show show) {
		deleteShows.add(show);
	}

	public List<Show> getNewShows() {
		return newShows;
	}

	public void setNewShows(List<Show> newShows) {
		this.newShows = newShows;
	}

	public List<Show> getUpdateShows() {
		return updateShows;
	}

	public void setUpdateShows(List<Show> updateShows) {
		this.updateShows = updateShows;
	}

	public List<Show> getDeleteShows() {
		return deleteShows;
	}

	public void setDeleteShows(List<Show> deleteShows) {
		this.deleteShows = deleteShows;
	}
}