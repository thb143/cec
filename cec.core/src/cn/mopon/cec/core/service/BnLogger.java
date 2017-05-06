package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BnLog;
import coo.base.model.Page;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.model.DateRangeSearchModel;
import coo.core.security.service.AbstractBnLogger;

/**
 * 业务日志组件。
 */
@Service
public class BnLogger extends AbstractBnLogger<BnLog> {
	@Resource
	private Dao<BnLog> bnLogDao;

	@Override
	public BnLog newBnLog() {
		return new BnLog();
	}

	/**
	 * 分页全文搜索日志记录。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的日志分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<BnLog> searchLog(DateRangeSearchModel searchModel) {
		FullTextCriteria criteria = bnLogDao.createFullTextCriteria();
		Query logQuery = searchModel.genQuery("createDate");
		criteria.addLuceneQuery(logQuery, Occur.MUST);
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return bnLogDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}
}