package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Seat;

/**
 * 外部座位分组模型。
 */
public class ExternalSeatsModel {
	private List<Seat> newSeats = new ArrayList<>();
	private List<Seat> updateSeats = new ArrayList<>();
	private List<Seat> deleteSeats = new ArrayList<>();

	/**
	 * 获取外部座位总数量。
	 * 
	 * @return 返回外部座位总数量。
	 */
	public Integer getCount() {
		return newSeats.size() + updateSeats.size();
	}

	/**
	 * 增加新增的外部座位。
	 * 
	 * @param seat
	 *            外部座位
	 */
	public void addNewSeat(Seat seat) {
		newSeats.add(seat);
	}

	/**
	 * 增加更新的外部座位。
	 * 
	 * @param seat
	 *            外部座位
	 */
	public void addUpdateSeat(Seat seat) {
		updateSeats.add(seat);
	}

	/**
	 * 增加删除的外部座位。
	 * 
	 * @param seat
	 *            外部座位
	 */
	public void addDeleteSeat(Seat seat) {
		deleteSeats.add(seat);
	}

	public List<Seat> getNewSeats() {
		return newSeats;
	}

	public void setNewSeats(List<Seat> newSeats) {
		this.newSeats = newSeats;
	}

	public List<Seat> getUpdateSeats() {
		return updateSeats;
	}

	public void setUpdateSeats(List<Seat> updateSeats) {
		this.updateSeats = updateSeats;
	}

	public List<Seat> getDeleteSeats() {
		return deleteSeats;
	}

	public void setDeleteSeats(List<Seat> deleteSeats) {
		this.deleteSeats = deleteSeats;
	}
}