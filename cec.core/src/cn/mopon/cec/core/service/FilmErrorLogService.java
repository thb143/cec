package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.FilmErrorLog;
import coo.core.hibernate.dao.Dao;

/**
 * 影片错误日志。
 */
@Service
public class FilmErrorLogService {
	@Resource
	private Dao<FilmErrorLog> filmErrorLogDao;

	/**
	 * 获取指定ID的影片错误日志。
	 * 
	 * @param filmErrorLogId
	 *            日志ID
	 * @return 返回日志。
	 */
	@Transactional(readOnly = true)
	public FilmErrorLog getFilmErrorLog(String filmErrorLogId) {
		return filmErrorLogDao.get(filmErrorLogId);
	}
}