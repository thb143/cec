package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import coo.core.model.UuidEntity;
import coo.core.security.annotations.LogField;

/**
 * 影厅。
 */
@Entity
@Table(name = "CEC_HallType")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HallType extends UuidEntity {
	/** 名称 */
	@LogField(text = "影厅类型名称")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}