package com.aarete.pi.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aarete.pi.constant.WorkbenchConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Aspect
@Component
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Pointcut(value = "execution(* com.aarete.pi.controller.*.*(..))")
	public void loggerPointCut() {
		// logger pointcut
	}

	@Around("loggerPointCut()")
	public Object applicationLogger(ProceedingJoinPoint joinPoint) throws Throwable {
		ObjectMapper objectMapper = new ObjectMapper();
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().toString();

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class<?> declaredClass = signature.getDeclaringType();
		Api classLevelAPIAnnotation = declaredClass.getAnnotation(Api.class);
		if (classLevelAPIAnnotation != null) {
			MDC.put(WorkbenchConstants.LOGGING_MDC_FUNCTIONALITY_HEAD, classLevelAPIAnnotation.value());
		}
		ApiOperation methodLevelAPIOperationAnnotation = signature.getMethod().getAnnotation(ApiOperation.class);
		if (methodLevelAPIOperationAnnotation != null) {
			MDC.put(WorkbenchConstants.LOGGING_MDC_FUNCTIONALITY_SUB_HEAD, methodLevelAPIOperationAnnotation.value());
		}
		Object[] argsArray = joinPoint.getArgs();
		Object responseObject = new Object();
		if (log.isInfoEnabled()) {
			log.info("Method Invoked {} : {} : {} () -> {}", "", className, methodName,
					objectMapper.writeValueAsString(argsArray));
			responseObject = joinPoint.proceed();
			log.info("Response -> {} : {} : {} () :{}", "", className, methodName,
					objectMapper.writeValueAsString(responseObject));
		}

		return responseObject;
	}

}