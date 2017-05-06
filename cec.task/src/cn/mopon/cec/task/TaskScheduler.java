package cn.mopon.cec.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.model.FilmSyncModel;
import cn.mopon.cec.core.service.BenefitCardService;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.FilmService;
import cn.mopon.cec.core.service.ShowService;
import cn.mopon.cec.core.service.SnackVoucherService;
import cn.mopon.cec.core.service.SpecialPolicyService;
import cn.mopon.cec.core.service.StatService;
import cn.mopon.cec.core.service.TicketOrderService;
import cn.mopon.cec.core.service.TicketVoucherService;
import coo.base.util.DateUtils;

/**
 * 定时任务调度组件。
 */
@Component("cn.mopon.cec.task.TaskScheduler")
@Lazy(false)
public class TaskScheduler {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ExecutorService hallSyncTaskExecutor;
	@Resource
	private FilmService filmService;
	@Resource
	private ExecutorService showSyncTaskExecutor;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private TicketOrderService ticketOrderService;
	@Resource
	private ExecutorService ticketOrderSyncTaskExecutor;
	@Resource
	private TicketVoucherService ticketVoucherService;
	@Resource
	private SnackVoucherService snackVoucherService;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private ExecutorService showCleanTaskExecutor;
	@Resource
	private StatService statService;
	@Resource
	private BenefitCardService benefitCardService;

	/**
	 * 定时同步影厅和座位。
	 */
	@Scheduled(cron = "${hall.sync.cron:0 0 3 * * ?}")
	public void syncHalls() {
		try {
			List<HallSyncTask> tasks = new ArrayList<>();
			for (Cinema cinema : cinemaService.getProvideTicketEnabledCinemas()) {
				tasks.add(new HallSyncTask(cinema.getId()));
			}
			hallSyncTaskExecutor.invokeAll(tasks);
			log.info("定时同步影厅已完成。");
		} catch (Exception e) {
			log.error("定时同步影厅时发生异常。", e);
		}
	}

	/**
	 * 定时同步影片。
	 */
	@Scheduled(cron = "${film.sync.cron:0 30 3 * * ?}")
	public void syncMovicesAndFilms() {
		try {
			filmService.syncFilms(new FilmSyncModel());
			log.info("定时同步影片已完成。");
		} catch (Exception e) {
			log.error("定时同步影片时发生异常。", e);
		}
	}

	/**
	 * 定时同步排期。
	 */
	@Scheduled(cron = "${show.sync.cron:0 */15 8-10,14-18 * * ?}")
	public void syncShows() {
		try {
			List<ShowSyncTask> tasks = new ArrayList<>();
			for (Cinema cinema : cinemaService.getProvideTicketEnabledCinemas()) {
				if (cinema.getTicketSettings().isNeedSyncShows()) {
					tasks.add(new ShowSyncTask(cinema.getId()));
				}
			}
			showSyncTaskExecutor.invokeAll(tasks);
			log.info("定时同步排期已完成。");
		} catch (Exception e) {
			log.error("定时同步排期时发生异常。", e);
		}
	}

	/**
	 * 定时清理超过保留天数的排期（包括影院排期、排期同步日志、渠道排期）。
	 */
	@Scheduled(cron = "${show.clean.cron:0 */30 1-5 * * ?}")
	public void cleanShows() {
		try {
			List<ShowCleanTask> tasks = new ArrayList<>();
			for (Cinema cinema : cinemaService.getProvideTicketEnabledCinemas()) {
				tasks.add(new ShowCleanTask(cinema.getId()));
			}
			showCleanTaskExecutor.invokeAll(tasks);
			log.info("定时清理排期已完成。");
		} catch (Exception e) {
			log.error("定时清理排期时发生异常。", e);
		}
	}

	/**
	 * 定时更新过期排期。
	 */
	@Scheduled(cron = "${show.update.expired.cron:0 */15 8-23 * * ?}")
	public void updateExpiredShows() {
		try {
			showService.updateExpiredShows();
			log.info("定时更新过期排期已完成。");
		} catch (Exception e) {
			log.error("定时更新过期排期时发生异常。", e);
		}
	}

	/**
	 * 定时更新过期渠道排期。
	 */
	@Scheduled(cron = "${channelShow.update.expired.cron:0 */15 8-23 * * ?}")
	public void updateExpiredChannelShows() {
		try {
			channelShowService.updateExpiredChannelShows();
			log.info("定时更新过期渠道排期已完成。");
		} catch (Exception e) {
			log.error("定时更新过期渠道排期时发生异常。", e);
		}
	}

