package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.mail.SeatEmptyMailModel;
import cn.mopon.cec.core.model.ExternalHallsModel;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.SimpleLog;

/**
 * 影厅管理。
 */
@Service
public class HallService {
	@Resource
	private Dao<Hall> hallDao;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private SeatService seatService;
	@Resource
	private MailService mailService;
	@Resource
	private Circuit circuit;
	@Resource
	private HallTypeService hallTypeService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取指定ID的影厅。
	 * 
	 * @param hallId
	 * 
	 *            影厅ID
	 * @return 返回对应的影厅。
	 */
	@Transactional
	public Hall getHall(String hallId) {
		return hallDao.get(hallId);
	}

	/**
	 * 更新影厅。
	 * 
	 * @param type
	 *            影院
	 */
	@Transactional
	public void updateHall(String[] type) {
		for (String typeStr : type) {
			String[] hallType = typeStr.split("[|]");
			if (StringUtils.isEmpty(typeStr)) {
				continue;
			}
			Hall origHall = getHall(hallType[0]);
			if (hallType.length == 2) {
				origHall.setHallType(hallTypeService.getHallType(hallType[1]));
			} else {
				origHall.setHallType(null);
			}
		}
	}

	/**
	 * 获取指定编码的影厅。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @param hallCode
	 *            影厅编码
	 * @return 返回对应的影厅。
	 */
	public Hall getHallByCode(String cinemaCode, String hallCode) {
		FullTextCriteria criteria = hallDao.createFullTextCriteria();
		criteria.addFilterField("cinema.code", cinemaCode);
		criteria.addFilterField("code", hallCode);
		return hallDao.searchUnique(criteria);
	}

	/**
	 * 启用影厅。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "hall.enable.log", vars = { "hall.cinema.name",
			"hall.name" })
	public List<Show> enableHall(Hall hall) {
		List<Show> shows = new ArrayList<>();
		if (hall.getStatus() == HallStatus.DISABLED) {
			hall.setStatus(HallStatus.ENABLED);
			shows = showService.getMatchedShows(hall);
		}
		return shows;
	}

	/**
	 * 禁用影厅。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回作废渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "hall.diable.log", vars = { "hall.cinema.name",
			"hall.name" })
	public Integer disableHall(Hall hall) {
		Integer count = 0;
		if (hall.getStatus() == HallStatus.ENABLED) {
			hall.setStatus(HallStatus.DISABLED);
			List<Show> shows = showService.getMatchedShows(hall);
			for (Show show : shows) {
				count += channelShowService.desChannelShows(show);
			}
		}
		return count;
	}

	/**
	 * 同步影厅。
	 * 
	 * @param cinemaId
	 *            影院ID
	 */
	@Transactional
	public void syncHalls(String cinemaId) {
		Cinema cinema = cinemaService.getCinema(cinemaId);
		syncHalls(cinema);
	}

	/**
	 * 同步影厅。
	 * 
	 * @param cinema
	 *            影院
	 */
	@Transactional
	@SimpleLog(code = "hall.sync.log", vars = { "cinema.name", "cinema.code" })
	public void syncHalls(Cinema cinema) {
		if (!cinema.getTicketSetted()) {
			messageSource.thrown("hall.sync.no.settings");
		}

		List<Hall> noSeatHalls = new ArrayList<>();

		ExternalHallsModel externalHallsModel = getExternalHallsModel(cinema);
		createHalls(externalHallsModel.getNewHalls(), noSeatHalls);
		updateHalls(externalHallsModel.getUpdateHalls(), noSeatHalls);
		deleteHalls(externalHallsModel.getDeleteHalls());

		cinema.setHallCount(externalHallsModel.getCount());

		if (CollectionUtils.isNotEmpty(noSeatHalls)) {
			SeatEmptyMailModel mailModel = new SeatEmptyMailModel(
					cinema.getName(), noSeatHalls, circuit.getShowWarnUsers());
			mailService.send(mailModel);
		}
	}

	/**
	 * 批量处理新增的影厅。
	 * 
	 * @param externalHalls
	 *            外部影厅列表
	 * @param noSeatHalls
	 *            无座位影厅列表
	 */
	private void createHalls(List<Hall> externalHalls, List<Hall> noSeatHalls) {
		for (Hall hall : externalHalls) {
			hall.setStatus(HallStatus.ENABLED);
			hall.setSeats(new ArrayList<Seat>());
			hallDao.save(hall);
			hall.getCinema().getHalls().add(hall);
			seatService.syncSeats(hall, noSeatHalls);
		}
	}

	/**
	 * 批量处理更新的影厅。
	 * 
	 * @param externalHalls
	 *            外部影厅列表
	 * @param noSeatHalls
	 *            无座位影厅列表
	 */
	private void updateHalls(List<Hall> externalHalls, List<Hall> noSeatHalls) {
		for (Hall hall : externalHalls) {
			Hall origHall = getHall(hall.getId());
			// 如果影厅是删除状态，则修改为启用状态。
			if (origHall.getStatus() == HallStatus.DELETE) {
				hall.setStatus(HallStatus.ENABLED);
			}
			BeanUtils.copyFields(hall, origHall, "seatCount,seats");
			seatService.syncSeats(origHall, noSeatHalls);
		}
	}

	/**
	 * 批量处理删除的影厅。
	 * 
	 * @param halls
	 *            影厅列表
	 */
	private void deleteHalls(List<Hall> halls) {
		for (Hall hall : halls) {
			hall.setStatus(HallStatus.DELETE);
		}
	}

	/**
	 * 获取外部影厅待处理模型。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回外部影厅待处理模型。
	 */
	private ExternalHallsModel getExternalHallsModel(Cinema cinema) {
		ExternalHallsModel model = new ExternalHallsModel();

		List<Hall> externalHalls = getExternalHalls(cinema);
		List<String> externalHallCodes = new ArrayList<>();
		for (Hall externalHall : externalHalls) {
			Hall origHall = cinema.getHall(externalHall.getCode());
			if (origHall != null) {
				// 设置ID用于更新操作
				externalHall.setId(origHall.getId());
				model.addUpdateHall(externalHall);
			} else {
				// 设置关联影院用于新增操作
				externalHall.setCinema(cinema);
				model.addNewHall(externalHall);
			}
			externalHallCodes.add(externalHall.getCode());
		}

		for (Hall hall : cinema.getHalls()) {
			if (!externalHallCodes.contains(hall.getCode())) {
				model.addDeleteHall(hall);
			}
		}
		return model;
	}

	/**
	 * 获取外部影厅列表。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回外部影厅列表。
	 */
	private List<Hall> getExternalHalls(Cinema cinema) {
		TicketAccessService ticketAccessService = TicketAccessServiceFactory
				.getTicketService(cinema.getTicketSettings());
		Cinema externalCinema = ticketAccessService.getCinema(cinema.getCode());
		return externalCinema.getHalls();
	}
}