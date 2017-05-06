package cn.mopon.cec.core.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.MemberAccessType;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 会员接入类型。
 */
@Service
public class MemberAccessTypeService {
	@Resource
	private Dao<MemberAccessType> memberAccessTypeDao;
	@Resource
	private CinemaService cinemaService;

	/**
	 * 获取会员接入类型列表。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @return 返回会员接入类型列表。
	 */
	@Transactional(readOnly = true)
	public List<MemberAccessType> getMemberAccessTypeList(String cinemaId) {
		List<MemberAccessType> memberAccessTypes = memberAccessTypeDao.getAll();
		if (cinemaId != null) {
			Iterator<MemberAccessType> iterator = memberAccessTypes.iterator();
			Cinema cinema = cinemaService.getCinema(cinemaId);
			while (iterator.hasNext()) {
				if (iterator.next().getProvider() != cinema.getProvider()) {
					iterator.remove();
				}
			}
		}
		return memberAccessTypes;
	}

	/**
	 * 根据ID获取会员接入类型。
	 * 
	 * @param id
	 *            编号
	 * @return 返回对应的会员接入类型。
	 */
	@Transactional(readOnly = true)
	public MemberAccessType getMemberAccessType(String id) {
		return memberAccessTypeDao.get(id);
	}

	/**
	 * 新增会员接入类型。
	 * 
	 * @param memberAccessType
	 *            会员接入类型
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "memberAccessType.add.log", vars = { "memberAccessType.name" })
	public void createMemberAccessType(MemberAccessType memberAccessType) {
		memberAccessTypeDao.save(memberAccessType);
	}

	/**
	 * 更新会员接入类型。
	 * 
	 * @param memberAccessType
	 *            会员接入类型
	 */
	@Transactional
	@DetailLog(target = "memberAccessType", code = "memberAccessType.edit.log", vars = { "memberAccessType.name" }, type = LogType.ALL)
	public void updateMemberAccessType(MemberAccessType memberAccessType) {
		MemberAccessType origMemberAccessType = getMemberAccessType(memberAccessType
				.getId());
		if (memberAccessType.getModel() == AccessModel.SINGLE) {
			BeanUtils.copyFields(memberAccessType, origMemberAccessType,
					"id,url,username,password");
		} else {
			BeanUtils.copyFields(memberAccessType, origMemberAccessType, "id");
		}
	}
}
