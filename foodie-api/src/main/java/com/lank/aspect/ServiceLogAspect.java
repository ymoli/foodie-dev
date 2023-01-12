package com.lank.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
//使用log4j日志，使用aop实现自定义配置
public class ServiceLogAspect {

    public static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    /*
    * AOP通知：
    * 1.前置通知：在方法调用之前执行
    * 2.后置通知：在方法正常调用之后执行
    * 3.最终通知：无论方法调用成功或失败，均执行
    * 4.异常通知：如果在方法调用过程中发生异常，则通知
    * 5.环绕通知：在方法调用之前和之后，都分别执行的通知
    * */

    /*
    * 切面表达式：
    * execution：表示所要执行的表达式主体
    * 第一处  *：代表方法的返回类型，*表示所有类型
    * 第二处  包名：代表aop监控的类所在的包
    * 第三处  ..：代表该包及其子包下的所有方法
    * 第四处  *：代表类名，*代表所有类
    * 第五处  .*(..)：*代表所有的方法名，(..)代表方法中的任何参数
    * */

    @Around("execution(* com.lank.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        //开始执行，{}在log4j中表示占位符，即获取指定class的指定方法
        logger.info("======= 开始执行 {}.{} =====",
                            proceedingJoinPoint.getTarget().getClass(),
                            proceedingJoinPoint.getSignature().getName());
        //开始时间
        long start = System.currentTimeMillis();
        //执行目标Service
        Object result = proceedingJoinPoint.proceed();
        //结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - start;
        if (takeTime > 3000){
            logger.error("===== 执行结束，耗时 {} 毫秒",takeTime);
        }else if (takeTime > 2000){
            logger.warn("===== 执行结束，耗时 {} 毫秒",takeTime);
        }else {
            logger.info("===== 执行结束，耗时 {} 毫秒",takeTime);
        }

        return result;
    }
}
