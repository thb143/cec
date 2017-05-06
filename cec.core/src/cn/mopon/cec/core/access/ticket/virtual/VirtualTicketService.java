package cn.mopon.cec.core.access.ticket.virtual;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.converter.ShowTypeConverter;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.exception.BusinessException;
import coo.base.util.CryptoUtils;
import coo.base.util.DateUtils;

/**
 * 虚拟售票接口。
 */
public class VirtualTicketService implements TicketAccessService {
	private TicketSettings settings;

	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public VirtualTicketService(TicketSettings settings) {
		this.settings = settings;
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		List<Hall> halls = new ArrayList<>();
		for (int i = 1; i < 4; i++) {
			Hall hall = new Hall();
			hall.setName(i + "号厅");
			hall.setCode(i + "");
			hall.setSeatCount(19 * 19);
			halls.add(hall);
		}
		cinema.setHalls(halls);
		return cinema;
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		return genHallSeats(20, hall);
	}

	/**
	 * 生成影厅座位。
	 * 
	 * @param count
	 *            数量
	 * @param hall
	 *            影厅
	 * @return 返回座位列表。
	 */
	private List<Seat> genHallSeats(int count, Hall hall) {
		List<Seat> seats = new ArrayList<>();
		for (int i = 1; i < count; i++) {
			for (int j = 1; j < count; j++) {
				Seat seat = new Seat();
				seat.setHall(hall);
				seat.setGroupCode("201408200011");
				seat.setCode(hall.getCode() + "_" + i * 10 + "_" + j);
				seat.setColNum(i + "");
				seat.setRowNum(j + "");
				seat.setXCoord(i);
				seat.setYCoord(j);
				seats.add(seat);
			}
		}
		return seats;
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		List<Show> shows = new ArrayList<>();
		Date nextDate = DateUtils.getNextDay();
		for (int i = 0; i < 10; i++) {
			Show show = new Show();
			show.setCode(DateUtils.format(nextDate, DateUtils.DAY_N) + "" + i);
			String filmCode = (i % 2 == 0) ? "001204042014" : "051100792014";
			show.setFilmCode(filmCode);
			Hall hall = new Hall();
			hall.setCode("1");
			hall.setCinema(cinema);
			show.setHall(hall);
			show.setShowTime(nextDate);
			show.setStdPrice(50.0);
			show.setMinPrice(20.0);
			show.setCinema(cinema);
			show.setShowType(ShowTypeConverter.getShowType(filmCode));
			shows.add(show);
			nextDate = new Date(nextDate.getTime() + 2 * 60 * 60 * 1000);
		}
		return shows;
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		List<ShowSeat> showSeatList = new ArrayList<ShowSeat>();
		for (Seat seat : channelShow.getHall().getSeats()) {
			ShowSeat sessionSeat = ShowSeat.createBySeat(seat);
			showSeatList.add(sessionSeat);
		}
		return showSeatList;
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		String cinemaCode = String.valueOf(System.currentTimeMillis());
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode(cinemaCode);
		return ticketOrder;
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		// nothing to do
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		TicketOrder remoteOrder = new TicketOrder();

		TicketVoucher voucher = new TicketVoucher();
		voucher.setPrintCode(CryptoUtils.genRandomCode("0123456789", 8));
		voucher.setVerifyCode(CryptoUtils.genRandomCode("0123456789", 8));
		remoteOrder.setVoucher(voucher);

		List<TicketOrderItem> items = new ArrayList<>();
		for (TicketOrderItem item : order.getOrderItems()) {
			TicketOrderItem orderItem = new TicketOrderItem();
			orderItem.setSeatCode(item.getSeatCode());
			orderItem
					.setTicketCode(CryptoUtils.genRandomCode("0123456789", 12));
			items.add(orderItem);
		}
		remoteOrder.setOrderItems(items);

		return remoteOrder;
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		return true;
	}

	@Override
	public TicketOrder queryOrder(TicketOrder order) {
		return null;
	}

	public TicketSettings getSettings() {
		return settings;
	}

	public void setSettings(TicketSettings settings) {
		this.settings = settings;
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		return null;
	}

	@Override
	public boolean confirmPrint(TicketOrder order) {
		throw new BusinessException("该接入商不支持打票。");
	}
}