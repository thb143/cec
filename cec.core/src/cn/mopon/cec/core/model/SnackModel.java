package cn.mopon.cec.core.model;

import cn.mopon.cec.core.entity.Snack;

/**
 * 卖品模型。
 */
public class SnackModel {
	/** 卖品 */
	private Snack snack;
	/** 是否允许新增渠道 */
	private Boolean isAddChannel;

	/**
	 * 构造方法。
	 * 
	 * @param snack
	 *            卖品
	 */
	public SnackModel(Snack snack) {
		this.snack = snack;
	}

	public Snack getSnack() {
		return snack;
	}

	public void setSnack(Snack snack) {
		this.snack = snack;
	}

	public Boolean getIsAddChannel() {
		return isAddChannel;
	}

	public void setIsAddChannel(Boolean isAddChannel) {
		this.isAddChannel = isAddChannel;
	}
}