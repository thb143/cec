package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

import coo.core.security.entity.BnLogEntity;

/**
 * 业务日志。
 */
@Entity
@Table(name = "CEC_BnLog")
@Indexed(index = "BnLog")
public class BnLog extends BnLogEntity {

}
