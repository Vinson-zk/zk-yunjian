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
* @Title: ZKSecThreadContext.java 
* @author Vinson 
* @Package com.zk.security.thread 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 7:10:36 PM 
* @version V1.0 
*/
package com.zk.security.thread;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.utils.ZKCollectionUtils;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.subject.ZKSecSubject;

/** 
* @ClassName: ZKSecThreadContext 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecThreadContext {

    private static final Logger logger = LogManager.getLogger(ZKSecThreadContext.class);

    public static final String SEC_MANAGER_KEY = ZKSecThreadContext.class.getName() + "_SEC_MANAGER_KEY";

    public static final String SUBJECT_KEY = ZKSecThreadContext.class.getName() + "_SUBJECT_KEY";

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<Map<Object, Object>>();

    protected ZKSecThreadContext() {
    }

    public static ThreadLocal<Map<Object, Object>> getResourcesSource() {
        return resources;
    }

    public static Map<Object, Object> getResources() {
        if (resources.get() == null) {
            return Collections.emptyMap();
        }
        else {
            return new HashMap<Object, Object>(resources.get());
        }
    }

    public static void setResources(Map<Object, Object> newResources) {
        if (ZKCollectionUtils.isEmpty(newResources)) {
            return;
        }
        ensureResourcesInitialized();
        resources.get().clear();
        resources.get().putAll(newResources);
    }

    private static Object getValue(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        return perThreadResources != null ? perThreadResources.get(key) : null;
    }

    private static void ensureResourcesInitialized() {
        if (resources.get() == null) {
            resources.set(new HashMap<Object, Object>());
        }
    }

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

    public static void remove() {
        resources.remove();
    }

    public static ZKSecSecurityManager getSecurityManager() {
        return (ZKSecSecurityManager) get(SEC_MANAGER_KEY);
    }

    public static void bind(ZKSecSecurityManager ZKSecSecurityManager) {
        if (ZKSecSecurityManager != null) {
            put(SEC_MANAGER_KEY, ZKSecSecurityManager);
        }
    }

    public static ZKSecSecurityManager unbindSecurityManager() {
        return (ZKSecSecurityManager) remove(SEC_MANAGER_KEY);
    }

    public static ZKSecSubject getSubject() {
        return (ZKSecSubject) get(SUBJECT_KEY);
    }

    public static void bind(ZKSecSubject subject) {
        if (subject != null) {
            put(SUBJECT_KEY, subject);
        }
        if (subject.getSecurityManager() != null) {
            bind(subject.getSecurityManager());
        }
    }

    public static ZKSecSubject unbindSubject() {
        return (ZKSecSubject) remove(SUBJECT_KEY);
    }

    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>>
            extends InheritableThreadLocal<Map<Object, Object>> {

        @SuppressWarnings({ "unchecked" })
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            }
            else {
                return null;
            }
        }
    }

}
