package cn.mopon.cec.core.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ThreadContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BnLog;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackType;
import cn.mopon.cec.core.enums.SnackStatus;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 卖品服务。
 */
@Service
public class SnackService {
	@Resource
	private SnackTypeService snackTypeService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private Dao<Snack> snackDao;
	@Resource
	private MessageSource messageSource;
	@Resource
	private SecurityService securityService;
	@Resource
	private BnLogger bnLogger;

	/**
	 * 获取卖品列表。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回对应的卖品。
	 */
	@Transactional(readOnly = true)
	public List<Snack> getSnacks(Cinema cinema) {
		FullTextCriteria criteria = snackDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", cinema.getId());
		criteria.addSortDesc("type.createDate", SortField.Type.LONG);
		return snackDao.searchBy(criteria);
	}

	/**
	 * 新增卖品。
	 * 
	 * @param snack
	 *            卖品
	 */
	@Transactional
	public void createSnack(String cinemaId, String[] snackTypeIds) {
		if (CollectionUtils.isEmpty(snackTypeIds)) {
			messageSource.thrown("snackType.snackTypeIds.none");
		}
		Cinema cinema = cinemaService.getCinema(cinemaId);
		for (String typeId : snackTypeIds) {
			SnackType type = snackTypeService.getSnackType(typeId);
			Snack snack = new Snack();
			snack.setCode(serialNumberService.getSnackCode(cinema.getCode()));
			snack.setCinema(cinema);
			snack.setType(type);
			snack.autoFillIn();
			snackDao.save(snack);
			cinema.getSnacks().add(snack);

			BnLog bnLog = new BnLog();
			bnLog.setMessage(messageSource.get("snack.add.log",
					cinema.getName(), type.getName(), snack.getCode()));
			bnLog.setCreateDate(new Date());
			bnLog.setCreator(getCurrentUsername());
			bnLogger.log(bnLog);
		}
	}

	/**
	 * 获取指定ID的卖品。
	 * 
	 * @param snackId
	 *            卖品ID
	 * @return 返回对应的卖品。
	 */
	@Transactional(readOnly = true)
	public Snack getSnack(String snackId) {
		return snackDao.get(snackId);
	}

	/**
	 * 更新卖品价格。
	 * 
	 * @param snack
	 *            卖品
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "snack", code = "snack.edit.log", vars = {
			"snack.cinema.name", "snack.type.name", "snack.code" }, type = LogType.ALL)
	public void updateSnack(Snack snack) {
		Snack origSnack = getSnack(snack.getId());
		BeanUtils.copyFields(snack, origSnack, "status");
		if (origSnack.getStatus() == SnackStatus.UNVALID) {
			origSnack.setStatus(SnackStatus.OFF);
		}
	}

	/**
	 * 删除卖品。
	 * 
	 * @param snack
	 *            卖品
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "snack.delete.log", vars = { "snack.cinema.name",
			"snack.type.name", "snack.code" })
	public void deleteSnack(Snack snack) {
		Snack origSnack = getSnack(snack.getId());
		if (origSnack.getStatus() != SnackStatus.UNVALID) {
			messageSource.thrown("snack.delete.valid");
		}
		Cinema cinema = origSnack.getCinema();
		snackDao.remove(origSnack);
		cinema.getSnacks().remove(origSnack);
	}

	/**
	 * 上架卖品。
	 * 
	 * @param snack
	 *            卖品
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "snack.enable.log", vars = { "snack.cinema.name",
			"snack.type.name", "snack.code" })
	public void enableSnack(Snack snack) {
		snack.setStatus(SnackStatus.ON);
	}

	/**
	 * 下架卖品。
	 * 
	 * @param snack
	 *            卖品
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "snack.disable.log", vars = { "snack.cinema.name",
			"snack.type.name", "snack.code" })
	public void disableSnack(Snack snack) {
		snack.setStatus(SnackStatus.OFF);
	}

	/**
	 * 获取当前登录用户的用户名，如果没有当前登录用户则返回系统管理员的用户名。
	 * 
	 * @return 返回当前登录用户的用户名。
	 */
	private String getCurrentUsername() {
		if (ThreadContext.getSecurityManager() == null
				|| !SecurityUtils.getSubject().isAuthenticated()) {
			return securityService.getAdminUser().getUsername();
		} else {
			return securityService.getCurrentUser().getUsername();
		}
	}
}