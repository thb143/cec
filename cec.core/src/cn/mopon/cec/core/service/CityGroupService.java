package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CityGroup;
import cn.mopon.cec.core.model.CitySelectModel;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.SimpleLog;

/**
 * 城市分组服务。
 */
@Service
public class CityGroupService {
	@Resource
	private Dao<CityGroup> cityGroupDao;
	@Resource
	private Dao<Cinema> cinemaDao;
	@Resource
	private MessageSource messageSource;

	/**
	 * 创建城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 */
	@AutoFillIn
	@Transactional
	@SimpleLog(code = "cityGroup.add.log", vars = { "cityGroup.name" })
	public void createCityGroup(CityGroup cityGroup) {
		if (!cityGroupDao.isUnique(cityGroup, "name")) {
			messageSource.thrown("cityGroup.add.exist");
		}
		cityGroupDao.save(cityGroup);
	}

	/**
	 * 更新城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 */
	@AutoFillIn
	@Transactional
	@SimpleLog(code = "cityGroup.edit.log", vars = { "cityGroup.name" })
	public void updateCityGroup(CityGroup cityGroup) {
		if (!cityGroupDao.isUnique(cityGroup, "name")) {
			messageSource.thrown("cityGroup.edit.name.exist");
		}
		CityGroup origCityGroup = getCityGroup(cityGroup.getId());
		BeanUtils.copyFields(cityGroup, origCityGroup, "cities");
		origCityGroup.setCities(cityGroup.getCities());
	}

	/**
	 * 查询所有城市分组。
	 * 
	 * @return 返回城市分组列表。
	 */
	@Transactional(readOnly = true)
	public List<CityGroup> getCityGroups() {
		return cityGroupDao.searchAll("createDate", false, SortField.Type.LONG);
	}

	/**
	 * 根据ID查询城市分组。
	 * 
	 * @param cityGroupId
	 *            分组ID
	 * @return 返回城市分组。
	 */
	@Transactional(readOnly = true)
	public CityGroup getCityGroup(String cityGroupId) {
		return cityGroupDao.get(cityGroupId);
	}

	/**
	 * 删除城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 */
	@Transactional
	@SimpleLog(code = "cityGroup.delete.log", vars = { "cityGroup.name" })
	public void deleteCityGroup(CityGroup cityGroup) {
		cityGroupDao.remove(cityGroup);
	}

	/**
	 * 缓存城市选择模型。
	 * 
	 * @param cityGroup
	 *            城市分组
	 * @return 返回城市选择模型。
	 */
	public CitySelectModel getSelectModel(CityGroup cityGroup) {
		List<Cinema> cinemas = cinemaDao.getAll();
		List<String> countyCodes = new ArrayList<>();
		for (Cinema cinema : cinemas) {
			countyCodes.add(cinema.getCounty().getCode());
		}
		CitySelectModel citySelectModel = new CitySelectModel(countyCodes);
		if (CollectionUtils.isNotEmpty(cityGroup.getCities())) {
			citySelectModel.genSelectedModel(cityGroup.getCities());
		}
		return citySelectModel;
	}
}
