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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.cache.utils.ZKCacheUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.framework.security.ZKAuthPermission;
import com.zk.framework.security.service.ZKSecAuthService;
import com.zk.framework.security.userdetails.ZKUser;
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
    protected static Logger log = LoggerFactory.getLogger(ZKUserCacheUtils.class);

    static ZKSecAuthService<?> secAuthService = null;

    /**
     * @return secService sa
     */
    @SuppressWarnings("unchecked")
    public static <ID> ZKSecAuthService<ID> getSecAuthService() {
        if (secAuthService == null) {
            try {
                secAuthService = ZKEnvironmentUtils.getApplicationContext().getBean(ZKSecAuthService.class);
            }
            catch(Exception e) {
                secAuthService = null;
                log.error("[20220510-1028-001] 设置 ZKSecAuthService 失败");
                throw e;
            }
        }
        return (ZKSecAuthService<ID>) secAuthService;
    }

    /**
     * @param secService
     *            the secService to set
     */
    public static void setSecAuthService(ZKSecAuthService<?> secAuthService) {
        ZKUserCacheUtils.secAuthService = secAuthService;
    }

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
         * 用户 角色 代码 缓存
         */
        public static final String USER_CACHE_ROLE_CODE = "USER_CACHE_ROLE_CODE";

        /**
         * 用户 权限 代码，含 api 接口代码 缓存
         */
        public static final String USER_CACHE_AUTH_CODE = "USER_CACHE_AUTH_CODE";

    }


    /*** 下面缓存先不实现 ******************************************/
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
        ZKCacheUtils.getCache(CacheName.USER_CACHE).clear();
        ZKCacheUtils.getCache(CacheName.USER_CACHE_AUTH_CODE).clear();
        ZKCacheUtils.getCache(CacheName.USER_CACHE_ROLE_CODE).clear();
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
        ZKCacheUtils.getCache(CacheName.USER_CACHE_AUTH_CODE).remove(userId);
        ZKCacheUtils.getCache(CacheName.USER_CACHE_ROLE_CODE).remove(userId);
    }

    /**
     * 设置用户实体缓存
     *
     * @Title: putUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:20 AM
     * @param user
     * @return void
     */
    public static void putUser(ZKUser<?> user) {
        ZKCacheUtils.getCache(CacheName.USER_CACHE).put(user.getPkId(), user);
    }

    /**
     * 取用户实体，先从缓存中取，未取出，则通过服务取；并设置缓存；
     *
     * @Title: getUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:25 AM
     * @param <ID>
     * @param userId
     * @return ZKUser<ID>
     */
    @SuppressWarnings("unchecked")
    public static <ID> ZKUser<ID> getUser(ID userId) {
        ZKUser<ID> user = (ZKUser<ID>) ZKCacheUtils.getCache(CacheName.USER_CACHE).get(userId);
        if (user == null) {
            ZKSecAuthService<ID> s = getSecAuthService();
            user = s.getByUserId(userId);
            if (user != null) {
                putUser(user);
            }
        }
        return user;
    }

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

    /*** 角色缓存 ******************************************/
    /**
     * 设置用户角色代码缓存
     *
     * @Title: putRoleCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:49 AM
     * @param <ID>
     * @param user
     * @param roleCodes
     * @return boolean
     */
    public static <ID> boolean putRoleCode(ZKUser<ID> user, List<String> roleCodes) {
        return ZKCacheUtils.getCache(CacheName.USER_CACHE_ROLE_CODE).put(user.getCompanyId() + user.getPkId(),
                roleCodes);
    }
    
    public static <ID> boolean putRoleCode(ID userId, List<String> roleCodes) {
        return putRoleCode(getUser(userId), roleCodes);
    }

    /**
     * 取用户角色代码缓存，先从缓存中取，未取出，则通过服务取；并设置缓存；
     *
     * @Title: getRoleCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:53 AM
     * @param <ID>
     * @param user
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public static <ID> List<String> getRoleCode(ZKUser<ID> user) {
        List<String> roleCodes = (List<String>) ZKCacheUtils.getCache(CacheName.USER_CACHE_ROLE_CODE)
                .get(user.getCompanyId() + user.getPkId());
        if (roleCodes == null) {
            ZKSecAuthService<ID> s = getSecAuthService();
            roleCodes = s.getByRoleCodesUserId(user.getPkId());
            if (roleCodes != null) {
                putRoleCode(user, roleCodes);
            }
        }
        return roleCodes;
    }

    public static <ID> List<String> getRoleCode(ID userId) {
        return getRoleCode(getUser(userId));
    }

    /**
     * 清理用户角色代码缓存
     *
     * @Title: cleanRoleCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:56 AM
     * @param <ID>
     * @param user
     * @return
     * @return boolean
     */
    public static <ID> boolean cleanRoleCode(ZKUser<ID> user) {
        return ZKCacheUtils.getCache(CacheName.USER_CACHE_ROLE_CODE).remove(user.getCompanyId() + user.getPkId());
    }

    public static <ID> boolean cleanRoleCode(ID userId) {
        return cleanRoleCode(getUser(userId));
    }

    /**
     * 清理所有用户角色代码缓存
     *
     * @Title: cleanAllRoleCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:18:04 AM
     * @return void
     */
    public static void cleanAllRoleCode() {
        ZKCacheUtils.getCache(CacheName.USER_CACHE_ROLE_CODE).clear();
    }

    /*** 权限代码缓存 ******************************************/
    /**
     * 设置用户权限代码缓存
     *
     * @Title: putAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:49 AM
     * @param <ID>
     * @param user
     * @param authPermission
     * @return boolean
     */
    public static <ID> boolean putAuth(ZKUser<ID> user, ZKAuthPermission authPermission) {
        return ZKCacheUtils.getCache(CacheName.USER_CACHE_AUTH_CODE).put(user.getCompanyId() + user.getPkId(),
                authPermission);
    }

    public static <ID> boolean putAuth(ID userId, ZKAuthPermission authPermission) {
        return putAuth(getUser(userId), authPermission);
    }

    /**
     * 取用户权限代码缓存和API接口代码，先从缓存中取，未取出，则通过服务取；并设置缓存；
     *
     * @Title: getAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:53 AM
     * @param <ID>
     * @param user
     * @return ZKAuthPermission
     */
    public static <ID> ZKAuthPermission getAuth(ZKUser<ID> user) {
        ZKAuthPermission auth = (ZKAuthPermission) ZKCacheUtils.getCache(CacheName.USER_CACHE_AUTH_CODE)
                .get(user.getCompanyId() + user.getPkId());
        if (auth == null) {
            ZKSecAuthService<ID> s = getSecAuthService();
            auth = s.getByAuthUser(user);
            if (auth != null) {
                putAuth(user, auth);
            }
        }
        return auth;
    }

    public static <ID> ZKAuthPermission getAuth(ID userId) {
        return getAuth(getUser(userId));
    }

    /**
     * 清理用户权限代码缓存
     *
     * @Title: cleanAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:56 AM
     * @param <ID>
     * @param user
     * @return boolean
     */
    public static <ID> boolean cleanAuth(ZKUser<ID> user) {
        return ZKCacheUtils.getCache(CacheName.USER_CACHE_AUTH_CODE).remove(user.getCompanyId() + user.getPkId());
    }
    public static <ID> boolean cleanAuth(ID userId) {
        return cleanAuth(getUser(userId));
    }

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
        ZKCacheUtils.getCache(CacheName.USER_CACHE_AUTH_CODE).clear();
    }

    /*************************************************************/
    /*** 下面缓存先不实现 ******************************************/
    /*************************************************************/
    // 设置用户菜单缓存
    // 取用户菜单缓存
    // 清理用户菜单缓存
    // 清理所有用户菜单缓存

    // 设置用户导航栏目缓存
    // 取用户导航栏目缓存
    // 清理用户导航栏目缓存
    // 清理所有用户导航栏目缓存

}
