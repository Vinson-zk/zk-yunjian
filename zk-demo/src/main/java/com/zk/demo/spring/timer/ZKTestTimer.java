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
* @Title: ZKTestTimer.java 
* @author Vinson 
* @Package com.zk.demo.spring.timer 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 28, 2022 9:23:04 AM 
* @version V1.0 
*/
package com.zk.demo.spring.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
* @ClassName: ZKTestTimer 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Component
public class ZKTestTimer {

    @Scheduled(cron = "0/5 * * * * ?")
    public void testTimer() {
        System.out.println("[^_^:20220128-0911-001] 测试的定时任务 111 ");
    }

    @Scheduled(cron = "0/3 * * * * ?")
    public void testTimer2() {
        System.out.println("[^_^:20220128-0911-001] 测试的定时任务 222 ");
    }

}
