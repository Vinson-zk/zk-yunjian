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
package com.zk.sys.utils;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.cache.ZKCache;
import com.zk.cache.utils.ZKCacheUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgUser;

/**
 * @ClassName: ZKSysUserCacheUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSysUserCacheUtils {

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
         * 用户权限信息: 角色代码；
         */
        public static final String USER_ROLE_CACHE = "USER_ROLE_CACHE";

        /**
         * 用户权限信息: 权限代码
         */
        public static final String USER_AUTH_CACHE = "USER_AUTH_CACHE";

        /**
         * 用户权限信息: API接口代码，区分应用存储
         */
        public static final String USER_API_CACHE_PREFIX_ = "USER_API_CACHE_PREFIX_";

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
        ZKCacheUtils.clean(CacheName.USER_ROLE_CACHE);
        ZKCacheUtils.clean(CacheName.USER_AUTH_CACHE);
        ZKCacheUtils.cleanByNamePrefix(CacheName.USER_API_CACHE_PREFIX_);
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
    public static void clean() {
        String userId = ZKSecSecurityUtils.getUserId();
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
    public static void clean(ZKSysOrgUser user) {
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
    public static void clean(String userId) {
        ZKCacheUtils.getCache(CacheName.USER_CACHE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_ROLE_CACHE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_AUTH_CACHE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_API_CACHE_PREFIX_ + userId.toString()).clear();
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
    public static boolean cleanUser(String userId) {
        return ZKCacheUtils.getCache(CacheName.USER_CACHE).remove(userId);
    }

    public static boolean cleanUser(ZKSysOrgUser user) {
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
    public static ZKSysOrgUser getUser() {
        String userId = ZKSecSecurityUtils.getUserId();
        if (userId == null)
            return null;
        return getUser(userId);
    }
    /**
     * 取缓存：取用户实体；
     *
     * @Title: getUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:25 AM
     * @param <ID>
     * @param userId
     * @return ZKUser<ID>
     */
    public static ZKSysOrgUser getUser(String userId) {
        ZKCache<String, ZKSysOrgUser> cache = ZKCacheUtils.getCache(CacheName.USER_CACHE);
        return cache.get(userId);
    }

    // 设置缓存：用户实体；
    public static boolean putUser(String userId, ZKSysOrgUser user) {
        ZKCache<String, ZKSysOrgUser> cache = ZKCacheUtils.getCache(CacheName.USER_CACHE);
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
        ZKCacheUtils.getCache(CacheName.USER_ROLE_CACHE).clear();
        ZKCacheUtils.getCache(CacheName.USER_AUTH_CACHE).clear();
        ZKCacheUtils.cleanByNamePrefix(CacheName.USER_API_CACHE_PREFIX_);
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
    public static void cleanAuth(String userId) {
        ZKCacheUtils.getCache(CacheName.USER_ROLE_CACHE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_AUTH_CACHE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_API_CACHE_PREFIX_ + userId.toString()).clear();
    }

    // 取缓存数据 --------------------
    // 取缓存：用户角色代码
    public static Set<String> getRoleCodes(String userId) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils.getCache(CacheName.USER_ROLE_CACHE);
        return cache.get(userId);
    }

    // 设置缓存：用户角色代码
    public static boolean putRoleCodes(String userId, Set<String> roleCodes) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils.getCache(CacheName.USER_ROLE_CACHE);
        if (roleCodes == null) {
            return cache.put(userId, roleCodes);
        }
        return false;
    }

    // 取缓存：用户权限代码
    public static Set<String> getAuthCodes(String userId) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils.getCache(CacheName.USER_AUTH_CACHE);
        return cache.get(userId);
    }

    // 设置缓存：用户权限代码
    public static boolean putAuthCodes(String userId, Set<String> authCodes) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils.getCache(CacheName.USER_AUTH_CACHE);
        if (authCodes == null) {
            return cache.put(userId, authCodes);
        }
        return false;
    }

    // 取缓存：用户拥有的指定应用下的 API 代码
    public static Set<String> getApiCodes(String userId, String systemCode) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils
                .getCache(CacheName.USER_API_CACHE_PREFIX_ + userId.toString());
        return  cache.get(systemCode);
    }

    // 设置缓存：用户拥有的指定应用下的 API 代码
    public static boolean putApiCodes(String userId, String systemCode, Set<String> apiCodes) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils
                .getCache(CacheName.USER_API_CACHE_PREFIX_ + userId.toString());
        if (apiCodes == null) {
            return cache.put(systemCode, apiCodes);
        }
        return false;
    }

    /*** 下面缓存先不实现 ******************************************/

    /************************************************************/
    // --- 用户菜单缓存
    /************************************************************/
    // 清理缓存：清理用户菜单缓存、清理所有用户菜单缓存
    // 取缓存：用户菜单缓存
    // 设置缓存：用户菜单缓存

    /************************************************************/
    // --- 导航栏目缓存
    /************************************************************/
    // 清理缓存：清理用户导航栏目缓存、清理所有用户导航栏目缓存
    // 取缓存：用户导航栏目缓存
    // 设置缓存：用户导航栏目缓存

}
