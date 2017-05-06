package cn.mopon.cec.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.MinPriceGroup;
import cn.mopon.cec.core.model.FilmMinPriceItem;
import coo.base.util.BeanUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 最低价分组服务。
 */
@Service
public class MinPriceGroupService {
	@Resource
	private Dao<MinPriceGroup> minPriceGroupDao;
	@Resource
	private FilmService filmService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增最低价分组。
	 * 
	 * @param minPriceGroup
	 *            最低价分组
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "minPriceGroup", code = "minPriceGroup.add.log", vars = {
			"minPriceGroup.film.name", "minPriceGroup.name" }, type = LogType.NEW)
	public void createMinPriceGroup(MinPriceGroup minPriceGroup) {
		checkDate(minPriceGroup);
		minPriceGroupDao.save(minPriceGroup);
		Film film = filmService.getFilm(minPriceGroup.getFilm().getId());
		film.getGroups().add(minPriceGroup);
	}

	/**
	 * 更新最低价分组。
	 * 
	 * @param minPriceGroup
	 *            最低价分组
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "minPriceGroup", code = "minPriceGroup.edit.log", vars = {
			"minPriceGroup.film.name", "minPriceGroup.name" }, type = LogType.ALL)
	public void updateMinPriceGroup(MinPriceGroup minPriceGroup) {
		checkDate(minPriceGroup);
		MinPriceGroup origMinPriceGroup = minPriceGroupDao.get(minPriceGroup
				.getId());
		if (StringUtils.isEmpty(minPriceGroup.getCityCode())) {
			origMinPriceGroup.setCityCode(null);
		}
		if (minPriceGroup.getMinPrices() == null) {
			origMinPriceGroup.setMinPrices(null);
		}
		BeanUtils.copyFields(minPriceGroup, origMinPriceGroup);
	}

	/**
	 * 删除最低价分组。
	 * 
	 * @param minPriceGroup
	 *            最低价分组
	 */
	@Transactional
	@SimpleLog(code = "minPriceGroup.delete.log", vars = {
			"minPriceGroup.film.name", "minPriceGroup.name" })
	public void deleteMinPriceGroup(MinPriceGroup minPriceGroup) {
		minPriceGroupDao.remove(minPriceGroup);
		minPriceGroup.getFilm().getGroups().remove(minPriceGroup);
	}

	/**
	 * 检查时间段是否重叠。
	 * 
	 * @param minPriceGroup
	 *            最低价分组
	 */
	private void checkDate(MinPriceGroup minPriceGroup) {
		List<FilmMinPriceItem> items = minPriceGroup.getMinPrices().getItems();
		for (int i = 0; i < items.size(); i++) {
			FilmMinPriceItem item = items.get(i);
			for (int j = i + 1; j < items.size(); j++) {
				if (items.get(j).getStartDate().toDate()
						.before(item.getStartDate().toDate())
						&& !items.get(j).getEndDate().toDate()
								.before(item.getStartDate().toDate())) {
					messageSource.thrown("minPriceRule.date.overlap");
				}

				if (!items.get(j).getStartDate().toDate()
						.before(item.getStartDate().toDate())
						&& items.get(j).getStartDate().toDate()
								.before(item.getEndDate().toDate())) {
					messageSource.thrown("minPriceRule.date.overlap");
				}

				if (items.get(j).getStartDate().toDate()
						.equals(item.getEndDate().toDate())) {
					messageSource.thrown("minPriceRule.date.overlap");
				}
			}
		}
	}
}
