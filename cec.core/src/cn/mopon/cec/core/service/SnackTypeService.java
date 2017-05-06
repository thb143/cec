package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.SnackGroup;
import cn.mopon.cec.core.entity.SnackType;
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
 * 卖品类型服务。
 */
@Service
public class SnackTypeService {
	@Resource
	private Dao<SnackType> snackTypeDao;
	@Resource
	private SnackGroupService snackGroupService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增卖品类型。
	 * 
	 * @param snackType
	 *            卖品类型
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "snackType.add.log", vars = { "snackType.group.name",
			"snackType.name", "snackType.remark" })
	public void createSnackType(SnackType snackType) {
		if (snackType.getImage() == null) {
			messageSource.thrown("snackType.image.none");
		}
		snackTypeDao.save(snackType);
		SnackGroup group = snackType.getGroup();
		group.getSnackTypes().add(snackType);
	}

	/**
	 * 获取指定ID的卖品类型。
	 * 
	 * @param snackTypeId
	 *            卖品类型ID
	 * @return 返回对应的卖品类型。
	 */
	@Transactional(readOnly = true)
	public SnackType getSnackType(String snackTypeId) {
		return snackTypeDao.get(snackTypeId);
	}

	/**
	 * 获取指定分类的卖品类型。
	 * 
	 * @param groupId
	 *            卖品分类ID
	 * @return 返回指定分类的卖品类型列表。
	 */
	@Transactional(readOnly = true)
	public List<SnackType> getSnackTypes(String groupId) {
		SnackGroup snackGroup = snackGroupService.getSnackGroup(groupId);
		if (snackGroup != null) {
			return snackGroup.getSnackTypes();
		}
		return new ArrayList<>();
	}

	/**
	 * 获取卖品类型。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的卖品类型列表。
	 */
	@Transactional(readOnly = true)
	public List<SnackType> searchSnackTypes(SearchModel searchModel) {
		FullTextCriteria criteria = snackTypeDao.createFullTextCriteria();
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return snackTypeDao.searchBy(criteria);
	}

	/**
	 * 更新卖品类型。
	 * 
	 * @param snackType
	 *            卖品类型
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "snackType", code = "snackType.edit.log", vars = {
			"snackType.group.name", "snackType.name", "snackType.remark" }, type = LogType.ALL)
	public void updateSnackType(SnackType snackType) {
		SnackType origSnackType = getSnackType(snackType.getId());
		BeanUtils.copyFields(snackType, origSnackType);
	}
}