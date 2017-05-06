package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ShowSyncLog;
import cn.mopon.cec.core.model.ShowSyncLogSearchModel;
import coo.base.model.Page;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 排期同步日志管理。
 */
@Service
public class ShowSyncLogService {
	@Resource
	private Dao<ShowSyncLog> showSyncLogDao;

	/**
	 * 分页搜索排期同步日志。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的排期同步日志分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<ShowSyncLog> searchShowSyncLog(
			ShowSyncLogSearchModel searchModel) {
		FullTextCriteria criteria = showSyncLogDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", searchModel.getCinemaId());
		criteria.addSortDesc("syncTime", SortField.Type.LONG);

		if (searchModel.getStatus() != null) {
			criteria.addFilterField("status", searchModel.getStatus()
					.getValue());
		}

		Query syncDateQuery = searchModel.genQuery("syncTime");
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);

		criteria.setKeyword(searchModel.getKeyword());
		return showSyncLogDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 获取指定ID的排期同步日志。
	 * 
	 * @param showSyncLogId
	 *            排期同步日志ID
	 * @return 返回排期同步日志。
	 */
	@Transactional(readOnly = true)
	public ShowSyncLog getShowSyncLog(String showSyncLogId) {
		return showSyncLogDao.get(showSyncLogId);
	}
}