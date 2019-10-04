package com.dev.olivebakery.utill;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MethodAop {

    private static final Logger logger = LoggerFactory.getLogger(MethodAop.class);

    //target 메소도의 파라미터등 정볼르 출력한다.
    @Before("execution(* com.dev.olivebakery.service.*.*.*(..)) || execution(* com.dev.olivebakery.service.*.*(..))")
    public void startLog(JoinPoint jp) {
        logger.info("메소드 이름:" + jp.getSignature().getName());
    }

}