package cn.mopon.cec.api.ticket.v1.vo;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.enums.SnackStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 卖品。
 */
@XStreamAlias("snack")
public class SnackVo {
	/** 编码 */
	private String code = "";
	/** 名称 */
	private String name = "";
	/** 类型 */
	private String type = "";
	/** 备注 */
	private String remark = "";
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 结算价 */
	private Double settlePrice = 0D;
	/** 网售价 */
	private Double submitPrice = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 状态 */
	@XStreamConverter(value = IEnumConverter.class, types = SnackStatus.class)
	private SnackStatus status;
	/** 权益卡价格列表 */
	private List<BenefitCardSnackPriceVo> benefitCardPrices = new ArrayList<>();
	/** 图片 */
	private String image = "";

	/**
	 * 构造方法。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @param snackRules
	 *            权益卡类卖品规则列表
	 * @param url
	 *            图片路径
	 */
	public SnackVo(SnackChannel snackChannel,
			List<BenefitCardTypeSnackRule> snackRules, String url) {
		url = url.endsWith("/") ? url : url + "/";
		code = snackChannel.getSnack().getCode();
		name = snackChannel.getSnack().getType().getName();
		type = snackChannel.getSnack().getType().getGroup().getName();
		remark = snackChannel.getSnack().getType().getRemark();
		stdPrice = snackChannel.getSnack().getStdPrice();
		settlePrice = snackChannel.getSettlePrice();
		submitPrice = snackChannel.getSnack().getSubmitPrice();
		connectFee = snackChannel.getConnectFee();
		status = snackChannel.getSnack().getStatus();
		image = url + snackChannel.getSnack().getType().getImage().getPath();
		for (BenefitCardTypeSnackRule snackRule : snackRules) {
			benefitCardPrices.add(new BenefitCardSnackPriceVo(snackChannel,
					snackRule));
		}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Double getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public SnackStatus getStatus() {
		return status;
	}

	public void setStatus(SnackStatus status) {
		this.status = status;
	}

	public List<BenefitCardSnackPriceVo> getBenefitCardPrices() {
		return benefitCardPrices;
	}

	public void setBenefitCardPrices(
			List<BenefitCardSnackPriceVo> benefitCardPrices) {
		this.benefitCardPrices = benefitCardPrices;
	}
}