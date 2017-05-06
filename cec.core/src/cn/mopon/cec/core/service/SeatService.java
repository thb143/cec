package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.model.ExternalSeatsModel;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;

/**
 * 座位管理。
 */
@Service
public class SeatService {
	/**
	 * 同步影厅座位。
	 * 
	 * @param hall
	 *            影厅
	 * @param noSeatHalls
	 *            无座位影厅列表
	 */
	@Transactional
	public void syncSeats(Hall hall, List<Hall> noSeatHalls) {
		ExternalSeatsModel externalSeatsModel = getExternalSeatsModel(hall,
				noSeatHalls);
		createSeats(hall, externalSeatsModel.getNewSeats());
		updateSeats(hall, externalSeatsModel.getUpdateSeats());
		deleteSeats(hall, externalSeatsModel.getDeleteSeats());
		if (externalSeatsModel.getCount() > 0) {
			hall.setSeatCount(externalSeatsModel.getCount());
		}
	}

	/**
	 * 新增座位。
	 * 
	 * @param hall
	 *            影厅
	 * @param externalSeats
	 *            外部座位列表
	 */
	private void createSeats(Hall hall, List<Seat> externalSeats) {
		for (Seat seat : externalSeats) {
			seat.setHall(hall);
			hall.getSeats().add(seat);
		}
	}

	/**
	 * 更新座位。
	 * 
	 * @param hall
	 *            影厅
	 * @param externalSeats
	 *            外部座位列表
	 */
	private void updateSeats(Hall hall, List<Seat> externalSeats) {
		for (Seat seat : externalSeats) {
			Seat origSeat = hall.getSeat(seat.getCode());
			if (!seat.equalsTo(origSeat)) {
				BeanUtils.copyFields(seat, origSeat, "hall");
			}
		}
	}

	/**
	 * 删除座位。
	 * 
	 * @param hall
	 *            影厅
	 * @param seats
	 *            座位列表
	 */
	private void deleteSeats(Hall hall, List<Seat> seats) {
		hall.getSeats().removeAll(seats);
	}

	/**
	 * 获取外部座位分组模型。
	 * 
	 * @param hall
	 *            影厅
	 * @param noSeatHalls
	 *            无座位影厅列表
	 * @return 返回外部座位分组模型。
	 */
	private ExternalSeatsModel getExternalSeatsModel(Hall hall,
			List<Hall> noSeatHalls) {
		ExternalSeatsModel model = new ExternalSeatsModel();

		List<Seat> externalSeats = getExternalSeats(hall);
		List<String> externalSeatCodes = new ArrayList<>();

		// 如果获取的外部影厅座位列表不为空才做处理。
		if (CollectionUtils.isNotEmpty(externalSeats)) {
			for (Seat externalSeat : externalSeats) {
				Seat origSeat = hall.getSeat(externalSeat.getCode());
				if (origSeat != null) {
					model.addUpdateSeat(externalSeat);
				} else {
					model.addNewSeat(externalSeat);
				}
				externalSeatCodes.add(externalSeat.getCode());
			}

			for (Seat seat : hall.getSeats()) {
				if (!externalSeatCodes.contains(seat.getCode())) {
					model.addDeleteSeat(seat);
				}
			}
		} else {
			noSeatHalls.add(hall);
		}
		return model;
	}

	/**
	 * 获取外部座位列表。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回外部座位列表。
	 */
	private List<Seat> getExternalSeats(Hall hall) {
		TicketAccessService ticketAccessService = TicketAccessServiceFactory
				.getTicketService(hall.getCinema().getTicketSettings());
		return ticketAccessService.getHallSeats(hall);
	}
}