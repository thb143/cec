package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BenefitCardSettle;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.ChannelShowType;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.mail.ChannelShowSubsidyMailModel;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.model.ChannelShowSearchModel;
import cn.mopon.cec.core.model.ShowSettleRulesModel;
import cn.mopon.cec.core.task.ChannelShowGenTask;
import coo.base.model.Page;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.SimpleLog;

/**
 * 渠道排期管理。
 */
@Service
public class ChannelShowService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private Dao<ChannelShow> channelShowDao;
	@Resource
	private Dao<Channel> channelDao;
	@Resource
	private Dao<Show> showDao;
	@Resource
	private Dao<BenefitCardSettle> benefitCardSettleDao;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private Circuit circuit;
	@Resource
	private MailService mailService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private ExecutorService channelShowGenTaskExecutor;

	/**
	 * 分页搜索渠道排期。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的渠道排期分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<ChannelShow> searchChannelShow(
			ChannelShowSearchModel searchModel) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("channel.id", searchModel.getChannelId());
		criteria.addSortDesc("showTime", SortField.Type.LONG);
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		criteria.addSortAsc("status", SortField.Type.STRING);

		if (searchModel.getStatus() != null) {
			criteria.addFilterField("status", searchModel.getStatus()
					.getValue());
		}

		Query showTimeQuery = searchModel.genQuery("showTime");
		criteria.addLuceneQuery(showTimeQuery, Occur.MUST);

		criteria.setKeyword(searchModel.getKeyword());

		return channelShowDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 生成渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @return 返回生成的渠道排期条数。
	 */
	@Transactional
	public Integer genChannelShows(Show show) {
		Integer count = 0;
		if (show.isValid()) {
			ShowSettleRulesModel rulesModel = getShowSettleRulesModel(show);
			count += genChannelShows(show, rulesModel.getSpecialChannels(),
					rulesModel.getBenefitRules());
			count += genChannelShows(show, rulesModel.getCinemaRule(),
					rulesModel.getChannelRules(), rulesModel.getBenefitRules());
		}
		return count;
	}

	/**
	 * 生成渠道排期。
	 * 
	 * @param showId
	 *            影院排期ID
	 * @return 返回生成的渠道排期条数。
	 */
	@Transactional
	public Integer genChannelShows(String showId) {
		Show show = showDao.get(showId);
		return genChannelShows(show);
	}

	/**
	 * 批量生成渠道排期。
	 * 
	 * @param showIds
	 *            排期ID列表
	 * @return 返回生成的渠道排期条数。
	 */
	public Integer batchGenChannelShows(String[] showIds) {
		Integer count = 0;
		try {
			List<ChannelShowGenTask> tasks = new ArrayList<>();
			for (String showId : showIds) {
				tasks.add(new ChannelShowGenTask(showId));
			}
			for (Future<Integer> result : channelShowGenTaskExecutor
					.invokeAll(tasks)) {
				count += result.get();
			}
		} catch (Exception e) {
			log.error("批量生成渠道排期时发生异常。", e);
		}
		return count;
	}

	/**
	 * 批量生成渠道排期。
	 * 
	 * @param shows
	 *            排期列表
	 * @return 返回生成的渠道排期条数。
	 */
	public void batchGenChannelShows(List<Show> shows) {
		for (Show show : shows) {
			channelShowGenTaskExecutor.submit(new ChannelShowGenTask(show
					.getId()));
		}
	}

	/**
	 * 获取渠道排期。
	 * 
	 * @param channelShowId
	 *            渠道排期ID
	 * @return 返回渠道排期。
	 */
	@Transactional(readOnly = true)
	public ChannelShow getChannelShow(String channelShowId) {
		return channelShowDao.get(channelShowId);
	}

	/**
	 * 上架渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	@Transactional
	@SimpleLog(code = "channelShow.enable.log", vars = {
			"channelShow.showCode", "channelShow.code",
			"channelShow.channel.name", "channelShow.film.name" })
	public void enableChannelShow(ChannelShow channelShow) {
		if (channelShow.getStatus() == ShelveStatus.INVALID) {
			messageSource.thrown("channelShow.enable.unvalid");
		}
		channelShow.setStatus(ShelveStatus.ON);
	}

	/**
	 * 批量上架渠道排期。
	 * 
	 * @param channelShowIds
	 *            渠道排期ID列表
	 */
	@Transactional
	@SimpleLog(code = "channelShow.batch.enable.log")
	public void batchEnableChannelShows(String[] channelShowIds) {
		for (String channelShowId : channelShowIds) {
			ChannelShow channelShow = getChannelShow(channelShowId);
			if (channelShow.isOnable()) {
				channelShow.setStatus(ShelveStatus.ON);
			}
		}
	}

	/**
	 * 下架渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	@Transactional
	@SimpleLog(code = "channelShow.disable.log", vars = {
			"channelShow.showCode", "channelShow.code",
			"channelShow.channel.name", "channelShow.film.name" })
	public void disableChannelShow(ChannelShow channelShow) {
		if (channelShow.getStatus() == ShelveStatus.INVALID) {
			messageSource.thrown("channelShow.disable.unvalid");
		}
		channelShow.setStatus(ShelveStatus.OFF);
	}

	/**
	 * 批量下架渠道排期。
	 * 
	 * @param channelShowIds
	 *            渠道排期ID列表
	 */
	@Transactional
	@SimpleLog(code = "channelShow.batch.disable.log")
	public void batchDisableChannelShows(String[] channelShowIds) {
		for (String channelShowId : channelShowIds) {
			ChannelShow channelShow = getChannelShow(channelShowId);
			if (channelShow.isOffable()) {
				channelShow.setStatus(ShelveStatus.OFF);
			}
		}
	}

	/**
	 * 获取影院匹配的的渠道排期。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回影院匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(Cinema cinema) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("cinema.id", cinema.getId());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取影院结算规则匹配的的渠道排期。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回影院结算规则匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(CinemaRule cinemaRule) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("cinemaRule.id", cinemaRule.getId());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取影院结算策略匹配的渠道排期。
	 * 
	 * @param cinemaPolicy
	 *            影院结算策略
	 * @return 返回影院结算策略匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(CinemaPolicy cinemaPolicy) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("cinemaRule.id", cinemaPolicy.getRuleIds()
				.toArray());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取渠道匹配的渠道排期。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回渠道匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(Channel channel) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("channel.id", channel.getId());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取渠道结算规则匹配的渠道排期。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回渠道结算规则匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(ChannelRule channelRule) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("channelRule.id", channelRule.getId());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取渠道结算规则分组匹配的渠道排期。
	 * 
	 * @param channelRuleGroup
	 *            渠道结算规则分组
	 * @return 返回渠道结算规则分组匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(
			ChannelRuleGroup channelRuleGroup) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("channelRule.id", channelRuleGroup.getRuleIds()
				.toArray());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取渠道结算策略匹配的渠道排期。
	 * 
	 * @param channelPolicy
	 *            渠道结算策略
	 * @return 返回渠道结算策略匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(ChannelPolicy channelPolicy) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("channelRule.id", channelPolicy.getRuleIds()
				.toArray());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取特殊定价渠道匹配的渠道排期。
	 * 
	 * @param specialChannel
	 *            特殊定价渠道
	 * @return 返回特殊定价渠道匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(
			SpecialChannel specialChannel) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("specialChannel.id", specialChannel.getId());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取特殊定价规则匹配的渠道排期。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回特殊定价规则匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(SpecialRule specialRule) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("specialRule.id", specialRule.getId());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取特殊定价策略匹配的渠道排期。
	 * 
	 * @param specialPolicy
	 *            特殊定价策略
	 * @return 返回特殊定价策略匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(SpecialPolicy specialPolicy) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		criteria.addFilterField("specialRule.id", specialPolicy.getRuleIds()
				.toArray());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 获取卡类定价策略匹配的渠道排期。
	 * 
	 * @param benefitCardType
	 *            卡类
	 * @return 返回卡类定价策略匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(
			BenefitCardType benefitCardType) {
		FullTextCriteria criteria = benefitCardSettleDao
				.createFullTextCriteria();
		criteria.addFilterField("channelShow.status",
				ShelveStatus.ON.getValue(), ShelveStatus.OFF.getValue());
		criteria.addFilterField("rule.id", benefitCardType.getRuleIds()
				.toArray());
		List<BenefitCardSettle> settles = benefitCardSettleDao
				.searchBy(criteria);
		List<ChannelShow> channelShows = new ArrayList<ChannelShow>();
		for (BenefitCardSettle settle : settles) {
			channelShows.add(settle.getChannelShow());
		}
		return channelShows;
	}

	/**
	 * 获取卡类定价规则匹配的渠道排期。
	 * 
	 * @param rule
	 *            卡类定价规则
	 * @return 返回卡类定价规则匹配的渠道排期。
	 */
	@Transactional(readOnly = true)
	public List<ChannelShow> getMatchedChannelShows(BenefitCardTypeRule rule) {
		List<ChannelShow> shows = new ArrayList<>();
		for (Hall hall : rule.getHalls()) {
			shows.addAll(getMatchedChannelShows(hall));
		}
		return shows;
	}

	private List<ChannelShow> getMatchedChannelShows(Hall hall) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("hall.id", hall.getId());
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 作废渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @return 作废渠道排期数。
	 */
	@Transactional
	public Integer desChannelShows(Show show) {
		Integer count = 0;
		for (ChannelShow channelShow : show.getChannelShows()) {
			if (channelShow.getStatus() != ShelveStatus.INVALID) {
				channelShow.setStatus(ShelveStatus.INVALID);
				count++;
			}
		}
		return count;
	}

	/**
	 * 作废渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @param type
	 *            渠道排期类型
	 * @return 作废渠道排期数。
	 */
	@Transactional
	public Integer desChannelShows(Show show, ChannelShowType type) {
		Integer count = 0;
		for (ChannelShow channelShow : show.getChannelShows()) {
			if (channelShow.getStatus() != ShelveStatus.INVALID
					&& channelShow.getType() == type) {
				channelShow.setStatus(ShelveStatus.INVALID);
				count++;
			}
		}
		return count;
	}

	/**
	 * 更新过期渠道排期。
	 */
	@Transactional
	public void updateExpiredChannelShows() {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		String upperTime = DateUtils.format(DateTime.now().toDate(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("expireTime", null, upperTime);
		// 每次更新1000条
		for (ChannelShow channelShow : channelShowDao.searchBy(criteria, 1000)) {
			channelShow.setStatus(ShelveStatus.INVALID);
		}
	}

	/**
	 * 根据特殊定价渠道生成渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @param specialChannels
	 *            特殊定价渠道列表
	 * @return 返回生成的渠道排期条数。
	 */
	private Integer genChannelShows(Show show,
			List<SpecialChannel> specialChannels,
			List<BenefitCardTypeRule> benefitRules) {
		Integer count = 0;
		// 如果没有找到匹配的特殊定价渠道，不生成渠道排期。
		// 如果已经生成渠道排期，影院排期进行更新导致匹配不到特殊定价渠道，则将之前生产的渠道排期置为失效状态。
		if (CollectionUtils.isNotEmpty(specialChannels)) {
			for (SpecialChannel specialChannel : specialChannels) {
				ChannelShow channelShow = ChannelShow.create(show,
						specialChannel, benefitRules);
				if (genChannelShow(channelShow)) {
					count++;
				}
				// 如果是独占特价策略，作废当前有效的普通渠道排期以及策略优先级低于当前独占特价策略的特价渠道排期。
				if (specialChannel.getRule().getPolicy().getExclusive()) {
					for (ChannelShow validChannelShow : show
							.getValidChannelShows()) {
						if (validChannelShow.getType() == ChannelShowType.NORMAL
								|| validChannelShow.getSpecialRule()
										.getPolicy().getOrdinal() < channelShow
										.getSpecialRule().getPolicy()
										.getOrdinal()) {
							validChannelShow.setStatus(ShelveStatus.INVALID);
						}
					}
				}
			}
		} else {
			desChannelShows(show, ChannelShowType.SPECIAL);
		}
		return count;
	}

	/**
	 * 根据影院结算规则和渠道结算规则生成渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @param cinemaRule
	 *            影院结算规则
	 * @param channelRules
	 *            渠道结算规则
	 * @return 返回生成的渠道排期条数。
	 */
	private Integer genChannelShows(Show show, CinemaRule cinemaRule,
			List<ChannelRule> channelRules,
			List<BenefitCardTypeRule> benefitRules) {
		Integer count = 0;
		// 如果没有找到匹配的影院结算策略，不生成渠道排期。
		// 如果已经生成渠道排期，影院排期进行更新导致匹配不到影院规则，则将之前生产的渠道排期置为失效状态。
		if (cinemaRule != null) {
			for (ChannelRule channelRule : channelRules) {
				ChannelShow channelShow = ChannelShow.create(show, cinemaRule,
						channelRule, benefitRules);
				if (genChannelShow(channelShow)) {
					count++;
				}
			}
		} else {
			desChannelShows(show, ChannelShowType.NORMAL);
		}
		return count;
	}

	/**
	 * 生成渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 生成了新的渠道排期返回true，没有生成新的渠道排期返回false。
	 */
	private Boolean genChannelShow(ChannelShow channelShow) {
		Boolean gened = false;
		ChannelShow origChannelShow = channelShow.getValidChannelShow();
		if (origChannelShow != null) {
			gened = createChannelShow(channelShow, origChannelShow);
		} else {
			gened = createChannelShow(channelShow);
		}
		// 如果渠道排期生成成功并且生成的渠道排期补贴费大于0，发送预警邮件。
		if (gened && channelShow.getSubsidyFee() > 0) {
			List<User> users = circuit.getPriceWarnUsers();
			ChannelShowSubsidyMailModel model = new ChannelShowSubsidyMailModel(
					channelShow, users);
			mailService.send(model);
		}
		return gened;
	}

	/**
	 * 创建渠道排期。
	 * 
	 * @param channelShow
	 *            新渠道排期
	 * @param origChannelShow
	 *            原渠道排期
	 * @return 如果创建了渠道排期返回true，否则返回false。
	 */
	private Boolean createChannelShow(ChannelShow channelShow,
			ChannelShow origChannelShow) {
		if (!channelShow.equalsTo(origChannelShow)
				|| !channelShow.benefitCardSettlesEqualsTo(origChannelShow)) {
			origChannelShow.setStatus(ShelveStatus.INVALID);
			channelShow.setCode(serialNumberService.getChannelShowCode());
			channelShow.setInvalidCode(origChannelShow.getCode());
			channelShowDao.save(channelShow);
			createBenefitCardSettles(channelShow);
			channelShow.getShow().getChannelShows().add(channelShow);
			return true;
		}
		return false;
	}

	/**
	 * 保存渠道排期权益卡类价格信息。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param settles
	 *            价格信息
	 */
	private void createBenefitCardSettles(ChannelShow channelShow) {
		for (BenefitCardSettle settle : channelShow.getBenefitCardSettles()) {
			settle.setChannelShow(channelShow);
			benefitCardSettleDao.save(settle);
		}
	}

	/**
	 * 创建渠道排期。
	 * 
	 * @param channelShow
	 *            新渠道排期
	 * @return 如果创建了渠道排期返回true，否则返回false。
	 */
	private Boolean createChannelShow(ChannelShow channelShow) {
		// 将渠道ID+影院ID+影院排期编码的MD5码做为初始的失效渠道排期编码。
		// 解决初次生成渠道排期重复的问题。
		String invalidChannelShowCode = channelShow.genInvalidCode();
		// 如果获取最后失效的渠道排期不为空，则将失效渠道排期编码设置为最后失效的渠道排期编码。
		// 解决开关引起的重新生成渠道排期重复的问题。
		ChannelShow lastInvalidChannelShow = channelShow
				.getLastInvalidChannelShow();
		if (lastInvalidChannelShow != null) {
			invalidChannelShowCode = lastInvalidChannelShow.getCode();
		}
		channelShow.setCode(serialNumberService.getChannelShowCode());
		channelShow.setInvalidCode(invalidChannelShowCode);
		channelShowDao.save(channelShow);
		createBenefitCardSettles(channelShow);
		channelShow.getShow().getChannelShows().add(channelShow);
		return true;
	}

	/**
	 * 获取排期结算规则列表模型。
	 * 
	 * @param show
	 *            排期
	 * @return 返回排期结算规则列表模型。
	 */
	private ShowSettleRulesModel getShowSettleRulesModel(Show show) {
		ShowSettleRulesModel model = new ShowSettleRulesModel();
		model.setCinemaRule(getMatchedCinemaRule(show));
		// 增加有效的特殊定价渠道。
		addSpecialChannels(model, show);
		// 如果特殊定价策略允许生成常规商品，则增加有效的渠道结算规则。
		if (!model.getExclusive()) {
			model.addChannelRules(getChannelRules(show));
		}
		// 增加有效的权益卡定价规则。
		addBenefitCardTypeRules(model, show);
		return model;
	}

	/**
	 * 获取排期匹配的影院结算规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回排期匹配的影院结算规则。
	 */
	private CinemaRule getMatchedCinemaRule(Show show) {
		return show.getCinema().getMatchedRule(show);
	}

	/**
	 * 获取排期匹配的渠道结算规则列表。
	 * 
	 * @param show
	 *            排期
	 * @return 返回排期匹配的渠道结算规则列表。
	 */
	private List<ChannelRule> getChannelRules(Show show) {
		List<ChannelRule> rules = new ArrayList<>();
		// 这里不过滤关闭的渠道，因为启用渠道时渠道从关闭状态修改成开放状态，此时全文索引中的状态还未修改。
		// 在获取匹配的渠道规则时会过滤掉关闭状态的渠道，而此时内存中的渠道状态已经是开放的，因此不受影响。
		for (Channel channel : channelDao.searchAll()) {
			ChannelRule rule = channel.getMatchedRule(show);
			if (rule != null) {
				rules.add(rule);
			}
		}
		return rules;
	}

	/**
	 * 增加排期匹配的特殊定价渠道。
	 * 
	 * @param model
	 *            排期结算规则列表模型
	 * @param show
	 *            排期
	 */
	private void addSpecialChannels(ShowSettleRulesModel model, Show show) {
		for (SpecialPolicy policy : specialPolicyService
				.getValidSpecialPolicys()) {
			List<SpecialRule> rules = policy.getMatchedRules(show);
			if (!rules.isEmpty()) {
				for (SpecialRule rule : rules) {
					model.addSpecialChannels(rule);
				}
				if (policy.getExclusive()) {
					model.setExclusive(true);
					break;
				}
			}
		}
	}

	/**
	 * 增加排期匹配的权益卡定价规则。
	 * 
	 * @param model
	 *            排期结算规则列表模型
	 * 
	 * @param show
	 *            排期
	 */
	private void addBenefitCardTypeRules(ShowSettleRulesModel model, Show show) {
		for (BenefitCardType cardType : benefitCardTypeService
				.getValidBenefitCardTypes()) {
			BenefitCardTypeRule rule = cardType.getMatchedRule(show);
			if (rule != null) {
				model.addBenefitRules(rule);
			}
		}
	}
}
