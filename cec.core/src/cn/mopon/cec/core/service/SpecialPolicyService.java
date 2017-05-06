package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.SpecialPolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.YearSpecialPolicyListModel;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 特殊定价策略管理。
 */
@Service
public class SpecialPolicyService {
	@Resource
	private Dao<SpecialPolicy> specialPolicyDao;
	@Resource
	private Dao<SpecialRule> specialRuleDao;
	@Resource
	private Dao<SpecialChannel> specialChannelDao;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取特殊定价策略按年份分组列表模型。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回特殊定价策略按年份分组列表模型。
	 */
	@Transactional(readOnly = true)
	public YearSpecialPolicyListModel getYearSpecialPolicyListModel(
			SearchModel searchModel) {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("name");
		criteria.addFilterField("valid", ValidStatus.UNVALID.getValue(),
				ValidStatus.VALID.getValue());
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("ordinal", SortField.Type.INT);
		YearSpecialPolicyListModel model = new YearSpecialPolicyListModel();
		for (SpecialPolicy policy : specialPolicyDao.searchBy(criteria)) {
			model.addPolicy(policy);
		}
		return model;
	}

	/**
	 * 全文搜索的特殊定价策略。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合查询条件的特殊定价策略。
	 */
	@Transactional(readOnly = true)
	public Page<SpecialPolicy> searchSpecialPolicys(SearchModel searchModel) {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("name");
		criteria.addSortAsc("name", SortField.Type.STRING);
		criteria.addFilterField("valid", ValidStatus.UNVALID.getValue(),
				ValidStatus.VALID.getValue());
		criteria.setKeyword(searchModel.getKeyword());
		return specialPolicyDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 新增特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "specialPolicy.add.log", vars = "policy.name")
	public void createSpecialPolicy(SpecialPolicy policy) {
		Integer ordinal = getMaxOrdinal() == null ? 1 : getMaxOrdinal() + 1;
		policy.setOrdinal(ordinal);
		specialPolicyDao.save(policy);
	}

	/**
	 * 更新特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	@Transactional
	@DetailLog(target = "policy", code = "specialPolicy.edit.log", vars = { "policy.name" }, type = LogType.ALL)
	public void updateSpecialPolicy(SpecialPolicy policy) {
		SpecialPolicy origPolicy = getSpecialPolicy(policy.getId());
		// 如果特殊定价策略处于待审核、待审批状态，不允许进行该操作。
		checkSpecialPolicyApprove(policy, "specialPolicy.edit.approve");
		BeanUtils
				.copyFields(policy, origPolicy, "status,valid,enabled,ordinal");
	}

	/**
	 * 删除特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	@Transactional
	@SimpleLog(code = "specialPolicy.delete.log", vars = "policy.name")
	public void deleteSpecialPolicy(SpecialPolicy policy) {
		// 如果特殊定价策略处于待审核、待审批状态，不允许进行该操作。
		checkSpecialPolicyApprove(policy, "specialPolicy.delete.approve");
		// 如果特殊定价策略未过期且已生效，不允许删除。
		if (!policy.getExpired() && policy.getValid() == ValidStatus.VALID) {
			messageSource.thrown("specialPolicy.delete.notexpired");
		}
		// 如果特殊定价策略已生效，修改为已失效。
		if (policy.getValid() == ValidStatus.VALID) {
			policy.setValid(ValidStatus.INVALID);
		} else {
			specialPolicyDao.remove(policy);
		}
	}

	/**
	 * 获取指定ID的特殊定价策略。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @return 返回指定的特殊定价策略。
	 */
	@Transactional(readOnly = true)
	public SpecialPolicy getSpecialPolicy(String policyId) {
		return specialPolicyDao.get(policyId);
	}

	/**
	 * 上移特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	@Transactional
	@SimpleLog(code = "specialPolicy.up.log", vars = "policy.name")
	public void upSpecialPolicy(SpecialPolicy policy) {
		moveSpecialPolicy(policy, Order.asc("ordinal"));
	}

	/**
	 * 下移特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	@Transactional
	@SimpleLog(code = "specialPolicy.down.log", vars = "policy.name")
	public void downSpecialPolicy(SpecialPolicy policy) {
		moveSpecialPolicy(policy, Order.desc("ordinal"));
	}

	/**
	 * 启用特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "specialPolicy.enable.log", vars = "policy.name")
	public List<Show> enableSpecialPolicy(SpecialPolicy policy) {
		// 如果特殊定价策略未生效，不允许启用。
		checkValidStatus(policy, "specialPolicy.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (policy.getEnabled() == EnabledStatus.DISABLED) {
			policy.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(policy);
		}
		return shows;
	}

	/**
	 * 停用特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "specialPolicy.disable.log", vars = "policy.name")
	public List<Show> disableSpecialPolicy(SpecialPolicy policy) {
		// 如果特殊定价策略未生效，不允许停用。
		checkValidStatus(policy, "specialPolicy.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (policy.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置特殊定价策略停用前先获取特殊定价策略影响到的影院排期
			shows = showService.getMatchedShows(policy);
			// 停用特殊定价策略时将特殊定价策略生成的渠道排期全部置为失效。
			policy.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(policy);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 复制特殊定价策略。
	 * 
	 * @param origPolicy
	 *            被复制的特殊定价策略
	 * @return 返回指定的特殊定价策略。
	 */
	@Transactional
	@SimpleLog(code = "specialPolicy.copy.log", vars = "origPolicy.name")
	public SpecialPolicy copySpecialPolicy(SpecialPolicy origPolicy) {
		SpecialPolicy policy = SpecialPolicy.copy(origPolicy);
		policy.setOrdinal(getMaxOrdinal() + 1);
		specialPolicyDao.save(policy);
		for (SpecialRule rule : policy.getRules()) {
			specialRuleDao.save(rule);
			for (SpecialChannel channel : rule.getChannels()) {
				specialChannelDao.save(channel);
			}
		}
		return policy;
	}

