package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BnLog;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.BenefitCardTypeSearchModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 权益卡卡类管理。
 */
@Service
public class BenefitCardTypeService {

	@Resource
	private Dao<BenefitCardType> benefitCardTypeDao;
	@Resource
	private Dao<BenefitCard> benefitCardDao;
	@Resource
	private MessageSource messageSource;
	@Resource
	private ShowService showService;
	@Resource
	private BnLogger bnLogger;

	/**
	 * 新增卡类。
	 * 
	 * @param type
	 *            卡类
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "type", code = "benefitCardType.add.log", vars = { "type.name" }, type = LogType.NEW)
	public void createBenefitCardType(BenefitCardType type) {
		if (!benefitCardTypeDao.isUnique(type, "code")) {
			messageSource.thrown("benefitCardType.add.exist");
		}
		if (!benefitCardTypeDao.isUnique(type, "prefix")) {
			messageSource.thrown("benefitCardType.add.prefix.exist");
		}
		if (!benefitCardTypeDao.isUnique(type, "name")) {
			messageSource.thrown("benefitCardType.add.name.exist");
		}
		benefitCardTypeDao.save(type);
	}

	/**
	 * 查询卡类列表。
	 * 
	 * @param searchModel
	 *            查询条件
	 * @return 卡类列表。
	 */
	@Transactional(readOnly = true)
	public List<BenefitCardType> searchBenefitCardType(
			BenefitCardTypeSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.setKeyword(searchModel.getKeyword());
		if (searchModel.getStatus() != null) {
			criteria.addFilterField("enabled", searchModel.getStatus()
					.getValue());
		}
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		criteria.addFilterField("valid", ValidStatus.UNVALID.getValue(),
				ValidStatus.VALID.getValue());
		criteria.clearSearchFields();
		criteria.addSearchField("name", "code", "prefix");
		List<BenefitCardType> cardTypes = benefitCardTypeDao.searchBy(criteria);
		Iterator<BenefitCardType> iter = cardTypes.iterator();
		while (iter.hasNext()) {
			BenefitCardType cardType = iter.next();
			if (cardType.getStatus() == PolicyStatus.APPROVED
					&& cardType.isBounded()) {
				iter.remove();
			}
		}
		return cardTypes;
	}

	/**
	 * 根据卡类id查询卡类。
	 * 
	 * @param id
	 *            卡类id
	 * @return 卡类。
	 */
	@Transactional(readOnly = true)
	public BenefitCardType getBenefitCardType(String id) {
		return benefitCardTypeDao.get(id);
	}

	/**
	 * 编辑权益卡类。
	 * 
	 * @param benefitCardType
	 *            　权益卡类
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "benefitCardType", code = "benefitCardType.update.log", vars = { "benefitCardType.name" }, type = LogType.ALL)
	public void updateBenefitCardType(BenefitCardType benefitCardType) {
		BenefitCardType origBenefitCardType = getBenefitCardType(benefitCardType
				.getId());
		checkBenefitCardCodeExist(benefitCardType, origBenefitCardType);
		checkBenefitCardPrefixExist(benefitCardType, origBenefitCardType);
		checkBenefitCardNameExist(benefitCardType, origBenefitCardType);
		// 如果是审批通过
		if (origBenefitCardType.getValid() == ValidStatus.VALID
				&& !origBenefitCardType.isBounded()) {
			// 如果因为修改规则，状态为待提交状态，再修改卡类的时候，把卡类状态修改已审核状态，再创建绑定记录。
			if (origBenefitCardType.getStatus() == PolicyStatus.SUBMIT) {
				origBenefitCardType.setStatus(PolicyStatus.APPROVED);
			}
			benefitCardType.setId(null);
			benefitCardType.setBoundType(origBenefitCardType);
			benefitCardType.setValid(ValidStatus.VALID);
			benefitCardType.setEnabled(origBenefitCardType.getEnabled());
			benefitCardType.autoFillIn();
			benefitCardType.setCreateDate(origBenefitCardType.getCreateDate());
			benefitCardTypeDao.save(benefitCardType);
			origBenefitCardType.setBoundType(benefitCardType);
		} else {
			BeanUtils.copyFields(benefitCardType, origBenefitCardType,
					"valid,enabled,creator,createDate");
		}
	}

	/**
	 * 启用权益卡类。
	 * 
	 * @param benefitCardType
	 *            　权益卡类
	 * @return 返回受影响的排期列表。
	 */
	@Transactional
	@SimpleLog(code = "benefitCardType.enable.log", vars = "benefitCardType.name")
	public List<Show> enable(BenefitCardType benefitCardType) {
		checkValidStatus(benefitCardType, "benefitCardType.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (benefitCardType.getEnabled() == EnabledStatus.DISABLED) {
			benefitCardType.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(benefitCardType);
		}
		return shows;
	}

	/**
	 * 停用权益卡类。
	 * 
	 * @param benefitCardType
	 *            　权益卡类
	 * @return 返回受影响的排期列表。
	 */
	@Transactional
	@SimpleLog(code = "benefitCardType.disabled.log", vars = "benefitCardType.name")
	public void disabled(BenefitCardType benefitCardType) {
		checkValidStatus(benefitCardType, "benefitCardType.disable.invalid");
		if (benefitCardType.getEnabled() == EnabledStatus.ENABLED) {
			benefitCardType.setEnabled(EnabledStatus.DISABLED);
		}
	}

