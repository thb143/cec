package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 影院结算策略管理。
 */
@Service
public class CinemaPolicyService {
	@Resource
	private Dao<CinemaPolicy> cinemaPolicyDao;
	@Resource
	private Dao<CinemaRule> cinemaRuleDao;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 分页搜索影院结算策略。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的影院结算策略分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<CinemaPolicy> searchPolicy(SearchModel searchModel) {
		FullTextCriteria criteria = cinemaPolicyDao.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("cinema.name", Analyze.NO);
		criteria.addSearchField("name", Analyze.NO);
		// 过滤”已失效“的策略。
		criteria.addFilterField("valid", ValidStatus.UNVALID.getValue(),
				ValidStatus.VALID.getValue());
		criteria.setKeyword(searchModel.getKeyword());
		return cinemaPolicyDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 获取指定ID的影院结算策略。
	 * 
	 * @param policyId
	 *            结算策略ID
	 * @return 返回指定ID的结算策略。
	 */
	@Transactional(readOnly = true)
	public CinemaPolicy getPolicy(String policyId) {
		return cinemaPolicyDao.get(policyId);
	}

	/**
	 * 新增结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "cinemaPolicy.add.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" })
	public void createCinemaPolicy(CinemaPolicy cinemaPolicy) {
		Cinema cinema = cinemaService.getCinema(cinemaPolicy.getCinema()
				.getId());
		cinemaPolicy.setOrdinal(cinema.genNewPolicyOrdinal());
		cinemaPolicy.setCinema(cinema);
		cinema.getPolicys().add(cinemaPolicy);
		cinemaPolicyDao.save(cinemaPolicy);
	}

	/**
	 * 更新结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "cinemaPolicy", code = "cinemaPolicy.edit.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" }, type = LogType.ALL)
	public void updateCinemaPolicy(CinemaPolicy cinemaPolicy) {
		CinemaPolicy origPolicy = cinemaPolicyDao.get(cinemaPolicy.getId());
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		checkCinemaPolicyApprove(origPolicy, "cinemaPolicy.edit.approve");
		BeanUtils.copyFields(cinemaPolicy, origPolicy,
				"status,valid,enabled,ordinal");
	}

	/**
	 * 删除结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 */
	@Transactional
	@SimpleLog(code = "cinemaPolicy.delete.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" })
	public void deleteCinemaPolicy(CinemaPolicy cinemaPolicy) {
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		checkCinemaPolicyApprove(cinemaPolicy, "cinemaPolicy.delete.approve");
		// 如果结算策略未过期且已生效，不允许删除。
		if (!cinemaPolicy.getExpired()
				&& cinemaPolicy.getValid() != ValidStatus.UNVALID) {
			messageSource.thrown("cinemaPolicy.delete.notexpired");
		}
		// 如果结算策略处于已过期、生效,则修改为”已失效“、停用。
		if (cinemaPolicy.getExpired()
				&& cinemaPolicy.getValid() != ValidStatus.UNVALID) {
			cinemaPolicy.setValid(ValidStatus.INVALID);
			cinemaPolicy.setEnabled(EnabledStatus.DISABLED);
		} else {
			cinemaPolicy.getCinema().getPolicys().remove(cinemaPolicy);
			cinemaPolicyDao.remove(cinemaPolicy);
		}
	}

