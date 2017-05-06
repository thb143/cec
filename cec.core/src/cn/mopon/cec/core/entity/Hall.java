package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.core.model.UuidEntity;

/**
 * 影厅。
 */
@Entity
@Table(name = "CEC_Hall")
@Indexed(index = "Hall")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hall extends UuidEntity implements Comparable<Hall> {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = "code")
	private Cinema cinema;
	/** 编码 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 名称 */
	@Field(analyze = Analyze.NO)
	private String name;
	/** 座位数量 */
	@NotNull
	private Integer seatCount = 0;
	/** 状态 */
	@Type(type = "IEnum")
	private HallStatus status = HallStatus.ENABLED;
	/** 自定义影厅类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	private HallType hallType;
	/** 备注 */
	private String remark;
	/** 关联座位 */
	@OneToMany(mappedBy = "hall", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Seat> seats = new ArrayList<Seat>();

	/**
	 * 获取影厅名称和影厅类型（用于策略规则显示）。
	 * 
	 * @return 影厅类型名称
	 */
	public String getFullName() {
		if (getHallType() != null) {
			return getName() + "(" + getHallType().getName() + ")";
		}
		return getName();
	}

	/**
	 * 获取指定编码的座位。
	 * 
	 * @param seatCode
	 *            座位编码
	 * @return 返回指定编码的座位。
	 */
	public Seat getSeat(String seatCode) {
		for (Seat seat : getSeats()) {
			if (seat.getCode().equals(seatCode)) {
				return seat;
			}
		}
		return null;
	}

	/**
	 * 获取原始的排期座位图。
	 * 
	 * @return 返回原始的排期座位图。
	 */
	public Map<String, ShowSeat> getShowSeatMap() {
		Map<String, ShowSeat> showSeatMap = new TreeMap<>();
		for (Seat seat : getSeats()) {
			showSeatMap.put(seat.getCode(), ShowSeat.createBySeat(seat));
		}
		return showSeatMap;
	}

	@Override
	public int compareTo(Hall other) {
		return getCode().compareTo(other.getCode());
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
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

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public Integer getSeatCount() {
		return seatCount;
	}

	public void setSeatCount(Integer seatCount) {
		this.seatCount = seatCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public HallStatus getStatus() {
		return status;
	}

	public void setStatus(HallStatus status) {
		this.status = status;
	}

	public HallType getHallType() {
		return hallType;
	}

	public void setHallType(HallType hallType) {
		this.hallType = hallType;
	}

}
