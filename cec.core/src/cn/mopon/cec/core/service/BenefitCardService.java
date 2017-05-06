package cn.mopon.cec.core.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.assist.constants.RedisKey;
import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.enums.BenefitCardStatus;
import cn.mopon.cec.core.model.BenefitCardSearchModel;
import coo.base.model.Page;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.security.annotations.SimpleLog;

/**
 * 权益卡管理。
 */
@Service
public class BenefitCardService {
	@Resource
	private Dao<BenefitCard> benefitCardDao;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Integer> benefitCardCache;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 分页查询权益卡列表。
	 * 
	 * @param searchModel
	 *            查询条件
	 * @return 权益卡分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BenefitCard> searchBenefitCard(
			BenefitCardSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		if (StringUtils.isNotEmpty(searchModel.getChannelId())) {
			criteria.addFilterField("channel.id", searchModel.getChannelId());
		}
		if (StringUtils.isNotEmpty(searchModel.getTypeId())) {
			criteria.addFilterField("type.id", searchModel.getTypeId());
		}
		if (searchModel.getStatus() != null) {
			criteria.addFilterField("status", searchModel.getStatus()
					.getValue());
		}
		criteria.addLuceneQuery(searchModel.genQuery("createDate"), Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		Page<BenefitCard> cardPage = benefitCardDao.searchPage(criteria,
				searchModel.getPageNo(), searchModel.getPageSize());
		for (BenefitCard card : cardPage.getContents()) {
			card.setDailyDiscountCount(getDailyDiscountCount(card.getCode()));
		}
		return cardPage;
	}

	/**
	 * 根据用户查询权益卡。
	 * 
	 * @param userId
	 *            用户
	 * @return 权益卡列表。
	 */
	@Transactional(readOnly = true)
	public List<BenefitCard> searchBenefitCardByUser(String userId) {
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		criteria.addFilterField("user.id", userId);
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		List<BenefitCard> cards = benefitCardDao.searchBy(criteria);
		for (BenefitCard card : cards) {
			card.setDailyDiscountCount(getDailyDiscountCount(card.getCode()));
		}
		return cards;
	}

	/**
	 * 根据ID查询唯一卡。
	 * 
	 * @param benefitCardId
	 *            权益卡ID
	 * @return 权益卡。
	 */
	@Transactional(readOnly = true)
	public BenefitCard getBenefitCard(String benefitCardId) {
		BenefitCard card = benefitCardDao.get(benefitCardId);
		if (card != null) {
			card.setDailyDiscountCount(getDailyDiscountCount(card.getCode()));
		}
		return card;
	}

	/**
	 * 根据渠道号/卡类编号/手机号码查询唯一卡。
	 * 
	 * @param channelCode
	 *            渠道号
	 * @param typeCode
	 *            权益卡号
	 * @param mobile
	 *            手机号码
	 * @return 权益卡。
	 */
	@Transactional(readOnly = true)
	public BenefitCard getBenefitCard(String channelCode, String typeCode,
			String mobile) {
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("type.code", typeCode);
		criteria.addFilterField("user.mobile", mobile);
		BenefitCard card = benefitCardDao.searchUnique(criteria);
		if (card != null) {
			card.setDailyDiscountCount(getDailyDiscountCount(card.getCode()));
		}
		return card;
	}

	/**
	 * 根据渠道开卡订单号查询唯一卡。
	 * 
	 * @param channelOrderCode
	 *            渠道开卡订单号
	 * @param channelCode
	 *            渠道编号
	 * @return 权益卡。
	 */
	@Transactional(readOnly = true)
	public BenefitCard getBenefitCardByChannelOrderCode(
			String channelOrderCode, String channelCode) {
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("channelOrderCode", channelOrderCode);
		return benefitCardDao.searchUnique(criteria);
	}

	/**
	 * 权益卡续费。
	 * 
	 * @param card
	 *            权益卡
	 * @return 续费后的权益卡信息。
	 */
	@Transactional
	public BenefitCard rechargeBenefitCard(BenefitCard card) {
		DateTime endDate = new DateTime(card.getEndDate());
		if (endDate.toDate().before(DateUtils.getToday())) {
			endDate = new DateTime(DateUtils.getToday());
			card.setAvailableDiscountCount(0);
		}
		endDate = endDate.plusMonths(Integer.parseInt(card.getType()
				.getValidMonth().getValue()));
		card.setEndDate(endDate.toDate());
		// 如果现在状态为过期，并且续费后的结束日期在当天之后，则修改状态为状态
		if (card.getStatus() == BenefitCardStatus.EXPIRE
				&& card.getEndDate().after(DateUtils.getToday())) {
			card.setStatus(BenefitCardStatus.NORMAL);
		}
		card.setTotalDiscountCount(card.getTotalDiscountCount()
				+ card.getType().getTotalDiscountCount());
		card.setAvailableDiscountCount(card.getAvailableDiscountCount()
				+ card.getType().getTotalDiscountCount());
		card.setRechargeCount(card.getRechargeCount() + 1);
		return card;
	}

	/**
	 * 冻结权益卡。
	 * 
	 * @param card
	 *            权益卡对象
	 */
	@Transactional
	@SimpleLog(code = "benefitCard.disable.log", vars = { "card.code" })
	public void disableBenefitCard(BenefitCard card) {
		card.setStatus(BenefitCardStatus.DISABLE);
	}

	/**
	 * 解冻权益卡。
	 * 
	 * @param card
	 *            权益卡对象
	 */
	@Transactional
	@SimpleLog(code = "benefitCard.enable.log", vars = { "card.code" })
	public void enableBenefitCard(BenefitCard card) {
		card.setStatus(BenefitCardStatus.NORMAL);
	}

	/**
	 * 权益卡延期。
	 * 
	 * @param card
	 *            权益卡对象
	 */
	@Transactional
	@SimpleLog(code = "benefitCard.delay.log", vars = { "card.code" })
	public void delayBenefitCard(BenefitCard card) {
		DateTime endDate = new DateTime(card.getEndDate());
		if (endDate.toDate().before(DateUtils.getToday())) {
			endDate = new DateTime(DateUtils.getToday());
		}
		endDate = endDate.plusMonths(Integer.parseInt(card.getType()
				.getValidMonth().getValue()));
		card.setEndDate(endDate.toDate());
		if (card.getStatus() == BenefitCardStatus.EXPIRE
				&& card.getEndDate().after(DateUtils.getToday())) {
			card.setStatus(BenefitCardStatus.NORMAL);
		}
	}

	/**
	 * 过期权益卡。
	 */
	@Transactional
	public void expireBenefitCard() {
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		criteria.addFilterField("status", BenefitCardStatus.NORMAL.getValue());
		criteria.addRangeField("endDate", null,
				DateUtils.format(DateUtils.getToday(), DateUtils.MILLISECOND_N));
		for (BenefitCard card : benefitCardDao.searchBy(criteria)) {
			card.setStatus(BenefitCardStatus.EXPIRE);
		}
	}

	/**
	 * 获取权益卡当天消费次数。
	 * 
	 * @param code
	 *            权益卡号
	 * @return 当天消费次数。
	 */
	public Integer getDailyDiscountCount(String code) {
		String key = getKey();
		Integer count = benefitCardCache.increment(key, code, 0).intValue();
		// 设置缓存过期时间。
		redisTemplate.expire(key, 24, TimeUnit.HOURS);
		return count;
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
