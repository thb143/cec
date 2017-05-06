package cn.mopon.cec.core.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.hibernate.search.annotations.Analyze;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.entity.BenefitCardConsumeOrder;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.PrintStatus;
import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.mail.AbnormalOrderProcessFailedListMailModel;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.model.TicketOrderSearchModel;
import coo.base.exception.BusinessException;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.SimpleLog;

/**
 * 选座票订单服务。
 */
@Service
public class TicketOrderService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	/** 满天星日志对象 */
	protected Logger mtxLog = LoggerFactory.getLogger("mtx");
	@Resource
	private Dao<TicketOrder> ticketOrderDao;
	@Resource
	private Dao<TicketVoucher> ticketVoucherDao;
	@Resource
	private VoucherService voucherService;
	@Resource
	private BenefitCardOrderService benefitCardOrderService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private Circuit circuit;
	@Resource
	private MailService mailService;
	@Resource
	private HomeService homeService;

	/**
	 * 分页搜索选座票订单。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * 
	 * @return 返回符合条件的选座票订单分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<TicketOrder> searchTicketOrder(
			TicketOrderSearchModel searchModel) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.setKeyword(searchModel.getKeyword());
		criteria.addSearchField("cinema.name", Analyze.NO);
		if (StringUtils.isNotEmpty(searchModel.getChannelCode())) {
			criteria.addFilterField("channel.code",
					searchModel.getChannelCode());
		}
		if (searchModel.getOrderStatus() != null) {
			criteria.addFilterField("status", searchModel.getOrderStatus()
					.getValue());
		}
		if (searchModel.getHasSnack() != null && searchModel.getHasSnack()) {
			criteria.addRangeField("snackCount", "1", null);
		}
		if (searchModel.getHasSnack() != null && !searchModel.getHasSnack()) {
			criteria.addFilterField("snackCount", "0");
		}
		if (searchModel.getTicketOrderType() != null) {
			criteria.addFilterField("type", searchModel.getTicketOrderType()
					.getValue());
		}
		Query syncDateQuery = searchModel.genQuery(searchModel.getOrderBy());
		criteria.addLuceneQuery(syncDateQuery, Occur.MUST);
		criteria.addSortDesc(searchModel.getOrderBy(), SortField.Type.LONG);
		return ticketOrderDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 全文搜索影院排期正常订单。
	 * 
	 * @param show
	 *            影院排期
	 * @return 返回影院排期正常订单列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketOrder> searchNormalOrder(Show show) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addSortDesc("createTime", SortField.Type.LONG);
		criteria.addFilterField("cinema.id", show.getCinema().getId());
		criteria.addFilterField("showCode", show.getCode());
		criteria.addFilterField("status", TicketOrderStatus.SUCCESS.getValue());
		return ticketOrderDao.searchBy(criteria);
	}

	/**
	 * 全文搜索影院排期退票订单。
	 * 
	 * @param show
	 *            影院排期
	 * @return 返回影院排期退票订单列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketOrder> searchRevokeOrder(Show show) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addSortDesc("createTime", SortField.Type.LONG);
		criteria.addFilterField("cinema.id", show.getCinema().getId());
		criteria.addFilterField("showCode", show.getCode());
		criteria.addFilterField("status", TicketOrderStatus.REVOKED.getValue());
		return ticketOrderDao.searchBy(criteria);
	}

	/**
	 * 全文搜索渠道排期正常订单。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回渠道排期正常订单列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketOrder> searchNormalOrder(ChannelShow channelShow) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addSortDesc("createTime", SortField.Type.LONG);
		criteria.addFilterField("channelShowCode", channelShow.getCode());
		criteria.addFilterField("status", TicketOrderStatus.SUCCESS.getValue());
		return ticketOrderDao.searchBy(criteria);
	}

	/**
	 * 全文搜索渠道排期退票订单。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回渠道排期退票订单列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketOrder> searchRevokeOrder(ChannelShow channelShow) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addSortDesc("createTime", SortField.Type.LONG);
		criteria.addFilterField("channelShowCode", channelShow.getCode());
		criteria.addFilterField("status", TicketOrderStatus.REVOKED.getValue());
		return ticketOrderDao.searchBy(criteria);
	}

	/**
	 * 获取选座票订单。
	 * 
	 * @param orderId
	 *            订单ID
	 * @return 返回指定的选座票订单。
	 */
	@Transactional(readOnly = true)
	public TicketOrder getTicketOrder(String orderId) {
		return ticketOrderDao.get(orderId);
	}

	/**
	 * 标记出票失败。
	 * 
	 * @param order
	 *            选座票订单
	 */
	@Transactional
	@SimpleLog(code = "ticketOrder.mark.failed.log", vars = "order.code")
	public void markTicketOrderFailed(TicketOrder order) {
		if (!order.isMarkFailable()) {
			messageSource.thrown("ticketOrder.status.invalid");
		}
		order.setConfirmTime(new Date());
		order.setStatus(TicketOrderStatus.FAILED);
		decrDiscountCount(order);
	}

	/**
	 * 退票。
	 * 
	 * @param order
	 *            选座票订单
	 */
	@Transactional
	@SimpleLog(code = "ticketOrder.revoke.log", vars = "order.code")
	public void revokeTicketOrder(TicketOrder order) {
		// 如果是已支付状态的订单未到过期时间不允许退票
		if (order.getStatus() == TicketOrderStatus.PAID && !order.isExpired()) {
			messageSource.thrown("ticketOrder.status.invalid");
		}
		// 如果是成功订单凭证是已打票状态不允许退票。
		if (order.getStatus() == TicketOrderStatus.SUCCESS
				&& order.getVoucher().getStatus() != PrintStatus.NO) {
			messageSource.thrown("ticketOrder.status.invalid");
		}
		Boolean refunded = getAccessService(order).refundTicket(order);
		if (refunded) {
			if (order.getStatus() == TicketOrderStatus.PAID) {
				order.setConfirmTime(new Date());
			}
			order.setRevokeTime(new Date());
			order.setStatus(TicketOrderStatus.REVOKED);
			if (order.getSnackOrder() != null) {
				processSnackOrder(order);
			}
			decrDiscountCount(order);
		} else {
			messageSource.thrown("ticketOrder.revoke.failed");
		}
	}

	/**
	 * 标记退票。
	 * 
	 * @param order
	 *            选座票订单
	 */
	@Transactional
	@SimpleLog(code = "ticketOrder.mark.revoke.log", vars = "order.code")
	public void markTicketOrderRevoked(TicketOrder order) {
		if (!order.isRevokeable()) {
			messageSource.thrown("ticketOrder.status.invalid");
		}
		order.setRevokeTime(new Date());
		order.setStatus(TicketOrderStatus.REVOKED);
		if (order.getSnackOrder() != null) {
			processSnackOrder(order);
		}
		decrDiscountCount(order);
	}

	/**
	 * 查询地面选座票订单信息。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 选座票订单。
	 */
	@Transactional
	public TicketOrder getCinemaTicketOrder(TicketOrder order) {
		TicketOrder externalOrder = getAccessService(order).queryOrder(order);
		if (externalOrder.getStatus() == TicketOrderStatus.SUCCESS) {
			// 火凤凰的地面订单在成功以后会把影院订单号替换成地面的订单号，这里覆盖掉原本的临时订单号。
			if (StringUtils.isNotBlank(externalOrder.getCinemaOrderCode())) {
				order.setCinemaOrderCode(externalOrder.getCinemaOrderCode());
			}
		}
		return externalOrder;
	}

	/**
	 * 更新过期选座票订单。
	 */
	@Transactional
	public void updateExpiredTicketOrder() {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addFilterField("status", TicketOrderStatus.UNPAID.getValue());
		String upperTime = DateUtils
				.format(new Date(), DateUtils.MILLISECOND_N);
		criteria.addRangeField("expireTime", null, upperTime);
		for (TicketOrder ticketOrder : ticketOrderDao.searchBy(criteria)) {
			ticketOrder.setStatus(TicketOrderStatus.CANCELED);
		}
	}

	/**
	 * 清理取消的选座票订单。
	 */
	@Transactional
	public void cleanCanceledTicketOrder() {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addFilterField("status", TicketOrderStatus.CANCELED.getValue());
		String upperTime = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("expireTime", null, upperTime);
		// 每次清理500条
		ticketOrderDao.remove(ticketOrderDao.searchBy(criteria, 500));
	}

	/**
	 * 全文搜索异常订单。
	 * 
	 * @return 返回异常订单列表。
	 */
	@Transactional(readOnly = true)
	public List<TicketOrder> searchAbnormalTicketOrder() {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addFilterField("status", TicketOrderStatus.PAID.getValue());
		// 支付时间在1-60分钟以内的异常订单，超过60分钟的异常订单人工处理
		String lowerTime = DateUtils.format(DateTime.now().minusMinutes(60)
				.toDate(), DateUtils.MILLISECOND_N);
		String upperTime = DateUtils.format(DateTime.now().minusMinutes(1)
				.toDate(), DateUtils.MILLISECOND_N);
		criteria.addRangeField("payTime", lowerTime, upperTime);
		return ticketOrderDao.searchBy(criteria);
	}

	/**
	 * 全文搜索超过过期时间的异常订单。
	 * 
	 * @return 返回超过超时时间的异常订单列表。
	 */
	private List<TicketOrder> searchExpireTimeAbnormalTicketOrder() {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addFilterField("status", TicketOrderStatus.PAID.getValue());
		// 超过过期时间的异常订单
		String upperTime = DateUtils.format(DateTime.now().toDate(),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("expireTime", null, upperTime);
		criteria.addSortAsc("cinema.name", SortField.Type.STRING);
		return ticketOrderDao.searchBy(criteria);
	}

	/**
	 * 处理异常订单。
	 * 
	 * @param orderId
	 *            订单ID
	 */
	@Transactional
	public void proccessAbnormalOrder(String orderId) {
		TicketOrder order = getTicketOrder(orderId);
		TicketOrder externalOrder = getAbnormalExternalOrder(order);
		// 如果获取外部订单为空直接返回。
		if (externalOrder == null) {
			return;
		}
		switch (externalOrder.getStatus()) {
		case FAILED:
			processFailedAbnormalOrder(order);
			break;
		case SUCCESS:
			processSuccessAbnormalOrder(order, externalOrder);
			break;
		case PAID:
			processPaidAbnormalOrder(order);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取异常订单的外部订单。
	 * 
	 * @param order
	 *            选座票订单。
	 * @return 如果获取订单异常返回null，否则返回外部订单。
	 */
	private TicketOrder getAbnormalExternalOrder(TicketOrder order) {
		try {
			return getCinemaTicketOrder(order);
		} catch (Exception e) {
			// 如果订单已超时，并且地面返回异常信息为“未找到订单”，设置订单状态为出票失败。
			Boolean isNotFound = "没有找到该订单".equals(e.getMessage())
					|| "订单号不存在".equals(e.getMessage())
					|| "订单不存在".equals(e.getMessage())
					|| "订单请求处理超时，座位信息已释放".equals(e.getMessage())
					|| "取票码序列号缺少或无效".equals(e.getMessage());
			if (order.getExpireTime().before(new Date()) && isNotFound) {
				order.setConfirmTime(new Date());
				order.setStatus(TicketOrderStatus.FAILED);
				decrDiscountCount(order);
			}
			return null;
		}
	}

	/**
	 * 处理已支付状态的异常订单。
	 * 
	 * @param order
	 *            选座票订单
	 */
	private void processPaidAbnormalOrder(TicketOrder order) {
		// 设定订单最终过期时间为订单过期时间再加15分钟（满天星的轮询问题）
		Date lastExpireTime = new DateTime(order.getExpireTime()).plusMinutes(
				15).toDate();
		// 订单已到最终过期时间，如果接入商不是满天星，设置订单状态为出票失败；接入商是满天星，则进行退票处理。
		Date now = new Date();
		if (lastExpireTime.before(now)) {
			if (order.getProvider() == Provider.MTX) {
				processTimeoutMtxAbnormalOrder(order);
			} else {
				order.setConfirmTime(now);
				order.setStatus(TicketOrderStatus.FAILED);
				decrDiscountCount(order);
			}
			if (order.getSnackOrder() != null) {
				processSnackOrder(order);
			}
		}
	}

	/**
	 * 处理出票失败的异常订单。
	 * 
	 * @param order
	 *            选座票订单
	 */
	private void processFailedAbnormalOrder(TicketOrder order) {
		// 如果地面订单是出票失败的，设置订单状态为出票失败。
		order.setConfirmTime(new Date());
		order.setStatus(TicketOrderStatus.FAILED);
		if (order.getSnackOrder() != null) {
			processSnackOrder(order);
		}
		decrDiscountCount(order);
	}

	/**
	 * 处理出票成功的异常订单。
	 * 
	 * @param order
	 *            选座票订单
	 * @param externalOrder
	 *            外部订单
	 */
	private void processSuccessAbnormalOrder(TicketOrder order,
			TicketOrder externalOrder) {
		// 如果订单已到过期时间，尝试调用退票接口退票。
		if (order.getExpireTime().before(new Date())) {
			if (order.getVoucher() == null) {
				order.setVoucher(externalOrder.getVoucher());
			}
			processTimeoutAbnormalOrder(order);
		} else {
			// 如果地面订单是出票成功的，设置订单状态为出票成功。
			order.confirm(externalOrder);
			createVoucher(order, externalOrder);
			if (order.getSnackOrder() != null) {
				processSnackOrder(order);
			}
			homeService.incrTicketOrderStat(order);
		}
	}

	/**
	 * 处理满天星超时的异常订单。尝试退票，如果退票成功标识订单为出票失败；如果退票失败需要人工干预处理。
	 * 
	 * @param order
	 *            超时的异常订单
	 */
	private void processTimeoutMtxAbnormalOrder(TicketOrder order) {
		for (int i = 0; i < 5; i++) {
			try {
				TicketAccessService accessService = getAccessService(order);
				Boolean revoked = accessService.refundTicket(order);
				if (revoked) {
					order.setConfirmTime(new Date());
					order.setStatus(TicketOrderStatus.FAILED);
					decrDiscountCount(order);
					return;
				} else {
					messageSource.thrown("ticketOrder.revoke.failed");
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				String msg = String.format(
						"[订单号]%s[第]%s[次调用满天星退票失败]%n[失败原因：%s]", order.getCode(),
						i, e.getMessage());
				mtxLog.error(msg);
			} finally {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
					mtxLog.error(e.getMessage());
				}
			}
		}
		throw new BusinessException("满天星轮询退票失败。");
	}

	/**
	 * 处理超时的异常订单。尝试退票，如果退票成功标识订单为出票失败；如果退票失败需要人工干预处理。
	 * 
	 * @param order
	 *            超时的异常订单
	 */
	private void processTimeoutAbnormalOrder(TicketOrder order) {
		try {
			TicketAccessService accessService = getAccessService(order);
			Boolean revoked = accessService.refundTicket(order);
			if (revoked) {
				order.setConfirmTime(new Date());
				order.setStatus(TicketOrderStatus.FAILED);
				if (order.getSnackOrder() != null) {
					processSnackOrder(order);
				}
				decrDiscountCount(order);
			} else {
				messageSource.thrown("ticketOrder.revoke.failed");
			}
		} catch (Exception e) {
			log.error("处理超时异常订单尝试退票失败。", e);
		}
	}

	/**
	 * 扣减已优惠次数。
	 * 
	 * @param card
	 *            权益卡
	 * @param count
	 *            需要优惠的次数
	 * @param disCountAmount
	 *            优惠金额
	 */
	private void decrDiscountCount(TicketOrder order) {
		BenefitCardConsumeOrder consumeOrder = order
				.getBenefitCardConsumeOrder();
		if (order.getBenefitCardConsumeOrder() == null) {
			return;
		}
		BenefitCard card = consumeOrder.getCard();
		// 减去已使用次数与金额
		card.setAvailableDiscountCount(card.getAvailableDiscountCount()
				+ consumeOrder.getDiscountCount());
		card.setDiscountAmount(NumberUtils.sub(card.getDiscountAmount(),
				consumeOrder.getDiscountAmount()));
		if (order.getPayTime().before(DateUtils.getToday()) || card.isExpire()) {
			return;
		}
		benefitCardOrderService.decrDailyDiscountCount(card.getCode(),
				consumeOrder.getDiscountCount());
	}

	/**
	 * 处理订单对应的卖品订单。
	 * 
	 * @param order
	 *            选座票订单
	 */
	private void processSnackOrder(TicketOrder order) {
		SnackOrder snackOrder = order.getSnackOrder();
		snackOrder.setCreateTime(order.getConfirmTime());
		snackOrder.setStatus(order.getStatus());
		if (order.getStatus() == TicketOrderStatus.REVOKED) {
			snackOrder.setRevokeTime(order.getRevokeTime());
		}
	}

	/**
	 * 创建凭证。
	 * 
	 * @param order
	 *            订单
	 * @param externalOrder
	 *            外部订单
	 */
	private void createVoucher(TicketOrder order, TicketOrder externalOrder) {
		TicketVoucher voucher = new TicketVoucher();
		BeanUtils.copyFields(externalOrder.getVoucher(), voucher);
		voucher.setOrder(order);
		voucher.setId(order.getId());
		voucher.setCode(voucherService.genTicketVoucherCode(order));
		voucher.setGenTime(new Date());
		Date showTime = voucher.getOrder().getShowTime();
		voucher.setExpireTime(DateUtils.getNextDay(showTime));
		order.setVoucher(voucher);
		ticketVoucherDao.save(voucher);
	}

	/**
	 * 发送处理异常订单失败邮件。
	 */
	@Transactional
	public void sendAbnormalOrderFailListMail() {
		List<TicketOrder> orders = searchExpireTimeAbnormalTicketOrder();
		if (CollectionUtils.isNotEmpty(orders)) {
			AbnormalOrderProcessFailedListMailModel model = new AbnormalOrderProcessFailedListMailModel(
					orders, circuit.getOrderWarnUsers());
			mailService.send(model);
		}
	}

	/**
	 * 获取接入接口服务。
	 * 
	 * @param order
	 *            订单
	 * @return 返回接入接口服务。
	 */
	private TicketAccessService getAccessService(TicketOrder order) {
		return TicketAccessServiceFactory.getTicketService(order.getCinema()
				.getTicketSettings());
	}
}
