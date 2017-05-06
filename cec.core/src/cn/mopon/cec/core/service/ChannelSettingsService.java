package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelSettings;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;

/**
 * 渠道设置。
 */
@Service
public class ChannelSettingsService {
	@Resource
	private Dao<ChannelSettings> channelSettingsDao;

	/**
	 * 查询渠道设置。
	 * 
	 * @param settingId
	 *            渠道设置ID
	 * @return 返回渠道设置对象。
	 */
	@Transactional(readOnly = true)
	public ChannelSettings getChannelSettings(String settingId) {
		return channelSettingsDao.get(settingId);
	}

	/**
	 * 更新渠道设置。
	 * 
	 * @param settings
	 *            渠道设置对象
	 */
	@Transactional
	@DetailLog(target = "settings", code = "channelSettings.edit.log", vars = { "settings.channel.name" }, type = LogType.ALL)
	public void updateChannelSettings(ChannelSettings settings) {
		ChannelSettings origSettings = getChannelSettings(settings.getId());
		BeanUtils.copyFields(settings, origSettings, "ticketApiMethods");
		origSettings.setTicketApiMethods(settings.getTicketApiMethods());
	}
}