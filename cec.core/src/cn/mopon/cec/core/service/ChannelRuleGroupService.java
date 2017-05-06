package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ThreadContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BnLog;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.OpenStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 渠道策略分组服务类。
 */
@Service
public class ChannelRuleGroupService {
	@Resource
	private Dao<ChannelRuleGroup> channelRuleGroupDao;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ChannelPolicyService channelPolicyService;
	@Resource
	private ChannelRuleService channelRuleService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private SecurityService securityService;
	@Resource
	private BnLogger bnLogger;

	/**
	 * 根据规则分组ID获取渠道结算策略规则分组。
	 * 
	 * @param channelRuleGroupId
	 *            分组ID
	 * @return 返回指定ID的渠道结算策略规则分组。
	 */
	@Transactional(readOnly = true)
	public ChannelRuleGroup getChannelRuleGroup(String channelRuleGroupId) {
		return channelRuleGroupDao.get(channelRuleGroupId);
	}

	/**
	 * 新增渠道结算策略规则分组。
	 * 
	 * @param channelRuleGroup
	 *            规则分组
	 */
	@Transactional
	public void createChannelRuleGroup(ChannelRuleGroup channelRuleGroup) {
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(
				channelRuleGroup.getPolicy(), "channelRuleGroup.add.approve");
		ChannelPolicy channelPolicy = channelRuleGroup.getPolicy();
		channelRuleGroupDao.save(channelRuleGroup);
		channelPolicy.getGroups().add(channelRuleGroup);
		// 将结算策略修改为“待提交”状态。
		channelPolicy.setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 批量新增渠道结算策略规则分组。
	 * 
	 * @param cinemaIds
	 *            影院ID数组
	 * @param channelPolicyId
	 *            策略ID
	 */
	@Transactional
	public void createChannelRuleGroup(String[] cinemaIds,
			String channelPolicyId) {
		if (CollectionUtils.isEmpty(cinemaIds)) {
			messageSource.thrown("channelRuleGroup.null.groupIds");
		}
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(channelPolicy,
				"channelRuleGroup.add.approve");
		for (String cinemaId : cinemaIds) {
			ChannelRuleGroup group = new ChannelRuleGroup();
			Cinema cinema = cinemaService.getCinema(cinemaId);
			group.setCinema(cinema);
			group.setOrdinal(channelPolicy.genNewGroupOrdinal());
			group.setPolicy(channelPolicy);
			channelRuleGroupDao.save(group);
			channelPolicy.getGroups().add(group);
		}
		// 将结算策略修改为“待提交”状态。
		channelPolicy.setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 批量设置规则分组影院开放或关闭。
	 * 
	 * @param cinemaIds
	 *            影院ID数组
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	public List<Show> setChannelRuleGroupOpen(String[] cinemaIds,
			String channelPolicyId) {
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		// 如果结算策略处于未生效状态，不允许进行该操作。
		channelPolicyService.checkValidStatus(channelPolicy,
				"channelRuleGroup.set.invalid");
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(channelPolicy,
				"channelRuleGroup.set.approve");
		List<Show> shows = new ArrayList<>();
		for (ChannelRuleGroup group : channelPolicy.getGroups()) {
			BnLog bnLog = new BnLog();
			bnLog.setCreateDate(new Date());
			bnLog.setCreator(getCurrentUsername());
			if (CollectionUtils.isEmpty(cinemaIds)) {
				shows.addAll(openOrCloseGroup(bnLog, group, OpenStatus.CLOSED));
			} else if (CollectionUtils.contains(cinemaIds, group.getCinema()
					.getId())) {
				shows.addAll(openOrCloseGroup(bnLog, group, OpenStatus.OPENED));
			} else {
				shows.addAll(openOrCloseGroup(bnLog, group, OpenStatus.CLOSED));
			}
			bnLogger.log(bnLog);
		}
		return shows;
	}

	/**
	 * 批量设置开放时，设置分组影院开放或者关闭。
	 * 
	 * @param bnLog
	 *            日志
	 * @param group
	 *            分组影院
	 * @param status
	 *            开放状态
	 * @return 返回影响到的影院排期列表。
	 */
	private List<Show> openOrCloseGroup(BnLog bnLog, ChannelRuleGroup group,
			OpenStatus status) {
		if (status == OpenStatus.CLOSED) {
			bnLog.setMessage(messageSource.get(
					"channelPolicyRuleGroup.close.log", group.getPolicy()
							.getChannel().getName(), group.getPolicy()
							.getName(), group.getCinema().getName()));
			return closeChannelRuleGroup(group);
		} else {
			bnLog.setMessage(messageSource.get(
					"channelPolicyRuleGroup.open.log", group.getPolicy()
							.getChannel().getName(), group.getPolicy()
							.getName(), group.getCinema().getName()));
			return openChannelRuleGroup(group);
		}
	}

	/**
	 * 上移策略。
	 * 
	 * @param channelRuleGroup
	 *            策略
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRuleGroup.up.log", vars = {
			"channelRuleGroup.policy.channel.name",
			"channelRuleGroup.policy.name", "channelRuleGroup.cinema.name" })
	public void upChannelPolicy(ChannelRuleGroup channelRuleGroup) {
		moveChannelRuleGroup(channelRuleGroup, Order.asc("ordinal"));
	}

	/**
	 * 下移策略。
	 * 
	 * @param channelRuleGroup
	 *            策略
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRuleGroup.down.log", vars = {
			"channelRuleGroup.policy.channel.name",
			"channelRuleGroup.policy.name", "channelRuleGroup.cinema.name" })
	public void downChannelPolicy(ChannelRuleGroup channelRuleGroup) {
		moveChannelRuleGroup(channelRuleGroup, Order.desc("ordinal"));
	}

	/**
	 * 移动规则分组。
	 * 
	 * @param group
	 *            规则分组
	 * @param order
	 *            排期
	 */
	@SuppressWarnings("unchecked")
	private void moveChannelRuleGroup(ChannelRuleGroup group, Order order) {
		Criteria criteria = channelRuleGroupDao.createCriteria();
		if (order.isAscending()) {
			criteria.add(Restrictions.lt("ordinal", group.getOrdinal()));
		} else {
			criteria.add(Restrictions.gt("ordinal", group.getOrdinal()));
		}
		criteria.add(Restrictions.eq("policy", group.getPolicy()));
		criteria.addOrder(order);
		List<ChannelRuleGroup> groups = criteria.list();
		if (!groups.isEmpty()) {
			Integer currentOrdinal = group.getOrdinal();
			ChannelRuleGroup switchGroup = groups.get(groups.size() - 1);
			group.setOrdinal(switchGroup.getOrdinal());
			switchGroup.setOrdinal(currentOrdinal);
		}
	}

	/**
	 * 对规则分组设置排序。
	 * 
	 * @param groupIds
	 *            分组ID
	 */
	@Transactional
	public void saveOrdinal(String[] groupIds) {
		for (int i = 0; i < groupIds.length; i++) {
			ChannelRuleGroup group = channelRuleGroupDao.get(groupIds[i]);
			group.setOrdinal(i + 1);
		}
	}

	/**
	 * 更新分组。
	 * 
	 * @param channelRuleGroup
	 *            分组
	 */
	@Transactional
	@DetailLog(target = "channelRuleGroup", code = "channelPolicyRuleGroup.edit.log", vars = {
			"channelRuleGroup.policy.channel.name",
			"channelRuleGroup.policy.name", "channelRuleGroup.cinema.name" }, type = LogType.ALL)
	public void update(ChannelRuleGroup channelRuleGroup) {
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(
				channelRuleGroup.getPolicy(),
				"channelRuleGroup.connectFee.set.approve");
		ChannelRuleGroup origChannelRuleGroup = channelRuleGroupDao
				.get(channelRuleGroup.getId());
		BeanUtils.copyFields(channelRuleGroup, origChannelRuleGroup,
				"ordinal,status,valid");
	}

	/**
	 * 删除规则分组。
	 * 
	 * @param channelRuleGroup
	 *            规则分组
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@SimpleLog(code = "channelPolicyRuleGroup.delete.log", vars = {
			"channelRuleGroup.policy.channel.name",
			"channelRuleGroup.policy.name", "channelRuleGroup.cinema.name" })
	public void deleteChannelRuleGroup(ChannelRuleGroup channelRuleGroup) {
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService
				.checkChannelPolicyApprove(channelRuleGroup.getPolicy(),
						"channelRuleGroup.delete.approve");
		// 如果策略已生效，规则分组下有规则，不允许删除。
		if (channelRuleGroup.getPolicy().getValid() != ValidStatus.UNVALID
				&& CollectionUtils.isNotEmpty(channelRuleGroup.getRules())) {
			messageSource.thrown("channelRuleGroup.delete.hasrules");
		}
		ChannelPolicy channelPolicy = channelRuleGroup.getPolicy();

		Criteria criteria = channelRuleGroupDao.createCriteria();
		criteria.add(Restrictions.gt("ordinal", channelRuleGroup.getOrdinal()));
		criteria.add(Restrictions.eq("policy", channelPolicy));
		List<ChannelRuleGroup> groups = criteria.list();
		for (ChannelRuleGroup group : groups) {
			group.setOrdinal(group.getOrdinal() - 1);
		}
		channelRuleGroupDao.remove(channelRuleGroup);
		channelPolicy.getGroups().remove(channelPolicy);
		if (searchChannelRuleGroupCount(channelPolicy.getId(), false) == 0
				&& channelPolicy.getValid() != ValidStatus.UNVALID) {
			channelPolicy.setStatus(PolicyStatus.APPROVED);
		}
	}

	/**
	 * 开放规则分组。
	 * 
	 * @param channelRuleGroup
	 *            规则分组
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRuleGroup.open.log", vars = {
			"channelRuleGroup.policy.channel.name",
			"channelRuleGroup.policy.name", "channelRuleGroup.cinema.name" })
	public List<Show> openChannelRuleGroup(ChannelRuleGroup channelRuleGroup) {
		List<Show> shows = new ArrayList<>();
		if (channelRuleGroup.getStatus() == OpenStatus.CLOSED) {
			// 开放规则分组时将规则分组影响到的排期重新生成渠道排期。
			channelRuleGroup.setStatus(OpenStatus.OPENED);
			shows = showService.getMatchedShows(channelRuleGroup);
		}
		return shows;
	}

	/**
	 * 关闭规则分组。
	 * 
	 * @param channelRuleGroup
	 *            规则分组
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRuleGroup.close.log", vars = {
			"channelRuleGroup.policy.channel.name",
			"channelRuleGroup.policy.name", "channelRuleGroup.cinema.name" })
	public List<Show> closeChannelRuleGroup(ChannelRuleGroup channelRuleGroup) {
		List<Show> shows = new ArrayList<>();
		if (channelRuleGroup.getStatus() == OpenStatus.OPENED) {
			// 在设置规则分组停用前先获取规则分组影响到的影院排期
			shows = showService.getMatchedShows(channelRuleGroup);
			// 停用规则分组时将规则分组影响到的排期全部置为失效。
			channelRuleGroup.setStatus(OpenStatus.CLOSED);
			if (CollectionUtils.isNotEmpty(channelRuleGroup.getRules())) {
				List<ChannelShow> channelShows = channelShowService
						.getMatchedChannelShows(channelRuleGroup);
				for (ChannelShow channelShow : channelShows) {
					channelShow.setStatus(ShelveStatus.INVALID);
				}
			}
		}
		return shows;
	}

	/**
	 * 搜索规则分组。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @param searchModel
	 *            搜索条件
	 * @param valid
	 *            生效状态
	 * @return 返回符合条件的规则分组列表。
	 */
	@Transactional(readOnly = true)
	public Page<ChannelRuleGroup> searchChannelRuleGroup(
			String channelPolicyId, SearchModel searchModel, Boolean valid) {
		FullTextCriteria criteria = channelRuleGroupDao
				.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addFilterField("policy.id", channelPolicyId);
		if (valid != null) {
			criteria.addFilterField("valid", valid);
		}
		criteria.addSearchField("cinema.name", Analyze.NO);
		criteria.addSearchField("cinema.county.city.name", Analyze.NO);
		criteria.addSortAsc("cinema.county.city.code", SortField.Type.STRING);
		criteria.setKeyword(searchModel.getKeyword());
		return channelRuleGroupDao.searchPage(criteria,
				searchModel.getPageNo(), searchModel.getPageSize());
	}

	/**
	 * 获取有未生效规则的规则分组。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回有未生效规则的规则分组列表。
	 */
	@Transactional(readOnly = true)
	public List<ChannelRuleGroup> searchUnvalidChannelRuleGroup(
			String channelPolicyId) {
		FullTextCriteria criteria = channelRuleGroupDao
				.createFullTextCriteria();
		criteria.addFilterField("policy.id", channelPolicyId);
		criteria.addFilterField("valid", false);
		return channelRuleGroupDao.searchBy(criteria);
	}

	/**
	 * 搜索规则分组。
	 * 
	 * @param groupId
	 *            分组ID
	 * @param channelPolicyId
	 *            策略ID
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的规则分组列表。
	 */
	@Transactional(readOnly = true)
	public List<ChannelRuleGroup> searchChannelRuleGroup(String groupId,
			String channelPolicyId, SearchModel searchModel) {
		FullTextCriteria criteria = channelRuleGroupDao
				.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addFilterField("policy.id", channelPolicyId);
		TermQuery query = new TermQuery(new Term("cinema.code",
				getChannelRuleGroup(groupId).getCinema().getCode()));
		criteria.addLuceneQuery(query, Occur.MUST_NOT);
		criteria.addSearchField("cinema.code", Analyze.NO);
		criteria.addSearchField("cinema.name", Analyze.NO);
		criteria.addSortAsc("cinema.county.city.code", SortField.Type.STRING);
		criteria.setKeyword(searchModel.getKeyword());
		return channelRuleGroupDao.searchBy(criteria);
	}

	/**
	 * 搜索规则分组。
	 * 
	 * @param cinemaId
	 *            分组影院ID
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回符合条件的规则分组。
	 */
	@Transactional(readOnly = true)
	public ChannelRuleGroup searchUniqueGroup(String cinemaId,
			String channelPolicyId) {
		FullTextCriteria criteria = channelRuleGroupDao
				.createFullTextCriteria();
		criteria.addFilterField("policy.id", channelPolicyId);
		criteria.addFilterField("cinema.id", cinemaId);
		return channelRuleGroupDao.searchUnique(criteria);
	}

	/**
	 * 搜索策略下的规则分组数量。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @param valid
	 *            生效状态
	 * @return 返回策略下的规则分组数量。
	 */
	@Transactional(readOnly = true)
	public Integer searchChannelRuleGroupCount(String channelPolicyId,
			Boolean valid) {
		FullTextCriteria criteria = channelRuleGroupDao
				.createFullTextCriteria();
		criteria.addFilterField("policy.id", channelPolicyId);
		if (valid != null) {
			criteria.addFilterField("valid", valid);
		}
		return channelRuleGroupDao.count(criteria);
	}

	/**
	 * 复制规则分组。
	 * 
	 * @param origGroup
	 *            原规则分组
	 * @param toGroup
	 *            复制到目标规则分组
	 */
	@Transactional
	public void copyChannelRuleGroup(ChannelRuleGroup origGroup,
			ChannelRuleGroup toGroup) {
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(origGroup.getPolicy(),
				"channelRuleGroup.copy.approve");
		for (ChannelRule origRule : origGroup.getRules()) {
			// 复制已生效或者未生效且未绑定的策略规则。
			if (isCopy(toGroup, origRule)) {
				ChannelRule rule = new ChannelRule();
				BeanUtils.copyFields(origRule, rule, "boundRule,halls");
				rule.setId(null);
				rule.setName(origRule.getName());
				rule.setPeriodRule(origRule.getPeriodRule());
				rule.setSettleRule(origRule.getSettleRule());
				rule.setShowType(origRule.getShowType());
				rule.setValid(ValidStatus.UNVALID);
				rule.setEnabled(EnabledStatus.DISABLED);
				rule.setStatus(RuleStatus.UNAUDIT);
				rule.autoFillIn();
				rule.setGroup(toGroup);
				channelRuleService.createChannelRule(rule);
			}
		}
	}

	/**
	 * 判断是否可进行复制。
	 * 
	 * @param group
	 *            结算规则分组
	 * @param origRule
	 *            原策略规则
	 * @return 如果原策略规则可复制到结算规则分组中，返回true，否则，返回false。
	 */
	private Boolean isCopy(ChannelRuleGroup group, ChannelRule origRule) {
		return (origRule.getValid() == ValidStatus.VALID || (origRule
				.getValid() == ValidStatus.UNVALID && !origRule.isBounded()))
				&& group.getCinema().getTicketSettings().getShowTypes()
						.contains(origRule.getShowType());
	}

	/**
	 * 复制影院规则到其他影院。
	 * 
	 * @param groupId
	 *            规则分组ID
	 * @param groupIds
	 *            待复制的规则分组ID
	 */
	@Transactional
	public void batchCopy(String groupId, String[] groupIds) {
		if (CollectionUtils.isEmpty(groupIds)) {
			messageSource.thrown("channelRuleGroup.null.groupIds");
		}
		for (String toGroupId : groupIds) {
			copyChannelRuleGroup(getChannelRuleGroup(groupId),
					getChannelRuleGroup(toGroupId));
		}
	}

	/**
	 * 批量设置接入费。
	 * 
	 * @param connectFee
	 *            接入费
	 * @param groupIds
	 *            规则分组ID数组
	 * @param channelPolicyId
	 *            channelPolicyId 策略ID
	 */
	@Transactional
	public void batchSetConnectFee(Double connectFee, String[] groupIds,
			String channelPolicyId) {
		if (CollectionUtils.isEmpty(groupIds)) {
			messageSource.thrown("channelRuleGroup.null.groupIds");
		}
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(channelPolicy,
				"channelRuleGroup.connectFee.set.approve");
		for (ChannelRuleGroup group : channelPolicy.getGroups()) {
			if (CollectionUtils.isNotEmpty(groupIds)
					&& CollectionUtils.contains(groupIds, group.getId())) {
				BnLog bnLog = new BnLog();
				bnLog.setMessage(messageSource.get(
						"channelPolicyRuleGroup.edit.log", channelPolicy
								.getChannel().getName(), channelPolicy
								.getName(), group.getCinema().getName()));
				bnLog.setCreateDate(new Date());
				bnLog.setCreator(getCurrentUsername());
				bnLog.setOrigData(group);

				group.setConnectFee(connectFee);

				bnLog.setNewData(group);
				bnLogger.log(bnLog);
			}
		}
	}

	/**
	 * 获取当前登录用户的用户名，如果没有当前登录用户则返回系统管理员的用户名。
	 * 
	 * @return 返回当前登录用户的用户名。
	 */
	private String getCurrentUsername() {
		if (ThreadContext.getSecurityManager() == null
				|| !SecurityUtils.getSubject().isAuthenticated()) {
			return securityService.getAdminUser().getUsername();
		} else {
			return securityService.getCurrentUser().getUsername();
		}
	}
}