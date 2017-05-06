package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ThreadContext;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.ShowErrorLog;
import cn.mopon.cec.core.entity.ShowSyncLog;
import cn.mopon.cec.core.entity.ShowUpdateLog;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.enums.ShowErrorType;
import cn.mopon.cec.core.enums.ShowStatus;
import cn.mopon.cec.core.enums.SyncStatus;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.mail.ShowSyncErrorMailModel;
import cn.mopon.cec.core.mail.ShowSyncFailedMailModel;
import cn.mopon.cec.core.model.ExternalShowsModel;
import cn.mopon.cec.core.model.ShowSearchModel;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.base.util.ThrowableUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;

/**
 * 排期管理。
 */
@Service
public class ShowService {
	@Resource
	private SecurityService securityService;
	@Resource
	private Dao<Show> showDao;
	@Resource
	private Dao<ShowSyncLog> showSyncLogDao;
	@Resource
	private Dao<ShowUpdateLog> showUpdateLogDao;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private FilmService filmService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MailService mailService;
	@Resource
	private Circuit circuit;
	@Resource
	private MessageSource messageSource;

	/**
	 * 分页搜索排期。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的排期分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<Show> searchShow(ShowSearchModel searchModel) {
		FullTextCriteria criteria = showDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", searchModel.getCinemaId());
		criteria.addSortDesc("showTime", SortField.Type.LONG);
		criteria.addSortAsc("code", SortField.Type.STRING);

		if (searchModel.getStatus() != null) {
			criteria.addFilterField("status", searchModel.getStatus()
					.getValue());
		}

		Query showTimeQuery = searchModel.genQuery("showTime");
		criteria.addLuceneQuery(showTimeQuery, Occur.MUST);

		criteria.setKeyword(searchModel.getKeyword());

		return showDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 根据排期ID获取排期。
	 * 
	 * @param showId
	 *            排期ID
	 * @return 返回排期。
	 */
	@Transactional(readOnly = true)
	public Show getShow(String showId) {
		return showDao.get(showId);
	}

	/**
	 * 同步排期。
	 * 
	 * @param cinemaId
	 *            影院ID
	 */
	@Transactional
	public void syncShows(String cinemaId) {
		Cinema cinema = cinemaService.getCinema(cinemaId);
		syncShows(cinema);
	}

	/**
	 * 同步排期。
	 * 
	 * @param cinema
	 *            影院
	 */
	@Transactional
	public void syncShows(Cinema cinema) {
		long startTime = System.currentTimeMillis();
		ShowSyncLog showSyncLog = new ShowSyncLog();
		showSyncLog.setCinema(cinema);

		ExternalShowsModel externalShowsModel = getExternalShowsModel(showSyncLog);
		showSyncLog.setProcessCount(externalShowsModel.getCount());

		// 如果不是同步失败，则处理同步过来的排期记录
		if (showSyncLog.getStatus() != SyncStatus.FAILED) {
			createShows(externalShowsModel.getNewShows(), showSyncLog);
			updateShows(externalShowsModel.getUpdateShows(), showSyncLog);
			deleteShows(externalShowsModel.getDeleteShows(), showSyncLog);
			// 设置排期最后更新时间
			cinema.getTicketSettings().setLastSyncShowTime(new Date());
		}

		long endTime = System.currentTimeMillis();
		showSyncLog.setDuration(endTime - startTime);
		showSyncLogDao.save(showSyncLog);

		sendShowSyncErrorMail(showSyncLog);
	}

