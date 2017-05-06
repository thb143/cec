package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.TicketAccessAdapter;
import coo.core.security.annotations.LogField;

/**
 * 选座接入类型。
 */
@Entity
@Table(name = "CEC_TicketAccessType")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TicketAccessType extends AccessTypeEntity {
	/** 适配器类型 */
	@Type(type = "IEnum")
	@LogField(text = "适配器类型")
	private TicketAccessAdapter adapter;

	public TicketAccessAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(TicketAccessAdapter adapter) {
		this.adapter = adapter;
	}
}