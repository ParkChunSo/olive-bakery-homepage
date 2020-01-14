package com.dev.olivebakery.utill;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.SourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodAop {

  private static final Logger logger = LoggerFactory.getLogger(MethodAop.class);

  //target 메소도의 파라미터등 정볼르 출력한다.
  @Before("execution(* com.dev.olivebakery.service.*.*.*(..)) || execution(* com.dev.olivebakery.service.*.*(..))")
  public void startLog(JoinPoint jp) {
    logger.info("메소드 이름:" + jp.getSignature().getName());
  }

  @Before("execution(* com.dev.olivebakery.service.SignService.*(..))")
  public void signServiceLog(JoinPoint jp){
    Signature signature = jp.getSignature();
    Object[] args = jp.getArgs();
    String kind = jp.getKind();
    SourceLocation sourceLocation = jp.getSourceLocation();
    StaticPart staticPart = jp.getStaticPart();
    Object target = jp.getTarget();
    Object aThis = jp.getThis();
  }

  @Before("execution(* com.dev.olivebakery.security.*.*.*(..))")
  public void tokenLog(JoinPoint jp){

  }

}