	/**
	 * 上移结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 */
	@Transactional
	@SimpleLog(code = "cinemaPolicy.up.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" })
	public void upCinemaPolicy(CinemaPolicy cinemaPolicy) {
		moveCinemaPolicy(cinemaPolicy, Order.asc("ordinal"));
	}

	/**
	 * 下移结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 */
	@Transactional
	@SimpleLog(code = "cinemaPolicy.down.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" })
	public void downCinemaPolicy(CinemaPolicy cinemaPolicy) {
		moveCinemaPolicy(cinemaPolicy, Order.desc("ordinal"));
	}

	/**
	 * 启用结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "cinemaPolicy.enable.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" })
	public List<Show> enablePolicy(CinemaPolicy cinemaPolicy) {
		// 如果结算策略未生效，不允许启用。
		checkValidStatus(cinemaPolicy, "cinemaPolicy.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (cinemaPolicy.getEnabled() == EnabledStatus.DISABLED) {
			cinemaPolicy.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(cinemaPolicy);
		}
		return shows;
	}

	/**
	 * 停用结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "cinemaPolicy.disable.log", vars = {
			"cinemaPolicy.cinema.name", "cinemaPolicy.name" })
	public List<Show> disablePolicy(CinemaPolicy cinemaPolicy) {
		// 如果结算策略未生效，不允许停用。
		checkValidStatus(cinemaPolicy, "cinemaPolicy.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (cinemaPolicy.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置结算策略停用前先获取结算策略影响到的影院排期
			shows = showService.getMatchedShows(cinemaPolicy);
			// 停用结算策略时将结算策略影响到的排期全部置为失效。
			cinemaPolicy.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(cinemaPolicy);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 复制结算策略。
	 * 
	 * @param origCinemaPolicy
	 *            原结算策略
	 * @param toCinemaId
	 *            复制到目标影院ID
	 * @return 返回指定的结算策略。
	 */
	@Transactional
	@SimpleLog(code = "cinemaPolicy.copy.log", vars = {
			"origCinemaPolicy.cinema.name", "origCinemaPolicy.name" })
	public CinemaPolicy copyPolicy(CinemaPolicy origCinemaPolicy,
			String toCinemaId) {
		CinemaPolicy cinemaPolicy = CinemaPolicy.copy(origCinemaPolicy,
				cinemaService.getCinema(toCinemaId));
		cinemaPolicyDao.save(cinemaPolicy);
		// 结算策略和结算策略规则之间没有设置级联保存关系，这里需要单独保存结算策略规则。
		for (CinemaRule cinemaRule : cinemaPolicy.getRules()) {
			cinemaRuleDao.save(cinemaRule);
		}
		return cinemaPolicy;
	}

	/**
	 * 检查结算策略是否允许进行操作，如果处于待审核、待审批状态抛出不允许操作的异常提示信息。
	 * 
	 * @param cinemaPolicy
	 *            影院结算策略
	 * @param msgKey
	 *            提示语Key
	 */
	public void checkCinemaPolicyApprove(CinemaPolicy cinemaPolicy,
			String msgKey) {
		if (cinemaPolicy.getStatus() == PolicyStatus.AUDIT
				|| cinemaPolicy.getStatus() == PolicyStatus.APPROVE) {
			messageSource.thrown(msgKey);
		}
	}

	/**
	 * 移动结算策略。
	 * 
	 * @param cinemaPolicy
	 *            结算策略
	 * @param order
	 *            排序
	 */
	@SuppressWarnings("unchecked")
	private void moveCinemaPolicy(CinemaPolicy cinemaPolicy, Order order) {
		Criteria criteria = cinemaPolicyDao.createCriteria();
		if (order.isAscending()) {
			criteria.add(Restrictions.gt("ordinal", cinemaPolicy.getOrdinal()));
		} else {
			criteria.add(Restrictions.lt("ordinal", cinemaPolicy.getOrdinal()));
		}
		criteria.add(Restrictions.eq("cinema", cinemaPolicy.getCinema()));
		criteria.add(Restrictions.in("valid", new Object[] {
				ValidStatus.UNVALID, ValidStatus.VALID }));
		criteria.addOrder(order);
		List<CinemaPolicy> policies = criteria.list();
		if (!policies.isEmpty()) {
			Integer currentOrdinal = cinemaPolicy.getOrdinal();
			CinemaPolicy switchPolicy = policies.get(0);
			cinemaPolicy.setOrdinal(switchPolicy.getOrdinal());
			switchPolicy.setOrdinal(currentOrdinal);
		}
	}

	/**
	 * 检测结算策略是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param policy
	 *            结算策略
	 * @param msgKey
	 *            提示语Key
	 * 
	 */
	private void checkValidStatus(CinemaPolicy policy, String msgKey) {
		if (policy.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}