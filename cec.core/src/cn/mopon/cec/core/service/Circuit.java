package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import cn.mopon.cec.core.entity.CircuitSettings;
import cn.mopon.cec.core.entity.NoticeUser;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.NoticeType;
import cn.mopon.cec.core.enums.ShowType;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;

/**
 * 院线设置管理（作为全局变量使用）。
 */
@Component
public class Circuit {
	private static final String CIRCUIT_ID = "CIRCUIID-0000-0000-0000-000000000000";
	@Resource
	private Dao<CircuitSettings> circuitSettingsDao;
	@Resource
	private Dao<NoticeUser> noticeUserDao;

	public List<SettleRuleType> getSettleRuleTypes() {
		return getCircuitSettings().getSettleRuleTypes();
	}

	public Integer getLockSeatTime() {
		return getCircuitSettings().getLockSeatTime();
	}

	public Integer getDayStatTime() {
		return getCircuitSettings().getDayStatTime();
	}

	/**
	 * 根据放映类型获取默认最低价。
	 * 
	 * @param showType
	 *            放映类型
	 * @return 返回默认最低价。
	 */
	public Double getMinPriceByShowType(ShowType showType) {
		double minPrice = 0;
		switch (showType) {
		case NORMAL2D:
			minPrice = getCircuitSettings().getDefaultMinPrice().getNormal2d();
			break;
		case NORMAL3D:
			minPrice = getCircuitSettings().getDefaultMinPrice().getNormal3d();
			break;
		case MAX2D:
			minPrice = getCircuitSettings().getDefaultMinPrice().getMax2d();
			break;
		case MAX3D:
			minPrice = getCircuitSettings().getDefaultMinPrice().getMax3d();
			break;
		case DMAX:
			minPrice = getCircuitSettings().getDefaultMinPrice().getDmax();
			break;
		default:
			break;
		}
		return minPrice;
	}

	public List<User> getShowWarnUsers() {
		return getCircuitSettings().getShowWarnUsers();
	}

	public List<User> getOrderWarnUsers() {
		return getCircuitSettings().getOrderWarnUsers();
	}

	public List<User> getStatNoticeUsers() {
		return getCircuitSettings().getStatNoticeUsers();
	}

	public List<User> getFilmWarnUsers() {
		return getCircuitSettings().getFilmWarnUsers();
	}

	public List<User> getPriceWarnUsers() {
		return getCircuitSettings().getPriceWarnUsers();
	}

	/**
	 * 保存院线设置信息。
	 * 
	 * @param circuitSettings
	 *            院线设置
	 */
	@Transactional
	@DetailLog(target = "circuitSettings", code = "circuitSettings.edit.log", type = LogType.ALL)
	public void save(CircuitSettings circuitSettings) {
		clearNoticeUsers();

		for (User user : circuitSettings.getShowWarnUsers()) {
			saveNoticeUser(circuitSettings, user, NoticeType.SHOW);
		}
		for (User user : circuitSettings.getPriceWarnUsers()) {
			saveNoticeUser(circuitSettings, user, NoticeType.PRICE);
		}
		for (User user : circuitSettings.getFilmWarnUsers()) {
			saveNoticeUser(circuitSettings, user, NoticeType.FILM);
		}
		for (User user : circuitSettings.getOrderWarnUsers()) {
			saveNoticeUser(circuitSettings, user, NoticeType.ORDER);
		}
		for (User user : circuitSettings.getStatNoticeUsers()) {
			saveNoticeUser(circuitSettings, user, NoticeType.STAT);
		}

		circuitSettingsDao.merge(circuitSettings);
	}

	/**
	 * 清空原通知用户列表。
	 */
	private void clearNoticeUsers() {
		List<NoticeUser> noticeUsers = getCircuitSettings().getNoticeUsers();
		noticeUserDao.remove(noticeUsers);
		getCircuitSettings().getNoticeUsers().removeAll(noticeUsers);
	}

	/**
	 * 保存通知用户。
	 * 
	 * @param circuitSettings
	 *            院线设置对象
	 * @param user
	 *            用户
	 * @param type
	 *            通知类型
	 */
	private void saveNoticeUser(CircuitSettings circuitSettings, User user,
			NoticeType type) {
		NoticeUser noticeUser = new NoticeUser();
		noticeUser.setCircuitSettings(circuitSettings);
		noticeUser.setUser(user);
		noticeUser.setType(type);
		noticeUserDao.save(noticeUser);
		circuitSettings.getNoticeUsers().add(noticeUser);
	}

	/**
	 * 获取院线设置。
	 * 
	 * @return 返回院线设置。
	 */
	@Transactional(readOnly = true)
	public CircuitSettings getCircuitSettings() {
		CircuitSettings circuitSettings = circuitSettingsDao.get(CIRCUIT_ID);
		setNoticeUsers(circuitSettings);
		return circuitSettings;
	}

	/**
	 * 设置通知用户。
	 * 
	 * @param circuitSettings
	 *            院线设置对象
	 */
	private void setNoticeUsers(CircuitSettings circuitSettings) {
		List<User> showWarnUsers = new ArrayList<>();
		List<User> priceWarnUsers = new ArrayList<>();
		List<User> orderWarnUsers = new ArrayList<>();
		List<User> filmWarnUsers = new ArrayList<>();
		List<User> statNoticeUsers = new ArrayList<>();
		for (NoticeUser noticeUser : circuitSettings.getNoticeUsers()) {
			if (noticeUser.getType() == NoticeType.SHOW) {
				showWarnUsers.add(noticeUser.getUser());
			}
			if (noticeUser.getType() == NoticeType.PRICE) {
				priceWarnUsers.add(noticeUser.getUser());
			}
			if (noticeUser.getType() == NoticeType.ORDER) {
				orderWarnUsers.add(noticeUser.getUser());
			}
			if (noticeUser.getType() == NoticeType.FILM) {
				filmWarnUsers.add(noticeUser.getUser());
			}
			if (noticeUser.getType() == NoticeType.STAT) {
				statNoticeUsers.add(noticeUser.getUser());
			}
		}
		circuitSettings.setShowWarnUsers(showWarnUsers);
		circuitSettings.setPriceWarnUsers(priceWarnUsers);
		circuitSettings.setOrderWarnUsers(orderWarnUsers);
		circuitSettings.setFilmWarnUsers(filmWarnUsers);
		circuitSettings.setStatNoticeUsers(statNoticeUsers);
	}

	/**
	 * 获取开放的除区间定价外的结算规则列表。
	 * 
	 * @return 返回开放的除区间定价外的结算规则类型列表。
	 */
	public List<SettleRuleType> getSettleRuleTypesExcludeRound() {
		List<SettleRuleType> settleRuleTypes = getSettleRuleTypes();
		List<SettleRuleType> settleTypes = new ArrayList<>();
		settleTypes.addAll(settleRuleTypes);
		Iterator<SettleRuleType> iterator = settleTypes.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getValue() == SettleRuleType.ROUND.getValue()) {
				iterator.remove();
				break;
			}
		}
		return settleTypes;
	}
}
