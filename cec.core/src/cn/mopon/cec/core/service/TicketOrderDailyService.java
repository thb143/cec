package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.DailyStatEntity;
import cn.mopon.cec.core.entity.TicketOrderStat;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.model.NormalOrderStat;
import cn.mopon.cec.core.model.RevokeOrderStat;
import cn.mopon.cec.core.model.ShowTypeStat;
import cn.mopon.cec.core.model.StatSearchModel;
import coo.base.model.Page;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 报表统计 日统计service 基类。
 */
public abstract class TicketOrderDailyService {
	@Resource
	protected Dao<TicketOrderStat> ticketOrderStatDao;
	@Resource
	protected Dao<Cinema> cinemaDao;
	@Resource
	protected Dao<Channel> channelDao;

	/**
	 * 合并同一维度值的两天的统计数据。
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            目标
	 */
	protected void mergeTicketOrderDaily(DailyStatEntity source,
			DailyStatEntity target) {
		mergeNormalOrderStat(source, target);
		mergeRefundOrderStat(source, target);
		mergeShowTypeStat(source, target);
	}

	/**
	 * 查询影院。
	 * 
	 * @param search
	 *            查询对象
	 * @return 影院page。
	 */
	@Transactional(readOnly = true)
	protected Page<Cinema> searchCinema(StatSearchModel search) {
		FullTextCriteria criteria = cinemaDao.createFullTextCriteria();
		criteria.addSearchField("name");
		criteria.addSearchField("code");
		if (StringUtils.isNotEmpty(search.getCinemaId())) {
			BooleanQuery multiFieldQuery = new BooleanQuery();
			for (String channelId : search.getCinemaId().split(",")) {
				TermQuery query = new TermQuery(new Term("id",
						channelId.toString()));
				multiFieldQuery.add(query, BooleanClause.Occur.SHOULD);
			}
			criteria.addLuceneQuery(multiFieldQuery, BooleanClause.Occur.MUST);
		}
		criteria.addSortAsc("ordinal", SortField.Type.INT);
		criteria.setKeyword(search.getKeyword());
		return cinemaDao.searchPage(criteria, search.getPageNo(),
				search.getPageSize());
	}

	/**
	 * 查询渠道。
	 * 
	 * @param search
	 *            查询对象
	 * @return 渠道page。
	 */
	@Transactional(readOnly = true)
	protected Page<Channel> searchChannel(StatSearchModel search) {
		FullTextCriteria criteria = channelDao.createFullTextCriteria();
		criteria.addSearchField("name");
		criteria.addSearchField("code");
		if (StringUtils.isNotEmpty(search.getChannelId())) {
			BooleanQuery multiFieldQuery = new BooleanQuery();
			for (String channelId : search.getChannelId().split(",")) {
				TermQuery query = new TermQuery(new Term("id",
						channelId.toString()));
				multiFieldQuery.add(query, BooleanClause.Occur.SHOULD);
			}
			criteria.addLuceneQuery(multiFieldQuery, BooleanClause.Occur.MUST);
		}
		criteria.addSortAsc("code", SortField.Type.STRING);
		criteria.setKeyword(search.getKeyword());
		return channelDao.searchPage(criteria, search.getPageNo(),
				search.getPageSize());
	}

	/**
	 * 合并放映类型订单统计。
	 * 
	 * @param sourceDaily
	 *            源
	 * @param targetDaily
	 *            目标
	 */
	private void mergeShowTypeStat(DailyStatEntity sourceDaily,
			DailyStatEntity targetDaily) {
		List<ShowTypeStat> stats = new ArrayList<>();
		for (ShowType showType : ShowType.values()) {
			ShowTypeStat stat = targetDaily.getShowTypeStat(showType);
			stat.addShowTypeStat(sourceDaily.getShowTypeStat(showType));
			stats.add(stat);
		}
		targetDaily.setShowTypeStat(stats);
	}

	/**
	 * 合并退款订单统计。
	 * 
	 * @param sourceDaily
	 *            源
	 * @param targetDaily
	 *            目标
	 */
	private void mergeRefundOrderStat(DailyStatEntity sourceDaily,
			DailyStatEntity targetDaily) {
		RevokeOrderStat target = targetDaily.getRefundOrderStat();
		RevokeOrderStat source = sourceDaily.getRefundOrderStat();
		target.setOrderCount(target.getOrderCount() + source.getOrderCount());
		target.setTicketCount(target.getTicketCount() + source.getTicketCount());
		target.setCinemaAmount(NumberUtils.add(target.getCinemaAmount(),
				source.getCinemaAmount()));
		target.setChannelAmount(NumberUtils.add(target.getChannelAmount(),
				source.getChannelAmount()));
		target.setServiceFee(NumberUtils.add(target.getServiceFee(),
				source.getServiceFee()));
		target.setChannelFee(NumberUtils.add(target.getChannelFee(),
				source.getChannelFee()));
		target.setCircuitFee(NumberUtils.add(target.getCircuitFee(),
				source.getCircuitFee()));
		target.setSubsidyAmount(NumberUtils.add(target.getSubsidyAmount(),
				source.getSubsidyAmount()));
		target.setRevokeAmount(NumberUtils.add(target.getRevokeAmount(),
				source.getRevokeAmount()));
		target.setAmount(NumberUtils.add(target.getAmount(), source.getAmount()));
		targetDaily.setRefundOrderStat(target);
	}

	/**
	 * 合并正常的订单统计。
	 * 
	 * @param sourceDaily
	 *            源
	 * @param targetDaily
	 *            目标
	 */
	private void mergeNormalOrderStat(DailyStatEntity sourceDaily,
			DailyStatEntity targetDaily) {
		NormalOrderStat target = targetDaily.getNormalOrderStat();
		NormalOrderStat source = sourceDaily.getNormalOrderStat();
		target.setOrderCount(target.getOrderCount() + source.getOrderCount());
		target.setTicketCount(target.getTicketCount() + source.getTicketCount());
		target.setCinemaAmount(NumberUtils.add(target.getCinemaAmount(),
				source.getCinemaAmount()));
		target.setChannelAmount(NumberUtils.add(target.getChannelAmount(),
				source.getChannelAmount()));
		target.setServiceFee(NumberUtils.add(target.getServiceFee(),
				source.getServiceFee()));
		target.setChannelFee(NumberUtils.add(target.getChannelFee(),
				source.getChannelFee()));
		target.setCircuitFee(NumberUtils.add(target.getCircuitFee(),
				source.getCircuitFee()));
		target.setSubsidyFee(NumberUtils.add(target.getSubsidyFee(),
				source.getSubsidyFee()));
		target.setAmount(NumberUtils.add(target.getAmount(), source.getAmount()));
		target.setSubmitAmount(NumberUtils.add(target.getSubmitAmount(),
				source.getSubmitAmount()));
		target.setConnectFee(NumberUtils.add(target.getConnectFee(),
				source.getConnectFee()));
		targetDaily.setNormalOrderStat(target);
	}

}
