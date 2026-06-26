package org.total.example.spring_core.basics.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("execution(* org.total.example.spring_core.basics.TaskManager.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[ASPECT] Before method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "execution(* org.total.example.spring_core.basics.TaskManager.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint,
                                  Object result) {
        log.info("[ASPECT] AfterReturning method: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(value = "execution(* org.total.example.spring_core.basics.TaskManager.*(..))", throwing = "throwable")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        log.info("[ASPECT] AfterThrowing method: {}, message: {}",
                joinPoint.getSignature().getName(), throwable.getMessage());
    }

    @After("execution(* org.total.example.spring_core.basics.TaskManager.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("[ASPECT] After method: {}", joinPoint.getSignature().getName());
    }

    @Around("execution(* org.total.example.spring_core.basics.TaskManager.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[ASPECT] Around method (before): {}", joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("[ASPECT] Around method (after): {}", joinPoint.getSignature().getName());
        return proceed;
    }

    @Before("@annotation(loggable)")
    public void log(JoinPoint joinPoint, Loggable loggable) {
        for (int i = 0; i < loggable.times(); i++) {
            log.info("[ASPECT LOG BEFORE] [{}] method: {}", loggable.value(),
                    joinPoint.getSignature().getName());
        }
    }
}
