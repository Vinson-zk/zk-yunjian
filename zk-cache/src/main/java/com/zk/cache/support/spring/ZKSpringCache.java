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
 * @Title: ZKSpringCache.java 
 * @author Vinson 
 * @Package com.zk.cache.support.spring 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:09:58 AM 
 * @version V1.0   
*/
package com.zk.cache.support.spring;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.zk.cache.ZKCache;

/** 
* @ClassName: ZKSpringCache 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSpringCache implements Cache {

    ZKCache<Object, Object> cache;

    public ZKSpringCache(ZKCache<Object, Object> cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return this.cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        return toWrapper(this.cache.get(key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Class<T> type) {
        return (T) this.cache.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object v = null;
        try {
            v = this.cache.get(key);
        }
        catch(Exception e) {
            e.printStackTrace();
            v = null;
        }
        if (v != null) {
            return (T) v;
        }
        else {
            return loadValue(key, valueLoader);
        }
    }

    private <T> T loadValue(Object key, Callable<T> valueLoader) {
        T value;
        try {
            value = valueLoader.call();
        }
        catch(Throwable ex) {
            throw new ValueRetrievalException(key, valueLoader, ex);
        }
        put(key, value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        this.cache.put(key, value);

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (this.cache.get(key) == null) {
            this.cache.put(key, value);
        }
        return toWrapper(value);
    }

    @Override
    public void evict(Object key) {
        this.cache.remove(key);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    private ValueWrapper toWrapper(Object v) {
        return (v == null) ? null : new SimpleValueWrapper(v);
    }

}
