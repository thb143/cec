package cn.mopon.cec.core.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.TicketAccessType;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 选座接入类型。
 */
@Service
public class TicketAccessTypeService {
	@Resource
	private Dao<TicketAccessType> ticketAccessTypeDao;
	@Resource
	private Dao<Cinema> cinemaDao;

	/**
	 * 获取选座票接入类型列表。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @return 返回选座票接入类型列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketAccessType> getTicketAccessTypeList(String cinemaId) {
		List<TicketAccessType> ticketAccessTypes = ticketAccessTypeDao.getAll();
		if (cinemaId != null) {
			Cinema cinema = cinemaDao.get(cinemaId);
			Iterator<TicketAccessType> iterator = ticketAccessTypes.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getProvider() != cinema.getProvider()) {
					iterator.remove();
				}
			}
		}
		return ticketAccessTypes;
	}

	/**
	 * 根据ID获取选座票接入类型。
	 * 
	 * @param id
	 *            编号
	 * @return 返回对应的选座票接入类型。
	 */
	@Transactional(readOnly = true)
	public TicketAccessType getTicketAccessType(String id) {
		return ticketAccessTypeDao.get(id);
	}

	/**
	 * 新增选座票接入类型。
	 * 
	 * @param ticketAccessType
	 *            选座票接入类型
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "ticketAccessType.add.log", vars = { "ticketAccessType.name" })
	public void createTicketAccessType(TicketAccessType ticketAccessType) {
		ticketAccessTypeDao.save(ticketAccessType);
	}

	/**
	 * 更新选座票接入类型。
	 * 
	 * @param ticketAccessType
	 *            选座票接入类型
	 */
	@Transactional
	@DetailLog(target = "ticketAccessType", code = "ticketAccessType.edit.log", vars = { "ticketAccessType.name" }, type = LogType.ALL)
	public void updateTicketAccessType(TicketAccessType ticketAccessType) {
		TicketAccessType origTicketAccessType = getTicketAccessType(ticketAccessType
				.getId());
		if (ticketAccessType.getModel() == AccessModel.SINGLE) {
			BeanUtils.copyFields(ticketAccessType, origTicketAccessType,
					"id,url,username,password");
		} else {
			BeanUtils.copyFields(ticketAccessType, origTicketAccessType, "id");
		}
	}
}
