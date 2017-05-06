package cn.mopon.cec.core.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BenefitCardUser;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.model.BenefitCardUserSearchModel;
import coo.base.model.Page;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 权益卡用户管理。
 */
@Service
public class BenefitCardUserService {

	@Resource
	private Dao<BenefitCardUser> benefitCardUserDao;

	/**
	 * 分页查询权益卡用户。
	 * 
	 * @param searchModel
	 *            查询条件。
	 * @return 权益卡用户分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BenefitCardUser> searchBenefitCardUser(
			BenefitCardUserSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardUserDao.createFullTextCriteria();
		if (StringUtils.isNotEmpty(searchModel.getChannelId())) {
			criteria.addFilterField("channel.id", searchModel.getChannelId());
		}
		criteria.addLuceneQuery(searchModel.genQuery("createDate"), Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		return benefitCardUserDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 创建权益卡用户。
	 * 
	 * @param channel
	 *            渠道
	 * @param mobile
	 *            手机号码
	 * @return 权益卡用户。
	 */
	@Transactional
	public BenefitCardUser createBenefitCardUser(Channel channel, String mobile) {
		BenefitCardUser user = new BenefitCardUser();
		user.setChannel(channel);
		user.setCreateDate(new Date());
		user.setMobile(mobile);
		benefitCardUserDao.save(user);
		return user;
	}

	/**
	 * 根据手机号码查询唯一权益卡用户。
	 * 
	 * @param mobile
	 *            手机号码
	 * @return 符合条件的权益卡用户。
	 */
	public BenefitCardUser getBenefitCardUser(String mobile) {
		return benefitCardUserDao.searchUnique("mobile", mobile);
	}
}