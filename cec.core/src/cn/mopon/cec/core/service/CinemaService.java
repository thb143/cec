package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.model.CinemaModel;
import cn.mopon.cec.core.model.CinemaSnackModel;
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
 * 影院管理。
 */
@Service
public class CinemaService {
	@Resource
	private Dao<Cinema> cinemaDao;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取按城市编码排序的影院列表。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回按城市编码排序的影院列表。
	 */
	@Transactional
	public List<Cinema> getCinemaList(SearchModel searchModel) {
		FullTextCriteria criteria = cinemaDao.createFullTextCriteria();
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSearchField("county.city.name", Analyze.NO);
		criteria.addSearchField("county.city.province.name", Analyze.NO);
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		List<Cinema> cinemas = cinemaDao.searchBy(criteria);
		Collections.sort(cinemas);
		return cinemas;
	}

	/**
	 * 分页搜索影院。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的影院分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<Cinema> searchCinema(SearchModel searchModel) {
		searchModel.setPageSize(30);
		FullTextCriteria criteria = cinemaDao.createFullTextCriteria();
		criteria.addSearchField("county.city.name", Analyze.NO);
		criteria.addSearchField("county.city.province.name", Analyze.NO);
		criteria.addSortAsc("county.city.code", SortField.Type.STRING);
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return cinemaDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 新增影院。
	 * 
	 * @param cinema
	 *            影院
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "cinema.add.log", vars = { "cinema.name", "cinema.code" })
	public void createCinema(Cinema cinema) {
		if (!cinemaDao.isUnique(cinema, "code")) {
			messageSource.thrown("cinema.add.exist");
		}
		if (!cinemaDao.isUnique(cinema, "name")) {
			messageSource.thrown("cinema.add.name.exist");
		}
		cinemaDao.save(cinema);
	}

	/**
	 * 获取指定ID的影院。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @return 返回对应的影院。
	 */
	@Transactional(readOnly = true)
	public Cinema getCinema(String cinemaId) {
		return cinemaDao.get(cinemaId);
	}

	/**
	 * 获取指定编码的影院。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @return 返回对应的影院。
	 */
	@Transactional(readOnly = true)
	public Cinema getCinemaByCode(String cinemaCode) {
		return cinemaDao.searchUnique("code", cinemaCode);
	}

	/**
	 * 更新影院。
	 * 
	 * @param cinema
	 *            影院
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "cinema", code = "cinema.edit.log", vars = {
			"cinema.name", "cinema.code" }, type = LogType.ALL)
	public void updateCinema(Cinema cinema) {
		if (!cinemaDao.isUnique(cinema, "code")) {
			messageSource.thrown("cinema.edit.exist");
		}
		if (!cinemaDao.isUnique(cinema, "name")) {
			messageSource.thrown("cinema.edit.name.exist");
		}
		Cinema origCinema = getCinema(cinema.getId());
		BeanUtils.copyFields(cinema, origCinema, "logo,deviceImg",
				"status,salable,ticketSetted,memberSetted");
	}

	/**
	 * 启用影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "cinema.enable.log", vars = { "cinema.name",
			"cinema.code" })
	public List<Show> enableCinema(Cinema cinema) {
		List<Show> shows = new ArrayList<>();
		if (cinema.getStatus() == EnabledStatus.DISABLED) {
			cinema.setStatus(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(cinema);
		}
		return shows;
	}

	/**
	 * 停用影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回作废渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "cinema.disable.log", vars = { "cinema.name",
			"cinema.code" })
	public Integer disableCinema(Cinema cinema) {
		Integer count = 0;
		if (cinema.getStatus() == EnabledStatus.ENABLED) {
			cinema.setStatus(EnabledStatus.DISABLED);
			List<Show> shows = showService.getMatchedShows(cinema);
			for (Show show : shows) {
				count += channelShowService.desChannelShows(show);
			}
		}
		return count;
	}

	/**
	 * 启售影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回上架渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "cinema.open.salable.log", vars = { "cinema.name",
			"cinema.code" })
	public Integer openCinemaSalable(Cinema cinema) {
		Integer count = 0;
		if (!cinema.getSalable()) {
			cinema.setSalable(true);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(cinema);
			for (ChannelShow channelShow : channelShows) {
				if (channelShow.isOnable()) {
					channelShow.setStatus(ShelveStatus.ON);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 停售影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回下架渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "cinema.close.salable.log", vars = { "cinema.name",
			"cinema.code" })
	public Integer closeCinemaSalable(Cinema cinema) {
		Integer count = 0;
		if (cinema.getSalable()) {
			cinema.setSalable(false);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(cinema);
			for (ChannelShow channelShow : channelShows) {
				if (channelShow.isOffable()) {
					channelShow.setStatus(ShelveStatus.OFF);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 获取启用并提供选座票的影院。
	 * 
	 * @return 返回启用并提供选座票的影院。
	 */
	@Transactional(readOnly = true)
	public List<Cinema> getProvideTicketEnabledCinemas() {
		FullTextCriteria criteria = cinemaDao.createFullTextCriteria();
		criteria.addFilterField("status", EnabledStatus.ENABLED.getValue());
		criteria.addFilterField("ticketSetted", true);
		return cinemaDao.searchBy(criteria);
	}

	/**
	 * 过滤已经选择影院。
	 * 
	 * @param channelPolicy
	 *            渠道结算策略
	 * @param cinemaModel
	 *            影院模型
	 * @return 返回所有提供选座票并启用的影院中过滤掉渠道策略已经选择的影院列表。
	 */
	@Transactional(readOnly = true)
	public List<Cinema> filterSelectedCinema(ChannelPolicy channelPolicy,
			CinemaModel cinemaModel) {
		List<Cinema> cinemas = searchEnabledCinemas(cinemaModel);
		for (ChannelRuleGroup group : channelPolicy.getGroups()) {
			cinemas.remove(group.getCinema());
		}
		return cinemas;
	}

	/**
	 * 根据影院名称搜索影院。
	 * 
	 * @param cinemaModel
	 *            搜索条件
	 * @return 返回启用的影院。
	 */
	@Transactional(readOnly = true)
	public List<Cinema> searchEnabledCinemas(CinemaModel cinemaModel) {
		FullTextCriteria criteria = cinemaDao.createFullTextCriteria();
		criteria.addFilterField("status", EnabledStatus.ENABLED.getValue());
		criteria.addFilterField("ticketSetted", true);
		criteria.addSortAsc("county.city.code", SortField.Type.STRING);
		criteria.setKeyword(cinemaModel.getKeyword());
		return cinemaDao.searchBy(criteria);
	}

	/**
	 * 根据影院名称搜索影院。
	 * 
	 * @param cinemaModel
	 *            搜索条件
	 * @return 返回启用的影院。
	 */
	@Transactional(readOnly = true)
	public List<Cinema> searchEnabledCinemas(CinemaSnackModel cinemaModel) {
		FullTextCriteria criteria = cinemaDao.createFullTextCriteria();
		criteria.addFilterField("status", EnabledStatus.ENABLED.getValue());
		criteria.addFilterField("ticketSetted", true);
		criteria.addSortAsc("county.city.code", SortField.Type.STRING);
		criteria.setKeyword(cinemaModel.getKeyword());
		return cinemaDao.searchBy(criteria);
	}
}