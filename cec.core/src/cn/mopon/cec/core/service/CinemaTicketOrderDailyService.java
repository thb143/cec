package cn.mopon.cec.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaTicketOrderDaily;
import cn.mopon.cec.core.model.StatSearchModel;
import coo.base.model.Page;
import coo.base.util.CollectionUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 报表统计、影院维度日统计。
 */
@Service
public class CinemaTicketOrderDailyService extends TicketOrderDailyService {

	@Resource
	private Dao<CinemaTicketOrderDaily> cinemaTicketOrderDailyDao;



	/**
	 * 查询影院销售报表。
	 * 
	 * @param search
	 *            查询对象
	 * @return 渠道影院报表数据。
	 */
	@Transactional(readOnly = true)
	public Page<CinemaTicketOrderDaily> searchCinemaTicketOrderStat(
			StatSearchModel search) {
		Page<Cinema> cinemaPage = searchCinema(search);

		Page<CinemaTicketOrderDaily> result = new Page<CinemaTicketOrderDaily>(
				cinemaPage.getCount(), cinemaPage.getNumber(),
				cinemaPage.getSize());
		if (cinemaPage.getCount() < 1) {
			return result;
		}

		for (Cinema cinema : cinemaPage.getContents()) {
			FullTextCriteria statCriteria = cinemaTicketOrderDailyDao
					.createFullTextCriteria();
			statCriteria.addLuceneQuery(search.genQuery("statDate"),
					BooleanClause.Occur.MUST);
			statCriteria.addFilterField("cinema.id", cinema.getId());
			List<CinemaTicketOrderDaily> orderDailies = cinemaTicketOrderDailyDao
					.searchBy(statCriteria);
			result.getContents().add(
					mergeTicketOrderDailys(orderDailies, cinema));
		}
		return result;
	}

	/**
	 * 合并日统计数据。
	 * 
	 * @param orderDailies
	 *            日统计数据
	 * @param cinema
	 *            影院
	 * @return 合并之后的统计数据。
	 */
	private CinemaTicketOrderDaily mergeTicketOrderDailys(
			List<CinemaTicketOrderDaily> orderDailies, Cinema cinema) {
		CinemaTicketOrderDaily result = new CinemaTicketOrderDaily(cinema);
		if (CollectionUtils.isEmpty(orderDailies)) {
			return result;
		}

		for (CinemaTicketOrderDaily daily : orderDailies) {
			mergeTicketOrderDaily(daily, result);
		}
		return result;
	}

}
