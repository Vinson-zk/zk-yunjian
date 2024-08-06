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
 * @Title: ZKCacheUtils.java 
 * @author Vinson 
 * @Package com.zk.cache.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 8:58:28 AM 
 * @version V1.0   
*/
package com.zk.cache.utils;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;
import com.zk.core.utils.ZKEnvironmentUtils;

/** 
* @ClassName: ZKCacheUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCacheUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKCacheUtils.class);

    private static final Logger logger = LogManager.getLogger(ZKCacheUtils.class);

    private static ZKCacheManager<?> cacheManager;

    private static final String SYS_DEFAULT_CACHE = "_sys_default_Cache_";

    /**
     * @return cacheManager
     */
    public static ZKCacheManager<?> getCacheManager() {
        if (cacheManager == null) {
            try {
                cacheManager = ZKEnvironmentUtils.getApplicationContext().getBean(ZKCacheManager.class);
            }
            catch(Exception e) {
                cacheManager = null;
                log.error("[20220510-0915-001] 设置缓存管理器失败");
                throw e;
            }
        }
        return cacheManager;
    }

    /**
     * @param cacheManager
     *            the cacheManager to set
     */
    public static void setCacheManager(ZKCacheManager<?> cacheManager) {
        ZKCacheUtils.cacheManager = cacheManager;
    }

    /**
     * 根据缓存名称取一个缓存
     * 
     * @param cacheName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ZKCache<K, V> getCache(String cacheName) {
        return (ZKCache<K, V>) getCacheManager().getCache(cacheName);
    }

    /**
     * @param cacheName
     * @param validTime
     *            ，有效时长，0-不过期，单位为毫秒
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ZKCache<K, V> getCache(String cacheName, long validTime) {
        return (ZKCache<K, V>) getCacheManager().getCache(cacheName, validTime);
    }

    /**
     * 默认操作系统缓存，缓存名为 SYS_CACHE 取一个缓存值
     * 
     * @param key
     * @return
     */
    public static <T> T getKey(String key) {
        ZKCache<String, T> cc = getCache(SYS_DEFAULT_CACHE);
        try {
            return cc.get(key);
        }
        catch(Exception e) {
            // TODO Auto-generated catch block
            logger.error("[>_<: get system cache by key {} is error, error-info:{}]", key, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 默认操作系统缓存，缓存名为 SYS_CACHE 添加一个缓存值
     * 
     * @param key
     * @param v
     */
    public static <T> void put(String key, T v) {
        try {
            getCache(SYS_DEFAULT_CACHE).put(key, v);
        }
        catch(Exception e) {
            logger.error("[>_<: put system cache by key-value {}-{} is error, error-info:{}]", key, e.getMessage(), v);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 默认操作系统缓存，缓存名为 SYS_CACHE 删除一个缓存
     * 
     * @param key
     */
    public static void remove(String key) {
        try {
            getCache(SYS_DEFAULT_CACHE).remove(key);
        }
        catch(Exception e) {
            logger.error("[>_<: remove system cache by key-value {} is error, error-info:{}]", key, e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 默认操作系统缓存，缓存名为 SYS_CACHE 清空缓存
     */
    public static void clean() {
        try {
            getCache(SYS_DEFAULT_CACHE).clear();
        }
        catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 从指定缓存中取一个值
     * 
     * @param cacheName
     * @param key
     * @return
     */
    public static <T> T getKey(String cacheName, String key) {
        ZKCache<String, T> cc = getCache(cacheName);
        try {
            return cc.get(key);
        }
        catch(Exception e) {
            // TODO Auto-generated catch block
            logger.error("[>_<: get system cache by key {} is error, error-info:{}]", key, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向指定缓存中添加一个值
     * 
     * @param cacheName
     * @param key
     * @param v
     */
    public static <T> void put(String cacheName, String key, T v) {
        try {
            getCache(cacheName).put(key, v);
        }
        catch(Exception e) {
            logger.error("[>_<: put system cache by key-value {}-{} is error, error-info:{}]", key, e.getMessage(), v);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 从指定缓存中删除一个值
     * 
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key) {
        try {
            getCache(cacheName).remove(key);
        }
        catch(Exception e) {
            logger.error("[>_<: remove {} cache by key-value {} is error, error-info:{}]", cacheName, key,
                    e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 清除指定缓存
     * 
     * @param cacheName
     */
    public static void clean(String cacheName) {
        try {
            getCache(cacheName).clear();
        }
        catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 按缓存名前缀，清空缓存
     * 
     * @param cacheNamePrefix
     */
    public static void cleanByNamePrefix(String cacheNamePrefix) {
        Collection<String> cacheNames = getCacheManager().getCacheNames();
        if (cacheNames != null && cacheNames.size() > 0) {
            for (String cacheName : cacheNames) {
                if (cacheName.indexOf(cacheNamePrefix) == 0) {
                    getCacheManager().getCache(cacheName).clear();
                }
            }
        }
    }

}
