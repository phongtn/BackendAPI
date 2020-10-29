package com.wind.config.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class LoggingConfig extends LoggingPointcut {

    @Around("service()")
    public Object userAdviceController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String clzName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info("[SERVICE-START] {}", clzName);

        Object value = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("[SERVICE-END] {}. Take: {}ms", clzName, duration);
        return value;
    }
}