	/**
	 * 检查特殊定价策略是否允许进行操作，如果处于待审核、待审批状态抛出不允许操作的异常提示信息。
	 * 
	 * @param policy
	 *            特殊定价策略
	 * @param msgKey
	 *            提示语Key
	 */
	public void checkSpecialPolicyApprove(SpecialPolicy policy, String msgKey) {
		if (policy.getStatus() == SpecialPolicyStatus.AUDIT
				|| policy.getStatus() == SpecialPolicyStatus.APPROVE) {
			messageSource.thrown(msgKey);
		}
	}

	/**
	 * 定时自动启用特殊定价策略。
	 * 
	 * @return 返回受影响的排期列表。
	 */
	@Transactional
	public List<Show> autoEnableSpecialPolicys() {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		String lowerDate = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		String upperDate = DateUtils.format(DateUtils.getNextDay(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("startDate", lowerDate, upperDate);
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());

		List<Show> shows = new ArrayList<>();
		for (SpecialPolicy policy : specialPolicyDao.searchBy(criteria)) {
			shows.addAll(enableSpecialPolicy(policy));
		}
		CollectionUtils.removeDuplicate(shows);
		return shows;
	}

	/**
	 * 定时自动停用特殊定价策略。
	 * 
	 * @return 返回受影响的排期列表。
	 */
	@Transactional
	public List<Show> autoDisableSpecialPolicys() {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		String lowerDate = DateUtils.format(DateUtils.getPrevDay(),
				DateUtils.MILLISECOND_N);
		String upperDate = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("endDate", lowerDate, upperDate);
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());

		List<Show> shows = new ArrayList<>();
		for (SpecialPolicy policy : specialPolicyDao.searchBy(criteria)) {
			shows.addAll(disableSpecialPolicy(policy));
			policy.setValid(ValidStatus.INVALID);
		}
		CollectionUtils.removeDuplicate(shows);
		return shows;
	}

	/**
	 * 获取当前有效的特殊定价策略列表。
	 * 
	 * @return 返回当前有效的特殊定价策略列表。
	 */
	public List<SpecialPolicy> getValidSpecialPolicys() {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		// 特殊定价策略状态是已生效的
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		// 排序
		criteria.addSortDesc("ordinal", SortField.Type.INT);

		return specialPolicyDao.searchBy(criteria);
	}

	/**
	 * 设置策略失效。
	 * 
	 * @param policyId
	 *            策略ID
	 */
	@Transactional
	public void setSpecialPolicyInvalid(String policyId) {
		getSpecialPolicy(policyId).setValid(ValidStatus.INVALID);
	}

	/**
	 * 获取需要自动启用的策略列表。
	 * 
	 * @return 返回需要自动启用的策略列表。
	 */
	@Transactional(readOnly = true)
	public List<SpecialPolicy> getAutoEnableSpecialPolicys() {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		String lowerDate = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		String upperDate = DateUtils.format(DateUtils.getNextDay(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("startDate", lowerDate, upperDate);
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		return specialPolicyDao.searchBy(criteria);
	}

	/**
	 * 获取需要自动停用的策略列表。
	 * 
	 * @return 返回需要自动停用的策略列表。
	 */
	@Transactional(readOnly = true)
	public List<SpecialPolicy> getAutoDisableSpecialPolicys() {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		String lowerDate = DateUtils.format(DateUtils.getPrevDay(),
				DateUtils.MILLISECOND_N);
		String upperDate = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("endDate", lowerDate, upperDate);
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		return specialPolicyDao.searchBy(criteria);
	}

	/**
	 * 移动特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 * @param order
	 *            排序
	 */
	@SuppressWarnings("unchecked")
	private void moveSpecialPolicy(SpecialPolicy policy, Order order) {
		Criteria criteria = specialPolicyDao.createCriteria();
		if (order.isAscending()) {
			criteria.add(Restrictions.gt("ordinal", policy.getOrdinal()));
		} else {
			criteria.add(Restrictions.lt("ordinal", policy.getOrdinal()));
		}
		criteria.add(Restrictions.in("valid", new Object[] {
				ValidStatus.UNVALID, ValidStatus.VALID }));
		criteria.addOrder(order);
		List<SpecialPolicy> policys = criteria.list();
		if (!policys.isEmpty()) {
			Integer currentOrdinal = policy.getOrdinal();
			SpecialPolicy switchPolicy = policys.get(0);
			policy.setOrdinal(switchPolicy.getOrdinal());
			switchPolicy.setOrdinal(currentOrdinal);
		}
	}

	/**
	 * 获取特殊定价策略当前排序最大值。
	 * 
	 * @return 返回特殊定价策略排序最大值。
	 */
	private Integer getMaxOrdinal() {
		Criteria criteria = specialPolicyDao.createCriteria();
		criteria.setProjection(Projections.max("ordinal"));
		return (Integer) criteria.uniqueResult();
	}

	/**
	 * 检测特殊定价策略是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param policy
	 *            特殊定价策略
	 * @param msgKey
	 *            提示语Key
	 * 
	 */
	private void checkValidStatus(SpecialPolicy policy, String msgKey) {
		if (policy.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}