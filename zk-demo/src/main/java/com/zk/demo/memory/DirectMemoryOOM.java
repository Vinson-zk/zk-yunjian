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
* @Title: DirectMemoryOOM.java 
* @author Vinson 
* @Package com.zk.demo.memory 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 29, 2020 3:04:33 PM 
* @version V1.0 
*/
package com.zk.demo.memory;

import java.lang.reflect.Field;

import org.springframework.objenesis.instantiator.util.UnsafeUtils;

/** 
* @ClassName: DirectMemoryOOM 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class DirectMemoryOOM {

    /**
     * 直接内存：使用 unsafe 分配本机内存
     * 
     * VM Args: -Xmx20M -XX:MaxDirectMemorySize=10M
     * 
     */

    public static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Field unsafeField = UnsafeUtils.getUnsafe().getClass().getDeclaredFields()[0];

        unsafeField.setAccessible(true);

        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

        while (true) {
            unsafe.allocateMemory(_1MB);
        }

    }

}
