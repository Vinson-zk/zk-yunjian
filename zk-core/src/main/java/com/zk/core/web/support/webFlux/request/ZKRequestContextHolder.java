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
* @Title: ZKRequestContextHolder.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.request 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2024 11:28:08 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/** 
* @ClassName: ZKRequestContextHolder 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRequestContextHolder {

    public static interface Key {
        public static final String serverWebExchange = "_zk_ServerWebExchange";
    }

    private static final Logger logger = LogManager.getLogger(ZKRequestContextHolder.class);

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocal<Map<Object, Object>>();

    public static ServerWebExchange getServerWebExchange() {
        ServerWebExchange serverWebExchange = get(Key.serverWebExchange);
        return serverWebExchange;
    }

    public static void setServerWebExchange(ServerWebExchange serverWebExchange) {
        put(Key.serverWebExchange, serverWebExchange);
    }

    public static ServerHttpRequest getRequest() {
        ServerWebExchange serverWebExchange = getServerWebExchange();
        if (serverWebExchange != null) {
            return serverWebExchange.getRequest();
        }
        return null;
    }

    public static ServerHttpResponse getResponse() {
        ServerWebExchange serverWebExchange = getServerWebExchange();
        if (serverWebExchange != null) {
            return serverWebExchange.getResponse();
        }
        return null;
    }

    protected static <T> T get(Object key) {
        if (logger.isTraceEnabled()) {
            String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }

        T value = getValue(key);
        if ((value != null) && logger.isTraceEnabled()) {
            String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" + key + "] "
                    + "bound to thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }
        return value;
    }

    protected static <T> void put(Object key, T value) {
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

    @SuppressWarnings("unchecked")
    protected static <T> T remove(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        Object value = perThreadResources != null ? perThreadResources.remove(key) : null;

        if ((value != null) && logger.isTraceEnabled()) {
            String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" + key + "]"
                    + "from thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }

        return (T) value;
    }

    private static void ensureResourcesInitialized() {
        if (resources.get() == null) {
            resources.set(new HashMap<Object, Object>());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getValue(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        return perThreadResources != null ? (T) perThreadResources.get(key) : null;
    }

}
