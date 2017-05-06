package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.NoticeType;
import coo.core.model.UuidEntity;

/**
 * 通知用户。
 */
@Entity
@Table(name = "CEC_NoticeUser")
@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NoticeUser extends UuidEntity {
	/** 关联院线设置 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "circuitSettingsId")
	private CircuitSettings circuitSettings;
	/** 关联用户 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	/** 类型 */
	@Type(type = "IEnum")
	private NoticeType type = NoticeType.SHOW;

	public CircuitSettings getCircuitSettings() {
		return circuitSettings;
	}

	public void setCircuitSettings(CircuitSettings circuitSettings) {
		this.circuitSettings = circuitSettings;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public NoticeType getType() {
		return type;
	}

	public void setType(NoticeType type) {
		this.type = type;
	}
}