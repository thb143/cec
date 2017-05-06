package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.TicketOrderStat;
import cn.mopon.cec.core.enums.TicketOrderKind;
import cn.mopon.cec.core.model.StatSearchModel;
import coo.base.model.Page;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 订单统计明细服务。
 */
@Service
public class TicketOrderStatService {
	@Resource
	private Dao<TicketOrderStat> ticketOrderStatDao;

	/**
	 * 分页查询指定条件下的 成功订单明细。
	 * 
	 * @param searchModel
	 *            查询对象
	 * @return 成功订单明细。
	 */
	@Transactional(readOnly = true)
	public Page<TicketOrderStat> searchTicketOrderStat(
			StatSearchModel searchModel) {
		FullTextCriteria criteria = getSearchCriteria(searchModel);
		return ticketOrderStatDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 查询所符合条件的所有订单信息。
	 * 
	 * @param searchModel
	 *            查询条件
	 * @return 订单统计列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketOrderStat> searchTicketOrderStatList(
			StatSearchModel searchModel) {
		FullTextCriteria criteria = getSearchCriteria(searchModel);
		List<TicketOrderStat> result = new ArrayList<TicketOrderStat>();
		int pageNo = 1;
		Page<TicketOrderStat> statPage;
		do {
			statPage = ticketOrderStatDao.searchPage(criteria, pageNo, 1000);
			result.addAll(statPage.getContents());
			pageNo++;
		} while (!statPage.getLast());
		return result;
	}

	/**
	 * 添加查询订单统计明细的查询条件。
	 * 
	 * @param searchModel
	 *            查询条件
	 * @return 全文搜索查询条件。
	 */
	private FullTextCriteria getSearchCriteria(StatSearchModel searchModel) {
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
		criteria.addSortDesc("revokeTime", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return criteria;
	}
}
