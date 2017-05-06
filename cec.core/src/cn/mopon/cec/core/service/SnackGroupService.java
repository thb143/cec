package cn.mopon.cec.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.SnackGroup;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 卖品分类服务。
 */
@Service
public class SnackGroupService {
	@Resource
	private Dao<SnackGroup> snackGroupDao;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增卖品分类。
	 * 
	 * @param snackGroup
	 *            卖品分类
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "snackGroup.add.log", vars = { "snackGroup.name" })
	public void createSnackGroup(SnackGroup snackGroup) {
		if (!snackGroupDao.isUnique(snackGroup, "name")) {
			messageSource.thrown("snackGroup.add.exist");
		}
		snackGroupDao.save(snackGroup);
	}

	/**
	 * 获取卖品分类列表。
	 * 
	 * @return 返回所有的卖品分类列表。
	 */
	@Transactional(readOnly = true)
	public List<SnackGroup> getSnackGroups() {
		return snackGroupDao.getAll("createDate", true);
	}

	/**
	 * 获取指定ID的卖品分类。
	 * 
	 * @param snackGroupId
	 *            卖品分类ID
	 * @return 返回对应的卖品分类。
	 */
	@Transactional(readOnly = true)
	public SnackGroup getSnackGroup(String snackGroupId) {
		return snackGroupDao.get(snackGroupId);
	}

	/**
	 * 更新卖品分类。
	 * 
	 * @param snackGroup
	 *            卖品分类
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "snackGroup", code = "snackGroup.edit.log", vars = { "snackGroup.name" }, type = LogType.ALL)
	public void updateSnackGroup(SnackGroup snackGroup) {
		SnackGroup origSnackGroup = snackGroupDao.get(snackGroup.getId());
		BeanUtils.copyFields(snackGroup, origSnackGroup);
	}
}