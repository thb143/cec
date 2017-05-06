package cn.mopon.cec.core.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.assist.constants.RedisKey;
import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.entity.BenefitCardConsumeOrder;
import cn.mopon.cec.core.entity.BenefitCardConsumeSnackOrder;
import cn.mopon.cec.core.entity.BenefitCardRechargeOrder;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.model.BenefitCardOrderSearchModel;
import coo.base.model.Page;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 权益卡订单服务。
 */
@Service
public class BenefitCardOrderService {
	@Resource
	private Dao<BenefitCardConsumeOrder> benefitCardConsumeOrderDao;
	@Resource
	private Dao<BenefitCardConsumeSnackOrder> benefitCardConsumeSnackOrderDao;
	@Resource
	private Dao<BenefitCardRechargeOrder> benefitCardRechargeOrderDao;
	@Resource
	private Dao<BenefitCard> benefitCardDao;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Integer> benefitCardCache;
	@Resource
	private SerialNumberService serialNumberService;

	/**
	 * 分页搜索权益卡开卡订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的权益卡开卡订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BenefitCard> searchOpenOrder(
			BenefitCardOrderSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		if (StringUtils.isNotEmpty(searchModel.getBenefitCardTypeCode())) {
			criteria.addFilterField("type.code",
					searchModel.getBenefitCardTypeCode());
		}
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("channel.code",
					searchModel.getChannelCode());
		}
		Query syncDateQuery = searchModel.genQuery("createDate");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		return benefitCardDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 分页搜索权益卡续费订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的权益卡续费订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BenefitCardRechargeOrder> searchRechargeOrder(
			BenefitCardOrderSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardRechargeOrderDao
				.createFullTextCriteria();
		if (StringUtils.isNotEmpty(searchModel.getBenefitCardTypeCode())) {
			criteria.addFilterField("card.type.code",
					searchModel.getBenefitCardTypeCode());
		}
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("channel.code",
					searchModel.getChannelCode());
		}
		if (searchModel.getRechargeStatus() != null) {
			criteria.addFilterField("status", searchModel.getRechargeStatus()
					.getValue());
		}
		criteria.addSearchField("card.code", "card.user.mobile",
				"channelOrderCode");
		Query syncDateQuery = searchModel.genQuery("createDate");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		return benefitCardRechargeOrderDao.searchPage(criteria,
				searchModel.getPageNo(), searchModel.getPageSize());
	}

	/**
	 * 分页搜索权益卡消费订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的权益卡消费订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BenefitCardConsumeOrder> searchConsumeOrder(
			BenefitCardOrderSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardConsumeOrderDao
				.createFullTextCriteria();
		if (StringUtils.isNotEmpty(searchModel.getBenefitCardTypeCode())) {
			criteria.addFilterField("card.type.code",
					searchModel.getBenefitCardTypeCode());
		}
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("order.channel.code",
					searchModel.getChannelCode());
		}
		if (searchModel.getConsumeStatus() != null) {
			criteria.addFilterField("order.status", searchModel
					.getConsumeStatus().getValue());
		}
		Query syncDateQuery = searchModel.genQuery("order.payTime");
		criteria.addSearchField("card.code", "card.user.mobile",
				"order.channelOrderCode", "order.code");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("order.payTime", SortField.Type.LONG);
		return benefitCardConsumeOrderDao.searchPage(criteria,
				searchModel.getPageNo(), searchModel.getPageSize());
	}

	/**
	 * 分页搜索权益卡卖品消费订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的权益卡卖品消费订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BenefitCardConsumeSnackOrder> searchConsumeSnackOrder(
			BenefitCardOrderSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardConsumeSnackOrderDao
				.createFullTextCriteria();
		if (StringUtils.isNotEmpty(searchModel.getBenefitCardTypeCode())) {
			criteria.addFilterField("card.type.code",
					searchModel.getBenefitCardTypeCode());
		}
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("snackOrder.channel.code",
					searchModel.getChannelCode());
		}
		if (searchModel.getConsumeStatus() != null) {
			criteria.addFilterField("snackOrder.status", searchModel
					.getConsumeStatus().getValue());
		}
		Query syncDateQuery = searchModel.genQuery("snackOrder.createTime");
		criteria.addSearchField("card.code", "card.user.mobile",
				"snackOrder.channelOrderCode", "snackOrder.code");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("snackOrder.createTime", SortField.Type.LONG);
		return benefitCardConsumeSnackOrderDao.searchPage(criteria,
				searchModel.getPageNo(), searchModel.getPageSize());
	}

	/**
	 * 根据渠道号/渠道续费订单号/
	 * 
	 * @param channelCode
	 *            渠道号
	 * @param channelOrderCode
	 *            渠道续费订单号
	 * @return 渠道续费订单。
	 */
	@Transactional(readOnly = true)
	public BenefitCardRechargeOrder getBenefitCardRechargeOrder(
			String channelCode, String channelOrderCode) {
		FullTextCriteria criteria = benefitCardRechargeOrderDao
				.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("channelOrderCode", channelOrderCode);
		return benefitCardRechargeOrderDao.searchUnique(criteria);
	}

	/**
	 * 添加续费订单记录。
	 * 
	 * @param card
	 *            未更改前的卡记录
	 * @param channel
	 *            渠道
	 * @param rechargeAmount
	 *            续费金额
	 * @param channelOrderCode
	 *            渠道续费订单号
	 * @return 返回添加完成的续费记录。
	 */
	@Transactional
	public BenefitCardRechargeOrder createBenefitCardRechargeOrder(
			BenefitCard card, Channel channel, Double rechargeAmount,
			String channelOrderCode) {
		BenefitCardRechargeOrder rechargeOrder = new BenefitCardRechargeOrder();
		DateTime endDate = new DateTime(card.getEndDate());
		if (endDate.toDate().before(DateUtils.getToday())) {
			endDate = new DateTime(card.getEndDate());
		}
		endDate = endDate.plusMonths(Integer.parseInt(card.getType()
				.getValidMonth().getValue()));
		rechargeOrder.setCard(card);
		rechargeOrder.setChannel(channel);
		rechargeOrder.setCode(serialNumberService
				.getBenefitCardRechargeOrderCode());
		rechargeOrder.setChannelOrderCode(channelOrderCode);
		rechargeOrder.setAmount(rechargeAmount);
		rechargeOrder.setOldEndDate(card.getEndDate());
		rechargeOrder.setOldDiscountCount(card.getAvailableDiscountCount());
		rechargeOrder.setCreateDate(new Date());
		benefitCardRechargeOrderDao.save(rechargeOrder);
		return rechargeOrder;
	}

	/**
	 * 增加权益卡当天消费次数。
	 * 
	 * @param code
	 *            权益卡号
	 * @param count
	 *            次数
	 * @return 累增后的权益卡消费次数。
	 */
	public Integer incrDailyDiscountCount(String code, Integer count) {
		return benefitCardCache.increment(getKey(), code, count).intValue();
	}

	/**
	 * 减少权益卡当天消费次数。
	 * 
	 * @param code
	 *            权益卡号
	 * @param count
	 *            次数
	 * @return 操作后的权益卡消费次数。
	 */
	public Integer decrDailyDiscountCount(String code, Integer count) {
		return benefitCardCache.increment(getKey(), code, -count).intValue();
	}

	/**
	 * 获取redis缓存key。
	 * 
	 * @return 缓存key。
	 */
	private String getKey() {
		return RedisKey.BENEFITCARD_DAILY_COUNT + "_"
				+ DateUtils.format(new Date());
	}
}