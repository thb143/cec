package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.FilmSyncLog;
import cn.mopon.cec.core.model.FilmSyncLogSearchModel;
import coo.base.model.Page;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 影片同步日志。
 */
@Service
public class FilmSyncLogService {
	@Resource
	private Dao<FilmSyncLog> filmSyncLogDao;

	/**
	 * 分页搜索影片同步日志。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的影片同步日志分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<FilmSyncLog> searchFilmSyncLog(
			FilmSyncLogSearchModel searchModel) {
		FullTextCriteria criteria = filmSyncLogDao.createFullTextCriteria();
		criteria.addSortDesc("syncTime", SortField.Type.LONG);

		if (searchModel.getStatus() != null) {
			criteria.addFilterField("status", searchModel.getStatus()
					.getValue());
		}

		Query syncDateQuery = searchModel.genQuery("syncTime");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);

		criteria.setKeyword(searchModel.getKeyword());
		return filmSyncLogDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 获取指定ID的影片同步日志。
	 * 
	 * @param filmSyncLogId
	 *            影片同步日志ID
	 * @return 返回影片同步日志。
	 */
	@Transactional(readOnly = true)
	public FilmSyncLog getFilmSyncLog(String filmSyncLogId) {
		return filmSyncLogDao.get(filmSyncLogId);
	}
}