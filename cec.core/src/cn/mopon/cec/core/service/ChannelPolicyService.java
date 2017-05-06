package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.ChannelChannelPolicyListModel;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 渠道策略服务类。
 */
@Service
public class ChannelPolicyService {
	@Resource
	private Dao<ChannelPolicy> channelPolicyDao;
	@Resource
	private Dao<ChannelRuleGroup> channelRuleGroupDao;
	@Resource
	private Dao<ChannelRule> channelRuleDao;
	@Resource
	private ChannelService channelService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增渠道结算策略。
	 * 
	 * @param channelPolicy
	 *            渠道结算策略
	 */
	@AutoFillIn
	@Transactional
	@SimpleLog(code = "channelPolicy.add.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" })
	public void createChannelPolicy(ChannelPolicy channelPolicy) {
		Channel channel = channelService.getChannel(channelPolicy.getChannel()
				.getId());
		channelPolicy.setOrdinal(channel.genNewPolicyOrdinal());
		channelPolicyDao.save(channelPolicy);
		channel.getPolicys().add(channelPolicy);
	}

	/**
	 * 按渠道分组搜索审批策略记录。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的审批策略记录分页对象。
	 */
	@Transactional(readOnly = true)
	public ChannelChannelPolicyListModel searchPolicy(SearchModel searchModel) {
		FullTextCriteria criteria = channelPolicyDao.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addFilterField("valid", ValidStatus.UNVALID.getValue(),
				ValidStatus.VALID.getValue());
		criteria.addSearchField("name", Analyze.NO);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("ordinal", SortField.Type.INT);
		List<ChannelPolicy> policys = channelPolicyDao.searchBy(criteria);
		ChannelChannelPolicyListModel channelPolicyLogListModel = new ChannelChannelPolicyListModel(
				channelService.searchOpenedChannels(new SearchModel()));
		setChannelPolicyListModel(policys, channelPolicyLogListModel);
		return channelPolicyLogListModel;
	}

	/**
	 * 向分组模型中装载数据。
	 * 
	 * @param policys
	 *            策略
	 * @param channelPolicyLogListModel
	 *            渠道结算策略按渠道分组的列表模型
	 */
	private void setChannelPolicyListModel(List<ChannelPolicy> policys,
			ChannelChannelPolicyListModel channelPolicyLogListModel) {
		for (ChannelPolicy channelPolicy : policys) {
			Integer count = channelRuleGroupService
					.searchChannelRuleGroupCount(channelPolicy.getId(), null);
			channelPolicyLogListModel.addPolicy(channelPolicy, count);
		}
	}

	/**
	 * 获取指定ID的渠道结算策略。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回对应的渠道结算策略。
	 */
	@Transactional(readOnly = true)
	public ChannelPolicy getChannelPolicy(String channelPolicyId) {
		return channelPolicyDao.get(channelPolicyId);
	}

	/**
	 * 按影院过滤规则分组的影院。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @param cinemaId
	 *            影院ID
	 * @return 返回按城市过滤影院后的策略对象。
	 */
	public ChannelPolicy groupChannelPolicy(String channelPolicyId,
			String cinemaId) {
		ChannelPolicy policy = getChannelPolicy(channelPolicyId);
		if (StringUtils.isNotEmpty(cinemaId)) {
			policy.setGroups(new ArrayList<ChannelRuleGroup>());
			ChannelRuleGroup group = channelRuleGroupService.searchUniqueGroup(
					cinemaId, channelPolicyId);
			policy.getGroups().add(group);
		}
		filterChannelRule(policy, policy.getGroups());
		return policy;
	}

	/**
	 * 过滤策略中不应该显示的规则。
	 * 
	 * @param policy
	 *            策略
	 * @param groups
	 *            分组影院
	 */
	public void filterChannelRule(ChannelPolicy policy,
			List<ChannelRuleGroup> groups) {
		for (ChannelRuleGroup group : groups) {
			policy.filterChannelRule(group);
		}
	}

	/**
	 * 更新策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "channelPolicy", code = "channelPolicy.edit.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" }, type = LogType.ALL)
	public void updateChannelPolicy(ChannelPolicy channelPolicy) {
		ChannelPolicy origChannelPolicy = channelPolicyDao.get(channelPolicy
				.getId());
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		checkChannelPolicyApprove(origChannelPolicy,
				"channelPolicy.edit.approve");
		BeanUtils.copyFields(channelPolicy, origChannelPolicy,
				"status,valid,enabled,ordinal");
	}

	/**
	 * 删除策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 */
	@Transactional
	@SimpleLog(code = "channelPolicy.delete.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" })
	public void deleteChannelPolicy(ChannelPolicy channelPolicy) {
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		checkChannelPolicyApprove(channelPolicy, "channelPolicy.delete.approve");
		// 如果策略未过期且已生效，不允许删除。
		if (!channelPolicy.getExpired()
				&& channelPolicy.getValid() != ValidStatus.UNVALID) {
			messageSource.thrown("channelPolicy.delete.notexpired");
		}
		Channel channel = channelPolicy.getChannel();
		// 如果策略已生效且已过期，进行逻辑删除。
		if (channelPolicy.getExpired()
				&& channelPolicy.getValid() != ValidStatus.UNVALID) {
			channelPolicy.setValid(ValidStatus.INVALID);
			channelPolicy.setEnabled(EnabledStatus.DISABLED);
		} else {
			channelPolicyDao.remove(channelPolicy);
		}
		channel.getPolicys().remove(channelPolicy);
	}

	/**
	 * 启用策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicy.enable.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" })
	public List<Show> enableChannelPolicy(ChannelPolicy channelPolicy) {
		// 如果策略未生效，不允许启用。
		checkValidStatus(channelPolicy, "channelPolicy.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (channelPolicy.getEnabled() == EnabledStatus.DISABLED) {
			channelPolicy.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(channelPolicy);
		}
		return shows;
	}

	/**
	 * 停用策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicy.disable.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" })
	public List<Show> disableChannelPolicy(ChannelPolicy channelPolicy) {
		// 如果策略未生效，不允许停用。
		checkValidStatus(channelPolicy, "channelPolicy.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (channelPolicy.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置结算策略停用前先获取结算策略影响到的影院排期
			shows = showService.getMatchedShows(channelPolicy);
			// 停用结算策略时将结算策略影响到的排期全部置为失效。
			channelPolicy.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(channelPolicy);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 上移策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 */
	@Transactional
	@SimpleLog(code = "channelPolicy.up.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" })
	public void upChannelPolicy(ChannelPolicy channelPolicy) {
		moveChannelPolicy(channelPolicy, Order.asc("ordinal"));
	}

	/**
	 * 下移策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 */
	@Transactional
	@SimpleLog(code = "channelPolicy.down.log", vars = {
			"channelPolicy.channel.name", "channelPolicy.name" })
	public void downChannelPolicy(ChannelPolicy channelPolicy) {
		moveChannelPolicy(channelPolicy, Order.desc("ordinal"));
	}

	/**
	 * 分页搜索策略。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的策略分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<ChannelPolicy> searchChannelPolicy(SearchModel searchModel) {
		FullTextCriteria criteria = channelPolicyDao.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("channel.name", Analyze.NO);
		criteria.addSearchField("name", Analyze.NO);
		criteria.addFilterField("valid", ValidStatus.UNVALID.getValue(),
				ValidStatus.VALID.getValue());
		criteria.setKeyword(searchModel.getKeyword());
		return channelPolicyDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 复制策略。
	 * 
	 * @param origChannelPolicy
	 *            原策略
	 * @param toChannelId
	 *            复制到目标渠道ID
	 * @return 返回指定的策略。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicy.copy.log", vars = {
			"origChannelPolicy.channel.name", "origChannelPolicy.name" })
	public ChannelPolicy copyChannelPolicy(ChannelPolicy origChannelPolicy,
			String toChannelId) {
		Channel toChannel = channelService.getChannel(toChannelId);
		ChannelPolicy channelPolicy = ChannelPolicy.copy(origChannelPolicy,
				toChannel);
		channelPolicyDao.save(channelPolicy);
		toChannel.getPolicys().add(channelPolicy);
		// 策略和策略规则分组之间没有设置级联保存关系，这里需要单独保存策略规则分组。
		for (ChannelRuleGroup group : channelPolicy.getGroups()) {
			channelRuleGroupDao.save(group);
			// 策略规则分组和策略规则之间没有设置级联保存关系，这里需要单独保存策略规则。
			for (ChannelRule rule : group.getRules()) {
				channelRuleDao.save(rule);
			}
		}
		return channelPolicy;
	}

	/**
	 * 检查策略是否允许进行操作，如果处于待审核、待审批状态抛出不允许操作的异常提示信息。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @param msgKey
	 *            提示语Key
	 */
	public void checkChannelPolicyApprove(ChannelPolicy channelPolicy,
			String msgKey) {
		if (channelPolicy.getStatus() == PolicyStatus.AUDIT
				|| channelPolicy.getStatus() == PolicyStatus.APPROVE) {
			messageSource.thrown(msgKey);
		}
	}

	/**
	 * 检测策略是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @param msgKey
	 *            提示语Key
	 * 
	 */
	public void checkValidStatus(ChannelPolicy channelPolicy, String msgKey) {
		if (channelPolicy.getValid() != ValidStatus.VALID) {
			messageSource.thrown(msgKey);
		}
	}

	/**
	 * 移动策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @param order
	 *            排序
	 */
	@SuppressWarnings("unchecked")
	private void moveChannelPolicy(ChannelPolicy channelPolicy, Order order) {
		Criteria criteria = channelPolicyDao.createCriteria();
		if (order.isAscending()) {
			criteria.add(Restrictions.gt("ordinal", channelPolicy.getOrdinal()));
		} else {
			criteria.add(Restrictions.lt("ordinal", channelPolicy.getOrdinal()));
		}
		criteria.add(Restrictions.eq("channel", channelPolicy.getChannel()));
		criteria.add(Restrictions.in("valid", new Object[] {
				ValidStatus.UNVALID, ValidStatus.VALID }));
		criteria.addOrder(order);
		List<ChannelPolicy> policies = criteria.list();
		if (!policies.isEmpty()) {
			Integer currentOrdinal = channelPolicy.getOrdinal();
			ChannelPolicy switchPolicy = policies.get(0);
			channelPolicy.setOrdinal(switchPolicy.getOrdinal());
			switchPolicy.setOrdinal(currentOrdinal);
		}
	}
}