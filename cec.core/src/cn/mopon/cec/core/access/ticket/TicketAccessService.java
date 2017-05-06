package cn.mopon.cec.core.access.ticket;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

/**
 * 网络售票接入接口。
 */
public interface TicketAccessService {
	/**
	 * 获取指定影院及关联影厅。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @return 返回影院基础信息及关联影厅信息。
	 */
	Cinema getCinema(String cinemaCode);

	/**
	 * 获取指定影厅的座位列表。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回影厅的座位列表。
	 */

	List<Seat> getHallSeats(Hall hall);

	/**
	 * 获取指定时间范围内的排期列表。
	 * 
	 * @param cinema
	 *            影院
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 返回指定时间范围内的排期列表。
	 */
	List<Show> getShows(Cinema cinema, Date startDate, Date endDate);

	/**
	 * 获取指定排期的排期座位列表。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param status
	 *            排期座位状态
	 * @return 返回指定排期的排期座位列表。
	 */
	List<ShowSeat> getShowSeats(ChannelShow channelShow, SellStatus... status);

	/**
	 * 锁定座位。
	 * 
	 * @param order
	 *            订单
	 * @return 返回地面订单。
	 */
	TicketOrder lockSeat(TicketOrder order);

	/**
	 * 释放座位。
	 * 
	 * @param order
	 *            订单
	 */
	void releaseSeat(TicketOrder order);

	/**
	 * 确认订单。
	 * 
	 * @param order
	 *            订单
	 * @return 返回订单。
	 */
	TicketOrder submitOrder(TicketOrder order);

	/**
	 * 退票。
	 * 
	 * @param order
	 *            订单
	 * @return 返回订单。
	 */
	Boolean refundTicket(TicketOrder order);

	/**
	 * 获取订单详情。
	 * 
	 * @param order
	 *            订单
	 * @return 返回订单。
	 */
	TicketOrder queryOrder(TicketOrder order);

	/**
	 * 查询订单打印状态。
	 * 
	 * @param order
	 *            订单
	 * @return 返回订单。
	 */
	TicketOrder queryPrint(TicketOrder order);

	/**
	 * 确认打票。
	 * 
	 * @param order
	 *            订单
	 * @return 返回订单。
	 */
	boolean confirmPrint(TicketOrder order);
}