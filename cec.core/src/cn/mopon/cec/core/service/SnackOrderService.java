package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.model.SnackOrderSearchModel;
import coo.base.model.Page;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 权益卡订单服务。
 */
@Service
public class SnackOrderService {
	@Resource
	private Dao<SnackOrder> snackOrderDao;

	@Resource
	private Dao<Snack> snackDao;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Integer> benefitCardCache;
	@Resource
	private SerialNumberService serialNumberService;

	/**
	 * 分页搜索成功卖品订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的权益卡开卡订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<SnackOrder> searchOpenOrder(SnackOrderSearchModel searchModel) {
		FullTextCriteria criteria = snackOrderDao.createFullTextCriteria();
		criteria.addFilterField("status", TicketOrderStatus.SUCCESS.getValue());
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("channel.code",
					searchModel.getChannelCode());
		}
		Query syncDateQuery = searchModel.genQuery("createTime");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("createTime", SortField.Type.LONG);
		return snackOrderDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 分页搜索退卖品订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的权益卡续费订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<SnackOrder> searchBackOrder(SnackOrderSearchModel searchModel) {
		FullTextCriteria criteria = snackOrderDao.createFullTextCriteria();
		criteria.addFilterField("status", TicketOrderStatus.REVOKED.getValue());
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("channel.code",
					searchModel.getChannelCode());
		}
		Query syncDateQuery = searchModel.genQuery("revokeTime");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("createTime", SortField.Type.LONG);
		return snackOrderDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}
}