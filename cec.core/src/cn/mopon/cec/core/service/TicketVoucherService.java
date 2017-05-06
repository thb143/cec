package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.model.TicketOrderSearchModel;
import coo.base.model.Page;
import coo.base.util.DateUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.SimpleLog;

/**
 * 选座票凭证管理。
 */
@Service
public class TicketVoucherService {
	@Resource
	private Dao<TicketVoucher> ticketVoucherDao;
	@Resource
	private VoucherService voucherService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 分页搜索凭证。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的凭证分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<TicketVoucher> searchTicketVoucher(
			TicketOrderSearchModel searchModel) {
		FullTextCriteria criteria = ticketVoucherDao.createFullTextCriteria();
		criteria.addLuceneQuery(searchModel.genQuery("genTime"), Occur.MUST);
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSortDesc("genTime", SortField.Type.LONG);
		return ticketVoucherDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 根据凭证ID获取凭证。
	 * 
	 * @param voucherId
	 *            凭证ID
	 * @return 返回凭证对象。
	 */
	@Transactional(readOnly = true)
	public TicketVoucher getTicketVoucher(String voucherId) {
		return ticketVoucherDao.get(voucherId);
	}

	/**
	 * 重置凭证。
	 * 
	 * @param voucher
	 *            选座票凭证
	 */
	@Transactional
	@SimpleLog(code = "ticketVoucher.reset.log", vars = "voucher.order.code")
	public void resetTicketVoucher(TicketVoucher voucher) {
		if (!voucher.isResetable()) {
			messageSource.thrown("ticketVoucher.status.invalid");
		}
		voucher.setPrintable(true);
		voucher.setReprintCount(voucher.getReprintCount() + 1);
	}

	/**
	 * 更新过期凭证。
	 */
	@Transactional
	public void updateExpiredTicketVoucher() {
		FullTextCriteria criteria = ticketVoucherDao.createFullTextCriteria();
		criteria.addFilterField("printable", true);
		String upperTime = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("expireTime", null, upperTime);

		// 每次更新500条
		for (TicketVoucher voucher : ticketVoucherDao.searchBy(criteria, 500)) {
			voucher.setPrintable(false);
			voucherService.desTicketVoucherCode(voucher.getOrder());
		}
	}
}