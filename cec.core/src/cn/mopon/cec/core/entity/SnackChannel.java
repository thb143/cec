package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.OpenStatus;
import cn.mopon.cec.core.enums.SnackStatus;
import coo.base.util.NumberUtils;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 卖品渠道。
 */
@Entity
@Table(name = "CEC_SnackChannel")
@Indexed(index = "SnackChannel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnackChannel extends ResourceEntity<User> implements
		Comparable<SnackChannel> {
	/** 关联卖品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "snackId")
	@IndexedEmbedded(includePaths = { "id", "code", "status", "cinema.code",
			"type.createDate" })
	private Snack snack;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "code" })
	private Channel channel;
	/** 接入费 */
	@LogField(text = "接入费")
	private Double connectFee = 0D;
	/** 开放状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private OpenStatus status = OpenStatus.OPENED;

	/**
	 * 判断卖品渠道是否有效。
	 * 
	 * @return 返回特殊定价渠道是否有效。
	 */
	public Boolean isValid() {
		return getChannel().getOpened() && status == OpenStatus.OPENED
				&& getSnack().getCinema().getStatus() == EnabledStatus.ENABLED
				&& getSnack().getStatus() == SnackStatus.ON;
	}

	/**
	 * 判断卖品渠道是否可售。
	 * 
	 * @return 返回卖品渠道是否可售。
	 */
	public Boolean isSalable() {
		return getSnack().getCinema().getSalable() && getChannel().getSalable()
				&& isValid();
	}

	/**
	 * 获取结算价。
	 * 
	 * @return 返回结算价。
	 */
	public Double getSettlePrice() {
		Double settlePrice = NumberUtils.add(getConnectFee(), getSnack()
				.getSubmitPrice());
		return settlePrice > 0 ? settlePrice : 0D;
	}

	@Override
	public int compareTo(SnackChannel other) {
		return getChannel().getCode().compareTo(other.getChannel().getCode());
	}

	public Snack getSnack() {
		return snack;
	}

	public void setSnack(Snack snack) {
		this.snack = snack;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public OpenStatus getStatus() {
		return status;
	}

	public void setStatus(OpenStatus status) {
		this.status = status;
	}
}