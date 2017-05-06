package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.MemberAccessAdapter;
import coo.core.security.annotations.LogField;

/**
 * 会员接入类型。
 */
@Entity
@Table(name = "CEC_MemberAccessType")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberAccessType extends AccessTypeEntity {
	/** 适配器类型 */
	@Type(type = "IEnum")
	@LogField(text = "适配器类型")
	private MemberAccessAdapter adapter;

	public MemberAccessAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(MemberAccessAdapter adapter) {
		this.adapter = adapter;
	}
}