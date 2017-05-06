package cn.mopon.cec.api.ticket.v1.vo;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.assist.settle.FixedMinusSettleRule;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.ValidMonth;
import cn.mopon.cec.core.enums.ValidStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 卡类VO。
 */
@XStreamAlias("benefitCardType")
public class BenefitCardTypesVo {
	/** 卡类编码 */
	private String code = "";
	/** 卡类名称 */
	private String name = "";
	/** 开卡金额 */
	private Double initAmount = 0D;
	/** 续费金额 */
	private Double rechargeAmount = 0D;
	/** 有效时长（月） */
	@XStreamConverter(value = IEnumConverter.class, types = ValidMonth.class)
	private ValidMonth validMonth;
	/** 启用状态 */
	@XStreamConverter(value = IEnumConverter.class, types = EnabledStatus.class)
	private EnabledStatus enabled;
	/** 享受优惠总量 */
	private Integer totalDiscountCount = 0;
	/** 每日优惠数量 */
	private Integer dailyDiscountCount = 0;
	/** 卡类影院优惠信息 */
	private List<CinemaBenefitCardVo> cinemas = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param benefitCardType
	 *            卡类
	 */
	public BenefitCardTypesVo(BenefitCardType benefitCardType) {
		code = benefitCardType.getCode();
		name = benefitCardType.getName();
		initAmount = benefitCardType.getInitAmount();
		rechargeAmount = benefitCardType.getRechargeAmount();
		validMonth = benefitCardType.getValidMonth();
		enabled = benefitCardType.getEnabled();
		totalDiscountCount = benefitCardType.getTotalDiscountCount();
		dailyDiscountCount = benefitCardType.getDailyDiscountCount();
		for (BenefitCardTypeRule rule : benefitCardType.getRules()) {
			if (rule.getEnabled() == EnabledStatus.ENABLED
					&& rule.getValid() == ValidStatus.VALID) {
				addHalls(rule.getHalls(),
						((FixedMinusSettleRule) rule.getSettleRule())
								.getMinus(), rule.getShowTypes());
			}
		}
		for (BenefitCardTypeSnackRule rule : benefitCardType.getSnackRules()) {
			if (rule.getEnabled() == EnabledStatus.ENABLED
					&& rule.getValid() == ValidStatus.VALID) {
				addSnacks(rule.getSnacks(),
						((FixedMinusSettleRule) rule.getSettleRule())
								.getMinus());
			}
		}
	}

	/**
	 * 添加影厅列表。
	 * 
	 * @param halls
	 *            影厅
	 * @param discountPrice
	 *            折扣价
	 * @param showTypes
	 *            放映类型
	 */
	private void addHalls(List<Hall> halls, Double discountPrice,
			List<ShowType> showTypes) {
		for (Hall hall : halls) {
			CinemaBenefitCardVo cinemaVo = getCinemaCardVo(hall.getCinema());
			HallBenefitCardVo hallVo = new HallBenefitCardVo(hall.getCode(),
					hall.getName());
			if (!checkRepeatHall(cinemaVo.getHalls(), hallVo)) {
				cinemaVo.addHall(hallVo, discountPrice, showTypes);
			} else {
				for (HallBenefitCardVo vo : cinemaVo.getHalls()) {
					if (vo.getHallCode().equals(hallVo.getHallCode())) {
						for (ShowType showType : showTypes) {
							vo.addShowType(showType, discountPrice);
						}
					}
				}
			}
		}
	}

	/**
	 * 校验重复的影厅。
	 * 
	 * @param halls
	 *            已有的影厅列表
	 * @param hall
	 *            需要校验的影厅
	 * @return 返回列表中是否已存在该影厅。
	 */
	private Boolean checkRepeatHall(List<HallBenefitCardVo> halls,
			HallBenefitCardVo hall) {
		List<String> hallCodes = new ArrayList<String>();
		for (HallBenefitCardVo vo : halls) {
			hallCodes.add(vo.getHallCode());
		}
		return hallCodes.contains(hall.getHallCode());
	}

	/**
	 * 获取影院影厅价格模型。
	 * 
	 * @param cinema
	 *            　影院
	 * @return　返回影院影厅价格模型。
	 */
	public CinemaBenefitCardVo getCinemaCardVo(Cinema cinema) {
		for (CinemaBenefitCardVo vo : cinemas) {
			if (vo.getCinemaCode().equals(cinema.getCode())) {
				return vo;
			}
		}
		CinemaBenefitCardVo vo = new CinemaBenefitCardVo(cinema.getName(),
				cinema.getCode());
		cinemas.add(vo);
		return vo;
	}

	/**
	 * 添加卖品列表。
	 * 
	 * @param snacks
	 *            卖品
	 * @param discountPrice
	 *            折扣价
	 */
	private void addSnacks(List<Snack> snacks, Double discountPrice) {
		for (Snack snack : snacks) {
			CinemaBenefitCardVo cinemaVo = getCinemaCardVo(snack.getCinema());
			SnackBenefitCardVo snackVo = new SnackBenefitCardVo(snack,
					discountPrice);
			if (!checkRepeatSnack(cinemaVo.getSnacks(), snackVo)) {
				cinemaVo.addSnack(snackVo);
			}
		}
	}

	/**
	 * 校验重复的卖品。
	 * 
	 * @param snacks
	 *            已有的卖品列表
	 * @param snack
	 *            需要校验的卖品
	 * @return 返回列表中是否已存在该卖品。
	 */
	private Boolean checkRepeatSnack(List<SnackBenefitCardVo> snacks,
			SnackBenefitCardVo snack) {
		List<String> snackCodes = new ArrayList<String>();
		for (SnackBenefitCardVo vo : snacks) {
			snackCodes.add(vo.getSnackCode());
		}
		return snackCodes.contains(snack.getSnackCode());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(Double initAmount) {
		this.initAmount = initAmount;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public ValidMonth getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(ValidMonth validMonth) {
		this.validMonth = validMonth;
	}

	public Integer getTotalDiscountCount() {
		return totalDiscountCount;
	}

	public void setTotalDiscountCount(Integer totalDiscountCount) {
		this.totalDiscountCount = totalDiscountCount;
	}

	public Integer getDailyDiscountCount() {
		return dailyDiscountCount;
	}

	public void setDailyDiscountCount(Integer dailyDiscountCount) {
		this.dailyDiscountCount = dailyDiscountCount;
	}

	public List<CinemaBenefitCardVo> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<CinemaBenefitCardVo> cinemas) {
		this.cinemas = cinemas;
	}

	public EnabledStatus getEnabled() {
		return enabled;
	}

	public void setEnabled(EnabledStatus enabled) {
		this.enabled = enabled;
	}
}