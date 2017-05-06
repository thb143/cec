package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.access.film.csp.CSPFilmService;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.FilmSyncLog;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.SyncStatus;
import cn.mopon.cec.core.mail.FilmSyncFailedMailModel;
import cn.mopon.cec.core.mail.FilmSyncMailModel;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.model.FilmSyncModel;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.base.util.ThrowableUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.model.SearchModel;

/**
 * 影片管理。
 */
@Service
public class FilmService {
	@Resource
	private Dao<Film> filmDao;
	@Resource
	private Dao<FilmSyncLog> filmSyncLogDao;
	@Resource
	private CSPFilmService cspFilmService;
	@Resource
	private Circuit circuit;
	@Resource
	private MailService mailService;

	/**
	 * 分页搜索影片。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的影片分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<Film> searchFilm(SearchModel searchModel) {
		FullTextCriteria criteria = filmDao.createFullTextCriteria();
		criteria.addSortDesc("publishDate", SortField.Type.LONG);
		criteria.addSortDesc("code", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return filmDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 根据影片ID获取指定影片。
	 * 
	 * @param filmId
	 *            影片ID
	 * @return 返回指定影片。
	 */
	@Transactional(readOnly = true)
	public Film getFilm(String filmId) {
		return filmDao.get(filmId);
	}

	/**
	 * 根据影片编码获取指定影片。
	 * 
	 * @param filmCode
	 *            影片编码
	 * @return 返回指定的影片。
	 */
	@Transactional(readOnly = true)
	public Film getFilmByCode(String filmCode) {
		// 如果影片编码长度大于14位，截断到12位。（火凤凰的影片编码是国标编码+2位）
		if (filmCode.length() > 12) {
			filmCode = filmCode.substring(0, 12);
		}
		// 把影片编码第4位替换成X进行查找。
		char[] chars = filmCode.toCharArray();
		chars[3] = 'X';
		filmCode = new String(chars);
		return filmDao.searchUnique("code", filmCode);
	}

	/**
	 * 同步影片。
	 * 
	 * @param filmSyncModel
	 *            影片同步条件
	 */
	@Transactional
	public void syncFilms(FilmSyncModel filmSyncModel) {
		filmSyncModel.genQuery("publishDate");
		FilmSyncLog filmSyncLog = new FilmSyncLog();
		try {
			List<Film> films = cspFilmService.getFilms(
					filmSyncModel.getStartDate(), filmSyncModel.getEndDate());
			long startTime = System.currentTimeMillis();
			List<Film> newFilms = filmDispose(films, filmSyncLog);
			long endTime = System.currentTimeMillis();
			filmSyncLog.setProcessCount(films.size());
			filmSyncLog.setDuration(endTime - startTime);
			if (!newFilms.isEmpty()) {
				List<User> users = circuit.getFilmWarnUsers();
				FilmSyncMailModel mailModel = new FilmSyncMailModel(newFilms,
						new Date(), users);
				mailService.send(mailModel);
			}
		} catch (Exception e) {
			filmSyncLog.setStatus(SyncStatus.FAILED);
			filmSyncLog.setMsg(ThrowableUtils.getStackTrace(e));
			List<User> users = circuit.getFilmWarnUsers();
			FilmSyncFailedMailModel mailModel = new FilmSyncFailedMailModel(
					filmSyncLog, users);
			mailService.send(mailModel);
		}
		filmSyncLogDao.save(filmSyncLog);
	}

	/**
	 * 影片处理，保存新增影片，更新已存在影片，记录影片同步日志。
	 * 
	 * @param films
	 *            外部影片列表
	 * @param filmSyncLog
	 *            影片同步日志对象
	 * @return 返回新增影片列表。
	 */
	private List<Film> filmDispose(List<Film> films, FilmSyncLog filmSyncLog) {
		List<Film> newFilms = new ArrayList<>();
		for (Film film : films) {
			// 过滤掉影片编码长度不等于12位的影片。
			if (StringUtils.isBlank(film.getCode())
					|| film.getCode().length() != 12) {
				continue;
			}
			Film origFilm = getFilmByCode(film.getCode());
			if (origFilm == null) {
				filmDao.save(film);
				newFilms.add(film);
				filmSyncLog.addCreateCount();
			} else {
				if (!film.equalsTo(origFilm)) {
					BeanUtils.copyFields(film, origFilm,
							"id,creatorId,createDate");
					filmSyncLog.addUpdateCount();
				}
			}
		}
		return newFilms;
	}

	/**
	 * 获取影片列表（选择影片组件使用）。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合查询条件的影片列表。
	 */
	@Transactional(readOnly = true)
	public List<Film> findFilms(SearchModel searchModel) {
		FullTextCriteria criteria = filmDao.createFullTextCriteria();
		DateTime dateTime = DateTime.now();
		// 公映日期三个月以后的影片。
		String startDate = DateUtils.format(dateTime.minusMonths(3)
				.toLocalDate().toDate(), DateUtils.DAY_N);
		criteria.addRangeField("publishDate", startDate, null);
		criteria.addSortDesc("publishDate", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return filmDao.searchBy(criteria);
	}
}