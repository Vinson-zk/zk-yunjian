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
* @Title: ZKUserCacheUtils.java 
* @author Vinson 
* @Package com.zk.framework.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date May 10, 2022 8:43:55 AM 
* @version V1.0 
*/
package com.zk.framework.security.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.cache.ZKCache;
import com.zk.cache.utils.ZKCacheUtils;
import com.zk.framework.security.userdetails.ZKUser;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKUserCacheUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKUserCacheUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKUserCacheUtils.class);

    /**
     * 定义缓存名称
     * 
     * @ClassName: CacheName
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    static interface CacheName {
        /**
         * 用户缓存
         */
        public static final String USER_CACHE = "USER_CACHE";

        /**
         * 用户权限信息: 包含公司权限代码、公司接口代码、用户权限代码、用户接口代码、用户角色代码
         */
        public static final String USER_AUTH_INFO_CACHE_PREFIX = "USER_AUTH_INFO_CACHE_PREFIX_";

    }

    /************************************************************/
    // --- 整体清理
    /************************************************************/
    /**
     * 清理所有缓存
     *
     * @Title: cleanAll
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:16:59 AM
     * @return void
     */
    public static void cleanAll() {
        ZKCacheUtils.clean(CacheName.USER_CACHE);
        ZKCacheUtils.cleanByNamePrefix(CacheName.USER_AUTH_INFO_CACHE_PREFIX);
    }
    /**
     * 清理当前用户缓存
     *
     * @Title: clean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:03 AM
     * @param <ID>
     * @return void
     */
    public static <ID> void clean() {
        ID userId = ZKSecSecurityUtils.getUserId();
        if (userId == null)
            return;
        clean(userId);
    }

    /**
     * 清理指定用户缓存 by ZKUser pkId 和 by ZKUser
     *
     * @Title: clean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:09 AM
     * @param <ID>
     * @param user
     * @return void
     */
    public static <ID> void clean(ZKUser<ID> user) {
        if (user == null)
            return;
        clean(user.getPkId());
    }

    /**
     * 清理指定用户缓存
     *
     * @Title: clean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:13 AM
     * @param <ID>
     * @param userId
     * @return void
     */
    public static <ID> void clean(ID userId) {
        ZKCacheUtils.getCache(CacheName.USER_CACHE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_AUTH_INFO_CACHE_PREFIX + userId.toString()).clear();
    }

    /************************************************************/
    // --- 用户实体缓存
    /************************************************************/

    // 清理缓存 --------------------
    /**
     * 清理用户实体缓存
     *
     * @Title: cleanUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:33 AM
     * @param <ID>
     * @param userId
     * @return
     * @return boolean
     */
    public static <ID> boolean cleanUser(ID userId) {
        return ZKCacheUtils.getCache(CacheName.USER_CACHE).remove(userId);
    }

    public static <ID> boolean cleanUser(ZKUser<ID> user) {
        return cleanUser(user.getPkId());
    }

    /**
     * 清理所有用户实体缓存
     *
     * @Title: cleanAllUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:30 AM
     * @return void
     */
    public static void cleanAllUser() {
        ZKCacheUtils.getCache(CacheName.USER_CACHE).clear();
    }

    // 取缓存数据 --------------------
    /**
     * 
     *
     * @Title: getUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 22, 2024 10:57:16 AM
     * @param <ID>
     * @return
     * @return ZKUser<ID>
     */
    public static <ID> ZKUser<ID> getUser() {
        ID userId = ZKSecSecurityUtils.getUserId();
        if (userId == null)
            return null;
        return getUser(userId);
    }
    
    /**
     * 取缓存：用户实体；
     *
     * @Title: getUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:25 AM
     * @param <ID>
     * @param userId
     * @return ZKUser<ID>
     */
    public static <ID> ZKUser<ID> getUser(ID userId) {
        ZKCache<ID, ZKUser<ID>> cache = ZKCacheUtils.getCache(CacheName.USER_CACHE);
        return cache.get(userId);
    }

    // 设置缓存：取缓存：用户实体；
    public static <ID> boolean putUser(ID userId, ZKUser<ID> user) {
        ZKCache<ID, ZKUser<ID>> cache = ZKCacheUtils.getCache(CacheName.USER_CACHE);
        if (user == null) {
            return cache.put(userId, user);
        }
        return false;
    }

    /************************************************************/
    // --- 用户权限缓存
    /************************************************************/

    // 清理缓存 --------------------
    /**
     * 清理所有用户权限代码缓存
     *
     * @Title: cleanAllAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:18:04 AM
     * @return void
     */
    public static void cleanAllAuth() {
        ZKCacheUtils.cleanByNamePrefix(CacheName.USER_AUTH_INFO_CACHE_PREFIX);
    }

    /**
     * 清理用户权限代码缓存
     *
     * @Title: cleanAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 19, 2024 11:44:53 PM
     * @param <ID>
     * @param userId
     * @return void
     */
    public static <ID> void cleanAuth(ID userId) {
        ZKCacheUtils.getCache(CacheName.USER_AUTH_INFO_CACHE_PREFIX + userId.toString()).clear();
    }

    // 取缓存数据 --------------------
    // 取缓存：用户权限信息
    public static <ID> ZKSecAuthorizationInfo getAuthInfo(ID userId, String systemCode) {
        ZKCache<String, ZKSecAuthorizationInfo> cache = ZKCacheUtils
                .getCache(CacheName.USER_AUTH_INFO_CACHE_PREFIX + userId.toString());
        return cache.get(systemCode);
    }

    // 设置缓存：用户权限信息
    public static <ID> boolean putAuthInfo(ID userId, String systemCode, ZKSecAuthorizationInfo authInfo) {
        ZKCache<String, ZKSecAuthorizationInfo> cache = ZKCacheUtils
                .getCache(CacheName.USER_AUTH_INFO_CACHE_PREFIX + userId.toString());
        if (authInfo == null) {
            return cache.put(systemCode, authInfo);
        }
        return false;
    }

}
