package com.aiyi.core.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * �ڲ��Զ������������
 *
 * @author ��ʤ��
 * @time 2016��5��11������10:02:15
 * @email 719348277@qq.com
 */

public class Log {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	Map<String, Logger> loggerMap = new HashMap<>();

	public Logger getLogger(Class cls){
		Logger logger = loggerMap.get(cls.getName());
		if (null != logger){
			return logger;
		}
		logger = LoggerFactory.getLogger(cls);
		if (null != logger){
			loggerMap.put(cls.getName(), logger);
			return logger;
		}
		return null;
	}
	
	@Pointcut("@annotation(com.aiyi.core.annotation.po.Log)")
	public void logMethod(){}
	
	@Before("logMethod()")
	public void openTra(JoinPoint joinPoint) throws ClassNotFoundException{

		String targetName = joinPoint.getTarget().getClass().getName();

		String name = joinPoint.getSignature().getName();

		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);

		Method[] methods = targetClass.getMethods();

		String methodName = null;

		for (Method method : methods) {
			if (method.getName().equals(name)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {

					methodName = method.getAnnotation(com.aiyi.core.annotation.po.Log.class).action();

					if (methodName.equals("null")) {
						methodName = name;
					}
					break;
				}
			}
		}

		getLogger(targetClass).info("/-----------" + methodName + "进栈/"+ "\r\n\r\n args:" + JSON.toJSONString(arguments) + "\r\n");
		
	}
	
	@AfterReturning("logMethod()")
	public void cmmintTra(JoinPoint joinPoint) throws ClassNotFoundException{
		
		String targetName = joinPoint.getTarget().getClass().getName();

		String name = joinPoint.getSignature().getName();

		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);

		Method[] methods = targetClass.getMethods();

		String methodName = null;

		for (Method method : methods) {
			if (method.getName().equals(name)) {
				@SuppressWarnings("rawtypes")
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {

					methodName = method.getAnnotation(com.aiyi.core.annotation.po.Log.class).action();

					if (methodName.equals("null")) {
						methodName = name;
					}

					break;
				}
			}
		}

		getLogger(targetClass).info("/-----------" + methodName + "出栈/"+ "\r\n\r\n args:" + JSON.toJSONString(arguments) + "\r\n");

	}
}
