package cn.mopon.cec.core.service;

import java.util.List;

import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Actor;
import cn.mopon.cec.core.entity.Organ;
import cn.mopon.cec.core.entity.Role;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.entity.UserSettings;
import coo.core.security.service.AbstractSecurityService;

/**
 * 安全服务。
 */
@Service
public class SecurityService extends
		AbstractSecurityService<Organ, User, Role, Actor, UserSettings> {
	/**
	 * 获取系统中所有可用的用户列表。
	 * 
	 * @return 返回系统中所有可用的用户列表。
	 */
	@Transactional(readOnly = true)
	public List<User> getAllEnabledUser() {
		return userDao.searchAll("enabled", true, SortField.Type.INT);
	}
}