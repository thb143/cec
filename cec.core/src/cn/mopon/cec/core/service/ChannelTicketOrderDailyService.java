package cn.mopon.cec.core.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.TicketOrderStat;
import cn.mopon.cec.core.enums.TicketOrderKind;
import cn.mopon.cec.core.model.StatSearchModel;
import coo.base.model.Page;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 报表统计、渠道维度日统计。
 */
@Service
public class ChannelTicketOrderDailyService extends TicketOrderDailyService {

	@Resource
	private Dao<ChannelTicketOrderDaily> channelTicketOrderDailyDao;

	/**
	 * 查询渠道销售报表。
	 * 
	 * @param search
	 *            查询对象
	 * @return 渠道销售报表数据。
	 */
	@Transactional(readOnly = true)
	public Page<ChannelTicketOrderDaily> searchChannelTicketOrderStat(
			StatSearchModel search) {
		Page<Channel> channelPage = searchChannel(search);
		Page<ChannelTicketOrderDaily> result = new Page<ChannelTicketOrderDaily>(
				channelPage.getCount(), channelPage.getNumber(),
				channelPage.getSize());
		if (channelPage.getCount() < 1) {
			return result;
		}

		for (Channel channel : channelPage.getContents()) {
			FullTextCriteria statCriteria = channelTicketOrderDailyDao
					.createFullTextCriteria();
			statCriteria.addLuceneQuery(search.genQuery("statDate"),
					BooleanClause.Occur.MUST);
			statCriteria.addFilterField("channel.id", channel.getId());
			List<ChannelTicketOrderDaily> orderDailies = channelTicketOrderDailyDao
					.searchBy(statCriteria);

			result.getContents().add(
					mergeTicketOrderDailys(orderDailies, channel));
		}

		return result;
	}

	/**
	 * 查询所有渠道的每天销售总和
	 * 
	 * @param date
	 *            指定日期
	 * @return 所有渠道的每天销售总和
	 */
	@Transactional(readOnly = true)
	public ChannelTicketOrderDaily searchTicketOrderStatForDay(Date date) {
		StatSearchModel searchModel = new StatSearchModel();
		searchModel.setStartDate(date);
		searchModel.setEndDate(date);
		searchModel.setPageSize(Integer.MAX_VALUE);
		FullTextCriteria statCriteria = channelTicketOrderDailyDao
				.createFullTextCriteria();
		statCriteria.addLuceneQuery(searchModel.genQuery("statDate"),
				BooleanClause.Occur.MUST);
		List<ChannelTicketOrderDaily> orderDailies = channelTicketOrderDailyDao
				.searchBy(statCriteria);
		return mergeTicketOrderDailys(orderDailies, null);
	}

	/**
	 * 合并日统计数据。
	 * 
	 * @param orderDailies
	 *            日统计数据
	 * @param channel
	 *            渠道
	 * @return 合并之后的统计数据。
	 */
	private ChannelTicketOrderDaily mergeTicketOrderDailys(
			List<ChannelTicketOrderDaily> orderDailies, Channel channel) {
		ChannelTicketOrderDaily result = new ChannelTicketOrderDaily(channel);
		if (CollectionUtils.isEmpty(orderDailies)) {
			return result;
		}

		for (ChannelTicketOrderDaily daily : orderDailies) {
			mergeTicketOrderDaily(daily, result);
		}
		return result;
	}

	/**
	 * 查询指定条件下的订单明细。
	 * 
	 * @param searchModel
	 *            查询对象
	 * @return 成功订单明细。
	 */
	@Transactional(readOnly = true)
	public Page<TicketOrderStat> searchTicketOrderStat(
			StatSearchModel searchModel) {
		FullTextCriteria criteria = ticketOrderStatDao.createFullTextCriteria();
		criteria.addLuceneQuery(searchModel.genQuery("statDate"),
				BooleanClause.Occur.MUST);
		if (StringUtils.isNotEmpty(searchModel.getChannelId())) {
			criteria.addFilterField("channel.id", searchModel.getChannelId());
		}
		if (StringUtils.isNotEmpty(searchModel.getCinemaId())) {
			criteria.addFilterField("cinema.id", searchModel.getCinemaId());
		}
		if (searchModel.getShowType() != null) {
			criteria.addFilterField("showType", searchModel.getShowType()
					.getValue());
		}
		criteria.addFilterField("kind", TicketOrderKind.NORMAL.getValue(),
				TicketOrderKind.REVOKE.getValue());
		criteria.addSortDesc("confirmTime", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return ticketOrderStatDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

}
