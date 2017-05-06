package cn.mopon.cec.api.ticket.v1.vo;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 卡类可使用影院VO。
 */
@XStreamAlias("cinema")
public class CinemaBenefitCardVo {
	private String cinemaCode;
	private String cinemaName;
	private List<HallBenefitCardVo> halls = new ArrayList<>();
	private List<SnackBenefitCardVo> snacks = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param cinemaName
	 *            　影院名称
	 * @param cinemaCode
	 *            　影院编码
	 */
	public CinemaBenefitCardVo(String cinemaName, String cinemaCode) {
		this.cinemaCode = cinemaCode;
		this.cinemaName = cinemaName;
	}

	/**
	 * 添加影厅列表。
	 * 
	 * @param hallVo
	 *            影厅VO
	 */
	public void addHall(HallBenefitCardVo hallVo, Double discountPrice,
			List<ShowType> showTypes) {
		for (ShowType showType : showTypes) {
			hallVo.addShowType(showType, discountPrice);
		}
		halls.add(hallVo);
	}

	/**
	 * 添加卖品列表。
	 * 
	 * @param snackVo
	 *            卖品VO
	 */
	public void addSnack(SnackBenefitCardVo snackVo) {
		snacks.add(snackVo);
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public List<HallBenefitCardVo> getHalls() {
		return halls;
	}

	public void setHalls(List<HallBenefitCardVo> halls) {
		this.halls = halls;
	}

	public List<SnackBenefitCardVo> getSnacks() {
		return snacks;
	}

	public void setSnacks(List<SnackBenefitCardVo> snacks) {
		this.snacks = snacks;
	}
}