	/**
	 * 定时更新过期选座票订单。
	 */
	@Scheduled(cron = "${ticketOrder.update.expired.cron:0 */15 8-23 * * ?}")
	public void updateExpiredTicketOrder() {
		try {
			ticketOrderService.updateExpiredTicketOrder();
			log.info("定时更新过期选座票订单已完成。");
		} catch (Exception e) {
			log.error("定时更新过期选座票订单时发生异常。", e);
		}
	}

	/**
	 * 定时清理取消的选座票订单。
	 */
	@Scheduled(cron = "${ticketOrder.clean.canceled.cron:0 */2 1-5 * * ?}")
	public void cleanExpiredTicketOrder() {
		try {
			ticketOrderService.cleanCanceledTicketOrder();
			log.info("定时清理取消的选座票订单已完成。");
		} catch (Exception e) {
			log.error("定时清理取消的选座票订单时发生异常。", e);
		}
	}

	/**
	 * 定时更新过期选座票凭证。
	 */
	@Scheduled(cron = "${ticketVoucher.update.expired.cron:0 */2 1-5 * * ?}")
	public void updateExpiredTicketVoucher() {
		try {
			ticketVoucherService.updateExpiredTicketVoucher();
			log.info("定时更新过期选座票凭证已完成。");
		} catch (Exception e) {
			log.error("定时更新过期选座票凭证时发生异常。", e);
		}
	}

	/**
	 * 定时处理异常订单。
	 */
	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60)
	public void processAbnormalOrders() {
		try {
			List<TicketOrderSyncTask> tasks = new ArrayList<>();
			for (TicketOrder abnormalOrder : ticketOrderService
					.searchAbnormalTicketOrder()) {
				tasks.add(new TicketOrderSyncTask(abnormalOrder.getId()));
			}
			ticketOrderSyncTaskExecutor.invokeAll(tasks);
			log.info("定时处理异常订单已完成。");
		} catch (Exception e) {
			log.error("定时处理异常订单时发生异常。", e);
		}
	}

	/**
	 * 定时发送异常订单处理失败邮件。
	 */
	@Scheduled(cron = "${abnormal.order.mail.cron:0 0 9-20 * * ?}")
	public void sendProcessAbnormalOrdersFailMail() {
		try {
			ticketOrderService.sendAbnormalOrderFailListMail();
			log.info("定时发送异常订单处理失败邮件已完成。");
		} catch (Exception e) {
			log.error("定时发送异常订单处理失败邮件时发生异常。", e);
		}
	}

	/**
	 * 定时生成选座票订单日统计记录。
	 */
	@Scheduled(cron = "${ticketOrder.detail.gen.cron:0 30 6 * * ?}")
	public void syncTicketOrderDetail() {
		try {
			Date date = DateUtils.getPrevDay();
			statService.deleteExistStatDateOrder(date);
			statService.syncTicketOrderDetail(date);
			log.info("定时生成选座票订单日统计记录任务已完成。");
		} catch (Exception e) {
			log.error("定时生成选座票订单日统计记录时发生异常。", e);
		}
	}

	/**
	 * 定时启用特殊定价策略。
	 */
	@Scheduled(cron = "${specialPolicy.auto.enable.cron:0 0 0 * * ?}")
	public void autoEnableSpecialPolicys() {
		try {
			List<Show> shows = specialPolicyService.autoEnableSpecialPolicys();
			channelShowService.batchGenChannelShows(shows);
			log.info("定时启用特殊定价策略已完成。");
		} catch (Exception e) {
			log.error("定时启用特殊定价策略时发生异常。", e);
		}
	}

	/**
	 * 定时停用特殊定价策略。
	 */
	@Scheduled(cron = "${specialPolicy.auto.disable.cron:0 30 0 * * ?}")
	public void autoDisableSpecialPolicys() {
		try {
			List<Show> shows = specialPolicyService.autoDisableSpecialPolicys();
			channelShowService.batchGenChannelShows(shows);
			log.info("定时停用特殊定价策略已完成。");
		} catch (Exception e) {
			log.error("定时停用特殊定价策略时发生异常。", e);
		}
	}

	/**
	 * 定时过期权益卡。
	 */
	@Scheduled(cron = "${benefitCard.auto.expire.cron:0 0 0 * * ?}")
	public void expireBenefitCard() {
		try {
			benefitCardService.expireBenefitCard();
			log.info("定时过期权益卡已完成。");
		} catch (Exception e) {
			log.error("定时过期权益卡时发生异常。", e);
		}
	}

	/**
	 * 定时更新过期卖品凭证。
	 */
	@Scheduled(cron = "${snackVoucher.update.expired.cron:0 */2 1-5 * * ?}")
	public void updateExpiredSnackVoucher() {
		try {
			snackVoucherService.updateExpiredSnackVoucher();
			log.info("定时更新过期卖品凭证已完成。");
		} catch (Exception e) {
			log.error("定时更新过期卖品凭证时发生异常。", e);
		}
	}
}