	/**
	 * 删除权益卡类。
	 * 
	 * @param benefitCardType
	 *            权益卡类
	 */
	@Transactional
	public void delete(BenefitCardType benefitCardType) {
		// 检查卡类是否可以删除。
		checkBenefitCardTypeDelete(benefitCardType);
		// 检查卡类下是否有卡。
		FullTextCriteria criteria = benefitCardDao.createFullTextCriteria();
		criteria.addFilterField("type.id", benefitCardType.getId());
		if (benefitCardDao.count(criteria) > 0) {
			messageSource.thrown("benefitCardType.delete.hascard");
		}
		// 手动记录日志。
		logDelete(benefitCardType);
		// 解除绑定关系。
		if (benefitCardType.isBounded()) {
			if (benefitCardType.hasBoundRule()) {
				benefitCardType.getBoundType().setStatus(PolicyStatus.SUBMIT);
			}
			benefitCardType.getBoundType().setBoundType(null);
		}
		benefitCardTypeDao.remove(benefitCardType);
	}

	/**
	 * 查询所有有效权益卡类型。
	 * 
	 * @return 权益卡类型列表。
	 */
	@Transactional(readOnly = true)
	public List<BenefitCardType> getValidBenefitCardTypes() {
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		criteria.addSortDesc("createDate", SortField.Type.LONG);
		List<BenefitCardType> types = benefitCardTypeDao.searchBy(criteria);
		Iterator<BenefitCardType> typesIter = types.iterator();
		while (typesIter.hasNext()) {
			BenefitCardType type = typesIter.next();
			if (type.isBounded() && type.getStatus() != PolicyStatus.APPROVED) {
				typesIter.remove();
			}
		}
		return types;
	}

	/**
	 * 记录卡类删除日志。
	 * 
	 * @param benefitCardType
	 *            卡类
	 */
	private void logDelete(BenefitCardType benefitCardType) {
		String message = "";
		if (benefitCardType.isBounded()) {
			message = String.format("删除权益卡类[%s]编辑记录。", benefitCardType
					.getBoundType().getName());
		} else {
			message = String.format("删除权益卡类[%s]。", benefitCardType.getName());
		}
		BnLog bnLog = bnLogger.newBnLog();
		bnLog.setEntityId(benefitCardType.getId());
		bnLog.setMessage(message);
		bnLog.setOrigData(benefitCardType);
		bnLogger.log(bnLog);
	}

	/**
	 * 检查卡类编号是否存在。
	 * 
	 * @param benefitCardType
	 *            权益卡类
	 */
	private void checkBenefitCardCodeExist(BenefitCardType benefitCardType,
			BenefitCardType origBenefitCardType) {
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.addLuceneQuery(
				new TermQuery(new Term("id", benefitCardType.getId())),
				Occur.MUST_NOT);
		if (origBenefitCardType.isBounded()) {
			criteria.addLuceneQuery(new TermQuery(new Term("boundType.id",
					benefitCardType.getId())), Occur.MUST_NOT);
		}
		criteria.addFilterField("code", benefitCardType.getCode());
		if (benefitCardTypeDao.count(criteria) > 0) {
			messageSource.thrown("benefitCardType.edit.exist");
		}
	}

	/**
	 * 检查卡类前缀是否存在。
	 * 
	 * @param benefitCardType
	 *            权益卡类
	 */
	private void checkBenefitCardPrefixExist(BenefitCardType benefitCardType,
			BenefitCardType origBenefitCardType) {
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.addLuceneQuery(
				new TermQuery(new Term("id", benefitCardType.getId())),
				Occur.MUST_NOT);
		if (origBenefitCardType.isBounded()) {
			criteria.addLuceneQuery(new TermQuery(new Term("boundType.id",
					benefitCardType.getId())), Occur.MUST_NOT);
		}
		criteria.addFilterField("prefix", benefitCardType.getPrefix());
		if (benefitCardTypeDao.count(criteria) > 0) {
			messageSource.thrown("benefitCardType.edit.prefix.exist");
		}
	}

	/**
	 * 检查卡类前缀是否存在。
	 * 
	 * @param benefitCardType
	 *            权益卡类
	 */
	private void checkBenefitCardNameExist(BenefitCardType benefitCardType,
			BenefitCardType origBenefitCardType) {
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.addLuceneQuery(
				new TermQuery(new Term("id", benefitCardType.getId())),
				Occur.MUST_NOT);
		if (origBenefitCardType.isBounded()) {
			criteria.addLuceneQuery(new TermQuery(new Term("boundType.id",
					benefitCardType.getId())), Occur.MUST_NOT);
		}
		criteria.addFilterField("name", benefitCardType.getName());
		if (benefitCardTypeDao.count(criteria) > 0) {
			messageSource.thrown("benefitCardType.edit.name.exist");
		}
	}

	/**
	 * 检查权益卡类是否在审核或者审批中。
	 * 
	 * @param benefitCardType
	 *            权益卡类
	 */
	private void checkBenefitCardTypeDelete(BenefitCardType benefitCardType) {
		// 检查卡类状态。
		if (benefitCardType.getStatus() == PolicyStatus.AUDIT
				|| benefitCardType.getStatus() == PolicyStatus.APPROVE
				|| benefitCardType.getStatus() == PolicyStatus.APPROVED) {
			messageSource.thrown("specialPolicy.delete.approve");
		}

		// 检查卡类的是否生效
		if (benefitCardType.getValid() == ValidStatus.VALID
				&& !benefitCardType.isBounded()) {
			messageSource.thrown("benefitCardType.delete.valid");
		}
	}

	/**
	 * 检测权益卡是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param cardType
	 *            权益卡
	 * @param msgKey
	 *            提示语Key
	 * 
	 */
	private void checkValidStatus(BenefitCardType cardType, String msgKey) {
		if (cardType.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}
