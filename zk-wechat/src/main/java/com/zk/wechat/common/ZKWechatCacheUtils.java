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
* @Title: ZKWechatCacheUtils.java 
* @author Vinson 
* @Package com.zk.wechat.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 7, 2021 3:28:10 PM 
* @version V1.0 
*/
package com.zk.wechat.common;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXAccountAuthAccessToken;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXUserAuthAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPComponentAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPPreAuthCode;

/** 
* @ClassName: ZKWechatCacheUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWechatCacheUtils {
    
    private static Logger logger = LogManager.getLogger(ZKWechatCacheUtils.class);

    static ZKCacheManager<String> zkCacheManager = null;

    @SuppressWarnings("unchecked")
    private static ZKCacheManager<String> getCacheManager() {
        if (zkCacheManager == null) {
            zkCacheManager = (ZKCacheManager<String>) ZKEnvironmentUtils.getApplicationContext()
                    .getBean("zkCacheManager");
        }
        return zkCacheManager;
    }

    /**
     * 缓存名称
     * 
     * @ClassName: KEY_PREFIX
     * @Description: TODO(simple description this class what to do.)
     * @author bs
     * @version 1.0
     */
    private static interface Cache_Name {

        /**
         * 微信开放平台，第三方平台 ComponentAccessToken 存放的缓存名称
         */
        public static final String ZKWXTPComponentAccessToken = "_wx_third_party_componentAccessToken_";

        /**
         * 预授权码存放的缓存名称
         */
        public static final String ZKWXTPPreAuthCode = "_wx_third_party_PreAuthCode_";

        /**
         * 目标授权账号 AccessToken 缓存名称
         */
        public static final String ZKWXAccountAuthAccessToken = "_wx_account_auth_AccessToken_";

        /**
         * 用户授权 Token 缓存名称
         */
        public static final String ZKWXUserAuthAccessToken = "_wx_user_auth_AccessToken_";

//        /**
//         * 授权方 JS 令牌
//         */
//        public static final String JsTicket = "_wx_js_ticket_";

    }

    /**
     * 取微信开放平台，第三方平台 ComponentAccessToken 存放的缓存
     */
    private static ZKCache<String, ZKWXTPComponentAccessToken> getCacheWXTPComponentAccessToken() {
        return getCacheManager().getCache(Cache_Name.ZKWXTPComponentAccessToken, 7200000); // 两小时
    }

    /**
     * 取预授权码存放的缓存
     */
    private static ZKCache<String, ZKWXTPPreAuthCode> getCacheWXTPPreAuthCode() {
        return getCacheManager().getCache(Cache_Name.ZKWXTPPreAuthCode, 3600000); // 1 小时
    }

    /**
     * 取授权方 Token 缓存
     */
    private static ZKCache<String, ZKWXAccountAuthAccessToken> getCacheWXAccountAuthAccessToken() {
        return getCacheManager().getCache(Cache_Name.ZKWXAccountAuthAccessToken, 7200000); // 两小时
    }

    /**
     * 取 微信用户 ZKWXUserAccessToken 缓存
     */
    private static ZKCache<String, ZKWXUserAuthAccessToken> getCacheWXUserAccessToken() {
        return getCacheManager().getCache(Cache_Name.ZKWXUserAuthAccessToken, 7200000); // 两小时
    }

    /*******************************************************/
    /*** 第三方平台 ComponentAccessToken 存放的缓存 */
    /**
     * 存 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: putWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:52 PM
     * @param componentAccessToken
     * @return void
     */
    public static void putWXTPComponentAccessToken(ZKWXTPComponentAccessToken componentAccessToken) {
        try {
            getCacheWXTPComponentAccessToken().put(componentAccessToken.getThirdPartyAppid(), componentAccessToken);
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-001] 存 第三方平台 ComponentAccessToken 缓存失败。 componentAccessToken：{}",
                    ZKJsonUtils.toJsonStr(componentAccessToken));
            e.printStackTrace();
        }
    }

    /**
     * 取 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: getWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:59 PM
     * @param thirdPartyAppid
     * @return
     * @return ZKWXTPComponentAccessToken
     */
    public static ZKWXTPComponentAccessToken getWXTPComponentAccessToken(String thirdPartyAppid) {
        try {
            return getCacheWXTPComponentAccessToken().get(thirdPartyAppid);
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-002] 取 第三方平台 ComponentAccessToken 缓存失败。thirdPartyAppid:{}",
                    thirdPartyAppid);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: removeWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:05 PM
     * @param thirdPartyAppid
     * @return void
     */
    public static void removeWXTPComponentAccessToken(String thirdPartyAppid) {
        try {
            getCacheWXTPComponentAccessToken().remove(thirdPartyAppid);
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-003] 删 第三方平台 ComponentAccessToken 缓存失败。thirdPartyAppid:{}",
                    thirdPartyAppid);
            e.printStackTrace();
        }
    }

    /**
     * 清空 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: clearWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:11 PM
     * @return void
     */
    public static void clearWXTPComponentAccessToken() {
        try {
            getCacheWXTPComponentAccessToken().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-004] 清空 第三方平台 ComponentAccessToken 缓存失败。");
            e.printStackTrace();
        }
    }

    /*******************************************************/
    /*** 预授权码存放 缓存 */
    /**
     * 存 预授权码 缓存
     *
     * @Title: putWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:52 PM
     * @param preAuthCode
     * @return void
     */
    public static void putWXTPPreAuthCode(ZKWXTPPreAuthCode preAuthCode) {
        try {
            getCacheWXTPPreAuthCode().put(preAuthCode.getIdentification(), preAuthCode);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-001] 存 预授权码 缓存失败。 preAuthCode：{}",
                    ZKJsonUtils.toJsonStr(preAuthCode));
            e.printStackTrace();
        }
    }

    /**
     * 取 预授权码 缓存
     *
     * @Title: getWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:59 PM
     * @param identification
     * @return ZKWXTPPreAuthCode
     */
    public static ZKWXTPPreAuthCode getWXTPPreAuthCode(String identification) {
        try {
            return getCacheWXTPPreAuthCode().get(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-002] 取 预授权码 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 预授权码 缓存
     *
     * @Title: removeWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:05 PM
     * @param identification
     * @return void
     */
    public static void removeWXTPPreAuthCode(String identification) {
        try {
            getCacheWXTPPreAuthCode().remove(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-003] 删 预授权码 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
    }

    /**
     * 清空 预授权码 缓存
     *
     * @Title: clearWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:11 PM
     * @return void
     */
    public static void clearWXTPPreAuthCode() {
        try {
            getCacheWXTPPreAuthCode().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-004] 清空 预授权码 缓存失败。");
            e.printStackTrace();
        }
    }

    /*******************************************************/
    /*** 目标授权账号 缓存 */
    /**
     * 存 目标授权账号 AccessToken 缓存
     *
     * @Title: putWXAccountAuthAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:52 PM
     * @param authAccountAccessToken
     * @return void
     */
    public static void putWXAccountAuthAccessToken(ZKWXAccountAuthAccessToken authAccountAccessToken) {
        try {
            getCacheWXAccountAuthAccessToken().put(authAccountAccessToken.getIdentification(),
                    authAccountAccessToken);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-001] 存 存目标授权账号 AccessToken 缓存失败。 authAccountAccessToken：{}",
                    ZKJsonUtils.toJsonStr(authAccountAccessToken));
            e.printStackTrace();
        }
    }

    /**
     * 取 目标授权账号 AccessToken 缓存
     *
     * @Title: getWXAccountAuthAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:59 PM
     * @param identification
     * @return ZKWXTPPreAuthCode
     */
    public static ZKWXAccountAuthAccessToken getWXAccountAuthAccessToken(String identification) {
        try {
            return getCacheWXAccountAuthAccessToken().get(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-002] 取 目标授权账号 AccessToken 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 目标授权账号 AccessToken 缓存
     *
     * @Title: removeWXAccountAuthAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:05 PM
     * @param identification
     * @return void
     */
    public static void removeWXAccountAuthAccessToken(String identification) {
        try {
            getCacheWXAccountAuthAccessToken().remove(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211108-1432-003] 删 目标授权账号 AccessToken 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
    }

    /**
     * 清空 目标授权账号 AccessToken 缓存
     *
     * @Title: clearWXAccountAuthAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:11 PM
     * @return void
     */
    public static void clearWXAccountAuthAccessToken() {
        try {
            getCacheWXAccountAuthAccessToken().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20211108-1432-004] 清空 目标授权账号 AccessToken 缓存失败。");
            e.printStackTrace();
        }
    }

    /*******************************************************/
    /*** 微信用户授权 缓存 */
    /**
     * 存 用户 ZKWXUserAccessToken 缓存
     *
     * @Title: putWXUserAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 11:25:26 AM
     * @param userAuthAccessToken
     * @return void
     */
    public static void putWXUserAccessToken(ZKWXUserAuthAccessToken userAuthAccessToken) {
        try {
            getCacheWXUserAccessToken().put(userAuthAccessToken.getIdentification(), userAuthAccessToken);
        }
        catch(Exception e) {
            logger.error("[>_<:20220519-1432-001] 存 预授权码 缓存失败。 preAuthCode：{}",
                    ZKJsonUtils.toJsonStr(userAuthAccessToken));
            e.printStackTrace();
        }
    }

    /**
     * 取 用户 ZKWXUserAccessToken 缓存
     *
     * @Title: getWXUserAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 11:29:10 AM
     * @param identification
     * @return
     * @return ZKWXUserAuthAccessToken
     */
    public static ZKWXUserAuthAccessToken getWXUserAccessToken(String identification) {
        try {
            return getCacheWXUserAccessToken().get(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20220519-1432-002] 取 用户 ZKWXUserAccessToken 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 用户 ZKWXUserAccessToken 缓存
     *
     * @Title: removeWXUserAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 11:28:45 AM
     * @param identification
     * @return void
     */
    public static void removeWXUserAccessToken(String identification) {
        try {
            getCacheWXUserAccessToken().remove(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20220519-1432-003] 删 用户 ZKWXUserAccessToken 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
    }

    /**
     * 清空 用户 ZKWXUserAccessToken 缓存
     *
     * @Title: clearWXUserAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 11:28:33 AM
     * @return void
     */
    public static void clearWXUserAccessToken() {
        try {
            getCacheWXUserAccessToken().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20220519-1432-004] 清空 用户 ZKWXUserAccessToken 缓存失败。");
            e.printStackTrace();
        }
    }


}
