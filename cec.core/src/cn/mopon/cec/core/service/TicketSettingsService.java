package cn.mopon.cec.core.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;

/**
 * 选座票接入设置。
 */
@Service
public class TicketSettingsService {
	@Resource
	private Dao<TicketSettings> ticketSettingsDao;

	/**
	 * 根据ID获取选座票接入设置。
	 * 
	 * @param id
	 *            选座票接入设置ID
	 * @return 返回指定的选座票接入设置。
	 */
	@Transactional(readOnly = true)
	public TicketSettings getTicketSettings(String id) {
		return ticketSettingsDao.get(id);
	}

	/**
	 * 更新选座票接入设置。
	 * 
	 * @param origCinema
	 *            原影院
	 * @param newTicketSettings
	 *            新选座票设置
	 */
	@Transactional
	@DetailLog(target = "origCinema", code = "cinema.edit.log", vars = {
			"origCinema.name", "origCinema.code" }, type = LogType.ALL)
	public void updateTicketSettings(Cinema origCinema,
			TicketSettings newTicketSettings) {
		origCinema.setTicketSetted(newTicketSettings.getCinema()
				.getTicketSetted());
		TicketSettings origTicketSettings = getTicketSettings(origCinema
				.getId());
		if (!origCinema.getTicketSetted()) {
			return;
		}
		// 新增选座票接入设置。
		if (origTicketSettings == null) {
			newTicketSettings.setLastSyncShowTime(new Date());
			ticketSettingsDao.save(newTicketSettings);
			// 新增选座票接入设置，是通过影院对象记录会员接入设置日志的
			origCinema.setTicketSettings(newTicketSettings);
		} else {
			ticketSettingsDao.getSession().evict(origTicketSettings);
			// 如果接入模式为“单家接入”,保留接入参数。
			if (newTicketSettings.getAccessType().getModel() != AccessModel.CENTER) {
				BeanUtils.copyFields(newTicketSettings, origTicketSettings,
						"id,lastSyncShowTime");
			} else {
				BeanUtils.copyFields(newTicketSettings, origTicketSettings,
						"id,url,username,password,lastSyncShowTime");
			}
			ticketSettingsDao.update(origTicketSettings);
		}
	}
}
