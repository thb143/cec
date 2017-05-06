package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * 影院选座票订单日统计。
 */
@Entity
@Table(name = "CEC_CinemaTicketOrderDaily")
@Indexed(index = "CinemaTicketOrderDaily")
public class CinemaTicketOrderDaily extends DailyStatEntity {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = "id")
	private Cinema cinema;

	/**
	 * 构造方法。
	 */
	public CinemaTicketOrderDaily() {

	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param statDate
	 *            统计日期
	 */
	public CinemaTicketOrderDaily(Cinema cinema, Date statDate) {
		this.cinema = cinema;
		this.statDate = statDate;
	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 */
	public CinemaTicketOrderDaily(Cinema cinema) {
		this.cinema = cinema;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}
}
