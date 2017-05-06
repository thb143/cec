package cn.mopon.cec.core.fantasy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class FanAspect {
	
	@Before("@annotation(cn.mopon.cec.core.fantasy.FanAnnotations)")
	public void before(JoinPoint joinPoint) {
		String object = (String) joinPoint.getTarget();
		if ("".equals(object) || object == null) {
			throw new RuntimeException("your string is null,please check it!!!");
		}
	}
}
