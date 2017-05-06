package cn.mopon.cec.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaChannelTicketOrderDaily;
import cn.mopon.cec.core.model.StatSearchModel;
import coo.base.model.Page;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 报表统计、影院、渠道 维度日统计。
 */
@Service
public class CinemaChannelTicketOrderDailyService extends
		TicketOrderDailyService {

	@Resource
	private Dao<CinemaChannelTicketOrderDaily> cinemaChannelTicketOrderDailyDao;

	/**
	 * 查询影院、渠道销售报表。
	 * 
	 * @param searchModel
	 *            查询对象
	 * @return 影院、渠道销售报表。
	 */
	@Transactional(readOnly = true)
	public Page<CinemaChannelTicketOrderDaily> searchCinemaChannelTicketOrderStat(
			StatSearchModel searchModel) {
		if (StringUtils.isNotEmpty(searchModel.getChannelId())) {
			return searchTicketOrderStatForChannel(searchModel);
		} else if (StringUtils.isNotEmpty(searchModel.getCinemaId())) {
			return searchTicketOrderStatForCinema(searchModel);
		}
		return new Page<>(0);
	}

	/**
	 * 查询渠道下的影院维度统计。
	 * 
	 * @param searchModel
	 *            查询对象
	 * @return 渠道下的影院维度统计。
	 */
	private Page<CinemaChannelTicketOrderDaily> searchTicketOrderStatForChannel(
			StatSearchModel searchModel) {
		Page<Cinema> cinemaPage = searchCinema(searchModel);

		Page<CinemaChannelTicketOrderDaily> result = new Page<CinemaChannelTicketOrderDaily>(
				cinemaPage.getCount(), cinemaPage.getNumber(),
				cinemaPage.getSize());
		if (cinemaPage.getCount() < 1) {
			return result;
		}

		Channel channel = channelDao.get(searchModel.getChannelId());
		for (Cinema cinema : cinemaPage.getContents()) {
			FullTextCriteria statCriteria = cinemaChannelTicketOrderDailyDao
					.createFullTextCriteria();
			statCriteria.addLuceneQuery(searchModel.genQuery("statDate"),
					BooleanClause.Occur.MUST);
			statCriteria.addFilterField("channel.id",
					searchModel.getChannelId());
			statCriteria.addFilterField("cinema.id", cinema.getId());
			List<CinemaChannelTicketOrderDaily> orderDailies = cinemaChannelTicketOrderDailyDao
					.searchBy(statCriteria);

			result.getContents().add(
					mergeTicketOrderDailys(orderDailies, cinema, channel));
		}
		return result;
	}

	/**
	 * 查询影院下的渠道维度统计。
	 * 
	 * @param searchModel
	 *            查询对象
	 * @return 渠道下的影院维度统计。
	 */
	private Page<CinemaChannelTicketOrderDaily> searchTicketOrderStatForCinema(
			StatSearchModel searchModel) {
		Page<Channel> channelPage = searchChannel(searchModel);
		Page<CinemaChannelTicketOrderDaily> result = new Page<>(
				channelPage.getCount(), channelPage.getNumber(),
				channelPage.getSize());
		if (channelPage.getCount() < 1) {
			return result;
		}

		Cinema cinema = cinemaDao.get(searchModel.getCinemaId());

		for (Channel channel : channelPage.getContents()) {
			FullTextCriteria statCriteria = cinemaChannelTicketOrderDailyDao
					.createFullTextCriteria();
			statCriteria.addLuceneQuery(searchModel.genQuery("statDate"),
					BooleanClause.Occur.MUST);
			statCriteria.addFilterField("channel.id", channel.getId());
			statCriteria.addFilterField("cinema.id", cinema.getId());
			List<CinemaChannelTicketOrderDaily> orderDailies = cinemaChannelTicketOrderDailyDao
					.searchBy(statCriteria);
			result.getContents().add(
					mergeTicketOrderDailys(orderDailies, cinema, channel));
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
	 * @param channel
	 *            渠道
	 * @return 合并之后的统计数据。
	 */
	private CinemaChannelTicketOrderDaily mergeTicketOrderDailys(
			List<CinemaChannelTicketOrderDaily> orderDailies, Cinema cinema,
			Channel channel) {
		CinemaChannelTicketOrderDaily result = new CinemaChannelTicketOrderDaily(
				cinema, channel);
		if (CollectionUtils.isEmpty(orderDailies)) {
			return result;
		}

		for (CinemaChannelTicketOrderDaily daily : orderDailies) {
			mergeTicketOrderDaily(daily, result);
		}
		return result;
	}

}
