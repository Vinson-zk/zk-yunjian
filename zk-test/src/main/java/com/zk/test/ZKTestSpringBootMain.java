/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKTestSpringBootMain.java 
* @author Vinson 
* @Package com.zk.test 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 9, 2024 4:45:52 PM 
* @version V1.0 
*/
package com.zk.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.utils.ZKJsonUtils;

/** 
* @ClassName: ZKTestSpringBootMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTestSpringBootMain {

    /**
     * 日志对象
     */
    static Logger log = LogManager.getLogger(ZKTestSpringBootMain.class);

    public static void main(String[] args) {

        // ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF
        System.out.println("[^_^:20201217-2157-001] " + ZKJsonUtils.toJsonStr(args));

    }

}
