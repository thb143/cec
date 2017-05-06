package cn.mopon.cec.core.fantasy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.mail.handlers.message_rfc822;

public class Test {
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		FanEntity fanEntity = new FanEntity();
		
		System.out.println(fanEntity.getName());
		
		Field [] fields = fanEntity.getClass().getDeclaredFields();
		
//		for (Field field : fields) {
//			FanAnnotations annotations = field.getAnnotation(FanAnnotations.class);
//			if (annotations != null) {
//			}
//		}
		
		fanEntity.setName("wo");
		
		Method[] methods = fanEntity.getClass().getMethods();
		for (Method method : methods) {
			FanAnnotations fanAnnotations = method.getAnnotation(FanAnnotations.class);
			if (fanAnnotations != null) {
				method.invoke(fanEntity, fanAnnotations.name());
			}
		}
		
		System.out.println(fanEntity.getName());
		
	}
}
