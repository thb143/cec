package cn.mopon.cec.core.fantasy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.LOCAL_VARIABLE})
public @interface FanAnnotations {
	String name() default "fantasy";
	
//	boolean notNull() default true;
}
