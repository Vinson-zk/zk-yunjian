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
* @Title: ZKLog4jTest.java 
* @author Vinson 
* @Package com.zk.log 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 3, 2023 4:40:02 PM 
* @version V1.0 
*/
package com.zk.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @ClassName: ZKLog4jTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLog4jTest {

    @Test
    public void testLog4j() {
        try {
            // log4j.properties
            Logger log4j = LoggerFactory.getLogger(ZKLog4jTest.class);

            System.out.println("ZKLog4jTest: ----- log4j.isTraceEnabled: " + log4j.isTraceEnabled());
            System.out.println("ZKLog4jTest: ----- log4j.isDebugEnabled: " + log4j.isDebugEnabled());
            System.out.println("ZKLog4jTest: ----- log4j.isInfoEnabled: " + log4j.isInfoEnabled());
            System.out.println("ZKLog4jTest: ----- log4j.isWarnEnabled: " + log4j.isWarnEnabled());
            System.out.println("ZKLog4jTest: ----- log4j.isErrorEnabled: " + log4j.isErrorEnabled());

            log4j.trace("ZKLog4jTest: ----- log4j.isTraceEnabled: {}", log4j.isTraceEnabled());
            log4j.debug("ZKLog4jTest: ----- log4j.isDebugEnabled: {}", log4j.isDebugEnabled());
            log4j.info("ZKLog4jTest: ----- log4j.isInfoEnabled: {}", log4j.isInfoEnabled());
            log4j.warn("ZKLog4jTest: ----- log4j.isWarnEnabled: {}", log4j.isWarnEnabled());
            log4j.error("ZKLog4jTest: ----- log4j.isErrorEnabled: {}", log4j.isErrorEnabled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLog4j2() {
        try {
            // log4j2.properties
            org.apache.logging.log4j.Logger log4j2 = org.apache.logging.log4j.LogManager.getLogger(ZKLog4jTest.class);
            log4j2.isTraceEnabled();
            log4j2.trace("ZKLog4jTest: ----- log4j2.isTraceEnabled: {}", log4j2.isTraceEnabled());
            log4j2.debug("ZKLog4jTest: ----- log4j2.isDebugEnabled: {}", log4j2.isDebugEnabled());
            log4j2.info("ZKLog4jTest: ----- log4j2.isInfoEnabled: {}", log4j2.isInfoEnabled());
            log4j2.warn("ZKLog4jTest: ----- log4j2.isWarnEnabled: {}", log4j2.isWarnEnabled());
            log4j2.error("ZKLog4jTest: ----- log4j2.isErrorEnabled: {}", log4j2.isErrorEnabled());

            System.out.println("ZKLog4jTest: ----- log4j2.isTraceEnabled: " + log4j2.isTraceEnabled());
            System.out.println("ZKLog4jTest: ----- log4j2.isDebugEnabled: " + log4j2.isDebugEnabled());
            System.out.println("ZKLog4jTest: ----- log4j2.isInfoEnabled: " + log4j2.isInfoEnabled());
            System.out.println("ZKLog4jTest: ----- log4j2.isWarnEnabled: " + log4j2.isWarnEnabled());
            System.out.println("ZKLog4jTest: ----- log4j2.isErrorEnabled: " + log4j2.isErrorEnabled());

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
