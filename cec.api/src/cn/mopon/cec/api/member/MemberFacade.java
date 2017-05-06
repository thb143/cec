package cn.mopon.cec.api.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.TicketOrder;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 会员接口面板。
 */
@Service
public class MemberFacade {
	@Resource
	private Dao<Cinema> cinemaDao;
	@Resource
	private Dao<TicketOrder> ticketOrderDao;

	/**
	 * 查询影院。
	 * 
	 * @param cinemaCode
	 *            影院编码。
	 * @return 返回影院。
	 */
	public Cinema queryCinema(String cinemaCode) {
		Cinema cinema = cinemaDao.searchUnique("code", cinemaCode);
		if (cinema == null) {
			ApiException.thrown(MemberReplyCode.CINEMA_NOT_EXIST);
		}
		return cinema;
	}

	/**
	 * 查询渠道指定订单号的选座票订单。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param orderCode
	 *            订单号
	 * @return 返回渠道指定订单号的选座票订单。
	 */
	public TicketOrder queryTicketOrder(String channelCode, String orderCode) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("code", orderCode);
		TicketOrder order = ticketOrderDao.searchUnique(criteria);
		if (order == null) {
			ApiException.thrown(MemberReplyCode.ORDER_NOT_EXIST);
		}
		return order;
	}
}
