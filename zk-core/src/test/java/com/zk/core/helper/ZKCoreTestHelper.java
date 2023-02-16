/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKCoreTestHelper.java 
 * @author Vinson 
 * @Package com.zk.core.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:45:32 PM 
 * @version V1.0   
*/
package com.zk.core.helper;

import org.springframework.context.ApplicationContext;

/** 
* @ClassName: ZKCoreTestHelper 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCoreTestHelper {


    static ApplicationContext ctx;

    public static ApplicationContext getCtxUtils() {
        if (ctx == null) {
            ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
        }
        return ctx;
    }


}
