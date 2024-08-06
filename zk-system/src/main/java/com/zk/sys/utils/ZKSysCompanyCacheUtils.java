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
* @Title: ZKCompanyCacheUtils.java 
* @author Vinson 
* @Package com.zk.framework.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 22, 2024 10:38:19 AM 
* @version V1.0 
*/
package com.zk.sys.utils;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.cache.ZKCache;
import com.zk.cache.utils.ZKCacheUtils;
import com.zk.framework.security.userdetails.ZKUser;
import com.zk.security.utils.ZKSecSecurityUtils;

/**
 * @ClassName: ZKSysCompanyCacheUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSysCompanyCacheUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKSysCompanyCacheUtils.class);

    /**
     * 定义缓存名称
     * 
     * @ClassName: CacheName
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    static interface CacheName {
//        /**
//         * 公司缓存
//         */
//        public static final String COMPANY_CACHE = "COMPANY_CACHE";

        /**
         * 公司拥有的 权限代码
         */
        public static final String COMPANY_AUTH_CACHE = "COMPANY_AUTH_CACHE";
        
        /**
         * 公司拥有的 API接口代码
         */
        public static final String COMPANY_API_CACHE_PREFIX = "COMPANY_API_CACHE_PREFIX_";

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
        ZKCacheUtils.clean(CacheName.COMPANY_AUTH_CACHE);
        ZKCacheUtils.cleanByNamePrefix(CacheName.COMPANY_API_CACHE_PREFIX);
    }

    /**
     * 清理当前登录用户所在公司缓存
     *
     * @Title: clean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:03 AM
     * @param <ID>
     * @return void
     */
    public static <ID> void clean() {
        ID companyId = ZKSecSecurityUtils.getCompanyId();
        if (companyId == null)
            return;
        clean(companyId);
    }

    /**
     * 清理指定用户所在公司缓存
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
        clean(user.getCompanyId());
    }

    /**
     * 清理指定公司缓存
     *
     * @Title: clean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:17:13 AM
     * @param <ID>
     * @param userId
     * @return void
     */
    public static <ID> void clean(ID companyId) {
        ZKCacheUtils.getCache(CacheName.COMPANY_AUTH_CACHE).remove(companyId);
        ZKCacheUtils.getCache(CacheName.COMPANY_API_CACHE_PREFIX + companyId.toString()).clear();
    }

    /************************************************************/
    // --- 公司权限缓存
    /************************************************************/

    // 清理缓存 --------------------
    /**
     * 清理公司权限代码缓存
     *
     * @Title: cleanAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 19, 2024 11:44:53 PM
     * @param <ID>
     * @param userId
     * @return void
     */
    public static <ID> void cleanAuth(ID companyId) {
        ZKCacheUtils.getCache(CacheName.COMPANY_AUTH_CACHE).remove(companyId);
        ZKCacheUtils.getCache(CacheName.COMPANY_API_CACHE_PREFIX + companyId.toString()).clear();
    }

    /**
     * 清理所有公司权限代码缓存
     *
     * @Title: cleanAllAuth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:18:04 AM
     * @return void
     */
    public static void cleanAllAuth() {
        ZKCacheUtils.getCache(CacheName.COMPANY_AUTH_CACHE).clear();
        ZKCacheUtils.cleanByNamePrefix(CacheName.COMPANY_API_CACHE_PREFIX);
    }

    // 取缓存数据 --------------------
    // 取缓存：公司拥有的权限代码
    public static Set<String> getAuthCodes(String companyId) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils.getCache(CacheName.COMPANY_AUTH_CACHE);
        return cache.get(companyId);
    }

    // 设置缓存：公司拥有的权限代码
    public static boolean putAuthCodes(String companyId, Set<String> authCodes) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils.getCache(CacheName.COMPANY_AUTH_CACHE);
        if (authCodes == null) {
            return cache.put(companyId, authCodes);
        }
        return false;
    }

    // 取缓存：公司拥有的指定应用下的 api 代码
    public static Set<String> getApiCodes(String companyId, String systemCode) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils
                .getCache(CacheName.COMPANY_API_CACHE_PREFIX + companyId.toString());
        return cache.get(systemCode);
    }

    // 设置缓存：公司拥有的指定应用下的 api 代码
    public static boolean putApiCodes(String companyId, String systemCode, Set<String> apiCodes) {
        ZKCache<String, Set<String>> cache = ZKCacheUtils
                .getCache(CacheName.COMPANY_API_CACHE_PREFIX + companyId.toString());
        if (apiCodes == null) {
            return cache.put(systemCode, apiCodes);
        }
        return false;
    }

}
