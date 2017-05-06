package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.SimpleLog;

/**
 * 影院结算规则管理。
 */
@Service
public class CinemaRuleService {
	@Resource
	private Dao<CinemaRule> cinemaRuleDao;
	@Resource
	private CinemaPolicyService cinemaPolicyService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取指定ID的影院结算规则。
	 * 
	 * @param cinemaRuleId
	 *            影院结算规则ID
	 * @return 返回指定ID的影院结算规则。
	 */
	@Transactional(readOnly = true)
	public CinemaRule getCinemaRule(String cinemaRuleId) {
		return cinemaRuleDao.get(cinemaRuleId);
	}

	/**
	 * 新增影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 */
	@Transactional
	@AutoFillIn
	public void createCinemaRule(CinemaRule cinemaRule) {
		CinemaPolicy policy = cinemaPolicyService.getPolicy(cinemaRule
				.getPolicy().getId());
		// 如果影院结算策略处于待审核、待审批状态，不允许进行该操作。
		cinemaPolicyService.checkCinemaPolicyApprove(policy,
				"cinemaRule.add.approve");
		cinemaRule.setValid(ValidStatus.UNVALID);
		cinemaRule.setEnabled(EnabledStatus.DISABLED);
		policy.getRules().add(cinemaRule);
		cinemaRule.setPolicy(policy);
		cinemaRuleDao.save(cinemaRule);
		// 将影院结算策略修改为“待提交”状态。
		policy.setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 更新影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 */
	@Transactional
	@AutoFillIn
	public void updateCinemaRule(CinemaRule cinemaRule) {
		CinemaPolicy policy = cinemaPolicyService.getPolicy(cinemaRule
				.getPolicy().getId());
		// 如果影院结算策略处于待审核、待审批状态，不允许进行该操作。
		cinemaPolicyService.checkCinemaPolicyApprove(policy,
				"cinemaRule.edit.approve");
		CinemaRule origCinemaRule = cinemaRuleDao.get(cinemaRule.getId());
		// 如果编辑的影院结算规则是已生效的，则新增一条绑定关系的影院结算规则，如果编辑的影院结算规则是未生效的，则直接更新该影院结算规则。
		if (origCinemaRule.getValid() == ValidStatus.VALID) {
			// 如果影院结算规则已经被编辑过，不允许再次进行编辑。
			if (origCinemaRule.getBoundRule() != null) {
				messageSource.thrown("cinemaRule.edit.repeat");
			}
			cinemaRule.setId(null);
			cinemaRule.setValid(ValidStatus.UNVALID);
			cinemaRule.setEnabled(EnabledStatus.DISABLED);
			cinemaRule.setStatus(RuleStatus.UNAUDIT);
			cinemaRule.setBoundRule(origCinemaRule);
			cinemaRule.autoFillIn();
			policy.getRules().add(cinemaRule);
			cinemaRule.setPolicy(policy);
			cinemaRuleDao.save(cinemaRule);
			origCinemaRule.setBoundRule(cinemaRule);
		} else {
			BeanUtils.copyFields(cinemaRule, origCinemaRule,
					"valid,enabled,status");
		}
		policy.setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 删除影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 */
	@Transactional
	@SimpleLog(code = "cinemaRule.delete.log", vars = {
			"cinemaRule.policy.cinema.name", "cinemaRule.policy.name",
			"cinemaRule.name" })
	public void deleteCinemaRule(CinemaRule cinemaRule) {
		// 如果影院结算策略处于待审核、待审批状态，不允许进行该操作。
		cinemaPolicyService.checkCinemaPolicyApprove(cinemaRule.getPolicy(),
				"cinemaRule.delete.approve");
		// 如果规则"已生效",不允许删除。
		if (cinemaRule.getValid() == ValidStatus.VALID) {
			messageSource.thrown("cinemaRule.delete.valid");
		}
		// 如果影院结算规则是副本则解除原本的关联，删除副本。
		if (cinemaRule.isCopy()) {
			CinemaRule boundCinemaRule = cinemaRule.getBoundRule();
			boundCinemaRule.setBoundRule(null);
			cinemaRule.getPolicy().getRules().remove(cinemaRule);
			cinemaRuleDao.remove(cinemaRule);
		} else {
			// 如果规则“未生效”，即没有产品关联，直接删除。
			cinemaRule.getPolicy().getRules().remove(cinemaRule);
			cinemaRuleDao.remove(cinemaRule);
		}
	}

	/**
	 * 启用影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "cinemaRule.enable.log", vars = {
			"cinemaRule.policy.cinema.name", "cinemaRule.policy.name",
			"cinemaRule.name" })
	public List<Show> enableCinemaRule(CinemaRule cinemaRule) {
		// 如果影院结算规则未生效，不允许启用。
		checkValidStatus(cinemaRule, "cinemaRule.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (cinemaRule.getEnabled() == EnabledStatus.DISABLED) {
			// 启用结算规则时将结算规则影响到的排期重新生成渠道排期。
			cinemaRule.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(cinemaRule);
		}
		return shows;
	}

	/**
	 * 停用影院结算规则 。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "cinemaRule.disable.log", vars = {
			"cinemaRule.policy.cinema.name", "cinemaRule.policy.name",
			"cinemaRule.name" })
	public List<Show> disableCinemaRule(CinemaRule cinemaRule) {
		// 如果影院结算规则未生效，不允许停用。
		checkValidStatus(cinemaRule, "cinemaRule.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (cinemaRule.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置结算规则停用前先获取结算规则影响到的影院排期
			shows = showService.getMatchedShows(cinemaRule);
			// 停用结算规则时将结算规则影响到的排期全部置为失效。
			cinemaRule.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(cinemaRule);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 复制影院结算规则。
	 * 
	 * @param cinemaRuleId
	 *            影院结算规则ID
	 */
	@Transactional
	public void copyCinemaRule(String cinemaRuleId) {
		CinemaRule origCinemaRule = cinemaRuleDao.get(cinemaRuleId);
		// 如果影院结算策略处于待审核、待审批状态，不允许进行该操作。
		cinemaPolicyService.checkCinemaPolicyApprove(
				origCinemaRule.getPolicy(), "cinemaRule.copy.approve");
		CinemaRule cinemaRule = CinemaRule.copy(origCinemaRule,
				origCinemaRule.getPolicy());
		cinemaRuleDao.save(cinemaRule);
		// 将影院结算策略修改为“待提交”状态。
		origCinemaRule.getPolicy().setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 标记结算规则审核通过。
	 * 
	 * @param cinemaRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signAuditPass(String cinemaRuleId) {
		CinemaRule cinemaRule = getCinemaRule(cinemaRuleId);
		if (cinemaRule.getPolicy().getStatus() != PolicyStatus.AUDIT) {
			messageSource.thrown("cinemaRule.sign.audit.invalid");
		}
		cinemaRule.setStatus(RuleStatus.AUDITPASS);
	}

	/**
	 * 标记结算规则退回。
	 * 
	 * @param cinemaRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signRefuse(String cinemaRuleId) {
		CinemaRule cinemaRule = getCinemaRule(cinemaRuleId);
		if (cinemaRule.getPolicy().getStatus() != PolicyStatus.AUDIT) {
			messageSource.thrown("cinemaRule.sign.audit.invalid");
		}
		cinemaRule.setStatus(RuleStatus.REFUSE);
	}

	/**
	 * 标记结算规则审批通过。
	 * 
	 * @param cinemaRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signApprovePass(String cinemaRuleId) {
		CinemaRule cinemaRule = getCinemaRule(cinemaRuleId);
		if (cinemaRule.getPolicy().getStatus() != PolicyStatus.APPROVE) {
			messageSource.thrown("cinemaRule.sign.approve.invalid");
		}
		cinemaRule.setStatus(RuleStatus.APPROVEPASS);
	}

	/**
	 * 标记结算规则审批退回。
	 * 
	 * @param cinemaRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signApproveRefuse(String cinemaRuleId) {
		CinemaRule cinemaRule = getCinemaRule(cinemaRuleId);
		if (cinemaRule.getPolicy().getStatus() != PolicyStatus.APPROVE) {
			messageSource.thrown("cinemaRule.sign.approve.invalid");
		}
		cinemaRule.setStatus(RuleStatus.REFUSE);
	}

	/**
	 * 检测影院结算规则是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @param msgKey
	 *            提示语Key
	 */
	private void checkValidStatus(CinemaRule cinemaRule, String msgKey) {
		if (cinemaRule.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}