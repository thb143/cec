package cn.mopon.cec.api.actions.api.ticket;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mopon.cec.core.enums.TicketApiMethod;

/**
 * 票务接口方法权限验证。
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TikcetApiMethodAuth {
	/** 访问类型 */
	TicketApiMethod value();
}