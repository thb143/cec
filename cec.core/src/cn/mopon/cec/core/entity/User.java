package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

import coo.core.security.entity.UserEntity;

/**
 * 用户。
 */
@Entity
@Table(name = "CEC_User")
@Indexed(index = "User")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends UserEntity<User, Actor, UserSettings> {
}
