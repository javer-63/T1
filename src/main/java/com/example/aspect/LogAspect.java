package com.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("@annotation(com.example.annotations.LogBefore)")
    public void before(JoinPoint joinPoint) {
        logger.info("Вызван метод {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "@annotation(com.example.annotations.LogAfterReturning)",
            returning = "result"
    )
    public void afterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Метод {} вернул {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(
            pointcut = "@annotation(com.example.annotations.LogAfterThrowing)",
            throwing = "exception"
    )
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("В методе {} произошло исключение {}",
                joinPoint.getSignature().getName(), exception.getClass());
    }

    @Around("@annotation(com.example.annotations.LogAround)")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceeded = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("Время выполнения {} - {}",
                joinPoint.getSignature().getName(), endTime - startTime);
        return proceeded;
    }
}
