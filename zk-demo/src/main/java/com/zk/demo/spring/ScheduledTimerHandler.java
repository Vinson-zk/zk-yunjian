/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ScheduledTimerHandler.java 
* @author Vinson 
* @Package com.zk.demo.spring 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 28, 2022 10:10:46 AM 
* @version V1.0 
*/
package com.zk.demo.spring;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
* @ClassName: ScheduledTimerHandler 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Component
@Aspect
public class ScheduledTimerHandler {

    public ScheduledTimerHandler(@Value("${server.port}") String sPort) {
        System.out.println("---------------- sPort: " + sPort);
    }

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void proxyAspect() {

    }

    @Around("proxyAspect()")
    public Object doInvoke(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        System.out.println("[^_^:20220128-1027-001] 定时任务 Scheduled 开始执行:" + method.getAnnotation(Scheduled.class));
        System.out.println("[^_^:20220128-1027-001] 定时任务 Scheduled 开始执行:" + joinPoint.getSignature());
        Object result = null;
        // result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println("[^_^:20220128-1027-002] 定时任务 Scheduled 完成执行，耗时: " + (end - start) + " 毫秒");

        return result;
    }

}