	/**
	 * 更新过期排期。
	 */
	@Transactional
	public void updateExpiredShows() {
		FullTextCriteria criteria = showDao.createFullTextCriteria();
		criteria.addFilterField("status", ShowStatus.NEW.getValue(),
				ShowStatus.UPDATE.getValue());
		String upperTime = DateUtils.format(DateTime.now().toDate(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("expireTime", null, upperTime);
		// 每次更新100条
		for (Show show : showDao.searchBy(criteria, 100)) {
			show.setStatus(ShowStatus.INVALID);
		}
	}

	/**
	 * 清理超过保留天数的影院排期、排期同步记录、渠道排期。
	 * 
	 * @param cinemaId
	 *            影院Id
	 */
	@Transactional
	public void cleanShows(String cinemaId) {
		Cinema cinema = cinemaService.getCinema(cinemaId);
		String upperTime = DateUtils.format(DateUtils.getPrevDay(cinema
				.getTicketSettings().getKeepShowDays()),
				DateUtils.MILLISECOND_N);
		cleanShows(cinema, upperTime);
		cleanShowSyncLogs(cinema, upperTime);
	}

	/**
	 * 清理影院排期。
	 * 
	 * @param cinema
	 *            影院
	 * @param upperTime
	 *            保留时间
	 */
	private void cleanShows(Cinema cinema, String upperTime) {
		FullTextCriteria showCriteria = showDao.createFullTextCriteria();
		showCriteria.addFilterField("status", ShowStatus.INVALID.getValue());
		showCriteria.addFilterField("cinema.id", cinema.getId());
		showCriteria.addRangeField("showTime", null, upperTime);
		showCriteria.addSortAsc("showTime", SortField.Type.STRING);
		// 每次清理50条
		showDao.remove(showDao.searchBy(showCriteria, 50));
	}

	/**
	 * 清理排期同步日志。
	 * 
	 * @param cinema
	 *            影院
	 * @param upperTime
	 *            保留时间
	 */
	private void cleanShowSyncLogs(Cinema cinema, String upperTime) {
		FullTextCriteria criteria = showSyncLogDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", cinema.getId());
		criteria.addRangeField("syncTime", null, upperTime);
		// 每次清理50条
		showSyncLogDao.remove(showSyncLogDao.searchBy(criteria, 50));
	}

	/**
	 * 获取影院匹配的排期。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回影院匹配的排期。
	 */
	public List<Show> getMatchedShows(Cinema cinema) {
		FullTextCriteria criteria = showDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", cinema.getId());
		criteria.addFilterField("status", ShowStatus.NEW.getValue(),
				ShowStatus.UPDATE.getValue());
		criteria.addSortDesc("showTime", SortField.Type.LONG);
		return showDao.searchBy(criteria);
	}

	/**
	 * 获取影厅匹配的排期。
	 * 
	 * @param hall
	 *            影院
	 * @return 返回影厅匹配的排期。
	 */
	public List<Show> getMatchedShows(Hall hall) {
		FullTextCriteria criteria = showDao.createFullTextCriteria();
		criteria.addFilterField("hall.id", hall.getId());
		criteria.addFilterField("status", ShowStatus.NEW.getValue(),
				ShowStatus.UPDATE.getValue());
		criteria.addSortDesc("showTime", SortField.Type.LONG);
		return showDao.searchBy(criteria);
	}

	/**
	 * 获取影院结算规则匹配的排期。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回影院结算规则匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(CinemaRule cinemaRule) {
		List<Show> shows = new ArrayList<Show>();
		Cinema cinema = cinemaRule.getPolicy().getCinema();
		for (Show show : getMatchedShows(cinema)) {
			CinemaRule targetCinemaRule = cinema.getMatchedRule(show);
			if (targetCinemaRule != null && cinemaRule.equals(targetCinemaRule)) {
				shows.add(show);
			}
		}
		return shows;
	}

	/**
	 * 获取影院结算策略匹配的排期。
	 * 
	 * @param cinemaPolicy
	 *            影院结算策略
	 * @return 返回影院结算策略匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(CinemaPolicy cinemaPolicy) {
		List<Show> shows = new ArrayList<Show>();
		Cinema cinema = cinemaPolicy.getCinema();
		for (Show show : getMatchedShows(cinema)) {
			CinemaRule cinemaRule = cinema.getMatchedRule(show);
			if (cinemaRule != null
					&& cinemaRule.getPolicy().equals(cinemaPolicy)) {
				shows.add(show);
			}
		}
		return shows;
	}

	/**
	 * 获取渠道匹配的排期。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回渠道匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(Channel channel) {
		List<Show> shows = new ArrayList<Show>();
		// 从渠道开放的影院入手获取匹配的排期
		for (Cinema cinema : channel.getOpenedCinemas()) {
			for (Show show : getMatchedShows(cinema)) {
				if (channel.getMatchedRule(show) != null) {
					shows.add(show);
				}
			}
		}
		return shows;
	}

	/**
	 * 获取渠道结算规则匹配的排期。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回渠道结算规则匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(ChannelRule channelRule) {
		List<Show> shows = new ArrayList<Show>();
		Cinema cinema = channelRule.getGroup().getCinema();
		for (Show show : getMatchedShows(cinema)) {
			ChannelRule targetChannelRule = channelRule.getChannel()
					.getMatchedRule(show);
			if (targetChannelRule != null
					&& channelRule.equals(targetChannelRule)) {
				shows.add(show);
			}
		}
		return shows;
	}

	/**
	 * 获取渠道结算规则分组匹配的排期。
	 * 
	 * @param channelRuleGroup
	 *            渠道结算规则分组
	 * @return 返回渠道结算规则分组匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(ChannelRuleGroup channelRuleGroup) {
		List<Show> shows = new ArrayList<Show>();
		Cinema cinema = channelRuleGroup.getCinema();
		for (Show show : getMatchedShows(cinema)) {
			ChannelRule channelRule = channelRuleGroup.getPolicy().getChannel()
					.getMatchedRule(show);
			if (channelRule != null
					&& channelRule.getGroup().equals(channelRuleGroup)) {
				shows.add(show);
			}
		}
		return shows;
	}

	/**
	 * 获取渠道结算策略匹配的排期。
	 * 
	 * @param channelPolicy
	 *            渠道结算策略
	 * @return 返回渠道结算策略匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(ChannelPolicy channelPolicy) {
		List<Show> shows = new ArrayList<Show>();
		for (ChannelRuleGroup channelRuleGroup : channelPolicy
				.getOpenedGroups()) {
			shows.addAll(getMatchedShows(channelRuleGroup));
		}
		return shows;
	}

	/**
	 * 获取特殊定价规则匹配的排期。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回特殊定价规则匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(SpecialRule specialRule) {
		List<Show> shows = new ArrayList<Show>();
		// 从特殊定价规则关联的影厅入手获取匹配的排期
		for (Hall hall : specialRule.getHalls()) {
			for (Show show : getMatchedShows(hall)) {
				List<SpecialRule> matchedRules = specialRule.getPolicy()
						.getMatchedRules(show);
				if (matchedRules.contains(specialRule)) {
					shows.add(show);
				}
			}
		}
		return shows;
	}

	/**
	 * 获取特殊定价策略匹配的排期。
	 * 
	 * @param specialPolicy
	 *            特殊定价策略
	 * @return 返回特殊定价策略匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(SpecialPolicy specialPolicy) {
		List<Show> shows = new ArrayList<>();
		// 从特殊定价策略关联的影厅入手获取匹配的排期
		for (Hall hall : specialPolicy.getHalls()) {
			for (Show show : getMatchedShows(hall)) {
				if (!specialPolicy.getMatchedRules(show).isEmpty()) {
					shows.add(show);
				}
			}
		}
		return shows;
	}

	/**
	 * 获取权益卡类关联的影院排期。
	 * 
	 * @param cardType
	 *            权益卡类
	 * @return 影院排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(BenefitCardType cardType) {
		List<Show> shows = new ArrayList<>();
		for (BenefitCardTypeRule rule : cardType.getRules()) {
			getMatchedShow(rule, shows);
		}
		return shows;
	}

	/**
	 * 获取权益卡类规则匹配的排期。
	 * 
	 * @param cardTypeRule
	 *            权益卡类规则
	 * @return 返回权益卡类规则匹配的排期。
	 */
	@Transactional(readOnly = true)
	public List<Show> getMatchedShows(BenefitCardTypeRule cardTypeRule) {
		List<Show> shows = new ArrayList<>();
		getMatchedShow(cardTypeRule, shows);
		return shows;
	}

	/**
	 * 获取权益卡规则关联的排期。
	 * 
	 * @param cardTypeRule
	 *            权益卡规则
	 * @param shows
	 *            排期列表
	 */
	private void getMatchedShow(BenefitCardTypeRule cardTypeRule,
			List<Show> shows) {
		for (Hall hall : cardTypeRule.getHalls()) {
			for (Show show : getMatchedShows(hall)) {
				if (cardTypeRule.matchShow(show)
						&& benefitRuleMatched(cardTypeRule, show)) {
					shows.add(show);
				}
			}
		}
	}

	/**
	 * 判断权益卡规则是否匹配排期。
	 * 
	 * @param cardTypeRule
	 *            权益卡规则
	 * @param show
	 *            排期
	 * @return 匹配返回true,不匹配返回false。
	 */
	private boolean benefitRuleMatched(BenefitCardTypeRule cardTypeRule,
			Show show) {
		BenefitCardType type = cardTypeRule.getType();
		if (cardTypeRule.getType().isBounded()) {
			type = type.getBoundType();
		}
		for (Channel channel : type.getChannels()) {
			if (channel.getMatchedRule(show) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 批量新增排期。
	 * 
	 * @param externalShows
	 *            外部排期列表
	 * @param showSyncLog
	 *            排期同步记录
	 */
	private void createShows(List<Show> externalShows, ShowSyncLog showSyncLog) {
		for (Show externalShow : externalShows) {
			if (checkExternalShow(externalShow, showSyncLog)) {
				createShow(externalShow);
				showSyncLog.addCreateCount();
				channelShowService.genChannelShows(externalShow);
			}
		}
	}

	/**
	 * 新增排期。
	 * 
	 * @param externalShow
	 *            外部排期
	 */
	private void createShow(Show externalShow) {
		externalShow.setStatus(ShowStatus.NEW);

		// 计算排期过期时间，放映时间+片长
		DateTime showTime = new DateTime(externalShow.getShowTime());
		DateTime expiredTime = showTime.plusMinutes(externalShow.getDuration());
		externalShow.setExpireTime(expiredTime.toDate());

		externalShow.autoFillIn();
		showDao.save(externalShow);
	}

	/**
	 * 批量更新排期。
	 * 
	 * @param externalShows
	 *            外部排期列表
	 * @param showSyncLog
	 *            排期同步记录
	 */
	private void updateShows(List<Show> externalShows, ShowSyncLog showSyncLog) {
		for (Show externalShow : externalShows) {
			Show origShow = getShowByCode(externalShow);
			if (checkExternalShow(externalShow, showSyncLog)
					&& isModified(externalShow, origShow)) {
				updateShow(externalShow);
				showSyncLog.addUpdateCount();
				channelShowService.genChannelShows(origShow);
			}
		}
	}

	/**
	 * 更新排期。
	 * 
	 * @param externalShow
	 *            外部排期
	 */
	private void updateShow(Show externalShow) {
		Show origShow = getShowByCode(externalShow);

		ShowUpdateLog updateLog = new ShowUpdateLog();
		updateLog.setMessage(messageSource.get("show.update.log",
				externalShow.getCode(), externalShow.getFilm().getName()));
		updateLog.setShow(origShow);
		updateLog.setCreateDate(new Date());
		updateLog.setCreator(getCurrentUsername());
		updateLog.setOrigData(origShow);

		BeanUtils.copyFields(externalShow, origShow);
		origShow.setStatus(ShowStatus.UPDATE);

		// 计算排期过期时间，放映时间+片长
		DateTime showTime = new DateTime(externalShow.getShowTime());
		DateTime expiredTime = showTime.plusMinutes(externalShow.getDuration());
		origShow.setExpireTime(expiredTime.toDate());

		origShow.autoFillIn();

		updateLog.setNewData(origShow);
		showUpdateLogDao.save(updateLog);
		origShow.getUpdateLogs().add(updateLog);
	}

	/**
	 * 批量删除排期。
	 * 
	 * @param shows
	 *            排期列表
	 * @param showSyncLog
	 *            排期同步日志
	 */
	private void deleteShows(List<Show> shows, ShowSyncLog showSyncLog) {
		for (Show show : shows) {
			deleteShow(show);
			showSyncLog.addDeleteCount();
		}
	}

	/**
	 * 删除排期。
	 * 
	 * @param show
	 *            排期
	 */
	private void deleteShow(Show show) {
		show.setStatus(ShowStatus.INVALID);
		channelShowService.desChannelShows(show);
	}

	/**
	 * 检查外部排期是否有效。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param showSyncLog
	 *            同步排期记录
	 * @return 如果有效返回true，否则返回false。
	 */
	private Boolean checkExternalShow(Show externalShow, ShowSyncLog showSyncLog) {
		return checkThrough(externalShow, showSyncLog)
				&& checkShowType(externalShow, showSyncLog)
				&& checkHall(externalShow, showSyncLog)
				&& checkFilm(externalShow, showSyncLog)
				&& checkPrice(externalShow, showSyncLog);
	}

	/**
	 * 检查是否连场。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param showSyncLog
	 *            同步排期记录
	 * @return 如果不是连场返回true，否则返回false。
	 */
	private Boolean checkThrough(Show externalShow, ShowSyncLog showSyncLog) {
		if (externalShow.getThrough()) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.SHOW,
					messageSource.get("show.sync.through.not.support"));
			return false;
		}
		return true;
	}

	/**
	 * 检查排期关联影片。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param showSyncLog
	 *            同步排期记录
	 * @return 如果找到关联影片返回true，否则返回false。
	 */
	private Boolean checkFilm(Show externalShow, ShowSyncLog showSyncLog) {
		String filmCode = externalShow.getFilmCode();
		if (StringUtils.isBlank(filmCode)) {
			externalShow.setFilmCode("NULL");
			showSyncLog.addErrorLog(externalShow, ShowErrorType.FILM,
					messageSource.get("show.sync.film.code.invalid", "NULL"));
			return false;
		}
		if (filmCode.length() < 12) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.FILM,
					messageSource.get("show.sync.film.code.invalid", filmCode));
			return false;
		}
		Film film = filmService.getFilmByCode(filmCode);
		if (film == null) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.FILM,
					messageSource.get("show.sync.film.not.exist"));
			return false;
		}
		externalShow.setFilm(film);
		double minPrice = externalShow.getLargerMinPrice();
		double defaultMinPrice = circuit.getMinPriceByShowType(externalShow
				.getShowType());
		if (minPrice < defaultMinPrice) {
			minPrice = defaultMinPrice;
		}
		externalShow.setMinPrice(minPrice);
		return true;
	}

	/**
	 * 检查排期的放映类型。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param showSyncLog
	 *            同步排期记录
	 * @return 如果放映类型有效返回true，否则返回false。
	 */
	private Boolean checkShowType(Show externalShow, ShowSyncLog showSyncLog) {
		// 检查放映类型是否有效
		if (externalShow.getShowType() == null) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.SHOW,
					messageSource.get("show.sync.showtype.invalid"));
			return false;
		}
		// 检查影院是否提供放映类型的选座票
		if (!showSyncLog.getCinema().getTicketSettings().getShowTypes()
				.contains(externalShow.getShowType())) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.SHOW,
					messageSource.get("show.sync.showtype.not.provide",
							externalShow.getShowType().getText()));
			return false;
		}
		return true;
	}

	/**
	 * 检查关联影厅是否存在且可用。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param showSyncLog
	 *            同步排期记录
	 * @return 如果找到关联的影厅且可用返回true，否则返回false。
	 */
	private Boolean checkHall(Show externalShow, ShowSyncLog showSyncLog) {
		Hall hall = showSyncLog.getCinema().getHall(
				externalShow.getHall().getCode());
		if (hall == null) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.SHOW,
					messageSource.get("show.sync.hall.not.exist"));
			return false;
		}
		// 影厅禁用过滤
		if (hall.getStatus() != HallStatus.ENABLED) {
			showSyncLog.addErrorLog(
					externalShow,
					ShowErrorType.SHOW,
					messageSource.get("show.sync.hall.not.enabled",
							hall.getName()));
			return false;
		}
		externalShow.setHall(hall);
		externalShow.setCinema(hall.getCinema());
		externalShow.setProvider(hall.getCinema().getProvider());
		return true;
	}

	/**
	 * 检查价格是否合法。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param showSyncLog
	 *            同步排期记录
	 * @return 如果不是连场返回true，否则返回false。
	 */
	private Boolean checkPrice(Show externalShow, ShowSyncLog showSyncLog) {
		Double minPrice = externalShow.getMinPrice();
		// Double stdPrice = externalShow.getStdPrice();
		// 如果最低价小于等于0，视为非法排期。
		if (minPrice <= 0) {
			showSyncLog.addErrorLog(externalShow, ShowErrorType.PRICE,
					messageSource.get("show.sync.minPrice.invalid", minPrice));
			return false;
		}
		// 如果标准价小于最低价，视为非法排期。
		// if (stdPrice < minPrice) {
		// showSyncLog.addErrorLog(externalShow, ShowErrorType.PRICE,
		// messageSource.get("show.sync.stdPrice.invalid", stdPrice,
		// minPrice));
		// return false;
		// }
		return true;
	}

	/**
	 * 判断排期是否被修改。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @param origShow
	 *            内部排期
	 * @return 如果外部排期被修改返回true，否则返回false。
	 */
	private Boolean isModified(Show externalShow, Show origShow) {
		// 如果内部排期是失效状态，则认为外部排期被修改过。
		if (origShow.getStatus() == ShowStatus.INVALID) {
			return true;
		} else {
			return !externalShow.equalsTo(origShow);
		}
	}

	/**
	 * 获取外部排期待处理模型。这里将外部排期按新增、更新、删除进行分组，方便后续处理。
	 * 
	 * @param showSyncLog
	 *            排期同步记录
	 * @return 返回外部排期待处理模型。
	 */
	private ExternalShowsModel getExternalShowsModel(ShowSyncLog showSyncLog) {
		ExternalShowsModel model = new ExternalShowsModel();

		List<Show> externalShows = getExternalShows(showSyncLog);
		// 如果获取外部排期为null，表示调用获取外部排期接口发生了异常，不处理已有的排期。
		if (externalShows == null) {
			return model;
		}

		List<String> externalShowCodes = new ArrayList<String>();
		for (Show show : externalShows) {
			if (!externalShowCodes.contains(show.getCode())) {
				if (isShowExist(show)) {
					model.addUpdateShow(show);
				} else {
					model.addNewShow(show);
				}
				externalShowCodes.add(show.getCode());
			}
		}

		for (Show show : getExistShows(showSyncLog)) {
			if (!externalShowCodes.contains(show.getCode())
					&& show.getStatus() != ShowStatus.INVALID) {
				model.addDeleteShow(show);
			}
		}

		return model;
	}

	/**
	 * 获取已同步的影院排期。
	 * 
	 * @param showSyncLog
	 *            排期同步记录
	 * @return 返回已同步的影院排期。
	 */
	private List<Show> getExistShows(ShowSyncLog showSyncLog) {
		FullTextCriteria criteria = showDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", showSyncLog.getCinema().getId());
		String startDateStr = DateUtils.format(showSyncLog.getSyncTime(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("showTime", startDateStr, null);
		return showDao.searchBy(criteria);
	}

	/**
	 * 判断外部排期是否存在对应的内部排期。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @return 如果存在对应的内部排期返回true，否则返回false。
	 */
	private Boolean isShowExist(Show externalShow) {
		return getShowByCode(externalShow) != null;
	}

	/**
	 * 根据外部排期获取内部排期。
	 * 
	 * @param externalShow
	 *            外部排期
	 * @return 返回对应的内部排期。
	 */
	private Show getShowByCode(Show externalShow) {
		FullTextCriteria criteria = showDao.createFullTextCriteria();
		criteria.addFilterField("cinema.code", externalShow.getCinema()
				.getCode());
		criteria.addFilterField("code", externalShow.getCode());
		return showDao.searchUnique(criteria);
	}

	/**
	 * 获取外部排期列表。
	 * 
	 * @param showSyncLog
	 *            排期同步日志
	 * @return 返回外部排期列表。
	 */
	private List<Show> getExternalShows(ShowSyncLog showSyncLog) {
		Cinema cinema = showSyncLog.getCinema();
		checkCinemaEnabled(cinema);
		TicketSettings ticketSettings = cinema.getTicketSettings();
		Integer syncShowDays = ticketSettings.getSyncShowDays();
		Date startDate = new DateTime(showSyncLog.getSyncTime()).toLocalDate()
				.toDate();
		Date endDate = new DateTime(startDate).plusDays(syncShowDays - 1)
				.toLocalDate().toDate();
		try {
			TicketAccessService ticketAccessService = TicketAccessServiceFactory
					.getTicketService(ticketSettings);
			List<Show> externalShows = ticketAccessService.getShows(cinema,
					startDate, endDate);
			// 如果获取地面排期为空视为异常情况。
			if (CollectionUtils.isEmpty(externalShows)) {
				showSyncLog.setMsg(messageSource.get("show.sync.no.shows"));
				showSyncLog.setStatus(SyncStatus.FAILED);
				return new ArrayList<Show>();
			}
			return externalShows;
		} catch (Exception e) {
			showSyncLog.setStatus(SyncStatus.FAILED);
			showSyncLog.setMsg(ThrowableUtils.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 检查影院是否可用。
	 * 
	 * @param cinema
	 *            影院
	 */
	private void checkCinemaEnabled(Cinema cinema) {
		if (cinema.getStatus() == EnabledStatus.DISABLED) {
			messageSource.thrown("show.sync.cinema.disabled");
		}
		if (!cinema.getTicketSetted()) {
			messageSource.thrown("show.sync.cinema.no.ticket.settings");
		}
	}

	/**
	 * 发送同步排期异常通知邮件。
	 * 
	 * @param showSyncLog
	 *            排期同步日志
	 */
	private void sendShowSyncErrorMail(ShowSyncLog showSyncLog) {
		if (showSyncLog.getStatus() == SyncStatus.ERROR) {
			sendShowErrorMail(showSyncLog);
		}
		if (showSyncLog.getStatus() == SyncStatus.FAILED) {
			ShowSyncFailedMailModel mailModel = new ShowSyncFailedMailModel(
					showSyncLog, circuit.getShowWarnUsers());
			mailService.send(mailModel);
		}
	}

	/**
	 * 发送同步排期数据异常通知邮件。
	 * 
	 * @param showSyncLog
	 *            排期同步日志
	 */
	private void sendShowErrorMail(ShowSyncLog showSyncLog) {
		for (ShowErrorType showErrorType : ShowErrorType.values()) {
			List<ShowErrorLog> showErrorLogs = showSyncLog
					.getErrorLogs(showErrorType);
			if (CollectionUtils.isNotEmpty(showErrorLogs)) {
				List<User> users = new ArrayList<>();
				switch (showErrorType) {
				case FILM:
					users = circuit.getFilmWarnUsers();
					break;
				case PRICE:
					users = circuit.getPriceWarnUsers();
					break;
				case SHOW:
					users = circuit.getShowWarnUsers();
					break;
				default:
					break;
				}
				ShowSyncErrorMailModel mailModel = new ShowSyncErrorMailModel(
						showSyncLog, showErrorType, showErrorLogs, users);
				mailService.send(mailModel);
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
