package cn.mopon.cec.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.HallType;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.SimpleLog;
import coo.core.security.annotations.DetailLog.LogType;

/**
 * 影厅类型。
 */
@Service
public class HallTypeService {
	@Resource
	private Dao<HallType> hallTypeDao;

	/**
	 * 获取影厅类型列表。
	 * 
	 * @return 返回影厅类型列表。
	 */
	@Transactional(readOnly = true)
	public List<HallType> getHallTypeList() {
		return hallTypeDao.getAll();
	}

	/**
	 * 新增影厅类型。
	 * 
	 * @param hallType
	 *            影厅类型
	 */
	@Transactional
	@SimpleLog(code = "hallType.add.log", vars = { "hallType.name" })
	public void createHallType(HallType hallType) {
		hallTypeDao.save(hallType);
	}

	/**
	 * 获取指定ID的影厅类型。
	 * 
	 * @param hallTypeId
	 *            影厅类型ID
	 * @return 返回对应的影厅类型。
	 */
	@Transactional(readOnly = true)
	public HallType getHallType(String hallTypeId) {
		return hallTypeDao.get(hallTypeId);
	}

	/**
	 * 更新影厅类型。
	 * 
	 * @param hallType
	 *            影厅类型
	 */
	@Transactional
	@DetailLog(target = "hallType", code = "hallType.edit.log", vars = {
			"hallType.name"}, type = LogType.ALL)
	public void updateHallType(HallType hallType) {
		HallType origHallType = getHallType(hallType.getId());
		BeanUtils.copyFields(hallType, origHallType);
	}
}