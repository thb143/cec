package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Hall;

/**
 * 外部影厅分组模型。
 */
public class ExternalHallsModel {
	private List<Hall> newHalls = new ArrayList<>();
	private List<Hall> updateHalls = new ArrayList<>();
	private List<Hall> deleteHalls = new ArrayList<>();

	/**
	 * 获取外部影厅总数量。
	 * 
	 * @return 返回外部影厅总数量。
	 */
	public Integer getCount() {
		return newHalls.size() + updateHalls.size();
	}

	/**
	 * 增加新增的外部影厅。
	 * 
	 * @param hall
	 *            外部影厅
	 */
	public void addNewHall(Hall hall) {
		newHalls.add(hall);
	}

	/**
	 * 增加更新的外部影厅。
	 * 
	 * @param hall
	 *            外部影厅
	 */
	public void addUpdateHall(Hall hall) {
		updateHalls.add(hall);
	}

	/**
	 * 增加删除的外部影厅。
	 * 
	 * @param hall
	 *            外部影厅
	 */
	public void addDeleteHall(Hall hall) {
		deleteHalls.add(hall);
	}

	public List<Hall> getNewHalls() {
		return newHalls;
	}

	public void setNewHalls(List<Hall> newHalls) {
		this.newHalls = newHalls;
	}

	public List<Hall> getUpdateHalls() {
		return updateHalls;
	}

	public void setUpdateHalls(List<Hall> updateHalls) {
		this.updateHalls = updateHalls;
	}

	public List<Hall> getDeleteHalls() {
		return deleteHalls;
	}

	public void setDeleteHalls(List<Hall> deleteHalls) {
		this.deleteHalls = deleteHalls;
	}
}