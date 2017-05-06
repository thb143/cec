package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;

/**
 * 会员接入设置。
 */
@Service
public class MemberSettingsService {
	@Resource
	private Dao<MemberSettings> memberSettingsDao;

	/**
	 * 根据ID获取会员接入设置。
	 * 
	 * @param id
	 *            会员接入设置ID
	 * @return 返回指定的会员接入设置。
	 */
	@Transactional(readOnly = true)
	public MemberSettings getMemberSettings(String id) {
		return memberSettingsDao.get(id);
	}

	/**
	 * 更新会员接入设置。
	 * 
	 * @param origCinema
	 *            原影院
	 * @param newMemberSettings
	 *            新会员接入设置
	 */
	@Transactional
	@DetailLog(target = "origCinema", code = "cinema.edit.log", vars = {
			"origCinema.name", "origCinema.code" }, type = LogType.ALL)
	public void updateMemberSettings(Cinema origCinema,
			MemberSettings newMemberSettings) {
		origCinema.setMemberSetted(newMemberSettings.getCinema()
				.getMemberSetted());
		if (!origCinema.getMemberSetted()) {
			return;
		}
		MemberSettings origMemberSettings = getMemberSettings(newMemberSettings
				.getId());
		// 新增会员接入设置。
		if (origMemberSettings == null) {
			memberSettingsDao.save(newMemberSettings);
			// 新增会员接入设置，是通过影院对象记录会员接入设置日志的
			origCinema.setMemberSettings(newMemberSettings);
		} else {
			// 如果接入模式为“单家接入”，保留接入参数。
			if (newMemberSettings.getAccessType().getModel() != AccessModel.CENTER) {
				BeanUtils.copyFields(newMemberSettings,
						origCinema.getMemberSettings(), "id");
			} else {
				BeanUtils.copyFields(newMemberSettings,
						origCinema.getMemberSettings(),
						"id,url,username,password");
			}
		}
	}
}
