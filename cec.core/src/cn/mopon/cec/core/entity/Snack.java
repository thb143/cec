package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.SnackStatus;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 卖品。
 */
@Entity
@Table(name = "CEC_Snack")
@Indexed(index = "Snack")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Snack extends ResourceEntity<User> {
	/** 关联卖品类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	@IndexedEmbedded(includePaths = { "id", "createDate" })
	private SnackType type;
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id" })
	private Cinema cinema;
	/** 编码 */
	@LogField(text = "卖品编号")
	@Field(analyze = Analyze.NO)
	private String code;
	/** 标准价 */
	@LogField(text = "标准价")
	private Double stdPrice = 0D;
	/** 结算价 */
	@LogField(text = "结算价")
	private Double submitPrice = 0D;
	/** 卖品状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private SnackStatus status = SnackStatus.UNVALID;
	/** 关联卖品渠道 */
	@OneToMany(mappedBy = "snack", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ContainedIn
	private List<SnackChannel> snackChannels = new ArrayList<>();

	/**
	 * 判断是否包含指定的卖品渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回是否包含指定的卖品渠道。
	 */
	public Boolean containsChannel(Channel channel) {
		for (SnackChannel snackChannel : getSnackChannels()) {
			if (snackChannel.getChannel().equals(channel)) {
				return true;
			}
		}
		return false;
	}

	public SnackType getType() {
		return type;
	}

	public void setType(SnackType type) {
		this.type = type;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
	}

	public SnackStatus getStatus() {
		return status;
	}

	public void setStatus(SnackStatus status) {
		this.status = status;
	}

	public List<SnackChannel> getSnackChannels() {
		Collections.sort(snackChannels);
		return snackChannels;
	}

	public void setSnackChannels(List<SnackChannel> snackChannels) {
		this.snackChannels = snackChannels;
	}
}