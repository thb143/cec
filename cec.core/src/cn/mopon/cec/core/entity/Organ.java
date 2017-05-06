package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import coo.core.security.entity.OrganEntity;

/**
 * 机构。
 */
@Entity
@Table(name = "CEC_Organ")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organ extends OrganEntity<Organ, User, Actor> {

}
