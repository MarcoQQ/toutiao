package com.marco.toutiao.aspect;

import com.marco.toutiao.controller.IndexController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.marco.toutiao.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
//        StringBuilder sb = new StringBuilder();
//        for(Object arg : joinPoint.getArgs()){
//            sb.append("arg:" + arg.toString() + "|");
//        }
        logger.info("before method:");
    }

    @After("execution(* com.marco.toutiao.controller.IndexController.*(..))")
    public void afterMethod(){
        logger.info("after method:");
    }
}
