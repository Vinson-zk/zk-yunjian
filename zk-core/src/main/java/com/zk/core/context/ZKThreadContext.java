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
* @Title: ZKThreadContext.java 
* @author Vinson 
* @Package com.zk.core.context 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2024 11:34:43 PM 
* @version V1.0 
*/
package com.zk.core.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 暂未使用
 * 
 * @ClassName: ZKThreadContext
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKThreadContext {

    public static interface Key {
        public static final String request = "_zk_request";

        public static final String response = "_zk_response";
    }

    private static final Logger logger = LogManager.getLogger(ZKThreadContext.class);

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocal<Map<Object, Object>>();

    public static Object get(Object key) {
        if (logger.isTraceEnabled()) {
            String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }

        Object value = getValue(key);
        if ((value != null) && logger.isTraceEnabled()) {
            String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" + key + "] "
                    + "bound to thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }
        return value;
    }

    public static void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        if (value == null) {
            remove(key);
            return;
        }

        ensureResourcesInitialized();
        resources.get().put(key, value);

        if (logger.isTraceEnabled()) {
            String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" + key + "] to thread "
                    + "[" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }
    }

    public static Object remove(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        Object value = perThreadResources != null ? perThreadResources.remove(key) : null;

        if ((value != null) && logger.isTraceEnabled()) {
            String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" + key + "]"
                    + "from thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }

        return value;
    }

    private static void ensureResourcesInitialized() {
        if (resources.get() == null) {
            resources.set(new HashMap<Object, Object>());
        }
    }

    private static Object getValue(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        return perThreadResources != null ? perThreadResources.get(key) : null;
    }

}
