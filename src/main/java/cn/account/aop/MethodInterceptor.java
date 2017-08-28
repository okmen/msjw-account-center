package cn.account.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.rpc.RpcContext;
/**
 * 用于统计方法调用次数，成功次数，失败次数
 * @author Mbenben
 *
 */
@Aspect
@Component
public class MethodInterceptor {
	public final Logger logger = LoggerFactory.getLogger(MethodInterceptor.class);
	
	@Pointcut("execution(public * cn.account.service.impl.*.*(..))")
	public void cmdMethod() {
		//System.out.println("cmdMethod");
		logger.info("cmdMethod");
	};

	@Before("cmdMethod()")
	public void before(JoinPoint joinPoint) {
		//System.out.println("before");
		logger.debug("add method before   " + joinPoint);
	}

	@After("cmdMethod()")
	public void after(JoinPoint joinPoint) {
		//System.out.println("after");
		logger.debug("add method after   " + joinPoint);
	}

	@Around(value = "cmdMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {

		try{
			MethodSignature signature = (MethodSignature) pjp.getSignature();
			Method method = signature.getMethod();
			Class[] parameterTypes = method.getParameterTypes();
			
			List<String> paramsType = new ArrayList<String>();
			for (Class clazz : parameterTypes) {
				paramsType.add(clazz.getSimpleName());
			}
			
			List<Object> paramsValue = new ArrayList<Object>();
			Object[] args = pjp.getArgs();
			for (Object object : args) {
				paramsValue.add(object);
			}
			Object resultObject = pjp.proceed();
			logger.info(method.getName()+":"+paramsType+","+paramsValue+"("+RpcContext.getContext().getRemoteHost()+")");
			return resultObject;
		}catch(Exception e){
			logger.error("AOP Add Exception:",e);
			throw e;
		}

	}

}
